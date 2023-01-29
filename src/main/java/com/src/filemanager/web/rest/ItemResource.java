package com.src.filemanager.web.rest;

import com.src.filemanager.repository.ItemRepository;
import com.src.filemanager.service.ItemService;
import com.src.filemanager.service.dto.ItemDTO;
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
 * REST controller for managing {@link com.src.filemanager.domain.Item}.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    private static final String ENTITY_NAME = "item";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    public ItemResource(ItemService itemService, ItemRepository itemRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
    }

    /**
     * {@code POST  /items} : Create a new item.
     *
     * @param itemDTO the itemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemDTO, or with status {@code 400 (Bad Request)} if the item has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/items")
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) throws URISyntaxException {
        log.debug("REST request to save Item : {}", itemDTO);
        if (itemDTO.getId() != null) {
            throw new BadRequestAlertException("A new item cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemDTO result = itemService.save(itemDTO);
        return ResponseEntity
            .created(new URI("/api/items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /items/:id} : Updates an existing item.
     *
     * @param id the id of the itemDTO to save.
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/items/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable(value = "id", required = false) final Long id, @RequestBody ItemDTO itemDTO)
        throws URISyntaxException {
        log.debug("REST request to update Item : {}, {}", id, itemDTO);
        if (itemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemDTO result = itemService.update(itemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /items/:id} : Partial updates given fields of an existing item, field will ignore if it is null
     *
     * @param id the id of the itemDTO to save.
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemDTO> partialUpdateItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemDTO itemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Item partially : {}, {}", id, itemDTO);
        if (itemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemDTO> result = itemService.partialUpdate(itemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /items} : get all the items.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of items in body.
     */
    @GetMapping("/items")
    public List<ItemDTO> getAllItems() {
        log.debug("REST request to get all Items");
        return itemService.findAll();
    }

    /**
     * {@code GET  /items/:id} : get the "id" item.
     *
     * @param id the id of the itemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        log.debug("REST request to get Item : {}", id);
        Optional<ItemDTO> itemDTO = itemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemDTO);
    }

    /**
     * {@code DELETE  /items/:id} : delete the "id" item.
     *
     * @param id the id of the itemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.debug("REST request to delete Item : {}", id);
        itemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
