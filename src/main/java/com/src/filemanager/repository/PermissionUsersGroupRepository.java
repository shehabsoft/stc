package com.src.filemanager.repository;

import com.src.filemanager.domain.PermissionUsersGroup;
import com.src.filemanager.domain.enumeration.PermissionLevel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the PermissionUsersGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionUsersGroupRepository extends JpaRepository<PermissionUsersGroup, Long> {

    Optional<PermissionUsersGroup> findByPermissionLevelAndPermissionGroup_GroupName(PermissionLevel permissionLevel,String groupName);
}
