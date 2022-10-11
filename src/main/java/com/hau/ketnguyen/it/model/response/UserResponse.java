package com.hau.ketnguyen.it.model.response;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserResponse {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String address;
    private Short status;
    private String avatar;
    private Date birthday;
    private Short gender;
    private Set<String> authorities;

    public UserResponse(Integer id, String username, String firstName, String lastName, String address,
                        Short status, String avatar, Date birthday, Short gender, Set<String> authorities) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.status = status;
        this.avatar = avatar;
        this.birthday = birthday;
        this.gender = gender;
        this.authorities = authorities;
    }
}
