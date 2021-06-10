package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.exception.technical.GlobalRequestFailedException;
import com.nhs.adminui.model.dto.InstitutionDTO;
import com.nhs.adminui.model.entity.Institution;
import com.nhs.adminui.model.form.InstitutionCreationForm;
import com.nhs.adminui.model.form.InstitutionUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class InstitutionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    @Autowired
    private ServiceUtil serviceUtil;

    public InstitutionDTO add(InstitutionCreationForm institutionCreationForm) throws Exception {
        try {
            ResponseEntity<InstitutionDTO> responseInstitutionDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getInstitutionsUri(),
                            HttpMethod.POST,
                            new HttpEntity<>(institutionCreationForm, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            InstitutionDTO.class);
            return responseInstitutionDTO.getBody();
        } catch (RestClientException ex) {
            if ((ex instanceof HttpClientErrorException.BadRequest && serviceUtil.causedByInvalid(ex)) ||
                    (ex instanceof HttpClientErrorException.BadRequest && serviceUtil.causedByDuplicate(ex))) {
                throw new Exception(serviceUtil.parseInvalidOrDuplicate(ex));
            } else {
                serviceUtil.logTechnicalWarning("INSTITUTION", ex);
                throw new GlobalRequestFailedException("INSTITUTION");
            }
        }
    }

    public InstitutionDTO findByCui(String cui) {
        try {
            ResponseEntity<InstitutionDTO> responseInstitutionDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getInstitutionsUri() + "/by-cui/" + cui,
                            HttpMethod.GET,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            InstitutionDTO.class);
            return responseInstitutionDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("INSTITUTION");
            } else {
                serviceUtil.logTechnicalWarning("INSTITUTION", ex);
                throw new GlobalRequestFailedException("INSTITUTION");
            }
        }

    }

    public ArrayList<Institution> getInstitutions() {
        ResponseEntity<ArrayList<Institution>> institutionResponseList =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getInstitutionsUri() + "/all",
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        new ParameterizedTypeReference<>() {
                        });
        return institutionResponseList.getBody();
    }

    public String[] getInstitutionTypes() {
        try {
            ResponseEntity<String[]> institutionResponse =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getInstitutionsUri() + "/types",
                            HttpMethod.GET,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            String[].class);
            return institutionResponse.getBody();
        } catch (RestClientException ex) {
            serviceUtil.logTechnicalWarning("INSTITUTION", ex);
            throw new GlobalRequestFailedException("INSTITUTION");
        }
    }

    public InstitutionDTO update(InstitutionUpdateForm institutionUpdateForm) throws Exception {
        try {
            ResponseEntity<InstitutionDTO> responseInstitutionDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getInstitutionsUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(institutionUpdateForm, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            InstitutionDTO.class);
            return responseInstitutionDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("INSTITUTION");
            } else if (ex instanceof HttpClientErrorException.BadRequest && (serviceUtil.causedByInvalid(ex) || serviceUtil.causedByDuplicate(ex))) {
                throw new Exception(serviceUtil.parseInvalidOrDuplicate(ex));
            } else {
                serviceUtil.logTechnicalWarning("INSTITUTION", ex);
                throw new GlobalRequestFailedException("INSTITUTION");
            }
        }
    }

    public InstitutionDTO deleteByCui(String cui) throws Exception {
        try {
            ResponseEntity<InstitutionDTO> responseInstitutionDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getInstitutionsUri() + "/by-cui/" + cui,
                            HttpMethod.DELETE,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            InstitutionDTO.class);
            return responseInstitutionDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("INSTITUTION");
            } else if (ex instanceof HttpClientErrorException.BadRequest && serviceUtil.causedByInvalid(ex)) {
                throw new Exception(serviceUtil.parseInvalid(ex));
            } else {
                serviceUtil.logTechnicalWarning("INSTITUTION", ex);
                throw new GlobalRequestFailedException("INSTITUTION");
            }
        }
    }
}
