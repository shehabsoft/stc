package com.src.filemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.src.filemanager.IntegrationTest;
import com.src.filemanager.domain.PermissionGroups;
import com.src.filemanager.repository.PermissionGroupsRepository;
import com.src.filemanager.service.dto.PermissionGroupsDTO;
import com.src.filemanager.service.mapper.PermissionGroupsMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PermissionGroupsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionGroupsResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permission-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionGroupsRepository permissionGroupsRepository;

    @Autowired
    private PermissionGroupsMapper permissionGroupsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionGroupsMockMvc;

    private PermissionGroups permissionGroups;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionGroups createEntity(EntityManager em) {
        PermissionGroups permissionGroups = new PermissionGroups().groupName(DEFAULT_GROUP_NAME);
        return permissionGroups;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionGroups createUpdatedEntity(EntityManager em) {
        PermissionGroups permissionGroups = new PermissionGroups().groupName(UPDATED_GROUP_NAME);
        return permissionGroups;
    }

    @BeforeEach
    public void initTest() {
        permissionGroups = createEntity(em);
    }

    @Test
    @Transactional
    void createPermissionGroups() throws Exception {
        int databaseSizeBeforeCreate = permissionGroupsRepository.findAll().size();
        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);
        restPermissionGroupsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeCreate + 1);
        PermissionGroups testPermissionGroups = permissionGroupsList.get(permissionGroupsList.size() - 1);
        assertThat(testPermissionGroups.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
    }

    @Test
    @Transactional
    void createPermissionGroupsWithExistingId() throws Exception {
        // Create the PermissionGroups with an existing ID
        permissionGroups.setId(1L);
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        int databaseSizeBeforeCreate = permissionGroupsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionGroupsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPermissionGroups() throws Exception {
        // Initialize the database
        permissionGroupsRepository.saveAndFlush(permissionGroups);

        // Get all the permissionGroupsList
        restPermissionGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionGroups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)));
    }

    @Test
    @Transactional
    void getPermissionGroups() throws Exception {
        // Initialize the database
        permissionGroupsRepository.saveAndFlush(permissionGroups);

        // Get the permissionGroups
        restPermissionGroupsMockMvc
            .perform(get(ENTITY_API_URL_ID, permissionGroups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissionGroups.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPermissionGroups() throws Exception {
        // Get the permissionGroups
        restPermissionGroupsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissionGroups() throws Exception {
        // Initialize the database
        permissionGroupsRepository.saveAndFlush(permissionGroups);

        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();

        // Update the permissionGroups
        PermissionGroups updatedPermissionGroups = permissionGroupsRepository.findById(permissionGroups.getId()).get();
        // Disconnect from session so that the updates on updatedPermissionGroups are not directly saved in db
        em.detach(updatedPermissionGroups);
        updatedPermissionGroups.groupName(UPDATED_GROUP_NAME);
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(updatedPermissionGroups);

        restPermissionGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionGroupsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isOk());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
        PermissionGroups testPermissionGroups = permissionGroupsList.get(permissionGroupsList.size() - 1);
        assertThat(testPermissionGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPermissionGroups() throws Exception {
        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();
        permissionGroups.setId(count.incrementAndGet());

        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionGroupsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissionGroups() throws Exception {
        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();
        permissionGroups.setId(count.incrementAndGet());

        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissionGroups() throws Exception {
        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();
        permissionGroups.setId(count.incrementAndGet());

        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionGroupsWithPatch() throws Exception {
        // Initialize the database
        permissionGroupsRepository.saveAndFlush(permissionGroups);

        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();

        // Update the permissionGroups using partial update
        PermissionGroups partialUpdatedPermissionGroups = new PermissionGroups();
        partialUpdatedPermissionGroups.setId(permissionGroups.getId());

        partialUpdatedPermissionGroups.groupName(UPDATED_GROUP_NAME);

        restPermissionGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionGroups))
            )
            .andExpect(status().isOk());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
        PermissionGroups testPermissionGroups = permissionGroupsList.get(permissionGroupsList.size() - 1);
        assertThat(testPermissionGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePermissionGroupsWithPatch() throws Exception {
        // Initialize the database
        permissionGroupsRepository.saveAndFlush(permissionGroups);

        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();

        // Update the permissionGroups using partial update
        PermissionGroups partialUpdatedPermissionGroups = new PermissionGroups();
        partialUpdatedPermissionGroups.setId(permissionGroups.getId());

        partialUpdatedPermissionGroups.groupName(UPDATED_GROUP_NAME);

        restPermissionGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionGroups))
            )
            .andExpect(status().isOk());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
        PermissionGroups testPermissionGroups = permissionGroupsList.get(permissionGroupsList.size() - 1);
        assertThat(testPermissionGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPermissionGroups() throws Exception {
        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();
        permissionGroups.setId(count.incrementAndGet());

        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionGroupsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissionGroups() throws Exception {
        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();
        permissionGroups.setId(count.incrementAndGet());

        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissionGroups() throws Exception {
        int databaseSizeBeforeUpdate = permissionGroupsRepository.findAll().size();
        permissionGroups.setId(count.incrementAndGet());

        // Create the PermissionGroups
        PermissionGroupsDTO permissionGroupsDTO = permissionGroupsMapper.toDto(permissionGroups);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionGroupsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionGroups in the database
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissionGroups() throws Exception {
        // Initialize the database
        permissionGroupsRepository.saveAndFlush(permissionGroups);

        int databaseSizeBeforeDelete = permissionGroupsRepository.findAll().size();

        // Delete the permissionGroups
        restPermissionGroupsMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissionGroups.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PermissionGroups> permissionGroupsList = permissionGroupsRepository.findAll();
        assertThat(permissionGroupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
