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
import mfpai.gouv.sn.domain.Referant;
import mfpai.gouv.sn.repository.ReferantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReferantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferantResourceIT {

    private static final String DEFAULT_NOM_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRENOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/referants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReferantRepository referantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferantMockMvc;

    private Referant referant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referant createEntity(EntityManager em) {
        Referant referant = new Referant().nomPrenom(DEFAULT_NOM_PRENOM);
        return referant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Referant createUpdatedEntity(EntityManager em) {
        Referant referant = new Referant().nomPrenom(UPDATED_NOM_PRENOM);
        return referant;
    }

    @BeforeEach
    public void initTest() {
        referant = createEntity(em);
    }

    @Test
    @Transactional
    void createReferant() throws Exception {
        int databaseSizeBeforeCreate = referantRepository.findAll().size();
        // Create the Referant
        restReferantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referant)))
            .andExpect(status().isCreated());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeCreate + 1);
        Referant testReferant = referantList.get(referantList.size() - 1);
        assertThat(testReferant.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
    }

    @Test
    @Transactional
    void createReferantWithExistingId() throws Exception {
        // Create the Referant with an existing ID
        referant.setId(1L);

        int databaseSizeBeforeCreate = referantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referant)))
            .andExpect(status().isBadRequest());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = referantRepository.findAll().size();
        // set the field null
        referant.setNomPrenom(null);

        // Create the Referant, which fails.

        restReferantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referant)))
            .andExpect(status().isBadRequest());

        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReferants() throws Exception {
        // Initialize the database
        referantRepository.saveAndFlush(referant);

        // Get all the referantList
        restReferantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(referant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPrenom").value(hasItem(DEFAULT_NOM_PRENOM)));
    }

    @Test
    @Transactional
    void getReferant() throws Exception {
        // Initialize the database
        referantRepository.saveAndFlush(referant);

        // Get the referant
        restReferantMockMvc
            .perform(get(ENTITY_API_URL_ID, referant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(referant.getId().intValue()))
            .andExpect(jsonPath("$.nomPrenom").value(DEFAULT_NOM_PRENOM));
    }

    @Test
    @Transactional
    void getNonExistingReferant() throws Exception {
        // Get the referant
        restReferantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReferant() throws Exception {
        // Initialize the database
        referantRepository.saveAndFlush(referant);

        int databaseSizeBeforeUpdate = referantRepository.findAll().size();

        // Update the referant
        Referant updatedReferant = referantRepository.findById(referant.getId()).get();
        // Disconnect from session so that the updates on updatedReferant are not directly saved in db
        em.detach(updatedReferant);
        updatedReferant.nomPrenom(UPDATED_NOM_PRENOM);

        restReferantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReferant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReferant))
            )
            .andExpect(status().isOk());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
        Referant testReferant = referantList.get(referantList.size() - 1);
        assertThat(testReferant.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
    }

    @Test
    @Transactional
    void putNonExistingReferant() throws Exception {
        int databaseSizeBeforeUpdate = referantRepository.findAll().size();
        referant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReferant() throws Exception {
        int databaseSizeBeforeUpdate = referantRepository.findAll().size();
        referant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(referant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReferant() throws Exception {
        int databaseSizeBeforeUpdate = referantRepository.findAll().size();
        referant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(referant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferantWithPatch() throws Exception {
        // Initialize the database
        referantRepository.saveAndFlush(referant);

        int databaseSizeBeforeUpdate = referantRepository.findAll().size();

        // Update the referant using partial update
        Referant partialUpdatedReferant = new Referant();
        partialUpdatedReferant.setId(referant.getId());

        restReferantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferant))
            )
            .andExpect(status().isOk());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
        Referant testReferant = referantList.get(referantList.size() - 1);
        assertThat(testReferant.getNomPrenom()).isEqualTo(DEFAULT_NOM_PRENOM);
    }

    @Test
    @Transactional
    void fullUpdateReferantWithPatch() throws Exception {
        // Initialize the database
        referantRepository.saveAndFlush(referant);

        int databaseSizeBeforeUpdate = referantRepository.findAll().size();

        // Update the referant using partial update
        Referant partialUpdatedReferant = new Referant();
        partialUpdatedReferant.setId(referant.getId());

        partialUpdatedReferant.nomPrenom(UPDATED_NOM_PRENOM);

        restReferantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReferant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReferant))
            )
            .andExpect(status().isOk());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
        Referant testReferant = referantList.get(referantList.size() - 1);
        assertThat(testReferant.getNomPrenom()).isEqualTo(UPDATED_NOM_PRENOM);
    }

    @Test
    @Transactional
    void patchNonExistingReferant() throws Exception {
        int databaseSizeBeforeUpdate = referantRepository.findAll().size();
        referant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReferant() throws Exception {
        int databaseSizeBeforeUpdate = referantRepository.findAll().size();
        referant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(referant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReferant() throws Exception {
        int databaseSizeBeforeUpdate = referantRepository.findAll().size();
        referant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(referant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Referant in the database
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReferant() throws Exception {
        // Initialize the database
        referantRepository.saveAndFlush(referant);

        int databaseSizeBeforeDelete = referantRepository.findAll().size();

        // Delete the referant
        restReferantMockMvc
            .perform(delete(ENTITY_API_URL_ID, referant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Referant> referantList = referantRepository.findAll();
        assertThat(referantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
