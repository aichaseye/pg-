package mfpai.gouv.sn.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import mfpai.gouv.sn.domain.Referant;
import mfpai.gouv.sn.repository.ReferantRepository;
import mfpai.gouv.sn.service.ReferantService;
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
 * REST controller for managing {@link mfpai.gouv.sn.domain.Referant}.
 */
@RestController
@RequestMapping("/api")
public class ReferantResource {

    private final Logger log = LoggerFactory.getLogger(ReferantResource.class);

    private static final String ENTITY_NAME = "referant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferantService referantService;

    private final ReferantRepository referantRepository;

    public ReferantResource(ReferantService referantService, ReferantRepository referantRepository) {
        this.referantService = referantService;
        this.referantRepository = referantRepository;
    }

    /**
     * {@code POST  /referants} : Create a new referant.
     *
     * @param referant the referant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referant, or with status {@code 400 (Bad Request)} if the referant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/referants")
    public ResponseEntity<Referant> createReferant(@Valid @RequestBody Referant referant) throws URISyntaxException {
        log.debug("REST request to save Referant : {}", referant);
        if (referant.getId() != null) {
            throw new BadRequestAlertException("A new referant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Referant result = referantService.save(referant);
        return ResponseEntity
            .created(new URI("/api/referants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /referants/:id} : Updates an existing referant.
     *
     * @param id the id of the referant to save.
     * @param referant the referant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referant,
     * or with status {@code 400 (Bad Request)} if the referant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/referants/{id}")
    public ResponseEntity<Referant> updateReferant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Referant referant
    ) throws URISyntaxException {
        log.debug("REST request to update Referant : {}, {}", id, referant);
        if (referant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Referant result = referantService.save(referant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, referant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /referants/:id} : Partial updates given fields of an existing referant, field will ignore if it is null
     *
     * @param id the id of the referant to save.
     * @param referant the referant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referant,
     * or with status {@code 400 (Bad Request)} if the referant is not valid,
     * or with status {@code 404 (Not Found)} if the referant is not found,
     * or with status {@code 500 (Internal Server Error)} if the referant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/referants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Referant> partialUpdateReferant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Referant referant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Referant partially : {}, {}", id, referant);
        if (referant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Referant> result = referantService.partialUpdate(referant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, referant.getId().toString())
        );
    }

    /**
     * {@code GET  /referants} : get all the referants.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referants in body.
     */
    @GetMapping("/referants")
    public ResponseEntity<List<Referant>> getAllReferants(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Referants");
        Page<Referant> page;
        if (eagerload) {
            page = referantService.findAllWithEagerRelationships(pageable);
        } else {
            page = referantService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referants/:id} : get the "id" referant.
     *
     * @param id the id of the referant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referants/{id}")
    public ResponseEntity<Referant> getReferant(@PathVariable Long id) {
        log.debug("REST request to get Referant : {}", id);
        Optional<Referant> referant = referantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referant);
    }

    /**
     * {@code DELETE  /referants/:id} : delete the "id" referant.
     *
     * @param id the id of the referant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/referants/{id}")
    public ResponseEntity<Void> deleteReferant(@PathVariable Long id) {
        log.debug("REST request to delete Referant : {}", id);
        referantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
