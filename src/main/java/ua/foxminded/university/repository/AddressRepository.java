package ua.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.university.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findByPersonId(int id);
}
