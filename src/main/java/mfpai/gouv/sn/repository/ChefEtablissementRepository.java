package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.ChefEtablissement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChefEtablissement entity.
 */
@Repository
public interface ChefEtablissementRepository extends JpaRepository<ChefEtablissement, Long> {
    default Optional<ChefEtablissement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ChefEtablissement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ChefEtablissement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct chefEtablissement from ChefEtablissement chefEtablissement left join fetch chefEtablissement.user",
        countQuery = "select count(distinct chefEtablissement) from ChefEtablissement chefEtablissement"
    )
    Page<ChefEtablissement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct chefEtablissement from ChefEtablissement chefEtablissement left join fetch chefEtablissement.user")
    List<ChefEtablissement> findAllWithToOneRelationships();

    @Query(
        "select chefEtablissement from ChefEtablissement chefEtablissement left join fetch chefEtablissement.user where chefEtablissement.id =:id"
    )
    Optional<ChefEtablissement> findOneWithToOneRelationships(@Param("id") Long id);
}
