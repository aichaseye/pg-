package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.Referant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Referant}.
 */
public interface ReferantService {
    /**
     * Save a referant.
     *
     * @param referant the entity to save.
     * @return the persisted entity.
     */
    Referant save(Referant referant);

    /**
     * Partially updates a referant.
     *
     * @param referant the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Referant> partialUpdate(Referant referant);

    /**
     * Get all the referants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Referant> findAll(Pageable pageable);

    /**
     * Get all the referants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Referant> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" referant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Referant> findOne(Long id);

    /**
     * Delete the "id" referant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
