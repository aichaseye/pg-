package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Demande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Demande entity.
 */
@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    default Optional<Demande> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Demande> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Demande> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct demande from Demande demande left join fetch demande.enseignant left join fetch demande.apprenant left join fetch demande.etablissement",
        countQuery = "select count(distinct demande) from Demande demande"
    )
    Page<Demande> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct demande from Demande demande left join fetch demande.enseignant left join fetch demande.apprenant left join fetch demande.etablissement"
    )
    List<Demande> findAllWithToOneRelationships();

    @Query(
        "select demande from Demande demande left join fetch demande.enseignant left join fetch demande.apprenant left join fetch demande.etablissement where demande.id =:id"
    )
    Optional<Demande> findOneWithToOneRelationships(@Param("id") Long id);
}
