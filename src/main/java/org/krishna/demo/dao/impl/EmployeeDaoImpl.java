package org.krishna.demo.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.krishna.demo.dao.EmployeeDao;
import org.krishna.demo.exceptions.EmployeeServiceException;
import org.krishna.demo.model.Employee;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeDaoImpl implements EmployeeDao {
	private String inputFile = "employees.json";
	
	private static Flux<Employee> employees; 
	private static Long count = 7L;
	
	@PostConstruct
	public void init() throws IOException {
		ObjectMapper jsonMapper = new ObjectMapper();
		InputStream ioStream = EmployeeDaoImpl.class.getResourceAsStream("/" + inputFile);
		List<Employee>employees = jsonMapper.readValue(ioStream, new TypeReference<List<Employee>>() {});
		EmployeeDaoImpl.employees = Flux.fromIterable(employees);
	}

	@Override
	public Flux<Employee> getAllEmployees() {
		return employees;
	}

	@Override
	public Mono<Employee> getEmployee(Long id) {
		return employees
				.filter(emp -> id == emp.getId()).last();
	}

	@Override
	public Flux<Employee> findEmployees(String search) {
		return employees
				.filter(emp -> this.searchFilter(search, emp));
	}
	
	private boolean searchFilter(String search, Employee employee) {
		return StringUtils.containsIgnoreCase(employee.getFirstName(), search) ||
				StringUtils.containsIgnoreCase(employee.getLastName(), search) ||
				StringUtils.containsIgnoreCase(employee.getTitle(), search) ||
				StringUtils.containsIgnoreCase(employee.getAddress1(), search) ||
				StringUtils.containsIgnoreCase(employee.getCity(), search) ||
				StringUtils.containsIgnoreCase(employee.getState(), search) ||
				(NumberUtils.isNumber(search) && employee.getSalary().compareTo(Double.parseDouble(search)) > 0);
	}

	@Override
	public Mono<Employee> addEmployee(Employee employee) {
		if(null == employee)
			throw new EmployeeServiceException("Employee cannot be null");
		employee.setId(++count);
		EmployeeDaoImpl.employees = Flux.merge(EmployeeDaoImpl.employees, Flux.just(employee));
		return Mono.just(employee);
	}

	@Override
	public Mono<Employee> updateEmployee(Long id, Employee employee) {
		if(null == id)
			throw new EmployeeServiceException("Id cannot be empty or null");
		if(null == employee)
			throw new EmployeeServiceException("Id cannot be null");
		
		// to do
		return Mono.just(employee);

	}

	@Override
	public void deleteEmployee(Long id) {
		if(null == id)
			throw new EmployeeServiceException("Id cannot be null");
		
		// to do
	}

}
