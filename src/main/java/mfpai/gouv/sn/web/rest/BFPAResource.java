package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mfpai.gouv.sn.domain.BFPA;
import mfpai.gouv.sn.repository.BFPARepository;
import mfpai.gouv.sn.service.BFPAService;
import mfpai.gouv.sn.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mfpai.gouv.sn.domain.BFPA}.
 */
@RestController
@RequestMapping("/api")
public class BFPAResource {

    private final Logger log = LoggerFactory.getLogger(BFPAResource.class);

    private static final String ENTITY_NAME = "bFPA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BFPAService bFPAService;

    private final BFPARepository bFPARepository;

    public BFPAResource(BFPAService bFPAService, BFPARepository bFPARepository) {
        this.bFPAService = bFPAService;
        this.bFPARepository = bFPARepository;
    }

    /**
     * {@code POST  /bfpas} : Create a new bFPA.
     *
     * @param bFPA the bFPA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bFPA, or with status {@code 400 (Bad Request)} if the bFPA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bfpas")
    public ResponseEntity<BFPA> createBFPA(@Valid @RequestBody BFPA bFPA) throws URISyntaxException {
        log.debug("REST request to save BFPA : {}", bFPA);
        if (bFPA.getId() != null) {
            throw new BadRequestAlertException("A new bFPA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BFPA result = bFPAService.save(bFPA);
        return ResponseEntity
            .created(new URI("/api/bfpas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bfpas/:id} : Updates an existing bFPA.
     *
     * @param id the id of the bFPA to save.
     * @param bFPA the bFPA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bFPA,
     * or with status {@code 400 (Bad Request)} if the bFPA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bFPA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bfpas/{id}")
    public ResponseEntity<BFPA> updateBFPA(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody BFPA bFPA)
        throws URISyntaxException {
        log.debug("REST request to update BFPA : {}, {}", id, bFPA);
        if (bFPA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bFPA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bFPARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BFPA result = bFPAService.save(bFPA);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bFPA.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bfpas/:id} : Partial updates given fields of an existing bFPA, field will ignore if it is null
     *
     * @param id the id of the bFPA to save.
     * @param bFPA the bFPA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bFPA,
     * or with status {@code 400 (Bad Request)} if the bFPA is not valid,
     * or with status {@code 404 (Not Found)} if the bFPA is not found,
     * or with status {@code 500 (Internal Server Error)} if the bFPA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bfpas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BFPA> partialUpdateBFPA(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BFPA bFPA
    ) throws URISyntaxException {
        log.debug("REST request to partial update BFPA partially : {}, {}", id, bFPA);
        if (bFPA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bFPA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bFPARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BFPA> result = bFPAService.partialUpdate(bFPA);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bFPA.getId().toString())
        );
    }

    /**
     * {@code GET  /bfpas} : get all the bFPAS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bFPAS in body.
     */
    @GetMapping("/bfpas")
    public ResponseEntity<List<BFPA>> getAllBFPAS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of BFPAS");
        Page<BFPA> page;
        if (eagerload) {
            page = bFPAService.findAllWithEagerRelationships(pageable);
        } else {
            page = bFPAService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bfpas/:id} : get the "id" bFPA.
     *
     * @param id the id of the bFPA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bFPA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bfpas/{id}")
    public ResponseEntity<BFPA> getBFPA(@PathVariable Long id) {
        log.debug("REST request to get BFPA : {}", id);
        Optional<BFPA> bFPA = bFPAService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bFPA);
    }

    /**
     * {@code DELETE  /bfpas/:id} : delete the "id" bFPA.
     *
     * @param id the id of the bFPA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bfpas/{id}")
    public ResponseEntity<Void> deleteBFPA(@PathVariable Long id) {
        log.debug("REST request to delete BFPA : {}", id);
        bFPAService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
