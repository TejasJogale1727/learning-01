package com.microservices.departmentservice.controller;

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

import com.microservices.departmentservice.client.EmployeeClient;
import com.microservices.departmentservice.model.Department;
import com.microservices.departmentservice.repository.DepartmentRepository;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentRepository repository;
	
	@Autowired
	private EmployeeClient employeeClient;

	@PostMapping
	public Department add(@RequestBody Department department) {
		LOGGER.info("Department add : {}", department);
		return repository.addDepartment(department);
	}

	@GetMapping
	public List<Department> getAll() {
		LOGGER.info("Department find");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Department getById(@PathVariable Long id) {
		LOGGER.info("Department find: id={}", id);
		return repository.findById(id);
	}

	@GetMapping("/with-employees")
	public List<Department> findAllWithEmployee() {
		LOGGER.info("Department findAll with employees");
		List<Department> departments = repository.findAll();
		
		departments.forEach(department->department.setEmployees(employeeClient.getByDepartmentId(department.getId())));
		
		return departments;
	}
}
