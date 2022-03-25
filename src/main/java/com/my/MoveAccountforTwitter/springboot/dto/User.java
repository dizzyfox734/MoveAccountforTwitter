package com.my.MoveAccountforTwitter.springboot.dto;

import lombok.Builder;

public class User {

    private String userPrincipal;
    private String userName;


    @Builder
    public User(String userPrincipal, String userName) {
        this.userPrincipal = userPrincipal;
        this.userName = userName;
    }
}
