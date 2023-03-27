package mfpai.gouv.sn.service.impl;

import java.util.Calendar;
import java.util.Optional;
import mfpai.gouv.sn.domain.Apprenant;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import mfpai.gouv.sn.repository.ApprenantRepository;
import mfpai.gouv.sn.service.ApprenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Apprenant}.
 */
@Service
@Transactional
public class ApprenantServiceImpl implements ApprenantService {

    private final Logger log = LoggerFactory.getLogger(ApprenantServiceImpl.class);

    private final ApprenantRepository apprenantRepository;

    public ApprenantServiceImpl(ApprenantRepository apprenantRepository) {
        this.apprenantRepository = apprenantRepository;
    }

    @Override
    public Apprenant save(Apprenant apprenant) {
        log.debug("Request to save Apprenant : {}", apprenant);

        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        year = year.substring(year.length() - 2);
        // Long last_insert=(long) (new Random().nextInt(100)+2000);
        Long last_insert = apprenantRepository.findOneByIdDesc();
        if (last_insert == null) {
            last_insert = 1L;
        }

        String order = getOrder(String.valueOf(last_insert));

        int sexe = 1;

        if (apprenant.getSexe().equals(Sexe.F)) {
            sexe = 2;
        }
        String matricule = year + sexe + order;
        //calcule de checksum (differance de la somme des valeurs du matricule de position paire et impaire)
        int sPaire = 0;
        int sImpaire = 0;
        int diff = 0;
        String lettre = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < matricule.length(); i++) {
            // convertire la chaine matricule en caractere puis en int
            char ch = matricule.charAt(i);
            int c = Character.getNumericValue(ch);
            // somme des valeur d'indice pair et impaire
            if (i % 2 == 0) {
                sPaire = sPaire + c;
            } else sImpaire = sImpaire + c;
            // differance
            diff = sPaire - sImpaire;
            if (sPaire < sImpaire) {
                diff = -diff;
            } else if (sPaire == sImpaire) {
                diff = 1;
            } else if (diff > 26) {
                diff = 26;
            }
        }
        char l = lettre.charAt(diff - 1);
        matricule = matricule + l;
        // ne pas pouvoir modifier le matricule
        if (apprenant.getMatriculeApp() == null) apprenant.setMatriculeApp(matricule);

        return apprenantRepository.save(apprenant);
    }

    public String getOrder(String id) {
        String order;

        if (id.length() == 1) {
            order = "0000" + id;
        } else if (id.length() == 2) {
            order = "000" + id;
        } else if (id.length() == 3) {
            order = "00" + id;
        } else if (id.length() == 4) {
            order = "0" + id;
        } else if (id.length() == 5) {
            order = id;
        } else {
            order = id.substring(0, 5);
        }
        return order;
    }

    @Override
    public Long findOneByIdDesc() {
        return apprenantRepository.findOneByIdDesc();
    }

    @Override
    public Optional<Apprenant> partialUpdate(Apprenant apprenant) {
        log.debug("Request to partially update Apprenant : {}", apprenant);

        return apprenantRepository
            .findById(apprenant.getId())
            .map(existingApprenant -> {
                if (apprenant.getMatriculeApp() != null) {
                    existingApprenant.setMatriculeApp(apprenant.getMatriculeApp());
                }
                if (apprenant.getNom() != null) {
                    existingApprenant.setNom(apprenant.getNom());
                }
                if (apprenant.getPrenom() != null) {
                    existingApprenant.setPrenom(apprenant.getPrenom());
                }
                if (apprenant.getSexe() != null) {
                    existingApprenant.setSexe(apprenant.getSexe());
                }
                if (apprenant.getTelephone() != null) {
                    existingApprenant.setTelephone(apprenant.getTelephone());
                }
                if (apprenant.getEmail() != null) {
                    existingApprenant.setEmail(apprenant.getEmail());
                }

                return existingApprenant;
            })
            .map(apprenantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Apprenant> findAll(Pageable pageable) {
        log.debug("Request to get all Apprenants");
        return apprenantRepository.findAll(pageable);
    }

    public Page<Apprenant> findAllWithEagerRelationships(Pageable pageable) {
        return apprenantRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Apprenant> findOne(Long id) {
        log.debug("Request to get Apprenant : {}", id);
        return apprenantRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Apprenant : {}", id);
        apprenantRepository.deleteById(id);
    }
}
