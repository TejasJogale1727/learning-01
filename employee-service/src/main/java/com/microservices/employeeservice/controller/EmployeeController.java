package com.microservices.employeeservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.employeeservice.model.Employee;
import com.microservices.employeeservice.repository.EmployeeRepository;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeRepository repository;

	@PostMapping
	public Employee add(@RequestBody Employee employee) {
		LOGGER.info("Employee add : {}", employee);
		return repository.addEmployee(employee);
	}

	@GetMapping
	public List<Employee> getAll() {
		LOGGER.info("Employee find");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Employee getById(@PathVariable Long id) {
		LOGGER.info("Employee find: id={}", id);
		return repository.findById(id);
	}
	
	@GetMapping("/department/{departmentId}")
	public List<Employee> getByDepartmentId(@PathVariable Long departmentId) {
		LOGGER.info("Employee find: departmentId={}", departmentId);
		return repository.findByDepartment(departmentId);
	}
}
