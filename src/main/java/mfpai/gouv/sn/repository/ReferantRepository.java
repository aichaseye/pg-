package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Referant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Referant entity.
 */
@Repository
public interface ReferantRepository extends JpaRepository<Referant, Long> {
    default Optional<Referant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Referant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Referant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct referant from Referant referant left join fetch referant.user",
        countQuery = "select count(distinct referant) from Referant referant"
    )
    Page<Referant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct referant from Referant referant left join fetch referant.user")
    List<Referant> findAllWithToOneRelationships();

    @Query("select referant from Referant referant left join fetch referant.user where referant.id =:id")
    Optional<Referant> findOneWithToOneRelationships(@Param("id") Long id);
}
