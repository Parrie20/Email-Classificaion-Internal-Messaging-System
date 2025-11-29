package org.example.EmailFetcher.repository;

import org.example.EmailFetcher.model.DepartmentHeadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartmentHeadRepository extends JpaRepository<DepartmentHeadEntity, String> {

    // ✅ Find Department Head by Exact Department Name (1:1 mapping)
    Optional<DepartmentHeadEntity> findByDepartmentIgnoreCase(String department);
    long countByDepartmentIgnoreCase(String department);

}
