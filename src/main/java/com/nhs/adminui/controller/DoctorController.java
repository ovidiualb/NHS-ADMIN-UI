package com.nhs.adminui.controller;

import com.nhs.adminui.model.entity.Doctor;
import com.nhs.adminui.model.entity.Institution;
import com.nhs.adminui.service.DoctorService;
import com.nhs.adminui.service.InstitutionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/add-form")
    public ModelAndView showAddForm(Doctor doctor) {
        String[] doctorSpecialties = doctorService.getSpecialties();
        String[] doctorTitles = doctorService.getTitles();
        return new ModelAndView("doctor/add-form")
                .addObject("doctorSpecialties", doctorSpecialties)
                .addObject("doctorTitles", doctorTitles);
    }

    @GetMapping("/get-form")
    public String showGetForm(Doctor doctor) {
        return "doctor/get-form";
    }

    @GetMapping("/update-search-form")
    public String showUpdateSearchForm(Doctor doctor) {
        return "doctor/update-search-form";
    }

    @GetMapping("/update-form-by-cnp")
    public ModelAndView showUpdateFormByCnp(Doctor doctor) {
        String[] doctorSpecialties = doctorService.getSpecialties();
        String[] doctorTitles = doctorService.getTitles();
        Doctor databaseDoctor = doctorService.findByCnp(doctor.getCnp());
        ArrayList<Institution> institutionArrayList = institutionService.getInstitutions();
        return new ModelAndView("doctor/update-form").addObject(databaseDoctor)
                .addObject("doctorSpecialties", doctorSpecialties)
                .addObject("doctorTitles", doctorTitles)
                .addObject("institutions", institutionArrayList);

    }

    @GetMapping("/delete-form")
    public String showDeleteForm(Doctor doctor) {
        return "doctor/delete-form";
    }

    @PostMapping
    public ModelAndView add(@Valid Doctor doctor) {
        Doctor databaseDoctor = doctorService.add(doctor);
        return new ModelAndView("doctor/home-doctor").addObject(databaseDoctor);
    }

    @GetMapping("/by-cnp")
    public ModelAndView getByCnp(Doctor doctor) {
        Doctor databaseDoctor = doctorService.findByCnp(doctor.getCnp());
        return new ModelAndView("doctor/home-doctor").addObject(databaseDoctor);
    }

    @PostMapping("/updated-doctor")
    public ModelAndView update(@Valid Doctor doctor) {
        if (doctor.getInstitutionCUIs() == null)
            doctor.setInstitutionCUIs("");
        Doctor databaseDoctor = doctorService.update(doctor);
        databaseDoctor.setInstitutionCUIs(databaseDoctor.getInstitutions().stream()
                .map(Institution::getName).collect(Collectors.joining(",")));
        return new ModelAndView("doctor/home-doctor").addObject(databaseDoctor);
    }

    @PostMapping("/delete-by-cnp")
    public ModelAndView deleteByCnp(Doctor doctor) {
        Doctor databaseDoctor = doctorService.findByCnp(doctor.getCnp());
        doctorService.deleteByCnp(databaseDoctor.getCnp());

        return new ModelAndView("doctor/home-doctor").addObject(databaseDoctor);
    }
}
