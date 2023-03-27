package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.Referant;
import mfpai.gouv.sn.repository.ReferantRepository;
import mfpai.gouv.sn.service.ReferantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Referant}.
 */
@Service
@Transactional
public class ReferantServiceImpl implements ReferantService {

    private final Logger log = LoggerFactory.getLogger(ReferantServiceImpl.class);

    private final ReferantRepository referantRepository;

    public ReferantServiceImpl(ReferantRepository referantRepository) {
        this.referantRepository = referantRepository;
    }

    @Override
    public Referant save(Referant referant) {
        log.debug("Request to save Referant : {}", referant);
        return referantRepository.save(referant);
    }

    @Override
    public Optional<Referant> partialUpdate(Referant referant) {
        log.debug("Request to partially update Referant : {}", referant);

        return referantRepository
            .findById(referant.getId())
            .map(existingReferant -> {
                if (referant.getNomPrenom() != null) {
                    existingReferant.setNomPrenom(referant.getNomPrenom());
                }

                return existingReferant;
            })
            .map(referantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Referant> findAll(Pageable pageable) {
        log.debug("Request to get all Referants");
        return referantRepository.findAll(pageable);
    }

    public Page<Referant> findAllWithEagerRelationships(Pageable pageable) {
        return referantRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Referant> findOne(Long id) {
        log.debug("Request to get Referant : {}", id);
        return referantRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Referant : {}", id);
        referantRepository.deleteById(id);
    }
}
