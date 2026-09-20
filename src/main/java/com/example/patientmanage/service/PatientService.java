package com.example.patientmanage.service;


import com.example.patientmanage.entity.Patient;
import com.example.patientmanage.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    public Patient save(Patient patient) {
        log.info("[Service] Bắt đầu xử lý lưu bệnh nhân: {}", patient.getName());

        // Giả lập lưu DB
        // patientRepository.save(patient);

        log.debug("[Service] Chi tiết bệnh nhân: {}", patient);
        return patient;
    }

    public List<Patient> searchByName(String keyword) {

        // ✅ TRACE: chi tiết nhất — từng bước xử lý
        log.trace("Bắt đầu tìm kiếm bệnh nhân với keyword='{}'", keyword);

        // ✅ DEBUG: thông tin debug
        log.debug("Gọi repository.findByNameContaining('{}')", keyword);

        List<Patient> results = patientRepository.findByNameContaining(keyword);

        // ✅ TRACE: kết quả trả về
        log.trace("Tìm thấy {} bệnh nhân", results.size());

        // ✅ INFO: sự kiện chính
        log.info("Hoàn tất tìm kiếm '{}' — {} kết quả", keyword, results.size());

        return results;
    }
}
