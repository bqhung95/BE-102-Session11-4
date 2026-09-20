package com.example.patientmanage.service;


import com.example.patientmanage.entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PatientService {
    public Patient save(Patient patient) {
        log.info("[Service] Bắt đầu xử lý lưu bệnh nhân: {}", patient.getName());

        // Giả lập lưu DB
        // patientRepository.save(patient);

        log.debug("[Service] Chi tiết bệnh nhân: {}", patient);
        return patient;
    }
}
