package com.dao.contact.service;

import com.dao.contact.Contact;
import java.util.List;

public interface ContactService {
    Contact getContact(Long id);
    List<Contact> getContacts();
    Long addContact(Contact contact);
    void updateContact(Long id, Contact contact);
    void deleteContact(Long id);
}
