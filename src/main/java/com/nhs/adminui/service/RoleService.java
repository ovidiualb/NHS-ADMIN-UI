package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.exception.technical.GlobalRequestFailedException;
import com.nhs.adminui.model.dto.RoleDTO;
import com.nhs.adminui.model.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ServiceUtil serviceUtil;

    public RoleDTO findByName(String name) {
        try {
            ResponseEntity<RoleDTO> responseRoleDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getRolesUri() + "/by-name/" + name,
                            HttpMethod.GET,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            RoleDTO.class);
            return responseRoleDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("ROLE");
            } else {
                serviceUtil.logTechnicalWarning("ROLE", ex);
                throw new GlobalRequestFailedException("ROLE");
            }
        }
    }

    public List<Role> getRolesList() {
        ParameterizedTypeReference<List<Role>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<Role>> roleResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getRolesUri() + "/all",
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        responseType);
        return roleResponse.getBody();
    }
}
