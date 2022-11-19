package com.hau.ketnguyen.it.model.request.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginRequest {
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
}
