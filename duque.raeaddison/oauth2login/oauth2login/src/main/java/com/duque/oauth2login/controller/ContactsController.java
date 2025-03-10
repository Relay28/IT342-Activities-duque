package com.duque.oauth2login.controller;

import java.io.IOException;
import java.util.List;

import com.duque.oauth2login.service.GooglePeopleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.api.services.people.v1.model.Person;


@Controller
@RequestMapping("/api/contacts")
public class ContactsController {

    private final GooglePeopleService googlePeopleService;


    public ContactsController(GooglePeopleService googlePeopleService) {
        this.googlePeopleService = googlePeopleService;
    }

    //Endpoint that Adds a Contact and redirects to contact.html to see changes
    @PostMapping("/add")
    public String addContact(@RequestParam String firstName, @RequestParam String lastName,
                             @RequestParam List<String> emails, @RequestParam List<String> phoneNumbers,
                             RedirectAttributes redirectAttributes) {
        try {
            //Calls method from service
            googlePeopleService.addContact(firstName, lastName, emails, phoneNumbers);

            //Passes successMessage attribute to be passed to conatcts.html
            redirectAttributes.addFlashAttribute("successMessage", "Contact added successfully!");
            return "redirect:/contacts";
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add contact.");
            return "redirect:/contacts";
        }
    }

    //Endpoint that Updates A Contact and redirects to contact.html to see changes
    @PostMapping("/update")
    public String updateContact(@RequestParam String resourceName,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam List<String> emails,
                                @RequestParam List<String> phoneNumbers,
                                RedirectAttributes redirectAttributes) {
        try {
            //Calls method from service
            googlePeopleService.updateContact(resourceName, firstName, lastName, emails, phoneNumbers);
            //Passes successMessage attribute to be passed to conatcts.html
            redirectAttributes.addFlashAttribute("successMessage", "Contact updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            if (e.getMessage().contains("etag")) {
                //Adds an attribute to be passed to conatcts.html
                redirectAttributes.addFlashAttribute("error", "Contact was modified by someone else. Please reload and try again.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update contact: " + e.getMessage());
            }
        }
        //redirects to contacts.html
        return "redirect:/contacts";
    }

    // Endpoint to delete a contact and redirects once again to contacts.html to see changes
    @PostMapping("/delete")
    public String deleteContact(@RequestParam String resourceName, RedirectAttributes redirectAttributes) {
        try {
            googlePeopleService.deleteContact(resourceName);
            redirectAttributes.addFlashAttribute("successMessage", "Contact deleted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to delete contact.");
        }
        return "redirect:/contacts";
    }

}

