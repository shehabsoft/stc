package com.src.filemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.src.filemanager.IntegrationTest;
import com.src.filemanager.domain.PermissionUsersGroup;
import com.src.filemanager.domain.enumeration.PermissionLevel;
import com.src.filemanager.repository.PermissionUsersGroupRepository;
import com.src.filemanager.service.dto.PermissionUsersGroupDTO;
import com.src.filemanager.service.mapper.PermissionUsersGroupMapper;
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
 * Integration tests for the {@link PermissionUsersGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionUsersGroupResourceIT {

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final PermissionLevel DEFAULT_PERMISSION_LEVEL = PermissionLevel.VIEW;
    private static final PermissionLevel UPDATED_PERMISSION_LEVEL = PermissionLevel.EDIT;

    private static final String ENTITY_API_URL = "/api/permission-users-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PermissionUsersGroupRepository permissionUsersGroupRepository;

    @Autowired
    private PermissionUsersGroupMapper permissionUsersGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionUsersGroupMockMvc;

    private PermissionUsersGroup permissionUsersGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionUsersGroup createEntity(EntityManager em) {
        PermissionUsersGroup permissionUsersGroup = new PermissionUsersGroup()
            .userEmail(DEFAULT_USER_EMAIL)
            .permissionLevel(DEFAULT_PERMISSION_LEVEL);
        return permissionUsersGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionUsersGroup createUpdatedEntity(EntityManager em) {
        PermissionUsersGroup permissionUsersGroup = new PermissionUsersGroup()
            .userEmail(UPDATED_USER_EMAIL)
            .permissionLevel(UPDATED_PERMISSION_LEVEL);
        return permissionUsersGroup;
    }

    @BeforeEach
    public void initTest() {
        permissionUsersGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeCreate = permissionUsersGroupRepository.findAll().size();
        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);
        restPermissionUsersGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeCreate + 1);
        PermissionUsersGroup testPermissionUsersGroup = permissionUsersGroupList.get(permissionUsersGroupList.size() - 1);
        assertThat(testPermissionUsersGroup.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testPermissionUsersGroup.getPermissionLevel()).isEqualTo(DEFAULT_PERMISSION_LEVEL);
    }

    @Test
    @Transactional
    void createPermissionUsersGroupWithExistingId() throws Exception {
        // Create the PermissionUsersGroup with an existing ID
        permissionUsersGroup.setId(1L);
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        int databaseSizeBeforeCreate = permissionUsersGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionUsersGroupMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPermissionUsersGroups() throws Exception {
        // Initialize the database
        permissionUsersGroupRepository.saveAndFlush(permissionUsersGroup);

        // Get all the permissionUsersGroupList
        restPermissionUsersGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionUsersGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].permissionLevel").value(hasItem(DEFAULT_PERMISSION_LEVEL.toString())));
    }

    @Test
    @Transactional
    void getPermissionUsersGroup() throws Exception {
        // Initialize the database
        permissionUsersGroupRepository.saveAndFlush(permissionUsersGroup);

        // Get the permissionUsersGroup
        restPermissionUsersGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, permissionUsersGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissionUsersGroup.getId().intValue()))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.permissionLevel").value(DEFAULT_PERMISSION_LEVEL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPermissionUsersGroup() throws Exception {
        // Get the permissionUsersGroup
        restPermissionUsersGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissionUsersGroup() throws Exception {
        // Initialize the database
        permissionUsersGroupRepository.saveAndFlush(permissionUsersGroup);

        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();

        // Update the permissionUsersGroup
        PermissionUsersGroup updatedPermissionUsersGroup = permissionUsersGroupRepository.findById(permissionUsersGroup.getId()).get();
        // Disconnect from session so that the updates on updatedPermissionUsersGroup are not directly saved in db
        em.detach(updatedPermissionUsersGroup);
        updatedPermissionUsersGroup.userEmail(UPDATED_USER_EMAIL).permissionLevel(UPDATED_PERMISSION_LEVEL);
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(updatedPermissionUsersGroup);

        restPermissionUsersGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionUsersGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
        PermissionUsersGroup testPermissionUsersGroup = permissionUsersGroupList.get(permissionUsersGroupList.size() - 1);
        assertThat(testPermissionUsersGroup.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testPermissionUsersGroup.getPermissionLevel()).isEqualTo(UPDATED_PERMISSION_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();
        permissionUsersGroup.setId(count.incrementAndGet());

        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionUsersGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionUsersGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();
        permissionUsersGroup.setId(count.incrementAndGet());

        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionUsersGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();
        permissionUsersGroup.setId(count.incrementAndGet());

        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionUsersGroupMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionUsersGroupWithPatch() throws Exception {
        // Initialize the database
        permissionUsersGroupRepository.saveAndFlush(permissionUsersGroup);

        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();

        // Update the permissionUsersGroup using partial update
        PermissionUsersGroup partialUpdatedPermissionUsersGroup = new PermissionUsersGroup();
        partialUpdatedPermissionUsersGroup.setId(permissionUsersGroup.getId());

        restPermissionUsersGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionUsersGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionUsersGroup))
            )
            .andExpect(status().isOk());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
        PermissionUsersGroup testPermissionUsersGroup = permissionUsersGroupList.get(permissionUsersGroupList.size() - 1);
        assertThat(testPermissionUsersGroup.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testPermissionUsersGroup.getPermissionLevel()).isEqualTo(DEFAULT_PERMISSION_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdatePermissionUsersGroupWithPatch() throws Exception {
        // Initialize the database
        permissionUsersGroupRepository.saveAndFlush(permissionUsersGroup);

        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();

        // Update the permissionUsersGroup using partial update
        PermissionUsersGroup partialUpdatedPermissionUsersGroup = new PermissionUsersGroup();
        partialUpdatedPermissionUsersGroup.setId(permissionUsersGroup.getId());

        partialUpdatedPermissionUsersGroup.userEmail(UPDATED_USER_EMAIL).permissionLevel(UPDATED_PERMISSION_LEVEL);

        restPermissionUsersGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionUsersGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPermissionUsersGroup))
            )
            .andExpect(status().isOk());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
        PermissionUsersGroup testPermissionUsersGroup = permissionUsersGroupList.get(permissionUsersGroupList.size() - 1);
        assertThat(testPermissionUsersGroup.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testPermissionUsersGroup.getPermissionLevel()).isEqualTo(UPDATED_PERMISSION_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();
        permissionUsersGroup.setId(count.incrementAndGet());

        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionUsersGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionUsersGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();
        permissionUsersGroup.setId(count.incrementAndGet());

        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionUsersGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissionUsersGroup() throws Exception {
        int databaseSizeBeforeUpdate = permissionUsersGroupRepository.findAll().size();
        permissionUsersGroup.setId(count.incrementAndGet());

        // Create the PermissionUsersGroup
        PermissionUsersGroupDTO permissionUsersGroupDTO = permissionUsersGroupMapper.toDto(permissionUsersGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionUsersGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(permissionUsersGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionUsersGroup in the database
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissionUsersGroup() throws Exception {
        // Initialize the database
        permissionUsersGroupRepository.saveAndFlush(permissionUsersGroup);

        int databaseSizeBeforeDelete = permissionUsersGroupRepository.findAll().size();

        // Delete the permissionUsersGroup
        restPermissionUsersGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissionUsersGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PermissionUsersGroup> permissionUsersGroupList = permissionUsersGroupRepository.findAll();
        assertThat(permissionUsersGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
