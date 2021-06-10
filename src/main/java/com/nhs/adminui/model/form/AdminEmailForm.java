package com.nhs.adminui.model.form;

import javax.validation.constraints.Email;

public class AdminEmailForm {

    @Email(regexp = ".+@.+\\.\\w+", message = "INVALID EMAIL ADDRESS")
    private String email; // Default validation through html "type" attribute (i.e. the "email" type) is not enough.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
