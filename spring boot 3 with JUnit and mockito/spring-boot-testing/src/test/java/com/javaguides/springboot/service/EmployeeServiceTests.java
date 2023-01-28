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
}
