package com.src.filemanager.web.rest;

import com.src.filemanager.repository.FileDataRepository;
import com.src.filemanager.service.FileDataService;
import com.src.filemanager.service.dto.FileDataDTO;
import com.src.filemanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.src.filemanager.domain.FileData}.
 */
@RestController
@RequestMapping("/api")
public class FileDataResource {

    private final Logger log = LoggerFactory.getLogger(FileDataResource.class);

    private static final String ENTITY_NAME = "fileData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FileDataService fileDataService;

    private final FileDataRepository fileDataRepository;

    public FileDataResource(FileDataService fileDataService, FileDataRepository fileDataRepository) {
        this.fileDataService = fileDataService;
        this.fileDataRepository = fileDataRepository;
    }

    /**
     * {@code POST  /file-data} : Create a new fileData.
     *
     * @param fileDataDTO the fileDataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileDataDTO, or with status {@code 400 (Bad Request)} if the fileData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-data")
    public ResponseEntity<FileDataDTO> createFileData(@RequestBody FileDataDTO fileDataDTO) throws URISyntaxException {
        log.debug("REST request to save FileData : {}", fileDataDTO);
        if (fileDataDTO.getId() != null) {
            throw new BadRequestAlertException("A new fileData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileDataDTO result = fileDataService.save(fileDataDTO);
        return ResponseEntity
            .created(new URI("/api/file-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-data/:id} : Updates an existing fileData.
     *
     * @param id the id of the fileDataDTO to save.
     * @param fileDataDTO the fileDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDataDTO,
     * or with status {@code 400 (Bad Request)} if the fileDataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-data/{id}")
    public ResponseEntity<FileDataDTO> updateFileData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileDataDTO fileDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FileData : {}, {}", id, fileDataDTO);
        if (fileDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileDataDTO result = fileDataService.update(fileDataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-data/:id} : Partial updates given fields of an existing fileData, field will ignore if it is null
     *
     * @param id the id of the fileDataDTO to save.
     * @param fileDataDTO the fileDataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileDataDTO,
     * or with status {@code 400 (Bad Request)} if the fileDataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fileDataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileDataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-data/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileDataDTO> partialUpdateFileData(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileDataDTO fileDataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileData partially : {}, {}", id, fileDataDTO);
        if (fileDataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileDataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileDataDTO> result = fileDataService.partialUpdate(fileDataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileDataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /file-data} : get all the fileData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileData in body.
     */
    @GetMapping("/file-data")
    public List<FileDataDTO> getAllFileData() {
        log.debug("REST request to get all FileData");
        return fileDataService.findAll();
    }

    /**
     * {@code GET  /file-data/:id} : get the "id" fileData.
     *
     * @param id the id of the fileDataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileDataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-data/{id}")
    public ResponseEntity<FileDataDTO> getFileData(@PathVariable Long id) {
        log.debug("REST request to get FileData : {}", id);
        Optional<FileDataDTO> fileDataDTO = fileDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDataDTO);
    }

    /**
     * {@code DELETE  /file-data/:id} : delete the "id" fileData.
     *
     * @param id the id of the fileDataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-data/{id}")
    public ResponseEntity<Void> deleteFileData(@PathVariable Long id) {
        log.debug("REST request to delete FileData : {}", id);
        fileDataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
