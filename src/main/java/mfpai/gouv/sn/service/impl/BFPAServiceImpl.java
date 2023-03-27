package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.BFPA;
import mfpai.gouv.sn.repository.BFPARepository;
import mfpai.gouv.sn.service.BFPAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link BFPA}.
 */
@Service
@Transactional
public class BFPAServiceImpl implements BFPAService {

    private final Logger log = LoggerFactory.getLogger(BFPAServiceImpl.class);

    private final BFPARepository bFPARepository;

    public BFPAServiceImpl(BFPARepository bFPARepository) {
        this.bFPARepository = bFPARepository;
    }

    @Override
    public BFPA save(BFPA bFPA) {
        log.debug("Request to save BFPA : {}", bFPA);
        return bFPARepository.save(bFPA);
    }

    @Override
    public Optional<BFPA> partialUpdate(BFPA bFPA) {
        log.debug("Request to partially update BFPA : {}", bFPA);

        return bFPARepository
            .findById(bFPA.getId())
            .map(existingBFPA -> {
                if (bFPA.getNomPrenom() != null) {
                    existingBFPA.setNomPrenom(bFPA.getNomPrenom());
                }

                return existingBFPA;
            })
            .map(bFPARepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BFPA> findAll(Pageable pageable) {
        log.debug("Request to get all BFPAS");
        return bFPARepository.findAll(pageable);
    }

    public Page<BFPA> findAllWithEagerRelationships(Pageable pageable) {
        return bFPARepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BFPA> findOne(Long id) {
        log.debug("Request to get BFPA : {}", id);
        return bFPARepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BFPA : {}", id);
        bFPARepository.deleteById(id);
    }
}
