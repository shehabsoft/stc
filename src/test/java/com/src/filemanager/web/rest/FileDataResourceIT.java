package com.src.filemanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.src.filemanager.IntegrationTest;
import com.src.filemanager.domain.FileData;
import com.src.filemanager.repository.FileDataRepository;
import com.src.filemanager.service.dto.FileDataDTO;
import com.src.filemanager.service.mapper.FileDataMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FileDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileDataResourceIT {

    private static final byte[] DEFAULT_BINERY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BINERY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BINERY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BINERY_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/file-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FileDataMapper fileDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileDataMockMvc;

    private FileData fileData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileData createEntity(EntityManager em) {
        FileData fileData = new FileData().binery(DEFAULT_BINERY).bineryContentType(DEFAULT_BINERY_CONTENT_TYPE);
        return fileData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileData createUpdatedEntity(EntityManager em) {
        FileData fileData = new FileData().binery(UPDATED_BINERY).bineryContentType(UPDATED_BINERY_CONTENT_TYPE);
        return fileData;
    }

    @BeforeEach
    public void initTest() {
        fileData = createEntity(em);
    }

    @Test
    @Transactional
    void createFileData() throws Exception {
        int databaseSizeBeforeCreate = fileDataRepository.findAll().size();
        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);
        restFileDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDataDTO)))
            .andExpect(status().isCreated());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeCreate + 1);
        FileData testFileData = fileDataList.get(fileDataList.size() - 1);
        assertThat(testFileData.getBinery()).isEqualTo(DEFAULT_BINERY);
        assertThat(testFileData.getBineryContentType()).isEqualTo(DEFAULT_BINERY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createFileDataWithExistingId() throws Exception {
        // Create the FileData with an existing ID
        fileData.setId(1L);
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        int databaseSizeBeforeCreate = fileDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileDataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFileData() throws Exception {
        // Initialize the database
        fileDataRepository.saveAndFlush(fileData);

        // Get all the fileDataList
        restFileDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileData.getId().intValue())))
            .andExpect(jsonPath("$.[*].bineryContentType").value(hasItem(DEFAULT_BINERY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].binery").value(hasItem(Base64Utils.encodeToString(DEFAULT_BINERY))));
    }

    @Test
    @Transactional
    void getFileData() throws Exception {
        // Initialize the database
        fileDataRepository.saveAndFlush(fileData);

        // Get the fileData
        restFileDataMockMvc
            .perform(get(ENTITY_API_URL_ID, fileData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileData.getId().intValue()))
            .andExpect(jsonPath("$.bineryContentType").value(DEFAULT_BINERY_CONTENT_TYPE))
            .andExpect(jsonPath("$.binery").value(Base64Utils.encodeToString(DEFAULT_BINERY)));
    }

    @Test
    @Transactional
    void getNonExistingFileData() throws Exception {
        // Get the fileData
        restFileDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFileData() throws Exception {
        // Initialize the database
        fileDataRepository.saveAndFlush(fileData);

        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();

        // Update the fileData
        FileData updatedFileData = fileDataRepository.findById(fileData.getId()).get();
        // Disconnect from session so that the updates on updatedFileData are not directly saved in db
        em.detach(updatedFileData);
        updatedFileData.binery(UPDATED_BINERY).bineryContentType(UPDATED_BINERY_CONTENT_TYPE);
        FileDataDTO fileDataDTO = fileDataMapper.toDto(updatedFileData);

        restFileDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDataDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
        FileData testFileData = fileDataList.get(fileDataList.size() - 1);
        assertThat(testFileData.getBinery()).isEqualTo(UPDATED_BINERY);
        assertThat(testFileData.getBineryContentType()).isEqualTo(UPDATED_BINERY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFileData() throws Exception {
        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();
        fileData.setId(count.incrementAndGet());

        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileDataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileData() throws Exception {
        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();
        fileData.setId(count.incrementAndGet());

        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileData() throws Exception {
        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();
        fileData.setId(count.incrementAndGet());

        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileDataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileDataWithPatch() throws Exception {
        // Initialize the database
        fileDataRepository.saveAndFlush(fileData);

        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();

        // Update the fileData using partial update
        FileData partialUpdatedFileData = new FileData();
        partialUpdatedFileData.setId(fileData.getId());

        restFileDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileData))
            )
            .andExpect(status().isOk());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
        FileData testFileData = fileDataList.get(fileDataList.size() - 1);
        assertThat(testFileData.getBinery()).isEqualTo(DEFAULT_BINERY);
        assertThat(testFileData.getBineryContentType()).isEqualTo(DEFAULT_BINERY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFileDataWithPatch() throws Exception {
        // Initialize the database
        fileDataRepository.saveAndFlush(fileData);

        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();

        // Update the fileData using partial update
        FileData partialUpdatedFileData = new FileData();
        partialUpdatedFileData.setId(fileData.getId());

        partialUpdatedFileData.binery(UPDATED_BINERY).bineryContentType(UPDATED_BINERY_CONTENT_TYPE);

        restFileDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileData.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileData))
            )
            .andExpect(status().isOk());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
        FileData testFileData = fileDataList.get(fileDataList.size() - 1);
        assertThat(testFileData.getBinery()).isEqualTo(UPDATED_BINERY);
        assertThat(testFileData.getBineryContentType()).isEqualTo(UPDATED_BINERY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFileData() throws Exception {
        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();
        fileData.setId(count.incrementAndGet());

        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileDataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileData() throws Exception {
        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();
        fileData.setId(count.incrementAndGet());

        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileDataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileData() throws Exception {
        int databaseSizeBeforeUpdate = fileDataRepository.findAll().size();
        fileData.setId(count.incrementAndGet());

        // Create the FileData
        FileDataDTO fileDataDTO = fileDataMapper.toDto(fileData);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileDataMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileDataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileData in the database
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileData() throws Exception {
        // Initialize the database
        fileDataRepository.saveAndFlush(fileData);

        int databaseSizeBeforeDelete = fileDataRepository.findAll().size();

        // Delete the fileData
        restFileDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileData.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileData> fileDataList = fileDataRepository.findAll();
        assertThat(fileDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
