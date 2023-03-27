package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.ChefEtablissement;
import mfpai.gouv.sn.repository.ChefEtablissementRepository;
import mfpai.gouv.sn.service.ChefEtablissementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChefEtablissement}.
 */
@Service
@Transactional
public class ChefEtablissementServiceImpl implements ChefEtablissementService {

    private final Logger log = LoggerFactory.getLogger(ChefEtablissementServiceImpl.class);

    private final ChefEtablissementRepository chefEtablissementRepository;

    public ChefEtablissementServiceImpl(ChefEtablissementRepository chefEtablissementRepository) {
        this.chefEtablissementRepository = chefEtablissementRepository;
    }

    @Override
    public ChefEtablissement save(ChefEtablissement chefEtablissement) {
        log.debug("Request to save ChefEtablissement : {}", chefEtablissement);
        return chefEtablissementRepository.save(chefEtablissement);
    }

    @Override
    public Optional<ChefEtablissement> partialUpdate(ChefEtablissement chefEtablissement) {
        log.debug("Request to partially update ChefEtablissement : {}", chefEtablissement);

        return chefEtablissementRepository
            .findById(chefEtablissement.getId())
            .map(existingChefEtablissement -> {
                if (chefEtablissement.getNomPrenom() != null) {
                    existingChefEtablissement.setNomPrenom(chefEtablissement.getNomPrenom());
                }

                return existingChefEtablissement;
            })
            .map(chefEtablissementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChefEtablissement> findAll(Pageable pageable) {
        log.debug("Request to get all ChefEtablissements");
        return chefEtablissementRepository.findAll(pageable);
    }

    public Page<ChefEtablissement> findAllWithEagerRelationships(Pageable pageable) {
        return chefEtablissementRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChefEtablissement> findOne(Long id) {
        log.debug("Request to get ChefEtablissement : {}", id);
        return chefEtablissementRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChefEtablissement : {}", id);
        chefEtablissementRepository.deleteById(id);
    }
}
