package site.edkw.crm.rest;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import site.edkw.crm.domain.User;
import site.edkw.crm.domain.Views;
import site.edkw.crm.dto.AuthCredentialsRequestDto;
import site.edkw.crm.dto.AuthJWTResponseDto;
import site.edkw.crm.security.jwt.JwtTokenProvider;
import site.edkw.crm.service.UserService;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping(value = "/api/v1/auth/")
public class AuthRestController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthRestController(AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider,
                              UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("login")
    public AuthJWTResponseDto login(@RequestBody AuthCredentialsRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            System.out.println(passwordEncoder.encode(requestDto.getPassword()));

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            AuthJWTResponseDto response = new AuthJWTResponseDto();
            response.setUsername(username);
            response.setToken(token);
            return response;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }


    @JsonView(Views.shortInfo.class)
    @PostMapping("register")
    public User registerUser(@RequestBody User user){
        return userService.register(user);
    }
}
