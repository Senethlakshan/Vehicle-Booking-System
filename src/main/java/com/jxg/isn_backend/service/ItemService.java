package com.jxg.isn_backend.service;

import com.jxg.isn_backend.repository.CategoryRepository;
import com.jxg.isn_backend.repository.SubCategoryRepository;
import org.springframework.transaction.annotation.Transactional;
import com.jxg.isn_backend.dto.request.CreateItemRequestDTO;
import com.jxg.isn_backend.dto.response.ItemResponseDTO;
import com.jxg.isn_backend.mapper.BlobMapper;
import com.jxg.isn_backend.mapper.UserMapper;
import com.jxg.isn_backend.model.FileBlob;
import com.jxg.isn_backend.model.Item;
import com.jxg.isn_backend.model.User;
import com.jxg.isn_backend.repository.ItemRepository;
import com.jxg.isn_backend.repository.SavedRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Value("${spring.app.localBlobDirectory}")
    private String FILE_DIRECTORY;
    private final ItemRepository itemRepository;
    private final BlobService blobService;
    private final SavedRepository savedRepository;


    public ItemService(ItemRepository itemRepository, BlobService blobService, SavedRepository savedRepository) {
        this.itemRepository = itemRepository;
//        this.itemRepository = itemRepository;
        this.blobService = blobService;
        this.savedRepository = savedRepository;
    }

    public List<ItemResponseDTO> findAll() {
        var itemList = itemRepository.findAll();
        var itemResponseDTOList = new ArrayList<ItemResponseDTO>();
        for (var item : itemList) {
            itemResponseDTOList.add(new ItemResponseDTO(
                    item.getId(),
                    item.getDescription(),
                    item.getName(),
                    item.getIsSold(),
                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            ));
        }
        return itemResponseDTOList;

    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findBySubcategoryId(Integer subcategoryId) {
        return itemRepository.findBySubcategoryId(subcategoryId);
    }

    public List<Item> findByCategoryId(Integer categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }

    public ItemResponseDTO createItem(CreateItemRequestDTO requestDTO, MultipartFile file) throws IOException {
        FileBlob fileBlob = this.blobService.saveBlobToLocal(FILE_DIRECTORY, file);
        var newItem = new Item();
        newItem.setCategory(requestDTO.category());
        newItem.setImageBlob(fileBlob);
        newItem.setName(requestDTO.name());
        newItem.setTags(requestDTO.tags());
        newItem.setDescription(requestDTO.description());
        newItem.setSubcategory(requestDTO.subCategory());
        newItem.setIsSold(false);

        var savedItem = this.itemRepository.save(newItem);
        return new ItemResponseDTO(
                savedItem.getId(),
                savedItem.getDescription(),
                savedItem.getName(),
                savedItem.getIsSold(),
                savedItem.getTags(),
                BlobMapper.INSTANCE.toDto(savedItem.getImageBlob()),
                savedItem.getSubcategory(),
                savedItem.getCategory(),
                UserMapper.INSTANCE.toUserMinDTO(savedItem.getCreatedBy()),
                UserMapper.INSTANCE.toUserMinDTO(savedItem.getLastModifiedBy()),
                savedItem.getCreatedDatetime(),
                savedItem.getLastModifiedDatetime()
        );
    }

    public ItemResponseDTO update(Long id, CreateItemRequestDTO requestDTO, MultipartFile file) throws IOException {
        FileBlob fileBlob = this.blobService.saveBlobToLocal(FILE_DIRECTORY, file);
        Optional<Item> optionalItem = this.itemRepository.findById(id);

        if(optionalItem.isPresent()) {
            var itemToBeSaved = optionalItem.get();
            if(requestDTO.name() != null) {
                itemToBeSaved.setName(requestDTO.name());
            }
            if (requestDTO.description() != null) {
                itemToBeSaved.setDescription(requestDTO.description());
            }
            if (requestDTO.isSold() != null) {
                itemToBeSaved.setIsSold(requestDTO.isSold());
            }
            if (requestDTO.tags() != null) {
                itemToBeSaved.setTags(requestDTO.tags());
            }
            if (requestDTO.subCategory() != null) {
                itemToBeSaved.setSubcategory(requestDTO.subCategory());
            }
            if (requestDTO.category() != null) {
                itemToBeSaved.setCategory(requestDTO.category());
            }
            itemToBeSaved.setImageBlob(fileBlob);

            var updatedItem = this.itemRepository.save(itemToBeSaved);
            return new ItemResponseDTO(
                    updatedItem.getId(),
                    updatedItem.getDescription(),
                    updatedItem.getName(),
                    updatedItem.getIsSold(),
                    updatedItem.getTags(),
                    BlobMapper.INSTANCE.toDto(updatedItem.getImageBlob()),
                    updatedItem.getSubcategory(),
                    updatedItem.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(updatedItem.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(updatedItem.getLastModifiedBy()),
                    updatedItem.getCreatedDatetime(),
                    updatedItem.getLastModifiedDatetime()
            );
        }

        var newItem = new Item();
        newItem.setCategory(requestDTO.category());
        newItem.setImageBlob(fileBlob);
        newItem.setName(requestDTO.name());
        newItem.setTags(requestDTO.tags());
        newItem.setDescription(requestDTO.description());
        newItem.setSubcategory(requestDTO.subCategory());
        newItem.setIsSold(false);

        var savedItem = this.itemRepository.save(newItem);
        return new ItemResponseDTO(
                savedItem.getId(),
                savedItem.getDescription(),
                savedItem.getName(),
                savedItem.getIsSold(),
                savedItem.getTags(),
                BlobMapper.INSTANCE.toDto(savedItem.getImageBlob()),
                savedItem.getSubcategory(),
                savedItem.getCategory(),
                UserMapper.INSTANCE.toUserMinDTO(savedItem.getCreatedBy()),
                UserMapper.INSTANCE.toUserMinDTO(savedItem.getLastModifiedBy()),
                savedItem.getCreatedDatetime(),
                savedItem.getLastModifiedDatetime()
        );
    }


    @Transactional
    public void deleteById(Long id) {
        // Fetch the item
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Remove related `Saved` entities
        savedRepository.deleteAllByItem(item);

        // Delete the item itself
        itemRepository.delete(item);
    }




    public List<ItemResponseDTO> getItemsByUser(User user) {
        List<Item> items = itemRepository.findByCreatedById(user.getId());
        List<ItemResponseDTO> itemResponseDTOs = new ArrayList<>();
        for (Item item : items) {
            itemResponseDTOs.add(new ItemResponseDTO(
                    item.getId(),
                    item.getDescription(),
                    item.getName(),
                    item.getIsSold(),
                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            ));
        }
        return itemResponseDTOs;
    }

    public List<ItemResponseDTO> findItemsBySubcategoryId(Integer subcategoryId) {
        List<Item> items = itemRepository.findBySubcategoryId(subcategoryId);
        List<ItemResponseDTO> itemResponseDTOs = new ArrayList<>();

        for (Item item : items) {
            itemResponseDTOs.add(new ItemResponseDTO(
                    item.getId(),
                    item.getDescription(),
                    item.getName(),
                    item.getIsSold(),
                    item.getTags(),
                    BlobMapper.INSTANCE.toDto(item.getImageBlob()),
                    item.getSubcategory(),
                    item.getCategory(),
                    UserMapper.INSTANCE.toUserMinDTO(item.getCreatedBy()),  // Mapping createdBy
                    UserMapper.INSTANCE.toUserMinDTO(item.getLastModifiedBy()),  // Mapping lastModifiedBy
                    item.getCreatedDatetime(),
                    item.getLastModifiedDatetime()
            ));
        }
        return itemResponseDTOs;
    }

    public ItemResponseDTO updateNameAndDescription(Long id, String name, String description) {
        // Fetch the item
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Update fields if provided
        if (name != null) {
            item.setName(name);
        }
        if (description != null) {
            item.setDescription(description);
        }

        // Save the updated item
        Item updatedItem = itemRepository.save(item);

        // Return the response DTO
        return new ItemResponseDTO(
                updatedItem.getId(),
                updatedItem.getDescription(),
                updatedItem.getName(),
                updatedItem.getIsSold(),
                updatedItem.getTags(),
                BlobMapper.INSTANCE.toDto(updatedItem.getImageBlob()),
                updatedItem.getSubcategory(),
                updatedItem.getCategory(),
                UserMapper.INSTANCE.toUserMinDTO(updatedItem.getCreatedBy()),
                UserMapper.INSTANCE.toUserMinDTO(updatedItem.getLastModifiedBy()),
                updatedItem.getCreatedDatetime(),
                updatedItem.getLastModifiedDatetime()
        );
    }


}

