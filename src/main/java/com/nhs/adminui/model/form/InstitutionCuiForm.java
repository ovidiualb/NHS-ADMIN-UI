package com.nhs.adminui.model.form;

import javax.validation.constraints.NotEmpty;

public class InstitutionCuiForm {

    @NotEmpty(message = "CUI CANNOT BE EMPTY")
    private String cui;

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }
}
