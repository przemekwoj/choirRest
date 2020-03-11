package com.przemek.choir.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Chorister {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank
    private String name;
    @Column(name = "phone_number")
    @Pattern(regexp = "[0-9]{9}")
    private String phoneNumber;

    public Chorister(@NotBlank String name, @Pattern(regexp = "[0-9]{9}") String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
