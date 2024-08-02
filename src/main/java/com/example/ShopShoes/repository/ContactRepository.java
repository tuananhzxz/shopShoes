package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
