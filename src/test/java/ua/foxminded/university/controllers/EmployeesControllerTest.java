package ua.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.domain.Employee;
import ua.foxminded.university.util.exceptions.ServiceException;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.service.interfaces.EmployeeService;

import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedDepartments;
import static ua.foxminded.university.data.EntityData.setExpectedEmployees;

@ExtendWith(MockitoExtension.class)
class EmployeesControllerTest {

    private static final String EMPLOYEES_URL = "/employees";
    private static final String EMPLOYEES_BY_DEPARTMENT_URL = "/employees/department/list/?id=1";
    private static final String EMPLOYEE_URL = "/employees/5";
    private static final String SAVE_URL = "/employees/save";
    private static final String EDIT_URL = "/employees/5/edit";
    private static final String DELETE_URL = "/employees/5/delete";

    private static final String ALL_VIEW = "employees/all";
    private static final String BY_DEPARTMENT_ID_VIEW = "employees/by-id";
    private static final String SHOW_VIEW = "employees/show";
    private static final String EDIT_VIEW = "employees/edit";
    private static final String REDIRECT = "redirect:/employees";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String EMPLOYEES_ATTRIBUTE = "employees";
    private static final String EMPLOYEE_ATTRIBUTE = "employee";
    private static final String DEPARTMENTS_ATTRIBUTE = "departments";
    private static final String DEPARTMENT_ATTRIBUTE = "department";

    private static final int DEPARTMENT_ID = 1;
    private static final int EMPLOYEE_ID = 5;

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private EmployeesController employeesController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeesController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .build();
    }

    @Test
    void whenFindEmployees_thenAllView() throws Exception {
        when(employeeService.findAll()).thenReturn(setExpectedEmployees());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEES_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEE_ATTRIBUTE, new Employee()))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEES_ATTRIBUTE, setExpectedEmployees()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENT_ATTRIBUTE, new Department()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()));
    }

    @Test
    void whenFindEmployees_thenServiceException() throws Exception {
        when(employeeService.findAll()).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEES_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindEmployees_thenServerError() throws Exception {
        when(employeeService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEES_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindEmployeesByDepartmentId_thenByDepartmentIdView() throws Exception {
        when(employeeService.findByDepartment(DEPARTMENT_ID)).thenReturn(setExpectedEmployees());
        when(departmentService.findAll()).thenReturn(setExpectedDepartments());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEES_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_DEPARTMENT_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEE_ATTRIBUTE, new Employee()))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEES_ATTRIBUTE, setExpectedEmployees()))
            .andExpect(MockMvcResultMatchers.model().attribute(DEPARTMENTS_ATTRIBUTE, setExpectedDepartments()));
    }

    @Test
    void whenFindEmployeesByDepartmentId_thenServiceException() throws Exception {
        when(employeeService.findByDepartment(DEPARTMENT_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEES_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindEmployeesByDepartmentId_thenServerError() throws Exception {
        when(employeeService.findByDepartment(DEPARTMENT_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEES_BY_DEPARTMENT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindEmployee_thenShowView() throws Exception {
        when(employeeService.find(setExpectedEmployees().get(0).getId()))
            .thenReturn(setExpectedEmployees().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEE_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(SHOW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEE_ATTRIBUTE, setExpectedEmployees().get(0)));
    }

    @Test
    void whenFindEmployee_thenServiceException() throws Exception {
        when(employeeService.find(EMPLOYEE_ID)).thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindEmployee_thenServerError() throws Exception {
        when(employeeService.find(EMPLOYEE_ID)).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EMPLOYEE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenSaveNewEmployee_thenNewEmployeeInRedirectView() throws Exception {
        ArgumentCaptor<Employee> employeeCapture = ArgumentCaptor.forClass(Employee.class);
        doNothing().when(employeeService).add(employeeCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name("redirect:/employees"));
    }

    @Test
    void whenSaveNewEmployee_thenServiceException() throws Exception {
        ArgumentCaptor<Employee> employeeCapture = ArgumentCaptor.forClass(Employee.class);
        doThrow(new ServiceException()).when(employeeService).add(employeeCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenSaveNewEmployee_thenServerError() throws Exception {
        ArgumentCaptor<Employee> employeeCapture = ArgumentCaptor.forClass(Employee.class);
        doThrow(new RuntimeException()).when(employeeService).add(employeeCapture.capture());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(SAVE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindEmployeeToUpdate_thenEditView() throws Exception {
        when(employeeService.find(setExpectedEmployees().get(0).getId()))
            .thenReturn(setExpectedEmployees().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attribute(EMPLOYEE_ATTRIBUTE, setExpectedEmployees().get(0)));
    }

    @Test
    void whenFindEmployeeToUpdate_thenServiceException() throws Exception {
        when(employeeService.find(EMPLOYEE_ID))
            .thenThrow(new ServiceException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindEmployeeToUpdate_thenServerError() throws Exception {
        when(employeeService.find(EMPLOYEE_ID))
            .thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenUpdateEmployee_thenUpdatedEmployeeRedirectView() throws Exception {
        doNothing().when(employeeService).update(EMPLOYEE_ID, new Employee());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EMPLOYEE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT))
            .andExpect(MockMvcResultMatchers.model().attributeExists(EMPLOYEE_ATTRIBUTE));
    }

    @Test
    void whenUpdateEmployee_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(employeeService).update(EMPLOYEE_ID, new Employee());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EMPLOYEE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateEmployee_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(employeeService).update(EMPLOYEE_ID, setExpectedEmployees().get(0));

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EMPLOYEE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteEmployee_thenNoEmployeeInRedirectView() throws Exception {
        doNothing().when(employeeService).delete(EMPLOYEE_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT));

    }

    @Test
    void whenDeleteEmployee_thenServiceException() throws Exception {
        doThrow(new ServiceException()).when(employeeService).delete(EMPLOYEE_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteEmployee_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(employeeService).delete(EMPLOYEE_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
