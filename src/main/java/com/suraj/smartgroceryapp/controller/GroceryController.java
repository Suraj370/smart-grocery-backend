package com.suraj.smartgroceryapp.controller;

import com.suraj.smartgroceryapp.dto.GroceryItemDTO;
import com.suraj.smartgroceryapp.entity.GroceryList;
import com.suraj.smartgroceryapp.service.GroceryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

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
        GroceryList updatedList = groceryService.addItemToList(listId, ownerUid, itemDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/lists/{id}")
                .buildAndExpand(updatedList.getId())
                .toUri();
        return ResponseEntity.created(location).body(updatedList);
           }

    @GetMapping("/my-lists")
    public ResponseEntity<List<GroceryList>> getMyLists(Principal principal) {
        // The principal.getName() method returns the 'username' from the JWT token.
        String ownerUid = principal.getName();

        List<GroceryList> lists = groceryService.getListsByOwner(ownerUid);

        if (lists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(lists);
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    public ResponseEntity<Void> deleteItemFromList(@PathVariable Long listId, @PathVariable Long itemId, Principal principal) {
        String ownerUid = principal.getName();
        try {
            groceryService.deleteItemFromList(listId, itemId, ownerUid);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
