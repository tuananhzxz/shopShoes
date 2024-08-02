package com.example.ShopShoes.repository;

import com.example.ShopShoes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);

}
