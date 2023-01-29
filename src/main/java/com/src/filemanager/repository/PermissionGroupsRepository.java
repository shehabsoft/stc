package com.src.filemanager.repository;

import com.src.filemanager.domain.PermissionGroups;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the PermissionGroups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionGroupsRepository extends JpaRepository<PermissionGroups, Long> {

      Optional<PermissionGroups> findByGroupName(String groupName);
}
