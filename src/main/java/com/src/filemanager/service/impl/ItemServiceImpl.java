package com.src.filemanager.service.impl;

import com.src.filemanager.domain.Item;
import com.src.filemanager.domain.PermissionUsersGroup;
import com.src.filemanager.repository.ItemRepository;
import com.src.filemanager.service.ItemService;
import com.src.filemanager.service.dto.ItemDTO;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import com.src.filemanager.service.mapper.ItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.src.filemanager.service.mapper.PermissionUsersGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Item}.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    private final PermissionUsersGroupMapper permissionUsersGroupMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper,PermissionUsersGroupMapper permissionUsersGroupMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.permissionUsersGroupMapper=permissionUsersGroupMapper;
    }

    @Override
    public ItemDTO save(ItemDTO itemDTO) {
        log.debug("Request to save Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO) {
        log.debug("Request to update Item : {}", itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        return itemMapper.toDto(item);
    }

    @Override
    public Optional<ItemDTO> partialUpdate(ItemDTO itemDTO) {
        log.debug("Request to partially update Item : {}", itemDTO);

        return itemRepository
            .findById(itemDTO.getId())
            .map(existingItem -> {
                itemMapper.partialUpdate(existingItem, itemDTO);

                return existingItem;
            })
            .map(itemRepository::save)
            .map(itemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> findAll() {
        log.debug("Request to get all Items");
        return itemRepository.findAll().stream().map(itemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        return itemRepository.findById(id).map(itemMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<ItemDTO> findByNameAndUserPermissionGroup(String name, PermissionUsersGroupDTO permissionUsersGroupDTO)
    {
        log.debug("Request to get Item : {}", name);
        PermissionUsersGroup permissionUsersGroup = permissionUsersGroupMapper.toEntity(permissionUsersGroupDTO);
        return itemRepository.findByNameAndPermissionUsersGroup(name,permissionUsersGroup).map(itemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.deleteById(id);
    }
}
