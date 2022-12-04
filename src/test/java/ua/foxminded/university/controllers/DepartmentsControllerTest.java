package ua.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Errors;
import ua.foxminded.university.domain.Department;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.department.DepartmentResponseDTO;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.service.interfaces.DepartmentService;
import ua.foxminded.university.util.mappers.DepartmentMapper;
import ua.foxminded.university.util.mappers.FacultyMapper;
import ua.foxminded.university.util.validators.DepartmentValidator;

import java.util.Collections;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentsControllerTest {

    private static final String DEPARTMENTS_URL = "/departments";
    private static final String DEPARTMENT_BY_FACULTY_URL = "/departments/faculty/list/?id=1";
    private static final String CREATE_URL = "/departments/new";
    private static final String EDIT_URL = "/departments/1/edit";
    private static final String UPDATE_URL = "/departments/1";
    private static final String SHOW_URL = "/departments/1";
    private static final String DELETE_URL = "/departments/1/delete";

    private static final String ALL_VIEW = "departments/all";
    private static final String BY_ID_VIEW = "departments/by-id";
    private static final String NEW_VIEW = "departments/new";
    private static final String EDIT_VIEW = "departments/edit";
    private static final String SHOW_VIEW = "departments/show";
    private static final String REDIRECT_VIEW = "redirect:/departments";
    private static final String NOT_FOUND_VIEW = "errors/not-found";
    private static final String GENERAL_ERROR_VIEW = "errors/general-error";

    private static final String DEPARTMENTS_ATTRIBUTE = "departments";
    private static final String DEPARTMENT_ATTRIBUTE = "department";
    private static final String FACULTIES_ATTRIBUTE = "faculties";
    private static final String FACULTY_ATTRIBUTE = "faculty";

    private static final int ACTUAL_ID = 1;

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private DepartmentValidator departmentValidator;

    @Mock
    private FacultyMapper facultyMapper;

    @InjectMocks
    private DepartmentsController departmentsController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(departmentsController)
            .setControllerAdvice(GlobalExceptionHandler.class)
            .setValidator(departmentValidator)
            .build();
    }

    @Test
    void whenFindDepartments_thenAllView() throws Exception {
        when(departmentService.findAll()).thenReturn(Collections.singletonList(new Department()));
        when(departmentMapper.convertToDepartmentResponseDTO(new Department()))
            .thenReturn(new DepartmentResponseDTO());
        when(facultyMapper.getFacultyDTO()).thenReturn(Collections.singletonList(new FacultyRequestDTO()));

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(ALL_VIEW))
            .andExpect(MockMvcResultMatchers.model()
                .attribute(DEPARTMENTS_ATTRIBUTE, Collections.singletonList(new DepartmentResponseDTO())))
            .andExpect(MockMvcResultMatchers.model()
                .attribute(FACULTIES_ATTRIBUTE, Collections.singletonList(new FacultyRequestDTO())))
            .andExpect(MockMvcResultMatchers.model().attribute(FACULTY_ATTRIBUTE, new FacultyRequestDTO()));
    }

    @Test
    void whenFindDepartments_thenEntityNotFoundException() throws Exception {
        when(departmentService.findAll()).thenThrow(new EntityNotFoundException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindDepartments_thenServerError() throws Exception {
        when(departmentService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenFindByFaculty_thenByIdView() throws Exception {
        when(departmentService.findByFaculty(ACTUAL_ID)).thenReturn(Collections.singletonList(new Department()));
        when(departmentMapper.convertToDepartmentResponseDTO(new Department()))
            .thenReturn(new DepartmentResponseDTO());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DEPARTMENT_BY_FACULTY_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(BY_ID_VIEW))
            .andExpect(MockMvcResultMatchers.model()
                .attribute(DEPARTMENTS_ATTRIBUTE, Collections.singletonList(new DepartmentResponseDTO())));
    }

    @Test
    void whenFindByFaculty_thenEntityNotFoundException() throws Exception {
        when(departmentService.findAll()).thenThrow(new EntityNotFoundException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenFindByFaculty_thenServerError() throws Exception {
        when(departmentService.findAll()).thenThrow(new RuntimeException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenNewDepartment_thenCreatePage() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.get(CREATE_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(NEW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DEPARTMENT_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attributeExists(FACULTIES_ATTRIBUTE));
    }

    @Test
    void whenSaveNewDepartment_thenNewDepartmentInRedirectView() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.post(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DEPARTMENT_ATTRIBUTE));
    }

    @Test
    void whenSaveNewDepartment_thenServerError() throws Exception {
        doThrow(RuntimeException.class).when(departmentService).add(any(Department.class));
        this.mockMvc
            .perform(MockMvcRequestBuilders.post(DEPARTMENTS_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenShowDepartment_thenDepartmentView() throws Exception {
        given(departmentService.find(ACTUAL_ID)).willReturn(new Department());
        given(departmentMapper.convertToDepartmentResponseDTO(new Department()))
            .willReturn(new DepartmentResponseDTO());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(SHOW_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(SHOW_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DEPARTMENT_ATTRIBUTE));
    }

    @Test
    void whenShowDepartment_thenEntityNotFoundException() throws Exception {
        given(departmentService.find(ACTUAL_ID)).willThrow(new EntityNotFoundException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenEditDepartment_thenEditView() throws Exception {
        given(departmentService.find(ACTUAL_ID)).willReturn(new Department());
        given(departmentMapper.convertToDepartmentRequestDTO(new Department()))
            .willReturn(new DepartmentRequestDTO());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name(EDIT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DEPARTMENT_ATTRIBUTE))
            .andExpect(MockMvcResultMatchers.model().attributeExists(FACULTIES_ATTRIBUTE));
    }

    @Test
    void whenEditDepartment_thenEntityNotFoundException() throws Exception {
        given(departmentService.find(ACTUAL_ID)).willThrow(new EntityNotFoundException());

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateDepartment_thenUpdatedDepartmentRedirectView() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.post(UPDATE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT_VIEW))
            .andExpect(MockMvcResultMatchers.model().attributeExists(DEPARTMENT_ATTRIBUTE));
    }

    @Test
    void whenUpdateDepartment_thenEntityNotFoundException() throws Exception {
        willDoNothing().given(departmentValidator).validate(any(), any(Errors.class));
        given(departmentMapper.convertToDepartment(any(DepartmentRequestDTO.class))).willReturn(new Department());
        willThrow(new EntityNotFoundException()).given(departmentService).update(ACTUAL_ID, new Department());

        this.mockMvc
            .perform(MockMvcRequestBuilders.post(UPDATE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenUpdateDepartment_thenServerError() throws Exception {
        this.mockMvc
            .perform(MockMvcRequestBuilders.post(EDIT_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }

    @Test
    void whenDeleteDepartment_thenNoDepartmentInRedirectView() throws Exception {
        doNothing().when(departmentService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.view().name(REDIRECT_VIEW));
    }

    @Test
    void whenDeleteDepartment_thenEntityNotFoundException() throws Exception {
        doThrow(new EntityNotFoundException()).when(departmentService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.view().name(NOT_FOUND_VIEW));
    }

    @Test
    void whenDeleteDepartment_thenServerError() throws Exception {
        doThrow(new RuntimeException()).when(departmentService).delete(ACTUAL_ID);

        this.mockMvc
            .perform(MockMvcRequestBuilders.get(DELETE_URL))
            .andExpect(MockMvcResultMatchers.status().is5xxServerError())
            .andExpect(MockMvcResultMatchers.view().name(GENERAL_ERROR_VIEW));
    }
}
