package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Apprenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Apprenant entity.
 */
@Repository
public interface ApprenantRepository extends JpaRepository<Apprenant, Long> {
    default Optional<Apprenant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Apprenant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Apprenant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct apprenant from Apprenant apprenant left join fetch apprenant.chefEtablissement left join fetch apprenant.etablissement",
        countQuery = "select count(distinct apprenant) from Apprenant apprenant"
    )
    Page<Apprenant> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct apprenant from Apprenant apprenant left join fetch apprenant.chefEtablissement left join fetch apprenant.etablissement"
    )
    List<Apprenant> findAllWithToOneRelationships();

    @Query(
        "select apprenant from Apprenant apprenant left join fetch apprenant.chefEtablissement left join fetch apprenant.etablissement where apprenant.id =:id"
    )
    Optional<Apprenant> findOneWithToOneRelationships(@Param("id") Long id);
    
    @Query(value =  "select max(id)from Apprenant")
     public Long  findOneByIdDesc( ); 
}
