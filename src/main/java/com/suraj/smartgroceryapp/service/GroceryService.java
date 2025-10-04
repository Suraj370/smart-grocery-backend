package com.suraj.smartgroceryapp.service;

import com.suraj.smartgroceryapp.dto.GroceryItemDTO;
import com.suraj.smartgroceryapp.entity.GroceryItem;
import com.suraj.smartgroceryapp.entity.GroceryList;
import com.suraj.smartgroceryapp.repository.GroceryListRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public interface GroceryService {

    Optional<GroceryList> getListById(Long listId);
    List<GroceryList> getListsByOwner(String ownerUid);
    GroceryList addItemtoList(Long listId, String ownerUid, GroceryItemDTO newItem);
}
