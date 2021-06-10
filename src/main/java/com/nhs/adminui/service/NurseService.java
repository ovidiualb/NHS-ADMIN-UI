package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.model.entity.Nurse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NurseService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    public Nurse add(Nurse nurse) {
        ResponseEntity<Nurse> nurseResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getNursesUri(),
                        HttpMethod.POST,
                        new HttpEntity<>(nurse, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Nurse.class);
        return nurseResponse.getBody();
    }

    public Nurse findByCnp(String cnp) {
        ResponseEntity<Nurse> nurseResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getNursesUri() + "/by-cnp/" + cnp,
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Nurse.class);
        Nurse nurseDTO = nurseResponse.getBody();
        if (nurseDTO != null) {
            return nurseDTO;
        } else {
            throw new GlobalNotFoundException("NURSES");
        }
    }

    public String[] getSpecialties() {
        ResponseEntity<String[]> nurseResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getNursesUri() + "/specialty",
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        String[].class);

        return nurseResponse.getBody();
    }

    public String[] getTitles() {
        ResponseEntity<String[]> nurseResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getNursesUri() + "/title",
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        String[].class);

        return nurseResponse.getBody();
    }

    public Nurse update(Nurse newNurse) {
        Nurse nurseDTO = findByCnp(newNurse.getCnp());
        if (nurseDTO != null) {
            restTemplate.exchange(
                    restConfig.getServerUrl() + restConfig.getNursesUri(),
                    HttpMethod.PUT,
                    new HttpEntity<>(newNurse, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                    Nurse.class);
            return findByCnp(nurseDTO.getCnp());
        } else {
            throw new GlobalNotFoundException("NURSES");
        }
    }

    public Nurse deleteByCnp(String cnp) {
        Nurse nurseDTO = findByCnp(cnp);
        if (nurseDTO != null) {
            ResponseEntity<Nurse> nurseResponse =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getNursesUri() + "/by-cnp/" + cnp,
                            HttpMethod.DELETE,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            Nurse.class);
            return nurseResponse.getBody();
        } else {
            throw new GlobalNotFoundException("NURSE");
        }
    }
}
