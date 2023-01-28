package com.javaguides.springboot.repository;

import com.javaguides.springboot.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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

    // JUnit test for get employee by id operation
    @DisplayName(("JUnt test for get employee by id operation."))
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given ->  precondition or setup
        Employee employee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();

        employeeRepository.save(employee);

        // when -> action or the behavior that we are testing
        Optional<Employee> employee2 = employeeRepository.findById(employee.getId());

        // then -> verify the output
        Assertions.assertThat(employee2).isNotNull();

    }


    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenEmployeeObject() {
        // given ->  precondition or setup
        Employee employee = Employee.builder()
                .firstName("Asif")
                .lastName("Akbar")
                .email("asif@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when -> action or the behavior that we are testing
        Employee employeeObject = employeeRepository.findByEmail(employee.getEmail()).get();

        // then -> verify the output
        Assertions.assertThat(employeeObject).isNotNull();
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given ->  precondition or setup
        Employee employee = Employee.builder()
                .firstName("Asif")
                .lastName("Akbar")
                .email("asif@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when -> action or the behavior that we are testing
        Employee savedemployee = employeeRepository.findById(employee.getId()).get();
        savedemployee.setEmail("akbar@gmail.com");
        savedemployee.setFirstName("Ali");
        Employee updatedEmployee = employeeRepository.save(savedemployee);

        // then -> verify the output
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("akbar@gmail.com");
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Ali");
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given ->  precondition or setup
        Employee employee = Employee.builder()
                .firstName("Asif")
                .lastName("Akbar")
                .email("asif@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when -> action or the behavior that we are testing
        // employeeRepository.delete(employee);
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then -> verify the output
        Assertions.assertThat(employeeOptional).isEmpty();
    }

}
