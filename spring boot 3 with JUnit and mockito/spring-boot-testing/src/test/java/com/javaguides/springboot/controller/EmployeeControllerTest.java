package com.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaguides.springboot.model.Employee;
import com.javaguides.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc; // --> tells Controllers without needing to start an HTTP server.

    @MockBean
    // --> tells spring to create a mock instance of Service and add it to the application context, so that it's injected into Controller
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;  // serialize and deserialize java object


    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given -- precondition or setup
        Employee employee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when -- action or behavior that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",  // $ -- > {}
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));

    }

    // JUnit test for getAll employees
    @DisplayName("JUnit test for getAll employees")
    @Test
    public void givenListOfEmployees_whenGetAllEmployee_thenReturnEmployeesList() throws Exception {
        // given ->  precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Golam Rabbani").lastName("Rishad").email("gr.rishad@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Sultan").lastName("Mahmud").email("sultan@gmail.com").build());

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when -> action or the behavior that we are testing
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        // then -> verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size()))); //  $ --> []

    }

    // positive scenario - valid employee id
    // JUnit test for getEmployeeById REST API
    @DisplayName("JUnit test for getEmployeeById REST API - positive scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given ->  precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when -> action or the behavior that we are testing
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        // then -> verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    // negative scenario - valid employee id
    // JUnit test for getEmployeeById REST API
    @DisplayName("JUnit test for getEmployeeById REST API - negative scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given ->  precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when -> action or the behavior that we are testing
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        // then -> verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    // JUnit test for updateEmployee API -- positive scenario
    @DisplayName("JUnit test for updateEmployee API")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given ->  precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();

        Employee updateEmployee = Employee.builder()
                .firstName("Sultan")
                .lastName("Mahmud")
                .email("sultan@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        // when -> action or the behavior that we are testing
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // then -> verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updateEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updateEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updateEmployee.getEmail())));
    }

    // JUnit test for updateEmployee API -- negative scenario
    @DisplayName("JUnit test for updateEmployee API negative scenario")
    @Test
    public void givenUpdatedInvalidEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given ->  precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Golam Rabbani")
                .lastName("Rishad")
                .email("gr.rishad@gmail.com")
                .build();

        Employee updateEmployee = Employee.builder()
                .firstName("Sultan")
                .lastName("Mahmud")
                .email("sultan@gmail.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        // when -> action or the behavior that we are testing
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        // then -> verify the output
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    // JUnit test for delete Employee REST API
    @DisplayName("JUnit test for delete Employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given ->  precondition or setup
        long employeeId = 1L;
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when -> action or the behavior that we are testing
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{iid}", employeeId));

        // then -> verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
