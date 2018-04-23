package org.krishna.demo.controllers;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.krishna.demo.exceptions.EmployeeNotFoundException;
import org.krishna.demo.exceptions.EmployeeServiceException;
import org.krishna.demo.exceptions.EmployeeServiceValidationError;
import org.krishna.demo.model.Employee;
import org.krishna.demo.model.EmployeeResponse;
import org.krishna.demo.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author krishna
 * EmployeeController is an api to get, add, delete, find and update employees. 
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin
public class EmployeeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	/*
	 * Employee service
	 */
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * This method will return all the employees fund in the system.
	 * 
	 * @return all the employees found in the system
	 * @return empty responseData if no users are found.
	 */
	@GetMapping
	public Flux<Employee> getAllEmployees() {
		
		LOGGER.debug("getAllEmployees from service");
		return this.employeeService.getAllEmployees();
	}
	
	/**
	 * This method will return the employee for the provied id.
	 * 
	 * @param id: Employee id
	 * @return employee if found else returns just the metadata
	 * 
	 */
	@GetMapping("/{id}")
	public Mono<Employee> getEmployee(@PathVariable String id) {
		LOGGER.debug("getEmployee id:", id);

		if(!StringUtils.isNumeric(id)) {
			LOGGER.debug("getEmployee validation failed");
			throw new EmployeeServiceValidationError("Id Not valid");
		}
				
		LOGGER.debug("getEmployee from service");
		return this.employeeService.getEmployee(Long.parseLong(id));
	}
	
	/**
	 * This method returns the list of users found for the provided search string.
	 * It searches by comparing the provided query string to certain attributes of employee.
	 * 
	 * @param search: query string. Example: part of user first name. "krish"
	 * @return List of employees found else return null in responseData.
	 */
	@GetMapping("/search")
	public Flux<Employee> findEmployee(@RequestParam("q") String search) {
		LOGGER.debug("findEmployee search:", search);
		if(StringUtils.isEmpty(search)) {
			LOGGER.debug("findEmployee validation failed");
			throw new EmployeeServiceValidationError("Search string not valid");
		}
		
		LOGGER.debug("findEmployee from service");
		return this.employeeService.findEmployees(search);

	}
	
	/**
	 * Adds the employee to the system.
	 * @param employee: employee to be added to the system
	 * @return created employee
	 */
	@PostMapping
	public Mono<Employee> addEmployee(@Valid @RequestBody Employee employee) {
		LOGGER.debug("addEmployee start:", employee.toString());
		
		LOGGER.debug("addEmployee using service");
		return this.employeeService.addEmployee(employee);
	}
	
	/**
	 * Updates the employee.
	 * 
	 * @param id: Employee id that need to be updated
	 * @param employee: Employee data to be updated
	 * @return updated employee
	 */
	@PutMapping("/{id}")
	public Mono<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {
		LOGGER.debug("updateEmployee id:", id);
		if(!StringUtils.isNumeric(id)) {
			LOGGER.debug("updateEmployee validation failed");
			throw new EmployeeServiceValidationError("Id not valid");
		}
		
		LOGGER.debug("updateEmployee using service");
		return this.employeeService.updateEmployee(Long.parseLong(id), employee);
	}
	
	/**
	 * Deletes the employee for the provided id
	 * @param id: Employee id
	 * @return none in responseData
	 */
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable String id) {
		LOGGER.debug("deleteEmployee id:", id);
		if(!StringUtils.isNumeric(id)) {
			LOGGER.debug("deleteEmployee validation failed");
			throw new EmployeeServiceValidationError("Id not valid");
		}
		
		LOGGER.debug("deleteEmployee using service");
		this.employeeService.deleteEmployee(Long.parseLong(id));

	}
	
	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<EmployeeResponse> notFoundExceptionHandler(Exception ex, WebRequest request) {
		EmployeeResponse response = new EmployeeResponse();
		response.getMetadata().setNotFoundOutcome();
		return new ResponseEntity<EmployeeResponse>(response, new HttpHeaders(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmployeeServiceValidationError.class)
	public ResponseEntity<EmployeeResponse> validationExceptionHandler(Exception ex, WebRequest request) {
		EmployeeResponse response = new EmployeeResponse();
		response.getMetadata().setValidationErrorOutcome();
		response.getMetadata().setMessage(ex.getMessage());;
		return new ResponseEntity<EmployeeResponse>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EmployeeServiceException.class)
	public ResponseEntity<EmployeeResponse> exceptionHandler(Exception ex, WebRequest request) {
		EmployeeResponse response = new EmployeeResponse();
		response.getMetadata().setErrorOutcome();
		response.getMetadata().setMessage(ex.getMessage());;
		return new ResponseEntity<EmployeeResponse>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
