package com.nhs.adminui.controller;

import com.nhs.adminui.model.entity.Nurse;
import com.nhs.adminui.service.NurseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/nurses")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/add-form")
    public ModelAndView showAddForm(Nurse nurse) {
        String[] nurseSpecialties = nurseService.getSpecialties();
        String[] nurseTitles = nurseService.getTitles();
        return new ModelAndView("nurse/add-form")
                .addObject("nurseSpecialties", nurseSpecialties)
                .addObject("nurseTitles", nurseTitles);
    }

    @GetMapping("/get-form")
    public String showGetForm(Nurse nurse) {
        return "nurse/get-form";
    }

    @GetMapping("/update-search-form")
    public String showUpdateSearchForm(Nurse nurse) {
        return "nurse/update-search-form";
    }

    @GetMapping("/update-form-by-cnp")
    public ModelAndView showUpdateFormByCnp(Nurse nurse) {
        String[] nurseSpecialties = nurseService.getSpecialties();
        String[] nurseTitles = nurseService.getTitles();
        Nurse databaseNurse = nurseService.findByCnp(nurse.getCnp());
        return new ModelAndView("nurse/update-form").addObject(databaseNurse)
                .addObject("nurseSpecialties", nurseSpecialties)
                .addObject("nurseTitles", nurseTitles);
    }

    @GetMapping("/delete-form")
    public String showDeleteForm(Nurse nurse) {
        return "nurse/delete-form";
    }

    @PostMapping
    public ModelAndView add(@Valid Nurse nurse) {
        Nurse databaseNurse = nurseService.add(nurse);
        return new ModelAndView("nurse/home-nurse").addObject(databaseNurse);
    }

    @GetMapping("/by-cnp")
    public ModelAndView getByCnp(Nurse nurse) {
        Nurse databaseNurse = nurseService.findByCnp(nurse.getCnp());
        return new ModelAndView("nurse/home-nurse").addObject(databaseNurse);
    }

    @PostMapping("/updated-nurse")
    public ModelAndView update(@Valid Nurse nurse) {
        Nurse databaseNurse = nurseService.update(nurse);
        return new ModelAndView("nurse/home-nurse").addObject(databaseNurse);
    }

    @PostMapping("/delete-by-cnp")
    public ModelAndView deleteByCnp(Nurse nurse) {
        Nurse databaseNurse = nurseService.findByCnp(nurse.getCnp());
        nurseService.deleteByCnp(databaseNurse.getCnp());
        return new ModelAndView("nurse/home-nurse").addObject(databaseNurse);
    }
}
