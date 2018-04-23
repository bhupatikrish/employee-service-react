package org.krishna.demo.service.impl;

import org.apache.commons.lang.StringUtils;
import org.krishna.demo.dao.EmployeeDao;
import org.krishna.demo.exceptions.EmployeeServiceException;
import org.krishna.demo.model.Employee;
import org.krishna.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeDao employeeDao;

	@Override
	public Flux<Employee> getAllEmployees() {
		return this.employeeDao.getAllEmployees();
	}

	@Override
	public Mono<Employee> getEmployee(Long id) {
		if(null == id)
			throw new EmployeeServiceException("Id cannot be null.");
		
		return this.employeeDao.getEmployee(id);

	}

	@Override
	public Flux<Employee> findEmployees(String search) {
		if(StringUtils.isEmpty(search))
			throw new EmployeeServiceException("Search cannot be empty.");
		
		return this.employeeDao.findEmployees(search);

	}

	@Override
	public Mono<Employee> addEmployee(Employee employee) {
		if(null == employee)
			throw new EmployeeServiceException("Employee cannot be empty");
		return this.employeeDao.addEmployee(employee);
	}

	@Override
	public Mono<Employee> updateEmployee(Long id, Employee employee) {
		if(null == id)
			throw new EmployeeServiceException("Id cannot be null.");
		if(null == employee)
			throw new EmployeeServiceException("Employee cannot be empty");
		
		return this.employeeDao.updateEmployee(id, employee);
	}

	@Override
	public void deleteEmployee(Long id) {
		if(null == id)
			throw new EmployeeServiceException("Id cannot be null.");
		
		this.employeeDao.deleteEmployee(id);
	}

}
