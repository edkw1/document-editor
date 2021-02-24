package site.edkw.crm.dto;

import lombok.Data;

@Data
public class AuthJWTResponseDto {
    private String username;
    private String token;
}
