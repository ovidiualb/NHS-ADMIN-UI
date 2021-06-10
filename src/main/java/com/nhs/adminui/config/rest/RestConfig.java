package com.nhs.adminui.config.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class RestConfig {

    @Value("${nhs.rest.server-url}")
    private String serverUrl;

    @Value("${nhs.rest.roles-uri}")
    private String rolesUri;

    @Value("${nhs.rest.admins-uri}")
    private String adminsUri;

    @Value("${nhs.rest.client-apps-uri}")
    private String clientAppsUri;

    @Value("${nhs.rest.institutions-uri}")
    private String institutionsUri;

    @Value("${nhs.rest.doctors-uri}")
    private String doctorsUri;

    @Value("${nhs.rest.nurses-uri}")
    private String nursesUri;

    @Value("${nhs.rest.patients-uri}")
    private String patientsUri;

    @Value("${nhs.rest.credentials}")
    private String credentials;

    public HttpHeaders buildAuthHeaders(String credentials) {
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(
                "Authorization",
                "Basic " + buildEncodedCredentials(credentials));
        return authHeaders;
    }

    public String buildEncodedCredentials(String credentials) {
        return new String(Base64.getEncoder().encode(credentials.getBytes()));
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getRolesUri() {
        return rolesUri;
    }

    public void setRolesUri(String rolesUri) {
        this.rolesUri = rolesUri;
    }

    public String getAdminsUri() {
        return adminsUri;
    }

    public void setAdminsUri(String adminsUri) {
        this.adminsUri = adminsUri;
    }

    public String getClientAppsUri() {
        return clientAppsUri;
    }

    public void setClientAppsUri(String clientAppsUri) {
        this.clientAppsUri = clientAppsUri;
    }

    public String getInstitutionsUri() {
        return institutionsUri;
    }

    public void setInstitutionsUri(String institutionsUri) {
        this.institutionsUri = institutionsUri;
    }

    public String getDoctorsUri() {
        return doctorsUri;
    }

    public void setDoctorsUri(String doctorsUri) {
        this.doctorsUri = doctorsUri;
    }

    public String getNursesUri() {
        return nursesUri;
    }

    public void setNursesUri(String nursesUri) {
        this.nursesUri = nursesUri;
    }

    public String getPatientsUri() {
        return patientsUri;
    }

    public void setPatientsUri(String patientsUri) {
        this.patientsUri = patientsUri;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}
