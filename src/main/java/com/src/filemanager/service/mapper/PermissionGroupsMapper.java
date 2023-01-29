package com.src.filemanager.service.mapper;

import com.src.filemanager.domain.PermissionGroups;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissionGroups} and its DTO {@link PermissionGroupsDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionGroupsMapper extends EntityMapper<PermissionGroupsDTO, PermissionGroups> {}
