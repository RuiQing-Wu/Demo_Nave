package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Nave;

@Repository
public interface NaveRepository extends JpaRepository<Nave, Long> {
}
