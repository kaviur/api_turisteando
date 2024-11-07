package com.proyecto.turisteando.dtos.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserRequestDto implements Serializable {

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 3, message = "El apellido debe tener al menos 3 caracteres")
    private String lastName;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

}
