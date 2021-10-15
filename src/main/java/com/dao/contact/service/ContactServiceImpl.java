package com.dao.contact.service;

import com.dao.contact.Contact;
import com.dao.contact.repository.ContactEntityTranslator;
import com.dao.contact.repository.ContactRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.persistence.EntityNotFoundException;

@Service
public class ContactServiceImpl implements ContactService {

    public static final ContactEntityTranslator CONTACT_ENTITY_TRANSLATOR = new ContactEntityTranslator();
    public final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact getContact(Long id) {
        try {
            return CONTACT_ENTITY_TRANSLATOR
                    .translateFrom(contactRepository.getById(id));
        } catch (EntityNotFoundException entityNotFoundException) {
            throw new ContactNotFoundException("contact not found with id: " + id);
        }
    }

    @Override
    public List<Contact> getContacts() {
        return CONTACT_ENTITY_TRANSLATOR
                .translateFrom(contactRepository.findAll());
    }

    @Override
    public Long addContact(Contact contact) {
        return contactRepository.save(
                CONTACT_ENTITY_TRANSLATOR.translateTo(
                        contact)).getId();
    }

    @Override
    public void updateContact(Long id, Contact contact) {
        contactRepository.save(
                CONTACT_ENTITY_TRANSLATOR.translateTo(
                        new Contact.ContactBuilder(contact).setId(id).build()));
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

}
