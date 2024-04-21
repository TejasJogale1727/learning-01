package com.microservices.departmentservice.client;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.microservices.departmentservice.model.Employee;

@HttpExchange
public interface EmployeeClient {
	@GetExchange("/employee/department/{departmentId}")
	public List<Employee> getByDepartmentId(@PathVariable Long departmentId);
}
