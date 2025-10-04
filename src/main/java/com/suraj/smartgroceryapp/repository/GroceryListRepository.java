package com.suraj.smartgroceryapp.repository;

import com.suraj.smartgroceryapp.entity.GroceryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing GroceryList persistence.
 */
@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {

    /**
     * Find all grocery lists belonging to a specific user.
     * This query is crucial for enforcing authorization.
     * @param ownerUid The unique ID of the list owner.
     * @return A list of GroceryList entities.
     */
    List<GroceryList> findAllByOwnerUid(String ownerUid);
}
