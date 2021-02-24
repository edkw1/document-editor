package site.edkw.crm.dto;

import lombok.Data;

@Data
public class AuthCredentialsRequestDto {
    private String username;
    private String password;
}
