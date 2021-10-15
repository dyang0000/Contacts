package com.dao.contact.controller;

import com.dao.contact.Contact;
import com.dao.contact.service.ContactService;
import com.dao.contact.service.ContactTranslator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;

@RestController
public class ContactController {
    public static final String API_V1 = "api/v1";
    public static final String CONTACTS_API_V1 = API_V1 + "/contacts";
    public static final String CONTACTS_BY_ID_API_V1 = CONTACTS_API_V1 + "/{id}";
    public static final ContactTranslator CONTACT_RESPONSE_TRANSLATOR = new ContactTranslator();

    public final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping(value = CONTACTS_BY_ID_API_V1)
    public ResponseEntity<ContactResponse> getContact(@PathVariable("id") Long id) {
        Contact contact = contactService.getContact(id);
        return ResponseEntity.ok(CONTACT_RESPONSE_TRANSLATOR.translateFrom(contact));
    }

    @GetMapping(value = CONTACTS_API_V1)
    public ResponseEntity<List<ContactResponse>> getContacts() {
        return ResponseEntity.ok(
                CONTACT_RESPONSE_TRANSLATOR.translateFrom(contactService.getContacts()));
    }

    @PostMapping(value = CONTACTS_API_V1)
    public ResponseEntity<Void> createContact(@Valid @RequestBody ContactRequest contactRequest) {
        Long createdId = contactService.addContact(CONTACT_RESPONSE_TRANSLATOR.translateTo(contactRequest));
        return ResponseEntity.created(createdUri(createdId)).build();
    }

    @PutMapping(value = CONTACTS_BY_ID_API_V1)
    public ResponseEntity<Void> updateContact(
            @PathVariable("id") Long id,
            @Valid @RequestBody ContactRequest contactRequest) {
        contactService.updateContact(id, CONTACT_RESPONSE_TRANSLATOR.translateTo(contactRequest));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = CONTACTS_BY_ID_API_V1)
    public ResponseEntity<Void> deleteContact(@PathVariable("id") Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    private URI createdUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
