package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.domain.Person;
import ru.job4j.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PersonService {

    private final PersonRepository store;

    public Person save(Person person) {
        return store.save(person);
    }

    public void delete(Person person) {
        store.delete(person);
    }

    public List<Person> findAll() {
        return store.findAll();
    }

    public Optional<Person> findById(int id) {
        return store.findById(id);
    }
}
