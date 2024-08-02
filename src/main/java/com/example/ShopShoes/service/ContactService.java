package com.example.ShopShoes.service;

import com.example.ShopShoes.dto.ContactDTO;

import java.util.List;

public interface ContactService {

    ContactDTO getContactById(Long contactId);

    ContactDTO createContact(ContactDTO contactDTO);

    void deleteContact(Long contactId);

    ContactDTO updateContact(Long contactId, ContactDTO contactDTO);

    List<ContactDTO> getAllContacts();
}
