package com.jxg.isn_backend.controller;

import com.jxg.isn_backend.dto.request.CreateItemRequestDTO;
import com.jxg.isn_backend.dto.response.ItemResponseDTO;
import com.jxg.isn_backend.mapper.BlobMapper;
import com.jxg.isn_backend.mapper.UserMapper;
import com.jxg.isn_backend.model.Item;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.service.AuthService;
import com.jxg.isn_backend.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final AuthService authService;

    public ItemController(ItemService itemService, AuthService authService) {
        this.itemService = itemService;
        this.authService = authService;
    }


    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        Optional<Item> itemOptional = itemService.findById(id);

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();

            ItemResponseDTO responseDTO = new ItemResponseDTO(
                    item.getId(),
                    item.getDescription(),
                    item.getName(),
                    item.getIsSold(),
                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()), // Add createdBy
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            );

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //    @GetMapping("/subcategory/{subcategoryId}")
//    public List<Item> getItemsBySubcategoryId(@PathVariable Integer subcategoryId) {
//        return itemService.findBySubcategoryId(subcategoryId);
//    }
@GetMapping("/subcategory/{subcategoryId}")
public ResponseEntity<List<ItemResponseDTO>> getItemsBySubcategoryId(@PathVariable Integer subcategoryId) {
    List<ItemResponseDTO> items = itemService.findItemsBySubcategoryId(subcategoryId);
    return ResponseEntity.ok(items);
}



    @GetMapping("/category/{categoryId}")
    public List<Item> getItemsByCategoryId(@PathVariable Integer categoryId) {
        return itemService.findByCategoryId(categoryId);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> addItem(@RequestPart(name = "data") CreateItemRequestDTO requestDTO, @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(this.itemService.createItem(requestDTO, file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(@PathVariable Long id, @RequestPart("data") CreateItemRequestDTO requestDTO, @RequestPart("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok(this.itemService.update(id, requestDTO, file));


    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.findById(id).isPresent()) {
            itemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<ItemResponseDTO>> getUserItems() {
        // Get the currently authenticated user
        User currentUser = authService.getCurrentLoggedUser();

        // Fetch items created by the logged-in user
        List<ItemResponseDTO> userItems = itemService.getItemsByUser(currentUser);

        return ResponseEntity.ok(userItems);
    }
}

