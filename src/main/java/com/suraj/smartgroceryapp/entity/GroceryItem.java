package com.suraj.smartgroceryapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Entity
@Table(name = "grocery_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING) // Store enum as a string in the database
    @Column(nullable = false)
    private GroceryCategory category;

    private Integer quantity;

    private String unit;

    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    /**
     * Relationship Mapping: Many items belong to one list.
     * @ManyToOne defines the relationship.
     * @JoinColumn specifies the Foreign Key column name in the 'grocery_item' table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false) // FK column name is 'list_id'
    private GroceryList list; // Reference to the parent list entity
}


