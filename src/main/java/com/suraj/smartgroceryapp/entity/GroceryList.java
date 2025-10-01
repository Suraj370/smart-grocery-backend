package com.suraj.smartgroceryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Represents the current active grocery list.
 * It establishes the One-to-Many relationship with GroceryItem.
 */
@Entity
@Table(name = "grocery_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Foreign Key to the User entity (User.uid).
     * This is the mechanism for ownership authorization.
     */
    @Column(nullable = false)
    private String ownerUid;

    /**
     * Relationship Mapping: A single list can have many items.
     * 'mappedBy' references the 'list' field in the child (GroceryItem) entity.
     * CascadeType.ALL ensures items are saved/deleted with the list.
     */
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GroceryItem> items;

    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean isCompleted;
}