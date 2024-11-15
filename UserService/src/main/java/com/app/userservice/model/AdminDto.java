package com.app.userservice.model;

import com.app.userservice.entity.Qualification;

public class AdminDto extends UserDto {
    private Qualification qualification;

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }
}

