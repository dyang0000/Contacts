package com.dao.contact.repository;

import com.dao.contact.Contact;
import java.util.List;
import java.util.stream.Collectors;

public class ContactEntityTranslator {

    public Contact translateFrom(ContactEntity contactEntity) {
        return Contact.builder()
                .setId(contactEntity.getId())
                .setFirstName(contactEntity.getFirstName())
                .setLastName(contactEntity.getLastName())
                .setEmail(contactEntity.getEmail())
                .build();
    }

    public List<Contact> translateFrom(List<ContactEntity> contactEntities) {
        return contactEntities.stream()
                .map(this::translateFrom)
                .collect(Collectors.toList());
    }

    public ContactEntity translateTo(Contact contact) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setId(contact.getId());
        contactEntity.setFirstName(contact.getFirstName());
        contactEntity.setLastName(contact.getLastName());
        contactEntity.setEmail(contact.getEmail());
        return contactEntity;
    }
}
