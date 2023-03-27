package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.BFPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BFPA entity.
 */
@Repository
public interface BFPARepository extends JpaRepository<BFPA, Long> {
    default Optional<BFPA> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BFPA> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BFPA> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct bFPA from BFPA bFPA left join fetch bFPA.user",
        countQuery = "select count(distinct bFPA) from BFPA bFPA"
    )
    Page<BFPA> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct bFPA from BFPA bFPA left join fetch bFPA.user")
    List<BFPA> findAllWithToOneRelationships();

    @Query("select bFPA from BFPA bFPA left join fetch bFPA.user where bFPA.id =:id")
    Optional<BFPA> findOneWithToOneRelationships(@Param("id") Long id);
}
