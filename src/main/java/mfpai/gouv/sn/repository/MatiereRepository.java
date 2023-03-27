package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Matiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Matiere entity.
 */
@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    default Optional<Matiere> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Matiere> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Matiere> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct matiere from Matiere matiere left join fetch matiere.referant left join fetch matiere.comptableMatiere left join fetch matiere.etablissement",
        countQuery = "select count(distinct matiere) from Matiere matiere"
    )
    Page<Matiere> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct matiere from Matiere matiere left join fetch matiere.referant left join fetch matiere.comptableMatiere left join fetch matiere.etablissement"
    )
    List<Matiere> findAllWithToOneRelationships();

    @Query(
        "select matiere from Matiere matiere left join fetch matiere.referant left join fetch matiere.comptableMatiere left join fetch matiere.etablissement where matiere.id =:id"
    )
    Optional<Matiere> findOneWithToOneRelationships(@Param("id") Long id);
    @Query(value = "select max(id) from Matiere ")
    public Long  findOneByIdDesc( );
}
