package com.src.filemanager.service.impl;

import com.src.filemanager.domain.FileData;
import com.src.filemanager.repository.FileDataRepository;
import com.src.filemanager.service.FileDataService;
import com.src.filemanager.service.dto.FileDataDTO;
import com.src.filemanager.service.mapper.FileDataMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileData}.
 */
@Service
@Transactional
public class FileDataServiceImpl implements FileDataService {

    private final Logger log = LoggerFactory.getLogger(FileDataServiceImpl.class);

    private final FileDataRepository fileDataRepository;

    private final FileDataMapper fileDataMapper;

    public FileDataServiceImpl(FileDataRepository fileDataRepository, FileDataMapper fileDataMapper) {
        this.fileDataRepository = fileDataRepository;
        this.fileDataMapper = fileDataMapper;
    }

    @Override
    public FileDataDTO save(FileDataDTO fileDataDTO) {
        log.debug("Request to save FileData : {}", fileDataDTO);
        FileData fileData = fileDataMapper.toEntity(fileDataDTO);
        fileData = fileDataRepository.save(fileData);
        return fileDataMapper.toDto(fileData);
    }

    @Override
    public FileDataDTO update(FileDataDTO fileDataDTO) {
        log.debug("Request to update FileData : {}", fileDataDTO);
        FileData fileData = fileDataMapper.toEntity(fileDataDTO);
        fileData = fileDataRepository.save(fileData);
        return fileDataMapper.toDto(fileData);
    }

    @Override
    public Optional<FileDataDTO> partialUpdate(FileDataDTO fileDataDTO) {
        log.debug("Request to partially update FileData : {}", fileDataDTO);

        return fileDataRepository
            .findById(fileDataDTO.getId())
            .map(existingFileData -> {
                fileDataMapper.partialUpdate(existingFileData, fileDataDTO);

                return existingFileData;
            })
            .map(fileDataRepository::save)
            .map(fileDataMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDataDTO> findAll() {
        log.debug("Request to get all FileData");
        return fileDataRepository.findAll().stream().map(fileDataMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDataDTO> findOne(Long id) {
        log.debug("Request to get FileData : {}", id);
        return fileDataRepository.findById(id).map(fileDataMapper::toDto);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<FileDataDTO> findOneByName(String fileName,String userEnail)
    {
        return fileDataRepository.findfileByCriticia(fileName,userEnail).map(fileDataMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileData : {}", id);
        fileDataRepository.deleteById(id);
    }
}
