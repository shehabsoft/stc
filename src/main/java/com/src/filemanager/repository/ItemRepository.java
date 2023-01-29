package com.src.filemanager.repository;

import com.src.filemanager.domain.Item;
import com.src.filemanager.domain.PermissionUsersGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByNameAndPermissionUsersGroup(String name, PermissionUsersGroup permissionUsersGroup);
}
