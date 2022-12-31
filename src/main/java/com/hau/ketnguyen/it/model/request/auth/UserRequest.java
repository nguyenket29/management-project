package com.hau.ketnguyen.it.model.request.auth;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class UserRequest extends SearchRequest{
    private String username;
    private Short status;
    private String email;
    private String type;
    private List<String> listRole;
}
