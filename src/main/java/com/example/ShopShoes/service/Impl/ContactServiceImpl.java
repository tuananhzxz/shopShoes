package com.example.ShopShoes.service.Impl;

import com.example.ShopShoes.dto.ContactDTO;
import com.example.ShopShoes.entity.Contact;
import com.example.ShopShoes.repository.ContactRepository;
import com.example.ShopShoes.service.ContactService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ModelMapper contactMapper;

    @Override
    public ContactDTO getContactById(Long contactId) {
        return contactRepository.findById(contactId)
                .map(contact -> contactMapper.map(contact, ContactDTO.class))
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + contactId));
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        return contactMapper.map(contactRepository.save(contactMapper.map(contactDTO, Contact.class)), ContactDTO.class);
    }

    @Override
    public void deleteContact(Long contactId) {
        contactRepository.deleteById(contactId);
    }

    @Override
    public ContactDTO updateContact(Long contactId, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + contactId));

        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());
        contact.setPhone(contactDTO.getPhone());
        contact.setMessage(contactDTO.getMessage());
        contact.setSubject(contactDTO.getSubject());

        return contactMapper.map(contactRepository.save(contact), ContactDTO.class);
    }

    @Override
    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(contact -> contactMapper.map(contact, ContactDTO.class))
                .toList();
    }

}
