package com.dao.contact.service;

import com.dao.contact.Contact;
import com.dao.contact.controller.ContactRequest;
import com.dao.contact.controller.ContactResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ContactTranslator {

    public ContactResponse translateFrom(Contact contact) {
        ContactResponse contactResponse = new ContactResponse();
        contactResponse.setId(contact.getId());
        contactResponse.setFirstName(contact.getFirstName());
        contactResponse.setLastName(contact.getLastName());
        contactResponse.setEmail(contact.getEmail());
        return contactResponse;
    }

    public List<ContactResponse> translateFrom(List<Contact> contacts) {
        return contacts.stream().map(this::translateFrom).collect(Collectors.toList());
    }

    public Contact translateTo(ContactRequest contactRequest) {
        return Contact.builder()
                .setFirstName(contactRequest.getFirstName())
                .setLastName(contactRequest.getLastName())
                .setEmail(contactRequest.getEmail())
                .build();
    }
}
