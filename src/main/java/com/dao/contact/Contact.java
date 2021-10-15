package com.dao.contact;

public class Contact {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;

    private Contact(ContactBuilder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static ContactBuilder builder() {
        return new ContactBuilder();
    }

    public static class ContactBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;

        public ContactBuilder() {

        }

        public ContactBuilder(Contact contact) {
            this.id = contact.id;
            this.firstName = contact.firstName;
            this.lastName = contact.lastName;
            this.email = contact.email;
        }

        public ContactBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ContactBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ContactBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ContactBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }
}
