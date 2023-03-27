package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.Etablissement;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.StatutEtab;
import mfpai.gouv.sn.domain.enumeration.TypeEtab;
import mfpai.gouv.sn.repository.EtablissementRepository;
import mfpai.gouv.sn.service.EtablissementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Etablissement}.
 */
@Service
@Transactional
public class EtablissementServiceImpl implements EtablissementService {

    private final Logger log = LoggerFactory.getLogger(EtablissementServiceImpl.class);

    private final EtablissementRepository etablissementRepository;

    public EtablissementServiceImpl(EtablissementRepository etablissementRepository) {
        this.etablissementRepository = etablissementRepository;
    }

    @Override
    public Etablissement save(Etablissement etablissement) {
        log.debug("Request to save Etablissement : {}", etablissement);
        String anneCreation = String.valueOf(etablissement.getAnneeCreation()).substring(2, 4);
        Long lastId = etablissementRepository.findOneByIdDesc();
        if (lastId == null) {
            lastId = 1L;
        }
        String order = getOrder(String.valueOf(lastId));
        // // String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // // Random rnd = new Random();
        // char letter = alphabet.charAt(rnd.nextInt(alphabet.length()));

        String type = "L";
        if (etablissement.getTypeEtab().equals(TypeEtab.CFP)) {
            type = "C";
        }
        String statut = "2";
        if (etablissement.getStatut() == StatutEtab.Public) {
            statut = "1";
        } else if (etablissement.getStatut() == StatutEtab.Mixte) {
            statut = "3";
        }
       
        String matricule = "ETFP-".concat(statut) .concat(anneCreation);
        String codeIA="";
        if(etablissement.getCodeIA()==CodeIA.Autres){
            codeIA= etablissement.getAutreCodeIA().substring(1, 3);
        }
        else if(etablissement.getCodeIA()==CodeIA.Autres && etablissement.getAutreCodeIA()!=null){
            codeIA= String.valueOf (etablissement.getCodeIA()).substring(1, 3);
        }
        else
        {codeIA= String.valueOf (etablissement.getCodeIA()).substring(1, 3);}

        matricule=matricule.concat(codeIA).concat(order).concat(type);
        // ne pouvavt pas etre modifier
        //  if(etablissement.getMatriculeEtab()== null)
        etablissement.setMatriculeEtab(matricule);

        return etablissementRepository.save(etablissement);
    }

    @Override
    public Optional<Etablissement> partialUpdate(Etablissement etablissement) {
        log.debug("Request to partially update Etablissement : {}", etablissement);

        return etablissementRepository
            .findById(etablissement.getId())
            .map(existingEtablissement -> {
                if (etablissement.getMatriculeEtab() != null) {
                    existingEtablissement.setMatriculeEtab(etablissement.getMatriculeEtab());
                }
                if (etablissement.getRegion() != null) {
                    existingEtablissement.setRegion(etablissement.getRegion());
                }
                if (etablissement.getDepartement() != null) {
                    existingEtablissement.setDepartement(etablissement.getDepartement());
                }
                if (etablissement.getAutreRegion() != null) {
                    existingEtablissement.setAutreRegion(etablissement.getAutreRegion());
                }
                if (etablissement.getAutreDep() != null) {
                    existingEtablissement.setAutreDep(etablissement.getAutreDep());
                }
                if (etablissement.getNomEtab() != null) {
                    existingEtablissement.setNomEtab(etablissement.getNomEtab());
                }
                if (etablissement.getCodeIA() != null) {
                    existingEtablissement.setCodeIA(etablissement.getCodeIA());
                }
                if (etablissement.getAutreCodeIA() != null) {
                    existingEtablissement.setAutreCodeIA(etablissement.getAutreCodeIA());
                }
                if (etablissement.getAutreNomeEtab() != null) {
                    existingEtablissement.setAutreNomeEtab(etablissement.getAutreNomeEtab());
                }
                if (etablissement.getTypeEtab() != null) {
                    existingEtablissement.setTypeEtab(etablissement.getTypeEtab());
                }
                if (etablissement.getAnneeCreation() != null) {
                    existingEtablissement.setAnneeCreation(etablissement.getAnneeCreation());
                }
                if (etablissement.getStatut() != null) {
                    existingEtablissement.setStatut(etablissement.getStatut());
                }
                if (etablissement.getEmailEtab() != null) {
                    existingEtablissement.setEmailEtab(etablissement.getEmailEtab());
                }
                if (etablissement.getTypeInsp() != null) {
                    existingEtablissement.setTypeInsp(etablissement.getTypeInsp());
                }

                return existingEtablissement;
            })
            .map(etablissementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Etablissement> findAll(Pageable pageable) {
        log.debug("Request to get all Etablissements");
        return etablissementRepository.findAll(pageable);
    }

    public Page<Etablissement> findAllWithEagerRelationships(Pageable pageable) {
        return etablissementRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etablissement> findOne(Long id) {
        log.debug("Request to get Etablissement : {}", id);
        return etablissementRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Etablissement : {}", id);
        etablissementRepository.deleteById(id);
    }
    @Override
    public Long findOneByIdDesc() {
        return etablissementRepository.findOneByIdDesc();
    }

    public String getOrder(String id) {
        String order;

        if (id.length() == 1) {
            order = "00" + id;
        } else if (id.length() == 2) {
            order = "0" + id;
        } else if (id.length() == 3) {
            order = "" + id;
        } else {
            order = id.substring(0, 3);
        }
        return order;
    }
}