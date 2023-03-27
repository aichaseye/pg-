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
import mfpai.gouv.sn.domain.Enseignant;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.NomDep;
import mfpai.gouv.sn.domain.enumeration.NomReg;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import mfpai.gouv.sn.repository.EnseignantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EnseignantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnseignantResourceIT {

    private static final String DEFAULT_MATRICULE_ENS = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ENS = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Long DEFAULT_NUM_CNI = 1L;
    private static final Long UPDATED_NUM_CNI = 2L;

    private static final Integer DEFAULT_ANNEE_DENTREE = 1;
    private static final Integer UPDATED_ANNEE_DENTREE = 2;

    private static final NomReg DEFAULT_REGION = NomReg.DAKAR;
    private static final NomReg UPDATED_REGION = NomReg.DIOURBEL;

    private static final NomDep DEFAULT_DEPARTEMENT = NomDep.Dakar;
    private static final NomDep UPDATED_DEPARTEMENT = NomDep.Pikine;

    private static final String DEFAULT_AUTRE_DEP = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_DEP = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_REGION = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_REGION = "BBBBBBBBBB";

    private static final CodeIA DEFAULT_CODE_IA = CodeIA.C01;
    private static final CodeIA UPDATED_CODE_IA = CodeIA.C02;

    private static final String DEFAULT_AUTRE_CODE_IA = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_CODE_IA = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.M;
    private static final Sexe UPDATED_SEXE = Sexe.F;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enseignants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnseignantMockMvc;

    private Enseignant enseignant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .matriculeEns(DEFAULT_MATRICULE_ENS)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .numCNI(DEFAULT_NUM_CNI)
            .anneeDentree(DEFAULT_ANNEE_DENTREE)
            .region(DEFAULT_REGION)
            .departement(DEFAULT_DEPARTEMENT)
            .autreDep(DEFAULT_AUTRE_DEP)
            .autreRegion(DEFAULT_AUTRE_REGION)
            .codeIA(DEFAULT_CODE_IA)
            .autreCodeIA(DEFAULT_AUTRE_CODE_IA)
            .sexe(DEFAULT_SEXE)
            .email(DEFAULT_EMAIL);
        return enseignant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enseignant createUpdatedEntity(EntityManager em) {
        Enseignant enseignant = new Enseignant()
            .matriculeEns(UPDATED_MATRICULE_ENS)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numCNI(UPDATED_NUM_CNI)
            .anneeDentree(UPDATED_ANNEE_DENTREE)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreDep(UPDATED_AUTRE_DEP)
            .autreRegion(UPDATED_AUTRE_REGION)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL);
        return enseignant;
    }

    @BeforeEach
    public void initTest() {
        enseignant = createEntity(em);
    }

    @Test
    @Transactional
    void createEnseignant() throws Exception {
        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();
        // Create the Enseignant
        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isCreated());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeCreate + 1);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getMatriculeEns()).isEqualTo(DEFAULT_MATRICULE_ENS);
        assertThat(testEnseignant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnseignant.getNumCNI()).isEqualTo(DEFAULT_NUM_CNI);
        assertThat(testEnseignant.getAnneeDentree()).isEqualTo(DEFAULT_ANNEE_DENTREE);
        assertThat(testEnseignant.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testEnseignant.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testEnseignant.getAutreDep()).isEqualTo(DEFAULT_AUTRE_DEP);
        assertThat(testEnseignant.getAutreRegion()).isEqualTo(DEFAULT_AUTRE_REGION);
        assertThat(testEnseignant.getCodeIA()).isEqualTo(DEFAULT_CODE_IA);
        assertThat(testEnseignant.getAutreCodeIA()).isEqualTo(DEFAULT_AUTRE_CODE_IA);
        assertThat(testEnseignant.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testEnseignant.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createEnseignantWithExistingId() throws Exception {
        // Create the Enseignant with an existing ID
        enseignant.setId(1L);

        int databaseSizeBeforeCreate = enseignantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setNom(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setPrenom(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumCNIIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setNumCNI(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeDentreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setAnneeDentree(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setRegion(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setDepartement(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIAIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setCodeIA(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = enseignantRepository.findAll().size();
        // set the field null
        enseignant.setEmail(null);

        // Create the Enseignant, which fails.

        restEnseignantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isBadRequest());

        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnseignants() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get all the enseignantList
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enseignant.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeEns").value(hasItem(DEFAULT_MATRICULE_ENS)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].numCNI").value(hasItem(DEFAULT_NUM_CNI.intValue())))
            .andExpect(jsonPath("$.[*].anneeDentree").value(hasItem(DEFAULT_ANNEE_DENTREE)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].autreDep").value(hasItem(DEFAULT_AUTRE_DEP)))
            .andExpect(jsonPath("$.[*].autreRegion").value(hasItem(DEFAULT_AUTRE_REGION)))
            .andExpect(jsonPath("$.[*].codeIA").value(hasItem(DEFAULT_CODE_IA.toString())))
            .andExpect(jsonPath("$.[*].autreCodeIA").value(hasItem(DEFAULT_AUTRE_CODE_IA)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        // Get the enseignant
        restEnseignantMockMvc
            .perform(get(ENTITY_API_URL_ID, enseignant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enseignant.getId().intValue()))
            .andExpect(jsonPath("$.matriculeEns").value(DEFAULT_MATRICULE_ENS))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.numCNI").value(DEFAULT_NUM_CNI.intValue()))
            .andExpect(jsonPath("$.anneeDentree").value(DEFAULT_ANNEE_DENTREE))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.autreDep").value(DEFAULT_AUTRE_DEP))
            .andExpect(jsonPath("$.autreRegion").value(DEFAULT_AUTRE_REGION))
            .andExpect(jsonPath("$.codeIA").value(DEFAULT_CODE_IA.toString()))
            .andExpect(jsonPath("$.autreCodeIA").value(DEFAULT_AUTRE_CODE_IA))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingEnseignant() throws Exception {
        // Get the enseignant
        restEnseignantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant
        Enseignant updatedEnseignant = enseignantRepository.findById(enseignant.getId()).get();
        // Disconnect from session so that the updates on updatedEnseignant are not directly saved in db
        em.detach(updatedEnseignant);
        updatedEnseignant
            .matriculeEns(UPDATED_MATRICULE_ENS)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numCNI(UPDATED_NUM_CNI)
            .anneeDentree(UPDATED_ANNEE_DENTREE)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreDep(UPDATED_AUTRE_DEP)
            .autreRegion(UPDATED_AUTRE_REGION)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL);

        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnseignant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getMatriculeEns()).isEqualTo(UPDATED_MATRICULE_ENS);
        assertThat(testEnseignant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getNumCNI()).isEqualTo(UPDATED_NUM_CNI);
        assertThat(testEnseignant.getAnneeDentree()).isEqualTo(UPDATED_ANNEE_DENTREE);
        assertThat(testEnseignant.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEnseignant.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEnseignant.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testEnseignant.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEnseignant.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testEnseignant.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testEnseignant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnseignant.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enseignant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enseignant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enseignant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .matriculeEns(UPDATED_MATRICULE_ENS)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numCNI(UPDATED_NUM_CNI)
            .departement(UPDATED_DEPARTEMENT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getMatriculeEns()).isEqualTo(UPDATED_MATRICULE_ENS);
        assertThat(testEnseignant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getNumCNI()).isEqualTo(UPDATED_NUM_CNI);
        assertThat(testEnseignant.getAnneeDentree()).isEqualTo(DEFAULT_ANNEE_DENTREE);
        assertThat(testEnseignant.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testEnseignant.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEnseignant.getAutreDep()).isEqualTo(DEFAULT_AUTRE_DEP);
        assertThat(testEnseignant.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEnseignant.getCodeIA()).isEqualTo(DEFAULT_CODE_IA);
        assertThat(testEnseignant.getAutreCodeIA()).isEqualTo(DEFAULT_AUTRE_CODE_IA);
        assertThat(testEnseignant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnseignant.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateEnseignantWithPatch() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();

        // Update the enseignant using partial update
        Enseignant partialUpdatedEnseignant = new Enseignant();
        partialUpdatedEnseignant.setId(enseignant.getId());

        partialUpdatedEnseignant
            .matriculeEns(UPDATED_MATRICULE_ENS)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .numCNI(UPDATED_NUM_CNI)
            .anneeDentree(UPDATED_ANNEE_DENTREE)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreDep(UPDATED_AUTRE_DEP)
            .autreRegion(UPDATED_AUTRE_REGION)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .sexe(UPDATED_SEXE)
            .email(UPDATED_EMAIL);

        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnseignant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnseignant))
            )
            .andExpect(status().isOk());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
        Enseignant testEnseignant = enseignantList.get(enseignantList.size() - 1);
        assertThat(testEnseignant.getMatriculeEns()).isEqualTo(UPDATED_MATRICULE_ENS);
        assertThat(testEnseignant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnseignant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnseignant.getNumCNI()).isEqualTo(UPDATED_NUM_CNI);
        assertThat(testEnseignant.getAnneeDentree()).isEqualTo(UPDATED_ANNEE_DENTREE);
        assertThat(testEnseignant.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEnseignant.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEnseignant.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testEnseignant.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEnseignant.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testEnseignant.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testEnseignant.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testEnseignant.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enseignant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enseignant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnseignant() throws Exception {
        int databaseSizeBeforeUpdate = enseignantRepository.findAll().size();
        enseignant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnseignantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enseignant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enseignant in the database
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnseignant() throws Exception {
        // Initialize the database
        enseignantRepository.saveAndFlush(enseignant);

        int databaseSizeBeforeDelete = enseignantRepository.findAll().size();

        // Delete the enseignant
        restEnseignantMockMvc
            .perform(delete(ENTITY_API_URL_ID, enseignant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enseignant> enseignantList = enseignantRepository.findAll();
        assertThat(enseignantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
