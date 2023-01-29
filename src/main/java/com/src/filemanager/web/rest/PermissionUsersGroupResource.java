package com.src.filemanager.web.rest;

import com.src.filemanager.repository.PermissionUsersGroupRepository;
import com.src.filemanager.service.PermissionUsersGroupService;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
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
 * REST controller for managing {@link com.src.filemanager.domain.PermissionUsersGroup}.
 */
@RestController
@RequestMapping("/api")
public class PermissionUsersGroupResource {

    private final Logger log = LoggerFactory.getLogger(PermissionUsersGroupResource.class);

    private static final String ENTITY_NAME = "permissionUsersGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionUsersGroupService permissionUsersGroupService;

    private final PermissionUsersGroupRepository permissionUsersGroupRepository;

    public PermissionUsersGroupResource(
        PermissionUsersGroupService permissionUsersGroupService,
        PermissionUsersGroupRepository permissionUsersGroupRepository
    ) {
        this.permissionUsersGroupService = permissionUsersGroupService;
        this.permissionUsersGroupRepository = permissionUsersGroupRepository;
    }

    /**
     * {@code POST  /permission-users-groups} : Create a new permissionUsersGroup.
     *
     * @param permissionUsersGroupDTO the permissionUsersGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionUsersGroupDTO, or with status {@code 400 (Bad Request)} if the permissionUsersGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permission-users-groups")
    public ResponseEntity<PermissionUsersGroupDTO> createPermissionUsersGroup(@RequestBody PermissionUsersGroupDTO permissionUsersGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save PermissionUsersGroup : {}", permissionUsersGroupDTO);
        if (permissionUsersGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissionUsersGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PermissionUsersGroupDTO result = permissionUsersGroupService.save(permissionUsersGroupDTO);
        return ResponseEntity
            .created(new URI("/api/permission-users-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permission-users-groups/:id} : Updates an existing permissionUsersGroup.
     *
     * @param id the id of the permissionUsersGroupDTO to save.
     * @param permissionUsersGroupDTO the permissionUsersGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionUsersGroupDTO,
     * or with status {@code 400 (Bad Request)} if the permissionUsersGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionUsersGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permission-users-groups/{id}")
    public ResponseEntity<PermissionUsersGroupDTO> updatePermissionUsersGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PermissionUsersGroupDTO permissionUsersGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PermissionUsersGroup : {}, {}", id, permissionUsersGroupDTO);
        if (permissionUsersGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionUsersGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionUsersGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PermissionUsersGroupDTO result = permissionUsersGroupService.update(permissionUsersGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionUsersGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /permission-users-groups/:id} : Partial updates given fields of an existing permissionUsersGroup, field will ignore if it is null
     *
     * @param id the id of the permissionUsersGroupDTO to save.
     * @param permissionUsersGroupDTO the permissionUsersGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionUsersGroupDTO,
     * or with status {@code 400 (Bad Request)} if the permissionUsersGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissionUsersGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionUsersGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/permission-users-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissionUsersGroupDTO> partialUpdatePermissionUsersGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PermissionUsersGroupDTO permissionUsersGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PermissionUsersGroup partially : {}, {}", id, permissionUsersGroupDTO);
        if (permissionUsersGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionUsersGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionUsersGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissionUsersGroupDTO> result = permissionUsersGroupService.partialUpdate(permissionUsersGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionUsersGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permission-users-groups} : get all the permissionUsersGroups.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionUsersGroups in body.
     */
    @GetMapping("/permission-users-groups")
    public List<PermissionUsersGroupDTO> getAllPermissionUsersGroups() {
        log.debug("REST request to get all PermissionUsersGroups");
        return permissionUsersGroupService.findAll();
    }

    /**
     * {@code GET  /permission-users-groups/:id} : get the "id" permissionUsersGroup.
     *
     * @param id the id of the permissionUsersGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionUsersGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/permission-users-groups/{id}")
    public ResponseEntity<PermissionUsersGroupDTO> getPermissionUsersGroup(@PathVariable Long id) {
        log.debug("REST request to get PermissionUsersGroup : {}", id);
        Optional<PermissionUsersGroupDTO> permissionUsersGroupDTO = permissionUsersGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionUsersGroupDTO);
    }

    /**
     * {@code DELETE  /permission-users-groups/:id} : delete the "id" permissionUsersGroup.
     *
     * @param id the id of the permissionUsersGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permission-users-groups/{id}")
    public ResponseEntity<Void> deletePermissionUsersGroup(@PathVariable Long id) {
        log.debug("REST request to delete PermissionUsersGroup : {}", id);
        permissionUsersGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
