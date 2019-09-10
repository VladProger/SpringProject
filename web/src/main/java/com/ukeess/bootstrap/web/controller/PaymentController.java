package com.ukeess.bootstrap.web.controller;

import com.braintreegateway.*;
import com.ukeess.bootstrap.web.dto.JwtAuthenticationResponse;
import com.ukeess.bootstrap.web.dto.LoginRequest;
import com.ukeess.bootstrap.web.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/buy")
public class PaymentController {
    @Autowired
    AuthService authService;
    private static BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "6znmrtbzhbd6pkcf",
            "w34rjsxcr56r2hc5",
            "b282132e93ad53bf990e1468138d3b5c"
    );

    @PostMapping("/signin")
    @ResponseStatus(OK)
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }
}
