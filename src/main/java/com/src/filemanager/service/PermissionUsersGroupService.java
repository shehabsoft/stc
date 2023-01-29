package com.src.filemanager.service;

import com.src.filemanager.domain.enumeration.PermissionLevel;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.src.filemanager.domain.PermissionUsersGroup}.
 */
public interface PermissionUsersGroupService {
    /**
     * Save a permissionUsersGroup.
     *
     * @param permissionUsersGroupDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionUsersGroupDTO save(PermissionUsersGroupDTO permissionUsersGroupDTO);

    /**
     * Updates a permissionUsersGroup.
     *
     * @param permissionUsersGroupDTO the entity to update.
     * @return the persisted entity.
     */
    PermissionUsersGroupDTO update(PermissionUsersGroupDTO permissionUsersGroupDTO);

    /**
     * Partially updates a permissionUsersGroup.
     *
     * @param permissionUsersGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PermissionUsersGroupDTO> partialUpdate(PermissionUsersGroupDTO permissionUsersGroupDTO);

    /**
     * Get all the permissionUsersGroups.
     *
     * @return the list of entities.
     */
    List<PermissionUsersGroupDTO> findAll();

    /**
     * Get the "id" permissionUsersGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionUsersGroupDTO> findOne(Long id);
    Optional<PermissionUsersGroupDTO> findOneByPermissionLevel(PermissionLevel  permissionLevel, PermissionGroupsDTO  permissionGroupsDTO);

    /**
     * Delete the "id" permissionUsersGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
