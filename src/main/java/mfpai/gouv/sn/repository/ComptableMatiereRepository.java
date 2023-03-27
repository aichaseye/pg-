package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.ComptableMatiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ComptableMatiere entity.
 */
@Repository
public interface ComptableMatiereRepository extends JpaRepository<ComptableMatiere, Long> {
    default Optional<ComptableMatiere> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ComptableMatiere> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ComptableMatiere> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct comptableMatiere from ComptableMatiere comptableMatiere left join fetch comptableMatiere.user",
        countQuery = "select count(distinct comptableMatiere) from ComptableMatiere comptableMatiere"
    )
    Page<ComptableMatiere> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct comptableMatiere from ComptableMatiere comptableMatiere left join fetch comptableMatiere.user")
    List<ComptableMatiere> findAllWithToOneRelationships();

    @Query(
        "select comptableMatiere from ComptableMatiere comptableMatiere left join fetch comptableMatiere.user where comptableMatiere.id =:id"
    )
    Optional<ComptableMatiere> findOneWithToOneRelationships(@Param("id") Long id);
}
