package com.src.filemanager.service.mapper;

import com.src.filemanager.domain.PermissionGroups;
import com.src.filemanager.domain.PermissionUsersGroup;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissionUsersGroup} and its DTO {@link PermissionUsersGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionUsersGroupMapper extends EntityMapper<PermissionUsersGroupDTO, PermissionUsersGroup> {
    @Mapping(target = "permissionGroup", source = "permissionGroup", qualifiedByName = "permissionGroupsId")
    PermissionUsersGroupDTO toDto(PermissionUsersGroup s);

    @Named("permissionGroupsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionGroupsDTO toDtoPermissionGroupsId(PermissionGroups permissionGroups);
}
