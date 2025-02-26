package com.webid.webid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;

@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getAllItems() {
        // Returning a generic JSON response
        Map<String, String> response = Map.of("message", "This is a generic JSON response");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getItemById(@PathVariable("id") Long id) {
        // Replace with actual service call
        String item = "Item " + id;
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody String newItem) {
        // Replace with actual service call
        return new ResponseEntity<>("Created: " + newItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateItem(@PathVariable("id") Long id, @RequestBody String updatedItem) {
        // Replace with actual service call
        return ResponseEntity.ok("Updated item " + id + " to " + updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        // Replace with actual service call
        return ResponseEntity.noContent().build();
    }
}
