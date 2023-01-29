package com.src.filemanager.service.impl;

import com.src.filemanager.domain.PermissionUsersGroup;
import com.src.filemanager.domain.enumeration.PermissionLevel;
import com.src.filemanager.repository.PermissionUsersGroupRepository;
import com.src.filemanager.service.PermissionUsersGroupService;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import com.src.filemanager.service.mapper.PermissionUsersGroupMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PermissionUsersGroup}.
 */
@Service
@Transactional
public class PermissionUsersGroupServiceImpl implements PermissionUsersGroupService {

    private final Logger log = LoggerFactory.getLogger(PermissionUsersGroupServiceImpl.class);

    private final PermissionUsersGroupRepository permissionUsersGroupRepository;


    private final PermissionUsersGroupMapper permissionUsersGroupMapper;

    public PermissionUsersGroupServiceImpl(
        PermissionUsersGroupRepository permissionUsersGroupRepository,
        PermissionUsersGroupMapper permissionUsersGroupMapper
    ) {
        this.permissionUsersGroupRepository = permissionUsersGroupRepository;
        this.permissionUsersGroupMapper = permissionUsersGroupMapper;
    }

    @Override
    public PermissionUsersGroupDTO save(PermissionUsersGroupDTO permissionUsersGroupDTO) {
        log.debug("Request to save PermissionUsersGroup : {}", permissionUsersGroupDTO);
        PermissionUsersGroup permissionUsersGroup = permissionUsersGroupMapper.toEntity(permissionUsersGroupDTO);
        permissionUsersGroup = permissionUsersGroupRepository.save(permissionUsersGroup);
        return permissionUsersGroupMapper.toDto(permissionUsersGroup);
    }

    @Override
    public PermissionUsersGroupDTO update(PermissionUsersGroupDTO permissionUsersGroupDTO) {
        log.debug("Request to update PermissionUsersGroup : {}", permissionUsersGroupDTO);
        PermissionUsersGroup permissionUsersGroup = permissionUsersGroupMapper.toEntity(permissionUsersGroupDTO);
        permissionUsersGroup = permissionUsersGroupRepository.save(permissionUsersGroup);
        return permissionUsersGroupMapper.toDto(permissionUsersGroup);
    }

    @Override
    public Optional<PermissionUsersGroupDTO> partialUpdate(PermissionUsersGroupDTO permissionUsersGroupDTO) {
        log.debug("Request to partially update PermissionUsersGroup : {}", permissionUsersGroupDTO);

        return permissionUsersGroupRepository
            .findById(permissionUsersGroupDTO.getId())
            .map(existingPermissionUsersGroup -> {
                permissionUsersGroupMapper.partialUpdate(existingPermissionUsersGroup, permissionUsersGroupDTO);

                return existingPermissionUsersGroup;
            })
            .map(permissionUsersGroupRepository::save)
            .map(permissionUsersGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionUsersGroupDTO> findAll() {
        log.debug("Request to get all PermissionUsersGroups");
        return permissionUsersGroupRepository
            .findAll()
            .stream()
            .map(permissionUsersGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionUsersGroupDTO> findOne(Long id) {
        log.debug("Request to get PermissionUsersGroup : {}", id);
        return permissionUsersGroupRepository.findById(id).map(permissionUsersGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionUsersGroupDTO> findOneByPermissionLevel(PermissionLevel permissionLevel, PermissionGroupsDTO permissionGroupsDTO)
    {

        return permissionUsersGroupRepository.findByPermissionLevelAndPermissionGroup_GroupName(permissionLevel,permissionGroupsDTO.getGroupName()).map(permissionUsersGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PermissionUsersGroup : {}", id);
        permissionUsersGroupRepository.deleteById(id);
    }
}
