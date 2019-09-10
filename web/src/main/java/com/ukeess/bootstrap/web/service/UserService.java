package com.ukeess.bootstrap.web.service;


import com.ukeess.bootstrap.web.dto.UserSummary;
import com.ukeess.bootstrap.web.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserSummary getCurrentUser(UserPrincipal userPrincipal) {
        return UserSummary.builder()
                .id(userPrincipal.getId())
                .email(userPrincipal.getEmail())
                .name(userPrincipal.getName())
                .build();
    }
}
