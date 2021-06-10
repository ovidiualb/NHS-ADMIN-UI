package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.model.dto.ClientAppDTO;
import com.nhs.adminui.model.entity.ClientApp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Service
public class ClientAppService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ClientApp add(ClientAppDTO clientAppDTO, String roleName) {
        ResponseEntity<ClientApp> clientAppResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getClientAppsUri() + "/with-role-name/" + roleName,
                        HttpMethod.POST,
                        new HttpEntity<>(clientAppDTO, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        ClientApp.class);
        return clientAppResponse.getBody();
    }

    public ClientApp findByName(String name) {
        ResponseEntity<ClientApp> clientAppResponse =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getClientAppsUri() + "/by-name/" + name,
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        ClientApp.class);
        ClientApp clientApp = clientAppResponse.getBody();
        if (clientApp != null) {
            return clientApp;
        } else {
            throw new GlobalNotFoundException("CLIENT APP");
        }
    }

    public ClientApp update(@Valid ClientApp clientApp, String roleName) {
        ClientApp databaseClientApp = findByName(clientApp.getName());
        if (databaseClientApp != null) {
            ResponseEntity<ClientApp> clientAppResponse =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getClientAppsUri() + "/with-role-name/" + roleName,
                            HttpMethod.PUT,
                            new HttpEntity<>(clientApp, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            ClientApp.class);
            return clientAppResponse.getBody();
        } else {
            throw new GlobalNotFoundException("CLIENT APP");
        }
    }

    public ClientApp deleteByName(String name) {
        ClientApp databaseClientApp = findByName(name);
        if (databaseClientApp != null) {
            ResponseEntity<ClientApp> clientAppResponse =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getClientAppsUri() + "/by-name/" + name,
                            HttpMethod.DELETE,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            ClientApp.class);
            return clientAppResponse.getBody();
        } else {
            throw new GlobalNotFoundException("CLIENT APP");
        }
    }
}
