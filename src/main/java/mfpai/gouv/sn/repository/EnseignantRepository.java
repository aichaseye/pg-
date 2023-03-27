package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Enseignant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Enseignant entity.
 */
@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
    default Optional<Enseignant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Enseignant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Enseignant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct enseignant from Enseignant enseignant left join fetch enseignant.bFPA left join fetch enseignant.etablissement",
        countQuery = "select count(distinct enseignant) from Enseignant enseignant"
    )
    Page<Enseignant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct enseignant from Enseignant enseignant left join fetch enseignant.bFPA left join fetch enseignant.etablissement")
    List<Enseignant> findAllWithToOneRelationships();

    @Query(
        "select enseignant from Enseignant enseignant left join fetch enseignant.bFPA left join fetch enseignant.etablissement where enseignant.id =:id"
    )
    Optional<Enseignant> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(value =  "select max(id)from Enseignant")
     public Long  findOneByIdDesc( );
}
