package org.example.EmailFetcher.repository;

import org.example.EmailFetcher.model.LanDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanDeviceRepository extends JpaRepository<LanDeviceEntity, Long> {
    boolean existsByEmail(String email);
}
