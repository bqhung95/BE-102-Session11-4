package com.example.patientmanage.controller;


import com.example.patientmanage.entity.Patient;
import com.example.patientmanage.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
        log.info("Nhận yêu cầu thêm bệnh nhân: name={}, age={}",
                patient.getName(), patient.getAge());

        if (patient.getAge() > 120) {
            log.warn("Cảnh báo: Tuổi bệnh nhân quá cao! name={}, age={}",
                    patient.getName(), patient.getAge());
        }
        Patient saved = patientService.save(patient);

        // Log INFO khi lưu thành công
        log.info("Đã thêm bệnh nhân thành công: {}", saved.getName());

        return ResponseEntity.ok(saved);
    }
}
