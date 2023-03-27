package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.BFPA;
import mfpai.gouv.sn.repository.BFPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BFPAResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BFPAResourceIT {

    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bfpas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BFPARepository bFPARepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBFPAMockMvc;

    private BFPA bFPA;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BFPA createEntity(EntityManager em) {
        BFPA bFPA = new BFPA().nomPrenom(DEFAULT_NOM_PRENOM);
        return bFPA;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BFPA createUpdatedEntity(EntityManager em) {
        BFPA bFPA = new BFPA().nomPrenom(UPDATED_NOM_PRENOM);
        return bFPA;
    }

    @BeforeEach
    public void initTest() {
        bFPA = createEntity(em);
    }

    @Test
    @Transactional
    void createBFPA() throws Exception {
        int databaseSizeBeforeCreate = bFPARepository.findAll().size();
        // Create the BFPA
        restBFPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bFPA)))
            .andExpect(status().isCreated());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeCreate + 1);
        BFPA testBFPA = bFPAList.get(bFPAList.size() - 1);
        assertThat(testBFPA.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
    }

    @Test
    @Transactional
    void createBFPAWithExistingId() throws Exception {
        // Create the BFPA with an existing ID
        bFPA.setId(1L);

        int databaseSizeBeforeCreate = bFPARepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBFPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bFPA)))
            .andExpect(status().isBadRequest());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = bFPARepository.findAll().size();
        // set the field null
        bFPA.setNomPrenom(null);

        // Create the BFPA, which fails.

        restBFPAMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bFPA)))
            .andExpect(status().isBadRequest());

        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBFPAS() throws Exception {
        // Initialize the database
        bFPARepository.saveAndFlush(bFPA);

        // Get all the bFPAList
        restBFPAMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bFPA.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)));
    }

    @Test
    @Transactional
    void getBFPA() throws Exception {
        // Initialize the database
        bFPARepository.saveAndFlush(bFPA);

        // Get the bFPA
        restBFPAMockMvc
            .perform(get(ENTITY_API_URL_ID, bFPA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bFPA.getId().intValue()))
            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM));
    }

    @Test
    @Transactional
    void getNonExistingBFPA() throws Exception {
        // Get the bFPA
        restBFPAMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBFPA() throws Exception {
        // Initialize the database
        bFPARepository.saveAndFlush(bFPA);

        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();

        // Update the bFPA
        BFPA updatedBFPA = bFPARepository.findById(bFPA.getId()).get();
        // Disconnect from session so that the updates on updatedBFPA are not directly saved in db
        em.detach(updatedBFPA);
        updatedBFPA.nomPrenom(UPDATED_NOM_PRENOM);

        restBFPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBFPA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBFPA))
            )
            .andExpect(status().isOk());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
        BFPA testBFPA = bFPAList.get(bFPAList.size() - 1);
        assertThat(testBFPA.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
    }

    @Test
    @Transactional
    void putNonExistingBFPA() throws Exception {
        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();
        bFPA.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBFPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bFPA.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bFPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBFPA() throws Exception {
        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();
        bFPA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBFPAMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bFPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBFPA() throws Exception {
        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();
        bFPA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBFPAMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bFPA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBFPAWithPatch() throws Exception {
        // Initialize the database
        bFPARepository.saveAndFlush(bFPA);

        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();

        // Update the bFPA using partial update
        BFPA partialUpdatedBFPA = new BFPA();
        partialUpdatedBFPA.setId(bFPA.getId());

        restBFPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBFPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBFPA))
            )
            .andExpect(status().isOk());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
        BFPA testBFPA = bFPAList.get(bFPAList.size() - 1);
        assertThat(testBFPA.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
    }

    @Test
    @Transactional
    void fullUpdateBFPAWithPatch() throws Exception {
        // Initialize the database
        bFPARepository.saveAndFlush(bFPA);

        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();

        // Update the bFPA using partial update
        BFPA partialUpdatedBFPA = new BFPA();
        partialUpdatedBFPA.setId(bFPA.getId());

        partialUpdatedBFPA.nomPrenom(UPDATED_NOM_PRENOM);

        restBFPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBFPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBFPA))
            )
            .andExpect(status().isOk());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
        BFPA testBFPA = bFPAList.get(bFPAList.size() - 1);
        assertThat(testBFPA.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
    }

    @Test
    @Transactional
    void patchNonExistingBFPA() throws Exception {
        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();
        bFPA.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBFPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bFPA.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bFPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBFPA() throws Exception {
        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();
        bFPA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBFPAMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bFPA))
            )
            .andExpect(status().isBadRequest());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBFPA() throws Exception {
        int databaseSizeBeforeUpdate = bFPARepository.findAll().size();
        bFPA.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBFPAMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bFPA)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BFPA in the database
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBFPA() throws Exception {
        // Initialize the database
        bFPARepository.saveAndFlush(bFPA);

        int databaseSizeBeforeDelete = bFPARepository.findAll().size();

        // Delete the bFPA
        restBFPAMockMvc
            .perform(delete(ENTITY_API_URL_ID, bFPA.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BFPA> bFPAList = bFPARepository.findAll();
        assertThat(bFPAList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
