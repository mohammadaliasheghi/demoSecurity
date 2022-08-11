package com.google.demosecurity.repository;

import com.google.demosecurity.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

    @Query("select u from Users u where u.id = :id ")
    Users get(Long id);
}
