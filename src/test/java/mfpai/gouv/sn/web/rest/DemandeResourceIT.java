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
import mfpai.gouv.sn.domain.Demande;
import mfpai.gouv.sn.domain.enumeration.Motif;
import mfpai.gouv.sn.domain.enumeration.NomEtab;
import mfpai.gouv.sn.domain.enumeration.TypeDemandeur;
import mfpai.gouv.sn.repository.DemandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DemandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemandeResourceIT {

    private static final Motif DEFAULT_MOTIF = Motif.CreationMatricule;
    private static final Motif UPDATED_MOTIF = Motif.MatriculeOublier;

    private static final TypeDemandeur DEFAULT_TYPE_DEMANDEUR = TypeDemandeur.Etablissement;
    private static final TypeDemandeur UPDATED_TYPE_DEMANDEUR = TypeDemandeur.Enseignant;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final NomEtab DEFAULT_NOM_ETAB = NomEtab.CEDT_G15;
    private static final NomEtab UPDATED_NOM_ETAB = NomEtab.CFP_OUAKAM;

    private static final String DEFAULT_AUTRE_NOM_ETAB = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_NOM_ETAB = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/demandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeMockMvc;

    private Demande demande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createEntity(EntityManager em) {
        Demande demande = new Demande()
            .motif(DEFAULT_MOTIF)
            .typeDemandeur(DEFAULT_TYPE_DEMANDEUR)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .nomEtab(DEFAULT_NOM_ETAB)
            .autreNomEtab(DEFAULT_AUTRE_NOM_ETAB)
            .email(DEFAULT_EMAIL);
        return demande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Demande createUpdatedEntity(EntityManager em) {
        Demande demande = new Demande()
            .motif(UPDATED_MOTIF)
            .typeDemandeur(UPDATED_TYPE_DEMANDEUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .nomEtab(UPDATED_NOM_ETAB)
            .autreNomEtab(UPDATED_AUTRE_NOM_ETAB)
            .email(UPDATED_EMAIL);
        return demande;
    }

    @BeforeEach
    public void initTest() {
        demande = createEntity(em);
    }

    @Test
    @Transactional
    void createDemande() throws Exception {
        int databaseSizeBeforeCreate = demandeRepository.findAll().size();
        // Create the Demande
        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isCreated());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeCreate + 1);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getMotif()).isEqualTo(DEFAULT_MOTIF);
        assertThat(testDemande.getTypeDemandeur()).isEqualTo(DEFAULT_TYPE_DEMANDEUR);
        assertThat(testDemande.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDemande.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDemande.getNomEtab()).isEqualTo(DEFAULT_NOM_ETAB);
        assertThat(testDemande.getAutreNomEtab()).isEqualTo(DEFAULT_AUTRE_NOM_ETAB);
        assertThat(testDemande.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createDemandeWithExistingId() throws Exception {
        // Create the Demande with an existing ID
        demande.setId(1L);

        int databaseSizeBeforeCreate = demandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMotifIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeRepository.findAll().size();
        // set the field null
        demande.setMotif(null);

        // Create the Demande, which fails.

        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeDemandeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeRepository.findAll().size();
        // set the field null
        demande.setTypeDemandeur(null);

        // Create the Demande, which fails.

        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeRepository.findAll().size();
        // set the field null
        demande.setNom(null);

        // Create the Demande, which fails.

        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeRepository.findAll().size();
        // set the field null
        demande.setPrenom(null);

        // Create the Demande, which fails.

        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeRepository.findAll().size();
        // set the field null
        demande.setEmail(null);

        // Create the Demande, which fails.

        restDemandeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isBadRequest());

        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandes() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get all the demandeList
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF.toString())))
            .andExpect(jsonPath("$.[*].typeDemandeur").value(hasItem(DEFAULT_TYPE_DEMANDEUR.toString())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nomEtab").value(hasItem(DEFAULT_NOM_ETAB.toString())))
            .andExpect(jsonPath("$.[*].autreNomEtab").value(hasItem(DEFAULT_AUTRE_NOM_ETAB)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get the demande
        restDemandeMockMvc
            .perform(get(ENTITY_API_URL_ID, demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demande.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF.toString()))
            .andExpect(jsonPath("$.typeDemandeur").value(DEFAULT_TYPE_DEMANDEUR.toString()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nomEtab").value(DEFAULT_NOM_ETAB.toString()))
            .andExpect(jsonPath("$.autreNomEtab").value(DEFAULT_AUTRE_NOM_ETAB))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingDemande() throws Exception {
        // Get the demande
        restDemandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande
        Demande updatedDemande = demandeRepository.findById(demande.getId()).get();
        // Disconnect from session so that the updates on updatedDemande are not directly saved in db
        em.detach(updatedDemande);
        updatedDemande
            .motif(UPDATED_MOTIF)
            .typeDemandeur(UPDATED_TYPE_DEMANDEUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .nomEtab(UPDATED_NOM_ETAB)
            .autreNomEtab(UPDATED_AUTRE_NOM_ETAB)
            .email(UPDATED_EMAIL);

        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testDemande.getTypeDemandeur()).isEqualTo(UPDATED_TYPE_DEMANDEUR);
        assertThat(testDemande.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDemande.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDemande.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testDemande.getAutreNomEtab()).isEqualTo(UPDATED_AUTRE_NOM_ETAB);
        assertThat(testDemande.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande
            .motif(UPDATED_MOTIF)
            .typeDemandeur(UPDATED_TYPE_DEMANDEUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .nomEtab(UPDATED_NOM_ETAB)
            .autreNomEtab(UPDATED_AUTRE_NOM_ETAB);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testDemande.getTypeDemandeur()).isEqualTo(UPDATED_TYPE_DEMANDEUR);
        assertThat(testDemande.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDemande.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDemande.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testDemande.getAutreNomEtab()).isEqualTo(UPDATED_AUTRE_NOM_ETAB);
        assertThat(testDemande.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateDemandeWithPatch() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande using partial update
        Demande partialUpdatedDemande = new Demande();
        partialUpdatedDemande.setId(demande.getId());

        partialUpdatedDemande
            .motif(UPDATED_MOTIF)
            .typeDemandeur(UPDATED_TYPE_DEMANDEUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .nomEtab(UPDATED_NOM_ETAB)
            .autreNomEtab(UPDATED_AUTRE_NOM_ETAB)
            .email(UPDATED_EMAIL);

        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemande))
            )
            .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandeList.get(demandeList.size() - 1);
        assertThat(testDemande.getMotif()).isEqualTo(UPDATED_MOTIF);
        assertThat(testDemande.getTypeDemandeur()).isEqualTo(UPDATED_TYPE_DEMANDEUR);
        assertThat(testDemande.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDemande.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDemande.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testDemande.getAutreNomEtab()).isEqualTo(UPDATED_AUTRE_NOM_ETAB);
        assertThat(testDemande.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demande))
            )
            .andExpect(status().isBadRequest());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemande() throws Exception {
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();
        demande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(demande)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Demande in the database
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        int databaseSizeBeforeDelete = demandeRepository.findAll().size();

        // Delete the demande
        restDemandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, demande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Demande> demandeList = demandeRepository.findAll();
        assertThat(demandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
