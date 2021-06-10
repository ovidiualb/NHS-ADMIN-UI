package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.model.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PatientService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    public Patient add(Patient patient) {
        ResponseEntity<Patient> patientResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getPatientsUri(),
                        HttpMethod.POST,
                        new HttpEntity<>(patient, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Patient.class);
        return patientResponse.getBody();
    }

    public Patient findByCnp(String cnp) {
        ResponseEntity<Patient> patientResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getPatientsUri() + "/by-cnp/" + cnp,
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Patient.class);
        Patient patientDTO = patientResponse.getBody();
        if (patientDTO != null) {
            return patientDTO;
        } else {
            throw new GlobalNotFoundException("PATIENTS");
        }
    }

    public Patient update(Patient newPatient) {
        Patient patientDTO = findByCnp(newPatient.getCnp());
        if (patientDTO != null) {
            restTemplate.exchange(
                    restConfig.getServerUrl() + restConfig.getPatientsUri(),
                    HttpMethod.PUT,
                    new HttpEntity<>(newPatient, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                    Patient.class);
            return findByCnp(patientDTO.getCnp());
        } else {
            throw new GlobalNotFoundException("PATIENTS");
        }
    }

    public Patient deleteByCnp(String cnp) {
        Patient patientDTO = findByCnp(cnp);
        if (patientDTO != null) {
            ResponseEntity<Patient> patientResponse =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getPatientsUri() + "/by-cnp/" + cnp,
                            HttpMethod.DELETE,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            Patient.class);
            return patientResponse.getBody();
        } else {
            throw new GlobalNotFoundException("PATIENTS");
        }
    }
}
