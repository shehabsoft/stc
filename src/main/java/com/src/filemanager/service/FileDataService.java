package com.src.filemanager.service;

import com.src.filemanager.service.dto.FileDataDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.src.filemanager.domain.FileData}.
 */
public interface FileDataService {
    /**
     * Save a fileData.
     *
     * @param fileDataDTO the entity to save.
     * @return the persisted entity.
     */
    FileDataDTO save(FileDataDTO fileDataDTO);

    /**
     * Updates a fileData.
     *
     * @param fileDataDTO the entity to update.
     * @return the persisted entity.
     */
    FileDataDTO update(FileDataDTO fileDataDTO);

    /**
     * Partially updates a fileData.
     *
     * @param fileDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileDataDTO> partialUpdate(FileDataDTO fileDataDTO);

    /**
     * Get all the fileData.
     *
     * @return the list of entities.
     */
    List<FileDataDTO> findAll();

    /**
     * Get the "id" fileData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDataDTO> findOne(Long id);

      Optional<FileDataDTO> findOneByName(String fileName,String userEnail);

    /**
     * Delete the "id" fileData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
