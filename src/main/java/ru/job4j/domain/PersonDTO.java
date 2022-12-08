package ru.job4j.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PersonDTO {
    @NotBlank
    private String login;
}
