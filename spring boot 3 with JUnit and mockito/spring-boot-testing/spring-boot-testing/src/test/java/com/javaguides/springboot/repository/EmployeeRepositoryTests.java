package com.javaguides.springboot.repository;

import com.javaguides.springboot.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    // JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // given -> precondition or setup
        Employee employee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();

        // when -> action or behavior that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then -> verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for get all employees operation
    @DisplayName("Junit test for findAll Employees operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeesList() {
        // given ->  precondition or setup
        Employee employee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();

        Employee employee1 = Employee.builder()
                .firstName("Tamim")
                .lastName("Iqbal")
                .email("t@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when -> action or the behavior that we are testing
        List<Employee> employeeList = employeeRepository.findAll();

        // then -> verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

}
