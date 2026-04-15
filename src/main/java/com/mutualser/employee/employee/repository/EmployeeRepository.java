package com.mutualser.employee.employee.repository;

import com.mutualser.employee.employee.model.Employee;
import com.mutualser.employee.employee.model.enumeration.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByAgeGreaterThanEqual(Integer age);

    List<Employee> findByGender(Gender gender);
}
