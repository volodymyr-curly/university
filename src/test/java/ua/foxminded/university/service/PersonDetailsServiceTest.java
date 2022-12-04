package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Person;
import ua.foxminded.university.repository.PersonRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedStudents;

@ExtendWith(SpringExtension.class)
@Import(PersonDetailsService.class)
class PersonDetailsServiceTest {

    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "User not found";
    private static final Person EXPECTED_PERSON = setExpectedStudents().get(0);

    @Autowired
    private UserDetailsService personDetailsService;

    @MockBean
    private PersonRepository personRepository;

    @Test
    void givenUser() {
        when(personRepository.findByEmail(EXPECTED_PERSON.getEmail()))
            .thenReturn(Optional.of(EXPECTED_PERSON));

        UserDetails actualPerson = personDetailsService.loadUserByUsername(EXPECTED_PERSON.getEmail());

        assertEquals(actualPerson.getUsername(), EXPECTED_PERSON.getEmail());
        verify(personRepository, times(INVOCATION_NUMBER)).findByEmail(EXPECTED_PERSON.getEmail());
    }


    @Test
    void givenStudent_whenAddStudent_thenEntityExistsException() {
        when(personRepository.findByEmail(EXPECTED_PERSON.getEmail()))
            .thenThrow(new UsernameNotFoundException(MESSAGE));

        assertThrows(UsernameNotFoundException.class, () -> personDetailsService
            .loadUserByUsername(EXPECTED_PERSON.getEmail()));
    }
}
