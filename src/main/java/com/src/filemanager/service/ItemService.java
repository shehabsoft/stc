package com.src.filemanager.service;

import com.src.filemanager.service.dto.ItemDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.src.filemanager.domain.Item}.
 */
public interface ItemService {
    /**
     * Save a item.
     *
     * @param itemDTO the entity to save.
     * @return the persisted entity.
     */
    ItemDTO save(ItemDTO itemDTO);

    /**
     * Updates a item.
     *
     * @param itemDTO the entity to update.
     * @return the persisted entity.
     */
    ItemDTO update(ItemDTO itemDTO);

    /**
     * Partially updates a item.
     *
     * @param itemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemDTO> partialUpdate(ItemDTO itemDTO);

    /**
     * Get all the items.
     *
     * @return the list of entities.
     */
    List<ItemDTO> findAll();

    /**
     * Get the "id" item.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemDTO> findOne(Long id);
    Optional<ItemDTO> findByNameAndUserPermissionGroup(String name, PermissionUsersGroupDTO permissionUsersGroupDTO);

    /**
     * Delete the "id" item.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
