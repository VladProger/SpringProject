package com.ukeess.bootstrap.web.service;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.CustomerRequest;
import com.braintreegateway.Environment;
import com.ukeess.bootstrap.web.dto.JwtAuthenticationResponse;
import com.ukeess.bootstrap.web.dto.LoginRequest;
import com.ukeess.bootstrap.web.dto.SignUpRequest;
import com.ukeess.bootstrap.web.exception.AppException;
import com.ukeess.bootstrap.web.exception.ConflictException;
import com.ukeess.bootstrap.web.model.Role;
import com.ukeess.bootstrap.web.model.RoleName;
import com.ukeess.bootstrap.web.model.User;
import com.ukeess.bootstrap.web.repository.RoleRepository;
import com.ukeess.bootstrap.web.repository.UserRepository;
import com.ukeess.bootstrap.web.security.JwtTokenProvider;
import com.ukeess.bootstrap.web.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class AuthService {

    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "6znmrtbzhbd6pkcf",
            "w34rjsxcr56r2hc5",
            "b282132e93ad53bf990e1468138d3b5c"
    );
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        log.info("User with [email: {}] has logged in", userPrincipal.getEmail());

        return new JwtAuthenticationResponse(jwt);
    }

    public Long registerUser(SignUpRequest signUpRequest) {

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ConflictException("Email [email: " + signUpRequest.getEmail() + "] is already taken");
        }

        User user = new User(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set. Add default roles to database."));

        user.setRoles(Collections.singleton(userRole));

        log.info("Successfully registered user with [email: {}]", user.getEmail());
        CustomerRequest request = new CustomerRequest()
                .firstName(user.getName())
                .email(user.getEmail())
                .paymentMethodNonce("fake-valid-visa-nonce");
        gateway.customer().create(request);

        return userRepository.save(user).getId();
        }
}
