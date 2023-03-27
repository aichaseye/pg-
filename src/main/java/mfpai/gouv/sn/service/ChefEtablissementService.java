package mfpai.gouv.sn.service;

import java.util.Optional;
import mfpai.gouv.sn.domain.ChefEtablissement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ChefEtablissement}.
 */
public interface ChefEtablissementService {
    /**
     * Save a chefEtablissement.
     *
     * @param chefEtablissement the entity to save.
     * @return the persisted entity.
     */
    ChefEtablissement save(ChefEtablissement chefEtablissement);

    /**
     * Partially updates a chefEtablissement.
     *
     * @param chefEtablissement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChefEtablissement> partialUpdate(ChefEtablissement chefEtablissement);

    /**
     * Get all the chefEtablissements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChefEtablissement> findAll(Pageable pageable);

    /**
     * Get all the chefEtablissements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChefEtablissement> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" chefEtablissement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChefEtablissement> findOne(Long id);

    /**
     * Delete the "id" chefEtablissement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
