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
import mfpai.gouv.sn.domain.Matiere;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.NomDep;
import mfpai.gouv.sn.domain.enumeration.NomReg;
import mfpai.gouv.sn.domain.enumeration.TypeStructure;
import mfpai.gouv.sn.repository.MatiereRepository;
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
 * Integration tests for the {@link MatiereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatiereResourceIT {

    private static final String DEFAULT_MATRICULE_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_MATIERE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_MATIERE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_MATIERE = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final NomReg DEFAULT_REGION = NomReg.DAKAR;
    private static final NomReg UPDATED_REGION = NomReg.DIOURBEL;

    private static final String DEFAULT_AUTRE_REGION = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_REGION = "BBBBBBBBBB";

    private static final NomDep DEFAULT_DEPARTEMENT = NomDep.Dakar;
    private static final NomDep UPDATED_DEPARTEMENT = NomDep.Pikine;

    private static final String DEFAULT_AUTRE_DEP = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_DEP = "BBBBBBBBBB";

    private static final CodeIA DEFAULT_CODE_IA = CodeIA.C01;
    private static final CodeIA UPDATED_CODE_IA = CodeIA.C02;

    private static final String DEFAULT_AUTRE_CODE_IA = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_CODE_IA = "BBBBBBBBBB";

    private static final TypeStructure DEFAULT_TYPE_STRUCTURE = TypeStructure.LT;
    private static final TypeStructure UPDATED_TYPE_STRUCTURE = TypeStructure.CS;

    private static final String DEFAULT_AUTRE_STRUCTURE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_STRUCTURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNEE_AFFECTATION = 1;
    private static final Integer UPDATED_ANNEE_AFFECTATION = 2;

    private static final String ENTITY_API_URL = "/api/matieres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatiereMockMvc;

    private Matiere matiere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createEntity(EntityManager em) {
        Matiere matiere = new Matiere()
            .matriculeMatiere(DEFAULT_MATRICULE_MATIERE)
            .nomMatiere(DEFAULT_NOM_MATIERE)
            .reference(DEFAULT_REFERENCE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .region(DEFAULT_REGION)
            .autreRegion(DEFAULT_AUTRE_REGION)
            .departement(DEFAULT_DEPARTEMENT)
            .autreDep(DEFAULT_AUTRE_DEP)
            .codeIA(DEFAULT_CODE_IA)
            .autreCodeIA(DEFAULT_AUTRE_CODE_IA)
            .typeStructure(DEFAULT_TYPE_STRUCTURE)
            .autreStructure(DEFAULT_AUTRE_STRUCTURE)
            .anneeAffectation(DEFAULT_ANNEE_AFFECTATION);
        return matiere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Matiere createUpdatedEntity(EntityManager em) {
        Matiere matiere = new Matiere()
            .matriculeMatiere(UPDATED_MATRICULE_MATIERE)
            .nomMatiere(UPDATED_NOM_MATIERE)
            .reference(UPDATED_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .region(UPDATED_REGION)
            .autreRegion(UPDATED_AUTRE_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreDep(UPDATED_AUTRE_DEP)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .typeStructure(UPDATED_TYPE_STRUCTURE)
            .autreStructure(UPDATED_AUTRE_STRUCTURE)
            .anneeAffectation(UPDATED_ANNEE_AFFECTATION);
        return matiere;
    }

    @BeforeEach
    public void initTest() {
        matiere = createEntity(em);
    }

    @Test
    @Transactional
    void createMatiere() throws Exception {
        int databaseSizeBeforeCreate = matiereRepository.findAll().size();
        // Create the Matiere
        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isCreated());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeCreate + 1);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getMatriculeMatiere()).isEqualTo(DEFAULT_MATRICULE_MATIERE);
        assertThat(testMatiere.getNomMatiere()).isEqualTo(DEFAULT_NOM_MATIERE);
        assertThat(testMatiere.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMatiere.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMatiere.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testMatiere.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testMatiere.getAutreRegion()).isEqualTo(DEFAULT_AUTRE_REGION);
        assertThat(testMatiere.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testMatiere.getAutreDep()).isEqualTo(DEFAULT_AUTRE_DEP);
        assertThat(testMatiere.getCodeIA()).isEqualTo(DEFAULT_CODE_IA);
        assertThat(testMatiere.getAutreCodeIA()).isEqualTo(DEFAULT_AUTRE_CODE_IA);
        assertThat(testMatiere.getTypeStructure()).isEqualTo(DEFAULT_TYPE_STRUCTURE);
        assertThat(testMatiere.getAutreStructure()).isEqualTo(DEFAULT_AUTRE_STRUCTURE);
        assertThat(testMatiere.getAnneeAffectation()).isEqualTo(DEFAULT_ANNEE_AFFECTATION);
    }

    @Test
    @Transactional
    void createMatiereWithExistingId() throws Exception {
        // Create the Matiere with an existing ID
        matiere.setId(1L);

        int databaseSizeBeforeCreate = matiereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setRegion(null);

        // Create the Matiere, which fails.

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartementIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setDepartement(null);

        // Create the Matiere, which fails.

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIAIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setCodeIA(null);

        // Create the Matiere, which fails.

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeStructureIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setTypeStructure(null);

        // Create the Matiere, which fails.

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnneeAffectationIsRequired() throws Exception {
        int databaseSizeBeforeTest = matiereRepository.findAll().size();
        // set the field null
        matiere.setAnneeAffectation(null);

        // Create the Matiere, which fails.

        restMatiereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isBadRequest());

        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMatieres() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get all the matiereList
        restMatiereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(matiere.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeMatiere").value(hasItem(DEFAULT_MATRICULE_MATIERE)))
            .andExpect(jsonPath("$.[*].nomMatiere").value(hasItem(DEFAULT_NOM_MATIERE)))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].autreRegion").value(hasItem(DEFAULT_AUTRE_REGION)))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].autreDep").value(hasItem(DEFAULT_AUTRE_DEP)))
            .andExpect(jsonPath("$.[*].codeIA").value(hasItem(DEFAULT_CODE_IA.toString())))
            .andExpect(jsonPath("$.[*].autreCodeIA").value(hasItem(DEFAULT_AUTRE_CODE_IA)))
            .andExpect(jsonPath("$.[*].typeStructure").value(hasItem(DEFAULT_TYPE_STRUCTURE.toString())))
            .andExpect(jsonPath("$.[*].autreStructure").value(hasItem(DEFAULT_AUTRE_STRUCTURE)))
            .andExpect(jsonPath("$.[*].anneeAffectation").value(hasItem(DEFAULT_ANNEE_AFFECTATION)));
    }

    @Test
    @Transactional
    void getMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        // Get the matiere
        restMatiereMockMvc
            .perform(get(ENTITY_API_URL_ID, matiere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(matiere.getId().intValue()))
            .andExpect(jsonPath("$.matriculeMatiere").value(DEFAULT_MATRICULE_MATIERE))
            .andExpect(jsonPath("$.nomMatiere").value(DEFAULT_NOM_MATIERE))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.autreRegion").value(DEFAULT_AUTRE_REGION))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.autreDep").value(DEFAULT_AUTRE_DEP))
            .andExpect(jsonPath("$.codeIA").value(DEFAULT_CODE_IA.toString()))
            .andExpect(jsonPath("$.autreCodeIA").value(DEFAULT_AUTRE_CODE_IA))
            .andExpect(jsonPath("$.typeStructure").value(DEFAULT_TYPE_STRUCTURE.toString()))
            .andExpect(jsonPath("$.autreStructure").value(DEFAULT_AUTRE_STRUCTURE))
            .andExpect(jsonPath("$.anneeAffectation").value(DEFAULT_ANNEE_AFFECTATION));
    }

    @Test
    @Transactional
    void getNonExistingMatiere() throws Exception {
        // Get the matiere
        restMatiereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere
        Matiere updatedMatiere = matiereRepository.findById(matiere.getId()).get();
        // Disconnect from session so that the updates on updatedMatiere are not directly saved in db
        em.detach(updatedMatiere);
        updatedMatiere
            .matriculeMatiere(UPDATED_MATRICULE_MATIERE)
            .nomMatiere(UPDATED_NOM_MATIERE)
            .reference(UPDATED_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .region(UPDATED_REGION)
            .autreRegion(UPDATED_AUTRE_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreDep(UPDATED_AUTRE_DEP)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .typeStructure(UPDATED_TYPE_STRUCTURE)
            .autreStructure(UPDATED_AUTRE_STRUCTURE)
            .anneeAffectation(UPDATED_ANNEE_AFFECTATION);

        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMatiere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getMatriculeMatiere()).isEqualTo(UPDATED_MATRICULE_MATIERE);
        assertThat(testMatiere.getNomMatiere()).isEqualTo(UPDATED_NOM_MATIERE);
        assertThat(testMatiere.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMatiere.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMatiere.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMatiere.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testMatiere.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testMatiere.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testMatiere.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testMatiere.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testMatiere.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testMatiere.getTypeStructure()).isEqualTo(UPDATED_TYPE_STRUCTURE);
        assertThat(testMatiere.getAutreStructure()).isEqualTo(UPDATED_AUTRE_STRUCTURE);
        assertThat(testMatiere.getAnneeAffectation()).isEqualTo(UPDATED_ANNEE_AFFECTATION);
    }

    @Test
    @Transactional
    void putNonExistingMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, matiere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(matiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatiereWithPatch() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere using partial update
        Matiere partialUpdatedMatiere = new Matiere();
        partialUpdatedMatiere.setId(matiere.getId());

        partialUpdatedMatiere
            .nomMatiere(UPDATED_NOM_MATIERE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .autreDep(UPDATED_AUTRE_DEP)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .typeStructure(UPDATED_TYPE_STRUCTURE)
            .autreStructure(UPDATED_AUTRE_STRUCTURE);

        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getMatriculeMatiere()).isEqualTo(DEFAULT_MATRICULE_MATIERE);
        assertThat(testMatiere.getNomMatiere()).isEqualTo(UPDATED_NOM_MATIERE);
        assertThat(testMatiere.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testMatiere.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMatiere.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMatiere.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testMatiere.getAutreRegion()).isEqualTo(DEFAULT_AUTRE_REGION);
        assertThat(testMatiere.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testMatiere.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testMatiere.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testMatiere.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testMatiere.getTypeStructure()).isEqualTo(UPDATED_TYPE_STRUCTURE);
        assertThat(testMatiere.getAutreStructure()).isEqualTo(UPDATED_AUTRE_STRUCTURE);
        assertThat(testMatiere.getAnneeAffectation()).isEqualTo(DEFAULT_ANNEE_AFFECTATION);
    }

    @Test
    @Transactional
    void fullUpdateMatiereWithPatch() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();

        // Update the matiere using partial update
        Matiere partialUpdatedMatiere = new Matiere();
        partialUpdatedMatiere.setId(matiere.getId());

        partialUpdatedMatiere
            .matriculeMatiere(UPDATED_MATRICULE_MATIERE)
            .nomMatiere(UPDATED_NOM_MATIERE)
            .reference(UPDATED_REFERENCE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .region(UPDATED_REGION)
            .autreRegion(UPDATED_AUTRE_REGION)
            .departement(UPDATED_DEPARTEMENT)
            .autreDep(UPDATED_AUTRE_DEP)
            .codeIA(UPDATED_CODE_IA)
            .autreCodeIA(UPDATED_AUTRE_CODE_IA)
            .typeStructure(UPDATED_TYPE_STRUCTURE)
            .autreStructure(UPDATED_AUTRE_STRUCTURE)
            .anneeAffectation(UPDATED_ANNEE_AFFECTATION);

        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatiere))
            )
            .andExpect(status().isOk());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
        Matiere testMatiere = matiereList.get(matiereList.size() - 1);
        assertThat(testMatiere.getMatriculeMatiere()).isEqualTo(UPDATED_MATRICULE_MATIERE);
        assertThat(testMatiere.getNomMatiere()).isEqualTo(UPDATED_NOM_MATIERE);
        assertThat(testMatiere.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testMatiere.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMatiere.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testMatiere.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testMatiere.getAutreRegion()).isEqualTo(UPDATED_AUTRE_REGION);
        assertThat(testMatiere.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testMatiere.getAutreDep()).isEqualTo(UPDATED_AUTRE_DEP);
        assertThat(testMatiere.getCodeIA()).isEqualTo(UPDATED_CODE_IA);
        assertThat(testMatiere.getAutreCodeIA()).isEqualTo(UPDATED_AUTRE_CODE_IA);
        assertThat(testMatiere.getTypeStructure()).isEqualTo(UPDATED_TYPE_STRUCTURE);
        assertThat(testMatiere.getAutreStructure()).isEqualTo(UPDATED_AUTRE_STRUCTURE);
        assertThat(testMatiere.getAnneeAffectation()).isEqualTo(UPDATED_ANNEE_AFFECTATION);
    }

    @Test
    @Transactional
    void patchNonExistingMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, matiere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(matiere))
            )
            .andExpect(status().isBadRequest());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatiere() throws Exception {
        int databaseSizeBeforeUpdate = matiereRepository.findAll().size();
        matiere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatiereMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(matiere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Matiere in the database
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatiere() throws Exception {
        // Initialize the database
        matiereRepository.saveAndFlush(matiere);

        int databaseSizeBeforeDelete = matiereRepository.findAll().size();

        // Delete the matiere
        restMatiereMockMvc
            .perform(delete(ENTITY_API_URL_ID, matiere.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Matiere> matiereList = matiereRepository.findAll();
        assertThat(matiereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
