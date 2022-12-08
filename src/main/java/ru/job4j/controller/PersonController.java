package ru.job4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.domain.Person;
import ru.job4j.domain.PersonDTO;
import ru.job4j.service.PersonService;
import ru.job4j.util.PasswordValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/persons")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    private BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private static final Logger LOG = Logger.getLogger(PersonController.class.getName());

    @GetMapping("/all")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        Optional<Person> person = this.personService.findById(id);
        if (person.isEmpty()) {
            LOG.error("No persons with this ID");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No persons with this ID");
        }
        return new ResponseEntity<>(person.get(), HttpStatus.OK);
    }

    @PutMapping("/")
    @Validated
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        Optional<Person> personRsl = personService.update(person);
        if (personRsl.isEmpty()) {
            LOG.error("No person found for update");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No person found for update");
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<Person> person = personService.findById(id);
        if (person.isEmpty()) {
            LOG.error("No persons with this id");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No persons with this id");
        }
        personService.delete(person.get());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    @Validated
    public ResponseEntity<Boolean> signUp(@Valid @RequestBody Person person) {
        String password = person.getPassword();
        if (!PasswordValidator.validate(password)) {
            LOG.error(PasswordValidator.invalidMsg());
            throw new IllegalArgumentException(PasswordValidator.invalidMsg());
        }
        person.setPassword(encoder.encode(password));
        return ResponseEntity.status(HttpStatus.OK)
                .body(personService.save(person).isPresent());
    }

    @PatchMapping("/{id}")
    @Validated
    public ResponseEntity<Person> patch(@PathVariable int id, @Valid @RequestBody PersonDTO personDTO) {
        Person person = personService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No persons with this id"));
        person.setLogin(personDTO.getLogin());
        return ResponseEntity.status(HttpStatus.OK)
                .body(person);

    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public void exceptionHandler(Exception e, HttpServletResponse res) throws IOException {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setContentType("application/json");
        res.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", PasswordValidator.invalidMsg());
            put("type", e.getClass());
        }}));
        LOG.error(e.getMessage());
    }

}
