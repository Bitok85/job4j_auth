package ru.job4j.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Person;

import java.util.List;
@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    List<Person> findAll();
}
