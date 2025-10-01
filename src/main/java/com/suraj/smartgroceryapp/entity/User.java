package com.suraj.smartgroceryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // Unique internal database ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    // User's display name or username
    private String displayName;

    private String password;

    // Timestamps
    private Instant createdAt;

    private Instant updatedAt;
}
