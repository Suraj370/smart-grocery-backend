package com.suraj.smartgroceryapp.service;

import com.suraj.smartgroceryapp.dto.GroceryItemDTO;
import com.suraj.smartgroceryapp.entity.GroceryItem;
import com.suraj.smartgroceryapp.entity.GroceryList;
import com.suraj.smartgroceryapp.repository.GroceryItemRepository;
import com.suraj.smartgroceryapp.repository.GroceryListRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroceryServiceImpl implements GroceryService{
    private GroceryListRepository groceryListRepository;
    private GroceryItemRepository groceryItemRepository;
    @Autowired
    public  GroceryServiceImpl(GroceryListRepository groceryListRepository, GroceryItemRepository groceryItemRepository){
        this.groceryListRepository = groceryListRepository;
        this.groceryItemRepository = groceryItemRepository;
    }

    @Override
    public Optional<GroceryList> getListById(Long listId) {
        return groceryListRepository.findById(listId);
    }

    @Override
    public List<GroceryList> getListsByOwner(String ownerUid) {
        return List.of();
    }

    /**
     * Adds an item to a list. If the list does not exist, a new one is created
     * automatically with a name based on the current date.
     *
     * @param listId Optional ID of the list. If null, a new list is created.
     * @param ownerUid The unique ID of the user.
     * @param itemDTO The new GroceryItem to add.
     * @return The updated or newly created GroceryList.
     */
    // Corrected addItemtoList service method

    @Override
    @Transactional
    public GroceryList addItemtoList(Long listId, String ownerUid, GroceryItemDTO itemDTO) {
        GroceryList existingList;
        if (listId != null) {
            // Find existing list
            existingList = getListById(listId).orElseThrow(
                    () -> new RuntimeException("Grocery list not found")
            );
        } else {
            // Create a new list if one doesn't exist
            existingList = new GroceryList();
            String listName = "GroceryList - " + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            existingList.setName(listName);
            existingList.setOwnerUid(ownerUid);
            existingList.setCompleted(false);
        }

        // Map DTO to a new GroceryItem entity
        GroceryItem newItem = new GroceryItem();
        newItem.setName(itemDTO.getName());
        newItem.setCategory(itemDTO.getCategory());
        newItem.setUnit(itemDTO.getUnit());
        newItem.setQuantity(itemDTO.getQuantity());

        // This is the crucial step: Set the relationship on the item first.
        newItem.setList(existingList); // <-- This is the missing link

        // Add the new item to the list's collection.
        // This is optional but good practice to keep the collection in sync.
        if (existingList.getItems() == null) {
            existingList.setItems(new ArrayList<>());
        }
        existingList.getItems().add(newItem);

        // Save the list. Due to CascadeType.ALL, this will automatically save the new item
        // and correctly set the foreign key.
        return groceryListRepository.save(existingList);
    }
}
