package com.microservices.employeeservice.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.microservices.employeeservice.model.Employee;


@Repository
public class EmployeeRepository {
	private List<Employee> employees = new ArrayList<>();
	
	
	public Employee addEmployee(Employee employee) {
		employees.add(employee);
		return employee;
	}
	
	public Employee findById(Long id) {
		return employees.stream().filter(department->department.id().equals(id)).findFirst().orElseThrow();
	}
	
	public List<Employee> findAll(){
		return employees;
	}
	
	public List<Employee> findByDepartment(Long departmentId){
		return employees.stream().filter(employee->employee.departmentId().equals(departmentId)).toList();
	}
}
