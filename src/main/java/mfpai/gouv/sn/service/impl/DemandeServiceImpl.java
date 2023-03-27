package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.Demande;
import mfpai.gouv.sn.repository.DemandeRepository;
import mfpai.gouv.sn.service.DemandeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Demande}.
 */
@Service
@Transactional
public class DemandeServiceImpl implements DemandeService {

    private final Logger log = LoggerFactory.getLogger(DemandeServiceImpl.class);

    private final DemandeRepository demandeRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public Demande save(Demande demande) {
        log.debug("Request to save Demande : {}", demande);
        return demandeRepository.save(demande);
    }

    @Override
    public Optional<Demande> partialUpdate(Demande demande) {
        log.debug("Request to partially update Demande : {}", demande);

        return demandeRepository
            .findById(demande.getId())
            .map(existingDemande -> {
                if (demande.getMotif() != null) {
                    existingDemande.setMotif(demande.getMotif());
                }
                if (demande.getTypeDemandeur() != null) {
                    existingDemande.setTypeDemandeur(demande.getTypeDemandeur());
                }
                if (demande.getNom() != null) {
                    existingDemande.setNom(demande.getNom());
                }
                if (demande.getPrenom() != null) {
                    existingDemande.setPrenom(demande.getPrenom());
                }
                if (demande.getNomEtab() != null) {
                    existingDemande.setNomEtab(demande.getNomEtab());
                }
                if (demande.getAutreNomEtab() != null) {
                    existingDemande.setAutreNomEtab(demande.getAutreNomEtab());
                }
                if (demande.getEmail() != null) {
                    existingDemande.setEmail(demande.getEmail());
                }

                return existingDemande;
            })
            .map(demandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Demande> findAll(Pageable pageable) {
        log.debug("Request to get all Demandes");
        return demandeRepository.findAll(pageable);
    }

    public Page<Demande> findAllWithEagerRelationships(Pageable pageable) {
        return demandeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Demande> findOne(Long id) {
        log.debug("Request to get Demande : {}", id);
        return demandeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Demande : {}", id);
        demandeRepository.deleteById(id);
    }
}
