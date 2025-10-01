package com.suraj.smartgroceryapp.dto;

import com.suraj.smartgroceryapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninResponse {
    private String token;
    private User user;


}

