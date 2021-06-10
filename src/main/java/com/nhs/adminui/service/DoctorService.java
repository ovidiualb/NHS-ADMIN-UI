package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.model.entity.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DoctorService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    public Doctor add(Doctor doctor) {
        ResponseEntity<Doctor> doctorResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getDoctorsUri(),
                        HttpMethod.POST,
                        new HttpEntity<>(doctor, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Doctor.class);
        return doctorResponse.getBody();
    }

    public String[] getSpecialties() {
        ResponseEntity<String[]> doctorResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getDoctorsUri() + "/specialty",
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        String[].class);

        return doctorResponse.getBody();
    }

    public String[] getTitles() {
        ResponseEntity<String[]> doctorResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getDoctorsUri() + "/title",
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        String[].class);

        return doctorResponse.getBody();
    }

    public Doctor findByCnp(String Cnp) {
        ResponseEntity<Doctor> doctorResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getDoctorsUri() + "/by-cnp/" + Cnp,
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Doctor.class);
        Doctor doctorDTO = doctorResponse.getBody();
        if (doctorDTO != null) {
            return doctorDTO;
        } else {
            throw new GlobalNotFoundException("DOCTORS");
        }
    }

    public Doctor update(Doctor newDoctor) {
        if (newDoctor != null) {
            restTemplate.exchange(
                    restConfig.getServerUrl() + restConfig.getDoctorsUri(),
                    HttpMethod.PUT,
                    new HttpEntity<>(newDoctor, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                    Doctor.class);
            return findByCnp(newDoctor.getCnp());
        } else {
            throw new GlobalNotFoundException("DOCTOR");
        }
    }

    public Doctor deleteByCnp(String Cnp) {
        Doctor doctorDTO = findByCnp(Cnp);
        if (doctorDTO != null) {
            ResponseEntity<Doctor> doctorResponse =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getDoctorsUri() + "/by-cnp/" + Cnp,
                            HttpMethod.DELETE,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            Doctor.class);
            return doctorResponse.getBody();
        } else {
            throw new GlobalNotFoundException("DOCTOR");
        }
    }
}
