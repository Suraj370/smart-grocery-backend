package com.suraj.smartgroceryapp.controller;

import com.suraj.smartgroceryapp.dto.GroceryItemDTO;
import com.suraj.smartgroceryapp.entity.GroceryList;
import com.suraj.smartgroceryapp.service.GroceryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@Controller
@RequestMapping("/api/grocery")
public class GroceryController {

    private GroceryService groceryService;

    public GroceryController(GroceryService groceryService){
        this.groceryService = groceryService;
    }

    @PostMapping({"/", "/{listId}/items"})
    public ResponseEntity<GroceryList> createItem(@PathVariable(required = false) Long listId,
                                                  @RequestBody GroceryItemDTO itemDTO,
                                                  Principal principal){
        String ownerUid = principal.getName();
        GroceryList updatedList = groceryService.addItemtoList(listId, ownerUid, itemDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/lists/{id}")
                .buildAndExpand(updatedList.getId())
                .toUri();
        return ResponseEntity.created(location).body(updatedList);
           }
}
