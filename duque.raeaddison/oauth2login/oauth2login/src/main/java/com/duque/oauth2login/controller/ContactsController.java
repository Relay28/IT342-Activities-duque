package com.duque.oauth2login.controller;
import com.duque.oauth2login.service.GoogleContactsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
//@RequestMapping("/contacts")
//public class ContactsController {
//
//    private final GoogleContactsService googleContactsService;
//
//    public ContactsController(GoogleContactsService googleContactsService) {
//        this.googleContactsService = googleContactsService;
//    }
//
//    @GetMapping
//    public String getContacts(OAuth2AuthenticationToken authentication) {
//        return googleContactsService.getContacts(authentication);
//    }
//
//}