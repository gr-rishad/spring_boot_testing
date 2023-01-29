package com.javaguides.springboot.service;

import com.javaguides.springboot.exception.ResourceNotFundException;
import com.javaguides.springboot.model.Employee;
import com.javaguides.springboot.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {


    /*
    || -->  dependency injection using mock() method --> Mockito.mock()
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;
    @BeforeEach
    public void setup() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }
 */

    // dependency inject using Annotation
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("GOlam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();
    }


    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for savedEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given ->  precondition or setup
        // what saveEmployee() method internally return ---> employeeRepository.findByEmail() & employeeRepository.save()
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when -> action or the behavior that we are testing
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then -> verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for savedEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given ->  precondition or setup
        // what saveEmployee() method internally return ---> employeeRepository.findByEmail() & employeeRepository.save()
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));
        //   BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when -> action or the behavior that we are testing
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then --> verify the output
        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // given ->  precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when -> action or the behavior that we are testing
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then -> verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given ->  precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("tony@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when -> action or the behavior that we are testing
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then -> verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit test for getEmployeeId method
    @DisplayName("JUnit test for getEmployeeId method")
    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() {

        // given ->  precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when -> action or the behavior that we are testing
        Employee savedEmployee=employeeService.getEmployeeById(employee.getId()).get();

        // then -> verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
    }
}
