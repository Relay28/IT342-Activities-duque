package com.duque.oauth2login.controller;

import com.duque.oauth2login.service.GooglePeopleService;
import com.google.api.services.people.v1.model.Person;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UiController {

    private final GooglePeopleService googlePeopleService;

    // Constructor to initialize the GooglePeopleService
    public UiController(GooglePeopleService googlePeopleService) {
        this.googlePeopleService = googlePeopleService;
    }

    // Endpoint to show the list of contacts
    @GetMapping("/contacts")
    public String showContacts(Model model,@AuthenticationPrincipal OAuth2User principal) {
        try {
            // Fetch the list of contacts using the service
            List<Person> contacts = googlePeopleService.getContacts();
            // Add the contacts to the model

            model.addAttribute("contacts", contacts);
            model.addAttribute("userinfo",principal.getAttributes());
            return "contacts";
        } catch (IOException e) {
            e.printStackTrace();
            // Add an error message to the model
            model.addAttribute("error", "Failed to fetch contacts.");
            return "error";
        }
    }

    // Endpoint to show the addcontact.html form
    @GetMapping("/add-contact")
    public String showAddContactForm() {
        return "addcontact";
    }

    // Endpoint to show the form to edit an existing contact i.e show editcontact.html
    @GetMapping("/edit-contact")
    public String showEditContactForm(@RequestParam String resourceName, Model model) {
        try {
            // Fetch the contact details using the service
            Person contact = googlePeopleService.getContact(resourceName);
            if (contact == null) {
                // Add an error message to the model if the contact is not found
                model.addAttribute("error", "Contact not found.");
                return "error";
            }

            // Extract the etag from the first metadata source
            String etag = contact.getMetadata().getSources().get(0).getEtag();

            // Add the contact and etag to the model
            model.addAttribute("contact", contact);
            model.addAttribute("etag", etag); // Pass etag to the form
            return "editcontact";
        } catch (IOException e) {
            // Add an error message to the model if fetching contact details fails
            model.addAttribute("error", "Failed to fetch contact details: " + e.getMessage());
            return "error";
        }
    }

}