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

    public Optional<Person> save(Person person) {
        return Optional.of(store.save(person));
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

    public Optional<Person> update(Person personUpd) {
        Optional<Person> person = findById(personUpd.getId());
        if (person.isPresent()) {
            person = save(personUpd);
        }
        return person;
    }
    public Optional<Person> findByLogin(String login) {
        return store.findByLogin(login);
    }
}
