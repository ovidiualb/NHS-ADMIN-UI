package com.nhs.adminui.model.dto;

import java.util.List;

public class ClientAppDTO {

    private int id;

    private String name;

    private String password;

    private int status;

    List<String> rolesList;

    String selectedRoleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<String> rolesList) {
        this.rolesList = rolesList;
    }

    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }
}
