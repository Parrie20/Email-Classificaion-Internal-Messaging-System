package org.example;

import org.example.EmailFetcher.DepartmentHeadService;
import org.example.EmailFetcher.model.DepartmentHeadEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department-heads")
@CrossOrigin
public class DepartmentHeadController {

    private final DepartmentHeadService service;

    public DepartmentHeadController(DepartmentHeadService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<DepartmentHeadEntity> register(@RequestBody DepartmentHeadEntity dto) {
        return ResponseEntity.ok(service.registerDepartmentHead(dto));
    }
}
