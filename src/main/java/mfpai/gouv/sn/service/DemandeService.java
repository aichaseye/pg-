package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Demande}.
 */
public interface DemandeService {
    /**
     * Save a demande.
     *
     * @param demande the entity to save.
     * @return the persisted entity.
     */
    Demande save(Demande demande);

    /**
     * Partially updates a demande.
     *
     * @param demande the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Demande> partialUpdate(Demande demande);

    /**
     * Get all the demandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Demande> findAll(Pageable pageable);

    /**
     * Get all the demandes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Demande> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" demande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Demande> findOne(Long id);

    /**
     * Delete the "id" demande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
