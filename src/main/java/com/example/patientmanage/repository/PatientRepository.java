package com.example.patientmanage.repository;

import com.example.patientmanage.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    List<Patient> findByNameContaining(String keyword);
}
