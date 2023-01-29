package com.src.filemanager.service;

import com.src.filemanager.service.dto.PermissionGroupsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.src.filemanager.domain.PermissionGroups}.
 */
public interface PermissionGroupsService {
    /**
     * Save a permissionGroups.
     *
     * @param permissionGroupsDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionGroupsDTO save(PermissionGroupsDTO permissionGroupsDTO);

    /**
     * Updates a permissionGroups.
     *
     * @param permissionGroupsDTO the entity to update.
     * @return the persisted entity.
     */
    PermissionGroupsDTO update(PermissionGroupsDTO permissionGroupsDTO);

    /**
     * Partially updates a permissionGroups.
     *
     * @param permissionGroupsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PermissionGroupsDTO> partialUpdate(PermissionGroupsDTO permissionGroupsDTO);

    /**
     * Get all the permissionGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PermissionGroupsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" permissionGroups.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionGroupsDTO> findOne(Long id);

    Optional<PermissionGroupsDTO> findOne(String name);
    /**
     * Delete the "id" permissionGroups.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
