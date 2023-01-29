package com.src.filemanager.web.rest;

import com.src.filemanager.repository.PermissionGroupsRepository;
import com.src.filemanager.service.PermissionGroupsService;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.src.filemanager.domain.PermissionGroups}.
 */
@RestController
@RequestMapping("/api")
public class PermissionGroupsResource {

    private final Logger log = LoggerFactory.getLogger(PermissionGroupsResource.class);

    private static final String ENTITY_NAME = "permissionGroups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionGroupsService permissionGroupsService;

    private final PermissionGroupsRepository permissionGroupsRepository;

    public PermissionGroupsResource(
        PermissionGroupsService permissionGroupsService,
        PermissionGroupsRepository permissionGroupsRepository
    ) {
        this.permissionGroupsService = permissionGroupsService;
        this.permissionGroupsRepository = permissionGroupsRepository;
    }

    /**
     * {@code POST  /permission-groups} : Create a new permissionGroups.
     *
     * @param permissionGroupsDTO the permissionGroupsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionGroupsDTO, or with status {@code 400 (Bad Request)} if the permissionGroups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permission-groups")
    public ResponseEntity<PermissionGroupsDTO> createPermissionGroups(@RequestBody PermissionGroupsDTO permissionGroupsDTO)
        throws URISyntaxException {
        log.debug("REST request to save PermissionGroups : {}", permissionGroupsDTO);
        if (permissionGroupsDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissionGroups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermissionGroupsDTO result = permissionGroupsService.save(permissionGroupsDTO);
        return ResponseEntity
            .created(new URI("/api/permission-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permission-groups/:id} : Updates an existing permissionGroups.
     *
     * @param id the id of the permissionGroupsDTO to save.
     * @param permissionGroupsDTO the permissionGroupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionGroupsDTO,
     * or with status {@code 400 (Bad Request)} if the permissionGroupsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionGroupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permission-groups/{id}")
    public ResponseEntity<PermissionGroupsDTO> updatePermissionGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PermissionGroupsDTO permissionGroupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PermissionGroups : {}, {}", id, permissionGroupsDTO);
        if (permissionGroupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionGroupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PermissionGroupsDTO result = permissionGroupsService.update(permissionGroupsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionGroupsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permission-groups/:id} : Partial updates given fields of an existing permissionGroups, field will ignore if it is null
     *
     * @param id the id of the permissionGroupsDTO to save.
     * @param permissionGroupsDTO the permissionGroupsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionGroupsDTO,
     * or with status {@code 400 (Bad Request)} if the permissionGroupsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissionGroupsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionGroupsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permission-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissionGroupsDTO> partialUpdatePermissionGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PermissionGroupsDTO permissionGroupsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PermissionGroups partially : {}, {}", id, permissionGroupsDTO);
        if (permissionGroupsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionGroupsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionGroupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissionGroupsDTO> result = permissionGroupsService.partialUpdate(permissionGroupsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionGroupsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permission-groups} : get all the permissionGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionGroups in body.
     */
    @GetMapping("/permission-groups")
    public ResponseEntity<List<PermissionGroupsDTO>> getAllPermissionGroups(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PermissionGroups");
        Page<PermissionGroupsDTO> page = permissionGroupsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permission-groups/:id} : get the "id" permissionGroups.
     *
     * @param id the id of the permissionGroupsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionGroupsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permission-groups/{id}")
    public ResponseEntity<PermissionGroupsDTO> getPermissionGroups(@PathVariable Long id) {
        log.debug("REST request to get PermissionGroups : {}", id);
        Optional<PermissionGroupsDTO> permissionGroupsDTO = permissionGroupsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionGroupsDTO);
    }

    /**
     * {@code DELETE  /permission-groups/:id} : delete the "id" permissionGroups.
     *
     * @param id the id of the permissionGroupsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permission-groups/{id}")
    public ResponseEntity<Void> deletePermissionGroups(@PathVariable Long id) {
        log.debug("REST request to delete PermissionGroups : {}", id);
        permissionGroupsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
