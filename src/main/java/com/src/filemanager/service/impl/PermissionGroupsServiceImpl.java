package com.src.filemanager.service.impl;

import com.src.filemanager.domain.PermissionGroups;
import com.src.filemanager.repository.PermissionGroupsRepository;
import com.src.filemanager.service.PermissionGroupsService;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.service.mapper.PermissionGroupsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PermissionGroups}.
 */
@Service
@Transactional
public class PermissionGroupsServiceImpl implements PermissionGroupsService {

    private final Logger log = LoggerFactory.getLogger(PermissionGroupsServiceImpl.class);

    private final PermissionGroupsRepository permissionGroupsRepository;

    private final PermissionGroupsMapper permissionGroupsMapper;

    public PermissionGroupsServiceImpl(
        PermissionGroupsRepository permissionGroupsRepository,
        PermissionGroupsMapper permissionGroupsMapper
    ) {
        this.permissionGroupsRepository = permissionGroupsRepository;
        this.permissionGroupsMapper = permissionGroupsMapper;
    }

    @Override
    public PermissionGroupsDTO save(PermissionGroupsDTO permissionGroupsDTO) {
        log.debug("Request to save PermissionGroups : {}", permissionGroupsDTO);
        PermissionGroups permissionGroups = permissionGroupsMapper.toEntity(permissionGroupsDTO);
        permissionGroups = permissionGroupsRepository.save(permissionGroups);
        return permissionGroupsMapper.toDto(permissionGroups);
    }

    @Override
    public PermissionGroupsDTO update(PermissionGroupsDTO permissionGroupsDTO) {
        log.debug("Request to update PermissionGroups : {}", permissionGroupsDTO);
        PermissionGroups permissionGroups = permissionGroupsMapper.toEntity(permissionGroupsDTO);
        permissionGroups = permissionGroupsRepository.save(permissionGroups);
        return permissionGroupsMapper.toDto(permissionGroups);
    }

    @Override
    public Optional<PermissionGroupsDTO> partialUpdate(PermissionGroupsDTO permissionGroupsDTO) {
        log.debug("Request to partially update PermissionGroups : {}", permissionGroupsDTO);

        return permissionGroupsRepository
            .findById(permissionGroupsDTO.getId())
            .map(existingPermissionGroups -> {
                permissionGroupsMapper.partialUpdate(existingPermissionGroups, permissionGroupsDTO);

                return existingPermissionGroups;
            })
            .map(permissionGroupsRepository::save)
            .map(permissionGroupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PermissionGroupsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PermissionGroups");
        return permissionGroupsRepository.findAll(pageable).map(permissionGroupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionGroupsDTO> findOne(Long id) {
        log.debug("Request to get PermissionGroups : {}", id);
        return permissionGroupsRepository.findById(id).map(permissionGroupsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionGroupsDTO> findOne(String name) {
        log.debug("Request to get PermissionGroups : {}", name);
        return permissionGroupsRepository.findByGroupName(name).map(permissionGroupsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PermissionGroups : {}", id);
        permissionGroupsRepository.deleteById(id);
    }
}
