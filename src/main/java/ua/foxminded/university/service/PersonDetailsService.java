package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.foxminded.university.domain.Person;
import ua.foxminded.university.repository.PersonRepository;
import ua.foxminded.university.security.PersonDetails;

import java.util.Optional;

@Slf4j
@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByEmail(email);

        if (person.isEmpty()) {
            log.error("Not found by email {}", email);
            throw new UsernameNotFoundException("User not found");
        }

        return new PersonDetails(person.get());
    }
}
