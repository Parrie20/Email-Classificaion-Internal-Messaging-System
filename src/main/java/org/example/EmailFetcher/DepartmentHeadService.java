package org.example.EmailFetcher;

import org.example.EmailFetcher.model.DepartmentHeadEntity;
import org.example.EmailFetcher.repository.DepartmentHeadRepository;
import org.springframework.stereotype.Service;

@Service
public class DepartmentHeadService {

    private final DepartmentHeadRepository repository;

    public DepartmentHeadService(DepartmentHeadRepository repository) {
        this.repository = repository;
    }

    public DepartmentHeadEntity registerDepartmentHead(DepartmentHeadEntity dto) {
        String department = dto.getDepartment().toUpperCase();
        long count = repository.countByDepartmentIgnoreCase(department);
        String generatedId = department + String.format("%03d", count + 1);

        DepartmentHeadEntity entity = new DepartmentHeadEntity(
                generatedId,
                dto.getName(),
                dto.getPhoneNumber(),
                dto.getEmail(),
                department
        );

        return repository.save(entity);
    }
}
