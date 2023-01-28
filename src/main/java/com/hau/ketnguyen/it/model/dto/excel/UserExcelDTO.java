package com.hau.ketnguyen.it.model.dto.excel;

import lombok.Data;

import java.util.List;

@Data
public class UserExcelDTO {
    private String username;
    private String email;
    private List<String> roles;
    private String status;
    private String type;
}
