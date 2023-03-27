package mfpai.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import mfpai.gouv.sn.IntegrationTest;
import mfpai.gouv.sn.domain.Etablissement;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.NomDep;
import mfpai.gouv.sn.domain.enumeration.NomEtab;
import mfpai.gouv.sn.domain.enumeration.NomReg;
import mfpai.gouv.sn.domain.enumeration.StatutEtab;
import mfpai.gouv.sn.domain.enumeration.TypeEtab;
import mfpai.gouv.sn.domain.enumeration.TypeInspection;
import mfpai.gouv.sn.repository.EtablissementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EtablissementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EtablissementResourceIT {

    private static final String DEFAULT_MATRICULE_ETAB = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ETAB = "BBBBBBBBBB";

    private static final NomReg DEFAULT_REGION = NomReg.DAKAR;
    private static final NomReg UPDATED_REGION = NomReg.DIOURBEL;

    private static final NomDep DEFAULT_DEPARTEMENT = NomDep.Dakar;
    private static final NomDep UPDATED_DEPARTEMENT = NomDep.Pikine;

    private static final String DEFAULT_AUTRE_REGION = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_DEP = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_DEP = "BBBBBBBBBB";

    private static final NomEtab DEFAULT_NOM_ETAB = NomEtab.CEDT_G15;
    private static final NomEtab UPDATED_NOM_ETAB = NomEtab.CFP_OUAKAM;

    private static final CodeIA DEFAULT_CODE_IA = CodeIA.C01;
    private static final CodeIA UPDATED_CODE_IA = CodeIA.C02;

    private static final String DEFAULT_AUTRE_CODE_IA = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_CODE_IA = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRE_NOME_ETAB = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_NOME_ETAB = "BBBBBBBBBB";

    private static final TypeEtab DEFAULT_TYPE_ETAB = TypeEtab.LyceeTechnique;
    private static final TypeEtab UPDATED_TYPE_ETAB = TypeEtab.CFP;

    private static final LocalDate DEFAULT_ANNEE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final StatutEtab DEFAULT_STATUT = StatutEtab.Prive;
    private static final StatutEtab UPDATED_STATUT = StatutEtab.Public;

    private static final String DEFAULT_EMAIL_ETAB = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ETAB = "BBBBBBBBBB";

    private static final TypeInspection DEFAULT_TYPE_INSP = TypeInspection.IA;
    private static final TypeInspection UPDATED_TYPE_INSP = TypeInspection.IEF;

    private static final String ENTITY_API_URL = "/api/etablissements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtablissementRepository etablissementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtablissementMockMvc;

    private Etablissement etablissement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .matriculeEtab(DEFAULT_MATRICULE_ETAB)
            .region(DEFAULT_REGION)
            .departement(DEFAULT_DEPARTEMENT)
            .autreRegion(DEFAULT_AUTRE_REGION)
            .autreDep(DEFAULT_AUTRE_DEP)
            .nomEtab(DEFAULT_NOM_ETAB)
            .codeIA(DEFAULT_CODE_IA)
            .autreCodeIA(DEFAULT_AUTRE_CODE_IA)
            .autreNomeEtab(DEFAULT_AUTRE_NOME_ETAB)
            .typeEtab(DEFAULT_TYPE_ETAB)
            .anneeCreation(DEFAULT_ANNEE_CREATION)
            .statut(DEFAULT_STATUT)
            .emailEtab(DEFAULT_EMAIL_ETAB)
            .typeInsp(DEFAULT_TYPE_INSP);
        return etablissement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etablissement createUpdatedEntity(EntityManager em) {
        Etablissement etablissement = new Etablissement()
            .matriculeEtab(UPDATED_MATRICULE_ETAB)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDep(UPDATED_AUTRE_DEP)
            .nomEtab(UPDATED_NOM_ETAB)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .autreNomeEtab(UPDATED_AUTRE_NOME_ETAB)
            .typeEtab(UPDATED_TYPE_ETAB)
            .anneeCreation(UPDATED_ANNEE_CREATION)
            .statut(UPDATED_STATUT)
            .emailEtab(UPDATED_EMAIL_ETAB)
            .typeInsp(UPDATED_TYPE_INSP);
        return etablissement;
    }

    @BeforeEach
    public void initTest() {
        etablissement = createEntity(em);
    }

    @Test
    @Transactional
    void createEtablissement() throws Exception {
        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();
        // Create the Etablissement
        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isCreated());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate + 1);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getMatriculeEtab()).isEqualTo(DEFAULT_MATRICULE_ETAB);
        assertThat(testEtablissement.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(DEFAULT_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDep()).isEqualTo(DEFAULT_AUTRE_DEP);
        assertThat(testEtablissement.getNomEtab()).isEqualTo(DEFAULT_NOM_ETAB);
        assertThat(testEtablissement.getCodeIA()).isEqualTo(DEFAULT_CODE_IA);
        assertThat(testEtablissement.getAutreCodeIA()).isEqualTo(DEFAULT_AUTRE_CODE_IA);
        assertThat(testEtablissement.getAutreNomeEtab()).isEqualTo(DEFAULT_AUTRE_NOME_ETAB);
        assertThat(testEtablissement.getTypeEtab()).isEqualTo(DEFAULT_TYPE_ETAB);
        assertThat(testEtablissement.getAnneeCreation()).isEqualTo(DEFAULT_ANNEE_CREATION);
        assertThat(testEtablissement.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testEtablissement.getEmailEtab()).isEqualTo(DEFAULT_EMAIL_ETAB);
        assertThat(testEtablissement.getTypeInsp()).isEqualTo(DEFAULT_TYPE_INSP);
    }

    @Test
    @Transactional
    void createEtablissementWithExistingId() throws Exception {
        // Create the Etablissement with an existing ID
        etablissement.setId(1L);

        int databaseSizeBeforeCreate = etablissementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setRegion(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setDepartement(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIAIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setCodeIA(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeEtabIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setTypeEtab(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setAnneeCreation(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setStatut(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailEtabIsRequired() throws Exception {
        int databaseSizeBeforeTest = etablissementRepository.findAll().size();
        // set the field null
        etablissement.setEmailEtab(null);

        // Create the Etablissement, which fails.

        restEtablissementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isBadRequest());

        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEtablissements() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get all the etablissementList
        restEtablissementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etablissement.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeEtab").value(hasItem(DEFAULT_MATRICULE_ETAB)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].autreRegion").value(hasItem(DEFAULT_AUTRE_REGION)))
            .andExpect(jsonPath("$.[*].autreDep").value(hasItem(DEFAULT_AUTRE_DEP)))
            .andExpect(jsonPath("$.[*].nomEtab").value(hasItem(DEFAULT_NOM_ETAB.toString())))
            .andExpect(jsonPath("$.[*].codeIA").value(hasItem(DEFAULT_CODE_IA.toString())))
            .andExpect(jsonPath("$.[*].autreCodeIA").value(hasItem(DEFAULT_AUTRE_CODE_IA)))
            .andExpect(jsonPath("$.[*].autreNomeEtab").value(hasItem(DEFAULT_AUTRE_NOME_ETAB)))
            .andExpect(jsonPath("$.[*].typeEtab").value(hasItem(DEFAULT_TYPE_ETAB.toString())))
            .andExpect(jsonPath("$.[*].anneeCreation").value(hasItem(DEFAULT_ANNEE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].emailEtab").value(hasItem(DEFAULT_EMAIL_ETAB)))
            .andExpect(jsonPath("$.[*].typeInsp").value(hasItem(DEFAULT_TYPE_INSP.toString())));
    }

    @Test
    @Transactional
    void getEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        // Get the etablissement
        restEtablissementMockMvc
            .perform(get(ENTITY_API_URL_ID, etablissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etablissement.getId().intValue()))
            .andExpect(jsonPath("$.matriculeEtab").value(DEFAULT_MATRICULE_ETAB))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.autreRegion").value(DEFAULT_AUTRE_REGION))
            .andExpect(jsonPath("$.autreDep").value(DEFAULT_AUTRE_DEP))
            .andExpect(jsonPath("$.nomEtab").value(DEFAULT_NOM_ETAB.toString()))
            .andExpect(jsonPath("$.codeIA").value(DEFAULT_CODE_IA.toString()))
            .andExpect(jsonPath("$.autreCodeIA").value(DEFAULT_AUTRE_CODE_IA))
            .andExpect(jsonPath("$.autreNomeEtab").value(DEFAULT_AUTRE_NOME_ETAB))
            .andExpect(jsonPath("$.typeEtab").value(DEFAULT_TYPE_ETAB.toString()))
            .andExpect(jsonPath("$.anneeCreation").value(DEFAULT_ANNEE_CREATION.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.emailEtab").value(DEFAULT_EMAIL_ETAB))
            .andExpect(jsonPath("$.typeInsp").value(DEFAULT_TYPE_INSP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEtablissement() throws Exception {
        // Get the etablissement
        restEtablissementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement
        Etablissement updatedEtablissement = etablissementRepository.findById(etablissement.getId()).get();
        // Disconnect from session so that the updates on updatedEtablissement are not directly saved in db
        em.detach(updatedEtablissement);
        updatedEtablissement
            .matriculeEtab(UPDATED_MATRICULE_ETAB)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDep(UPDATED_AUTRE_DEP)
            .nomEtab(UPDATED_NOM_ETAB)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .autreNomeEtab(UPDATED_AUTRE_NOME_ETAB)
            .typeEtab(UPDATED_TYPE_ETAB)
            .anneeCreation(UPDATED_ANNEE_CREATION)
            .statut(UPDATED_STATUT)
            .emailEtab(UPDATED_EMAIL_ETAB)
            .typeInsp(UPDATED_TYPE_INSP);

        restEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtablissement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getMatriculeEtab()).isEqualTo(UPDATED_MATRICULE_ETAB);
        assertThat(testEtablissement.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testEtablissement.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testEtablissement.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testEtablissement.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testEtablissement.getAutreNomeEtab()).isEqualTo(UPDATED_AUTRE_NOME_ETAB);
        assertThat(testEtablissement.getTypeEtab()).isEqualTo(UPDATED_TYPE_ETAB);
        assertThat(testEtablissement.getAnneeCreation()).isEqualTo(UPDATED_ANNEE_CREATION);
        assertThat(testEtablissement.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testEtablissement.getEmailEtab()).isEqualTo(UPDATED_EMAIL_ETAB);
        assertThat(testEtablissement.getTypeInsp()).isEqualTo(UPDATED_TYPE_INSP);
    }

    @Test
    @Transactional
    void putNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etablissement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etablissement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtablissementWithPatch() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement using partial update
        Etablissement partialUpdatedEtablissement = new Etablissement();
        partialUpdatedEtablissement.setId(etablissement.getId());

        partialUpdatedEtablissement
            .region(UPDATED_REGION)
            .autreDep(UPDATED_AUTRE_DEP)
            .nomEtab(UPDATED_NOM_ETAB)
            .codeIA(UPDATED_CODE_IA)
            .typeEtab(UPDATED_TYPE_ETAB)
            .typeInsp(UPDATED_TYPE_INSP);

        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getMatriculeEtab()).isEqualTo(DEFAULT_MATRICULE_ETAB);
        assertThat(testEtablissement.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(DEFAULT_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testEtablissement.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testEtablissement.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testEtablissement.getAutreCodeIA()).isEqualTo(DEFAULT_AUTRE_CODE_IA);
        assertThat(testEtablissement.getAutreNomeEtab()).isEqualTo(DEFAULT_AUTRE_NOME_ETAB);
        assertThat(testEtablissement.getTypeEtab()).isEqualTo(UPDATED_TYPE_ETAB);
        assertThat(testEtablissement.getAnneeCreation()).isEqualTo(DEFAULT_ANNEE_CREATION);
        assertThat(testEtablissement.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testEtablissement.getEmailEtab()).isEqualTo(DEFAULT_EMAIL_ETAB);
        assertThat(testEtablissement.getTypeInsp()).isEqualTo(UPDATED_TYPE_INSP);
    }

    @Test
    @Transactional
    void fullUpdateEtablissementWithPatch() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();

        // Update the etablissement using partial update
        Etablissement partialUpdatedEtablissement = new Etablissement();
        partialUpdatedEtablissement.setId(etablissement.getId());

        partialUpdatedEtablissement
            .matriculeEtab(UPDATED_MATRICULE_ETAB)
            .region(UPDATED_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreRegion(UPDATED_AUTRE_REGION)
            .autreDep(UPDATED_AUTRE_DEP)
            .nomEtab(UPDATED_NOM_ETAB)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .autreNomeEtab(UPDATED_AUTRE_NOME_ETAB)
            .typeEtab(UPDATED_TYPE_ETAB)
            .anneeCreation(UPDATED_ANNEE_CREATION)
            .statut(UPDATED_STATUT)
            .emailEtab(UPDATED_EMAIL_ETAB)
            .typeInsp(UPDATED_TYPE_INSP);

        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtablissement))
            )
            .andExpect(status().isOk());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
        Etablissement testEtablissement = etablissementList.get(etablissementList.size() - 1);
        assertThat(testEtablissement.getMatriculeEtab()).isEqualTo(UPDATED_MATRICULE_ETAB);
        assertThat(testEtablissement.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEtablissement.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testEtablissement.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testEtablissement.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testEtablissement.getNomEtab()).isEqualTo(UPDATED_NOM_ETAB);
        assertThat(testEtablissement.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testEtablissement.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testEtablissement.getAutreNomeEtab()).isEqualTo(UPDATED_AUTRE_NOME_ETAB);
        assertThat(testEtablissement.getTypeEtab()).isEqualTo(UPDATED_TYPE_ETAB);
        assertThat(testEtablissement.getAnneeCreation()).isEqualTo(UPDATED_ANNEE_CREATION);
        assertThat(testEtablissement.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testEtablissement.getEmailEtab()).isEqualTo(UPDATED_EMAIL_ETAB);
        assertThat(testEtablissement.getTypeInsp()).isEqualTo(UPDATED_TYPE_INSP);
    }

    @Test
    @Transactional
    void patchNonExistingEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etablissement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtablissement() throws Exception {
        int databaseSizeBeforeUpdate = etablissementRepository.findAll().size();
        etablissement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtablissementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etablissement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etablissement in the database
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtablissement() throws Exception {
        // Initialize the database
        etablissementRepository.saveAndFlush(etablissement);

        int databaseSizeBeforeDelete = etablissementRepository.findAll().size();

        // Delete the etablissement
        restEtablissementMockMvc
            .perform(delete(ENTITY_API_URL_ID, etablissement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etablissement> etablissementList = etablissementRepository.findAll();
        assertThat(etablissementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
