package com.nhs.adminui.service;

import com.nhs.adminui.config.rest.RestConfig;
import com.nhs.adminui.exception.business.GlobalNotFoundException;
import com.nhs.adminui.exception.technical.GlobalRequestFailedException;
import com.nhs.adminui.model.dto.AdminDTO;
import com.nhs.adminui.model.entity.Admin;
import com.nhs.adminui.model.form.AdminCreationForm;
import com.nhs.adminui.model.form.AdminUpdateForm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig restConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ServiceUtil serviceUtil;

    public AdminDTO add(AdminCreationForm adminCreationForm) throws Exception {
        Admin admin = buildAdmin(adminCreationForm);
        try {
            ResponseEntity<AdminDTO> responseAdminDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getAdminsUri(),
                            HttpMethod.POST,
                            new HttpEntity<>(admin, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            AdminDTO.class);
            return responseAdminDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.BadRequest && (serviceUtil.causedByInvalid(ex) || serviceUtil.causedByDuplicate(ex))) {
                throw new Exception(serviceUtil.parseInvalidOrDuplicate(ex));
            } else {
                serviceUtil.logTechnicalWarning("ADMIN", ex);
                throw new GlobalRequestFailedException("ADMIN");
            }
        }
    }

    public AdminDTO findByEmail(String email) {
        try {
            ResponseEntity<AdminDTO> responseAdminDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getAdminsUri() + "/by-email/" + email,
                            HttpMethod.GET,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            AdminDTO.class);
            return responseAdminDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("ADMIN");
            } else {
                serviceUtil.logTechnicalWarning("ADMIN", ex);
                throw new GlobalRequestFailedException("ADMIN");
            }
        }
    }

    public AdminUpdateForm findSensitiveByEmail(String email) {
        try {
            ResponseEntity<AdminUpdateForm> responseAdminDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getAdminsUri() + "/sensitive/by-email/" + email,
                            HttpMethod.GET,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            AdminUpdateForm.class);
            return responseAdminDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("ADMIN");
            } else {
                serviceUtil.logTechnicalWarning("ADMIN", ex);
                throw new GlobalRequestFailedException("ADMIN");
            }
        }
    }

    public AdminDTO update(AdminUpdateForm adminUpdateForm) throws Exception {
        Admin admin = buildAdmin(adminUpdateForm);
        try {
            ResponseEntity<AdminDTO> responseAdminDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getAdminsUri(),
                            HttpMethod.PUT,
                            new HttpEntity<>(admin, restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            AdminDTO.class);
            return responseAdminDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("ADMIN");
            } else if (ex instanceof HttpClientErrorException.BadRequest && (serviceUtil.causedByInvalid(ex) || serviceUtil.causedByDuplicate(ex))) {
                throw new Exception(serviceUtil.parseInvalidOrDuplicate(ex));
            } else {
                serviceUtil.logTechnicalWarning("ADMIN", ex);
                throw new GlobalRequestFailedException("ADMIN");
            }
        }
    }

    public AdminDTO deleteByEmail(String email) throws Exception {
        try {
            ResponseEntity<AdminDTO> responseAdminDTO =
                    restTemplate.exchange(
                            restConfig.getServerUrl() + restConfig.getAdminsUri() + "/by-email/" + email,
                            HttpMethod.DELETE,
                            new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                            AdminDTO.class);
            return responseAdminDTO.getBody();
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException.NotFound) {
                throw new GlobalNotFoundException("ADMIN");
            } else if (ex instanceof HttpClientErrorException.BadRequest && serviceUtil.causedByInvalid(ex)) {
                throw new Exception(serviceUtil.parseInvalid(ex));
            } else {
                serviceUtil.logTechnicalWarning("ADMIN", ex);
                throw new GlobalRequestFailedException("ADMIN");
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ResponseEntity<Admin> responseAdmin =
                restTemplate.exchange(
                        restConfig.getServerUrl() + restConfig.getAdminsUri() + "/sensitive/by-email/" + email,
                        HttpMethod.GET,
                        new HttpEntity<>(restConfig.buildAuthHeaders(restConfig.getCredentials())),
                        Admin.class);
        Optional<Admin> optionalAdmin = Optional.ofNullable(responseAdmin.getBody());
        return optionalAdmin.orElseThrow(() -> new UsernameNotFoundException(email));
    }

    private Admin buildAdmin(AdminCreationForm adminCreationForm) {
        Admin admin = modelMapper.map(adminCreationForm, Admin.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setStatus(1);
        admin.setRole("ADMIN");
        return admin;
    }

    private Admin buildAdmin(AdminUpdateForm adminUpdateForm) {
        Admin admin = modelMapper.map(adminUpdateForm, Admin.class);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return admin;
    }
}
