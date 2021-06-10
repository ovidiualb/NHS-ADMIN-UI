package com.nhs.adminui.controller;

import com.nhs.adminui.model.dto.ClientAppDTO;
import com.nhs.adminui.model.dto.RoleDTO;
import com.nhs.adminui.model.entity.ClientApp;
import com.nhs.adminui.model.entity.Role;
import com.nhs.adminui.service.ClientAppService;
import com.nhs.adminui.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/client-apps")
public class ClientAppController {

    @Autowired
    private ClientAppService clientAppService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/add-form")
    public ModelAndView showAddForm(ClientAppDTO clientAppDTO) {
        List<Role> rolesList = roleService.getRolesList();
        clientAppDTO.setRolesList(rolesList.stream().map(Role::getName).collect(Collectors.toList()));
        return new ModelAndView("client-app/add-form")
                .addObject(clientAppDTO);
    }

    @GetMapping("/get-form")
    public String showGetForm(ClientApp clientApp) {
        return "client-app/get-form";
    }

    @GetMapping("/update-search-form")
    public String showUpdateSearchForm(ClientApp clientApp) {
        return "client-app/update-search-form";
    }

    @GetMapping("/update-form")
    public ModelAndView showUpdateForm(ClientApp clientApp) {
        ClientApp databaseClientApp = clientAppService.findByName(clientApp.getName());
        ClientAppDTO clientAppDTO = modelMapper.map(databaseClientApp, ClientAppDTO.class);
        List<Role> rolesList = roleService.getRolesList();
        clientAppDTO.setRolesList(rolesList.stream().map(Role::getName).collect(Collectors.toList()));
        return new ModelAndView("client-app/update-form").addObject(clientAppDTO);
    }

    @GetMapping("/delete-form")
    public String showDeleteForm(ClientApp clientApp) {
        return "client-app/delete-form";
    }

    @PostMapping
    public ModelAndView add(ClientAppDTO clientAppDTO) {
        RoleDTO databaseRole = roleService.findByName(clientAppDTO.getSelectedRoleName());
        ClientApp databaseClientApp = clientAppService.add(clientAppDTO, databaseRole.getName());
        return new ModelAndView("client-app/home-client-app").addObject(databaseClientApp);
    }

    @GetMapping("/by-name")
    public ModelAndView getByName(ClientApp clientApp) {
        ClientApp databaseClientApp = clientAppService.findByName(clientApp.getName());
        return new ModelAndView("client-app/home-client-app").addObject(databaseClientApp);
    }

    @PostMapping("/updated-client-app")
    public ModelAndView update(ClientAppDTO clientAppDTO) {
        RoleDTO databaseRole = roleService.findByName(clientAppDTO.getSelectedRoleName());
        ClientApp clientApp = modelMapper.map(clientAppDTO, ClientApp.class);
        ClientApp databaseClientApp = clientAppService.update(clientApp, databaseRole.getName());
        return new ModelAndView("client-app/home-client-app").addObject(databaseClientApp);
    }

    @PostMapping("/deleted-client-app-name")
    public ModelAndView deleteByName(ClientApp clientApp) {
        ClientApp databaseClientApp = clientAppService.deleteByName(clientApp.getName());
        return new ModelAndView("client-app/home-client-app").addObject(databaseClientApp);
    }
}
