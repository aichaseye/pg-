package mfpai.gouv.sn.repository;

import java.util.List;
import java.util.Optional;
import mfpai.gouv.sn.domain.Etablissement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Etablissement entity.
 */
@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {
    default Optional<Etablissement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Etablissement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Etablissement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct etablissement from Etablissement etablissement left join fetch etablissement.bFPA",
        countQuery = "select count(distinct etablissement) from Etablissement etablissement"
    )
    Page<Etablissement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct etablissement from Etablissement etablissement left join fetch etablissement.bFPA")
    List<Etablissement> findAllWithToOneRelationships();

    @Query("select etablissement from Etablissement etablissement left join fetch etablissement.bFPA where etablissement.id =:id")
    Optional<Etablissement> findOneWithToOneRelationships(@Param("id") Long id);
    
    @Query(value =  "select max(id)from Etablissement")
     public Long  findOneByIdDesc( ); 
}
