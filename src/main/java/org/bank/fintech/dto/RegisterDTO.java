package org.bank.fintech.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RegisterDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String senha;
    
    private String role;
}
