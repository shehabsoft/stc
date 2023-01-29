package com.src.filemanager.service.mapper;

import com.src.filemanager.domain.FileData;
import com.src.filemanager.domain.Item;
import com.src.filemanager.service.dto.FileDataDTO;
import com.src.filemanager.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileData} and its DTO {@link FileDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileDataMapper extends EntityMapper<FileDataDTO, FileData> {
    @Mapping(target = "item", source = "item", qualifiedByName = "itemId")
    FileDataDTO toDto(FileData s);

    @Named("itemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoItemId(Item item);
}
