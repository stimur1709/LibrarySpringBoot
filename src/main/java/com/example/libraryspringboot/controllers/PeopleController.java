package com.example.libraryspringboot.controllers;

import com.example.libraryspringboot.models.Person;
import com.example.libraryspringboot.service.PeopleService;
import com.example.libraryspringboot.util.PersonValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;
    private final PersonValidation personValidation;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidation personValidation) {
        this.peopleService = peopleService;
        this.personValidation = personValidation;
    }

    //&sortField=fullName&sortDir=asc
    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                        @RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
                        @RequestParam(value = "sortField", required = false, defaultValue = "fullName") String sortField,
                        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
        Page<Person> people = peopleService.findAll(offset, limit, sortField, sortDir);
        model.addAttribute("peopleList", people.getContent());
        model.addAttribute("sizePage", people.getSize());
        model.addAttribute("numberPage", people.getNumber());
        model.addAttribute("numberingOrder", IntStream.range(0, people.getTotalPages()).toArray());
        model.addAttribute("totalPage", people.getTotalPages());
        model.addAttribute("sizingOrder", Arrays.asList(5, 10, 25, 50));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverse", sortDir.equals("asc") ? "desc" : "asc");
        return "people/index";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidation.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.show(id));
        return "people/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}/edit")
    public String change(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/edit";
        peopleService.change(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("{id}/delete")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
