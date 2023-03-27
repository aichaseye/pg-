package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.BFPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link BFPA}.
 */
public interface BFPAService {
    /**
     * Save a bFPA.
     *
     * @param bFPA the entity to save.
     * @return the persisted entity.
     */
    BFPA save(BFPA bFPA);

    /**
     * Partially updates a bFPA.
     *
     * @param bFPA the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BFPA> partialUpdate(BFPA bFPA);

    /**
     * Get all the bFPAS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BFPA> findAll(Pageable pageable);

    /**
     * Get all the bFPAS with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BFPA> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" bFPA.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BFPA> findOne(Long id);

    /**
     * Delete the "id" bFPA.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
