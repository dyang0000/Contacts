package com.dao.contact.service;

import com.dao.contact.Contact;
import com.dao.contact.repository.ContactEntity;
import com.dao.contact.repository.ContactRepository;
import net.bytebuddy.utility.RandomString;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import javax.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    private static final long ID = 100L;
    private static final String FIRST_NAME = "foo";
    private static final String LAST_NAME = "bar";
    private static final String EMAIL = "foo.bar@mail.com";

    private final ContactEntity contactEntity = contactEntity(ID, RandomString.make(5));
    private final List<ContactEntity> contactEntities = contactEntities();

    @Mock
    public ContactRepository contactRepository;
    public ContactServiceImpl contactService;

    @BeforeEach
    public void setup() {
        contactService = new ContactServiceImpl(contactRepository);
    }

    @Test
    void givenContactExists_whenGetContact_thenContactReturned() {
        Mockito.when(contactRepository.getById(ID))
                .thenReturn(contactEntity);

        Contact contact = contactService.getContact(ID);

        MatcherAssert.assertThat(contact, matchesContactEntity(contactEntity));
    }

    @Test
    void givenContactDoesntExists_whenGetContact_thenContactNotFoundException() {
        Mockito.when(contactRepository.getById(500L)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(
                ContactNotFoundException.class,
                () -> contactService.getContact(500L));

    }

    @Test
    void givenContactsExist_whenGetContacts_thenReturnListOfContacts() {
        Mockito.when(contactRepository.findAll()).thenReturn(contactEntities);

        List<Contact> results = contactService.getContacts();

        MatcherAssert.assertThat(results,
                Matchers.containsInAnyOrder(
                        contactEntities.stream().map(this::matchesContactEntity).collect(Collectors.toList())));
    }

    @Test
    void givenNoContactsExist_whenGetContacts_thenReturnEmptyList() {
        Mockito.when(contactRepository.findAll()).thenReturn(Collections.emptyList());

        List<Contact> results = contactService.getContacts();

        Assertions.assertEquals(0, results.size());
    }

    @Test
    void whenAddContact_thenIdisReturned() {
        Mockito.when(contactRepository.save(ArgumentMatchers.any())).thenReturn(contactEntity);

        Long result = contactService.addContact(Contact.builder().build());

        Assertions.assertEquals(result, contactEntity.getId());
    }

    @Test
    void whenUpdateContact_thenSaveIsCalled() {
        ArgumentCaptor<ContactEntity> contactEntityArgumentCaptor = ArgumentCaptor.forClass(ContactEntity.class);

        Contact contact = Contact.builder()
                .setId(10000L) // just to make sure this isn't used
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setEmail(EMAIL)
                .build();

        contactService.updateContact(contactEntity.getId(), contact);

        Mockito.verify(contactRepository).save(contactEntityArgumentCaptor.capture());

        Assertions.assertEquals(ID ,contactEntityArgumentCaptor.getValue().getId());
        Assertions.assertNotEquals(10000L, contactEntityArgumentCaptor.getValue().getId());
        Assertions.assertEquals(FIRST_NAME ,contactEntityArgumentCaptor.getValue().getFirstName());
        Assertions.assertEquals(LAST_NAME ,contactEntityArgumentCaptor.getValue().getLastName());
        Assertions.assertEquals(EMAIL ,contactEntityArgumentCaptor.getValue().getEmail());
    }

    @Test
    public void whenDeleteContact_thenDeleteIsCalled() {
        ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        contactService.deleteContact(ID);
        Mockito.verify(contactRepository).deleteById(idArgumentCaptor.capture());

        Assertions.assertEquals(ID, idArgumentCaptor.getValue());
    }

    private ContactEntity contactEntity(Long id, String entropy) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setId(id);
        contactEntity.setFirstName(FIRST_NAME + "-" + entropy);
        contactEntity.setLastName(LAST_NAME + "-" + entropy);
        contactEntity.setEmail(EMAIL + "-" + entropy);
        return contactEntity;
    }

    private List<ContactEntity> contactEntities() {
        List<ContactEntity> collect = new ArrayList<>();
        LongStream.range(0, 5)
                .forEach(idx -> collect.add(contactEntity(idx, RandomString.make(5))));
        return collect;
    }

    public Matcher<Contact> matchesContactEntity(ContactEntity contactEntity) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Contact contact) {
                return Objects.equals(contactEntity.getId(), contact.getId())
                        && contactEntity.getFirstName().equals(contact.getFirstName())
                        && contactEntity.getLastName().equals(contact.getLastName())
                        && contactEntity.getEmail().equals(contact.getEmail());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(" equal to ").appendValue(contactEntity);
            }
        };
    }
}