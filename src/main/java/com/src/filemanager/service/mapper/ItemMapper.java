package com.src.filemanager.service.mapper;

import com.src.filemanager.domain.Item;
import com.src.filemanager.domain.PermissionUsersGroup;
import com.src.filemanager.service.dto.ItemDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "permissionUsersGroup", source = "permissionUsersGroup", qualifiedByName = "permissionUsersGroupId")
    ItemDTO toDto(Item s);

    @Named("permissionUsersGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionUsersGroupDTO toDtoPermissionUsersGroupId(PermissionUsersGroup permissionUsersGroup);
}
