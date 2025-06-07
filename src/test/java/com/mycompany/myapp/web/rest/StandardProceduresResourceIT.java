package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.StandardProceduresAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StandardProcedures;
import com.mycompany.myapp.repository.StandardProceduresRepository;
import com.mycompany.myapp.service.dto.StandardProceduresDTO;
import com.mycompany.myapp.service.mapper.StandardProceduresMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StandardProceduresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StandardProceduresResourceIT {

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_SPECIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_SPECIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_UUID = "AAAAAAAAAA";
    private static final String UPDATED_USER_UUID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/standard-procedures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StandardProceduresRepository standardProceduresRepository;

    @Autowired
    private StandardProceduresMapper standardProceduresMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStandardProceduresMockMvc;

    private StandardProcedures standardProcedures;

    private StandardProcedures insertedStandardProcedures;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StandardProcedures createEntity() {
        return new StandardProcedures().isActive(DEFAULT_IS_ACTIVE).specification(DEFAULT_SPECIFICATION).userUUID(DEFAULT_USER_UUID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StandardProcedures createUpdatedEntity() {
        return new StandardProcedures().isActive(UPDATED_IS_ACTIVE).specification(UPDATED_SPECIFICATION).userUUID(UPDATED_USER_UUID);
    }

    @BeforeEach
    void initTest() {
        standardProcedures = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedStandardProcedures != null) {
            standardProceduresRepository.delete(insertedStandardProcedures);
            insertedStandardProcedures = null;
        }
    }

    @Test
    @Transactional
    void createStandardProcedures() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);
        var returnedStandardProceduresDTO = om.readValue(
            restStandardProceduresMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(standardProceduresDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StandardProceduresDTO.class
        );

        // Validate the StandardProcedures in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStandardProcedures = standardProceduresMapper.toEntity(returnedStandardProceduresDTO);
        assertStandardProceduresUpdatableFieldsEquals(
            returnedStandardProcedures,
            getPersistedStandardProcedures(returnedStandardProcedures)
        );

        insertedStandardProcedures = returnedStandardProcedures;
    }

    @Test
    @Transactional
    void createStandardProceduresWithExistingId() throws Exception {
        // Create the StandardProcedures with an existing ID
        standardProcedures.setId(1L);
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStandardProceduresMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        standardProcedures.setIsActive(null);

        // Create the StandardProcedures, which fails.
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        restStandardProceduresMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserUUIDIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        standardProcedures.setUserUUID(null);

        // Create the StandardProcedures, which fails.
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        restStandardProceduresMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStandardProcedures() throws Exception {
        // Initialize the database
        insertedStandardProcedures = standardProceduresRepository.saveAndFlush(standardProcedures);

        // Get all the standardProceduresList
        restStandardProceduresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(standardProcedures.getId().intValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].specification").value(hasItem(DEFAULT_SPECIFICATION)))
            .andExpect(jsonPath("$.[*].userUUID").value(hasItem(DEFAULT_USER_UUID)));
    }

    @Test
    @Transactional
    void getStandardProcedures() throws Exception {
        // Initialize the database
        insertedStandardProcedures = standardProceduresRepository.saveAndFlush(standardProcedures);

        // Get the standardProcedures
        restStandardProceduresMockMvc
            .perform(get(ENTITY_API_URL_ID, standardProcedures.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(standardProcedures.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.specification").value(DEFAULT_SPECIFICATION))
            .andExpect(jsonPath("$.userUUID").value(DEFAULT_USER_UUID));
    }

    @Test
    @Transactional
    void getNonExistingStandardProcedures() throws Exception {
        // Get the standardProcedures
        restStandardProceduresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStandardProcedures() throws Exception {
        // Initialize the database
        insertedStandardProcedures = standardProceduresRepository.saveAndFlush(standardProcedures);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the standardProcedures
        StandardProcedures updatedStandardProcedures = standardProceduresRepository.findById(standardProcedures.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStandardProcedures are not directly saved in db
        em.detach(updatedStandardProcedures);
        updatedStandardProcedures.isActive(UPDATED_IS_ACTIVE).specification(UPDATED_SPECIFICATION).userUUID(UPDATED_USER_UUID);
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(updatedStandardProcedures);

        restStandardProceduresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, standardProceduresDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isOk());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStandardProceduresToMatchAllProperties(updatedStandardProcedures);
    }

    @Test
    @Transactional
    void putNonExistingStandardProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        standardProcedures.setId(longCount.incrementAndGet());

        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStandardProceduresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, standardProceduresDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStandardProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        standardProcedures.setId(longCount.incrementAndGet());

        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStandardProceduresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStandardProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        standardProcedures.setId(longCount.incrementAndGet());

        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStandardProceduresMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStandardProceduresWithPatch() throws Exception {
        // Initialize the database
        insertedStandardProcedures = standardProceduresRepository.saveAndFlush(standardProcedures);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the standardProcedures using partial update
        StandardProcedures partialUpdatedStandardProcedures = new StandardProcedures();
        partialUpdatedStandardProcedures.setId(standardProcedures.getId());

        partialUpdatedStandardProcedures.isActive(UPDATED_IS_ACTIVE);

        restStandardProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStandardProcedures.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStandardProcedures))
            )
            .andExpect(status().isOk());

        // Validate the StandardProcedures in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStandardProceduresUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStandardProcedures, standardProcedures),
            getPersistedStandardProcedures(standardProcedures)
        );
    }

    @Test
    @Transactional
    void fullUpdateStandardProceduresWithPatch() throws Exception {
        // Initialize the database
        insertedStandardProcedures = standardProceduresRepository.saveAndFlush(standardProcedures);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the standardProcedures using partial update
        StandardProcedures partialUpdatedStandardProcedures = new StandardProcedures();
        partialUpdatedStandardProcedures.setId(standardProcedures.getId());

        partialUpdatedStandardProcedures.isActive(UPDATED_IS_ACTIVE).specification(UPDATED_SPECIFICATION).userUUID(UPDATED_USER_UUID);

        restStandardProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStandardProcedures.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStandardProcedures))
            )
            .andExpect(status().isOk());

        // Validate the StandardProcedures in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStandardProceduresUpdatableFieldsEquals(
            partialUpdatedStandardProcedures,
            getPersistedStandardProcedures(partialUpdatedStandardProcedures)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStandardProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        standardProcedures.setId(longCount.incrementAndGet());

        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStandardProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, standardProceduresDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStandardProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        standardProcedures.setId(longCount.incrementAndGet());

        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStandardProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStandardProcedures() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        standardProcedures.setId(longCount.incrementAndGet());

        // Create the StandardProcedures
        StandardProceduresDTO standardProceduresDTO = standardProceduresMapper.toDto(standardProcedures);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStandardProceduresMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(standardProceduresDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StandardProcedures in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStandardProcedures() throws Exception {
        // Initialize the database
        insertedStandardProcedures = standardProceduresRepository.saveAndFlush(standardProcedures);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the standardProcedures
        restStandardProceduresMockMvc
            .perform(delete(ENTITY_API_URL_ID, standardProcedures.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return standardProceduresRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected StandardProcedures getPersistedStandardProcedures(StandardProcedures standardProcedures) {
        return standardProceduresRepository.findById(standardProcedures.getId()).orElseThrow();
    }

    protected void assertPersistedStandardProceduresToMatchAllProperties(StandardProcedures expectedStandardProcedures) {
        assertStandardProceduresAllPropertiesEquals(expectedStandardProcedures, getPersistedStandardProcedures(expectedStandardProcedures));
    }

    protected void assertPersistedStandardProceduresToMatchUpdatableProperties(StandardProcedures expectedStandardProcedures) {
        assertStandardProceduresAllUpdatablePropertiesEquals(
            expectedStandardProcedures,
            getPersistedStandardProcedures(expectedStandardProcedures)
        );
    }
}
