package org.example.EmailFetcher.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "department_heads")
public class DepartmentHeadEntity {

    @Id
    private String id;  // e.g., SALES001, CRM002

    private String name;
    private String phoneNumber;
    private String email;

    @Column(unique = true)
    private String department;  // Maps to category in categorized_emails

    // Constructors
    public DepartmentHeadEntity() {}

    public DepartmentHeadEntity(String id, String name, String phoneNumber, String email, String department) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.department = department;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }


    public static String generateDepartmentId(String department) {
        String prefix = department.substring(0, Math.min(department.length(), 3)).toUpperCase(); // e.g., "SAL" for "Sales"
        String uniqueId = UUID.randomUUID().toString().substring(0, 5).toUpperCase(); // Random 5-char string
        return prefix + uniqueId;
    }
}
