package org.krishna.demo.service;

import org.krishna.demo.model.Employee;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

	public Flux<Employee> getAllEmployees();
	public Mono<Employee> getEmployee(Long id);
	public Flux<Employee> findEmployees(String search);
	public Mono<Employee> addEmployee(Employee employee);
	public Mono<Employee> updateEmployee(Long id, Employee employee);
	public void deleteEmployee(Long id);
}
