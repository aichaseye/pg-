package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.Enseignant;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.repository.EnseignantRepository;
import mfpai.gouv.sn.service.EnseignantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Enseignant}.
 */
@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private final Logger log = LoggerFactory.getLogger(EnseignantServiceImpl.class);

    private final EnseignantRepository enseignantRepository;

    public EnseignantServiceImpl(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    public Enseignant save(Enseignant enseignant) {
        log.debug("Request to save Enseignant : {}", enseignant);
        // String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        // year = year.substring(year.length() - 2);
        // Long last_insert=(long) (new Random().nextInt(100)+2000);
        Long last_insert = enseignantRepository.findOneByIdDesc();
        if (last_insert == null) {
            last_insert = 1L;
        }

        String order = getOrder(String.valueOf(last_insert));

        
        String matricule = String.valueOf(enseignant.getAnneeDentree()).substring(2);
        String codeIA="";
        if(enseignant.getCodeIA()==CodeIA.Autres){
            codeIA= enseignant.getAutreCodeIA().substring(1,3);
        }
        else if(enseignant.getCodeIA()==CodeIA.Autres && enseignant.getAutreCodeIA()!=null){
            codeIA= String.valueOf (enseignant.getCodeIA()).substring(1,3);
        }
        else
        {codeIA= String.valueOf (enseignant.getCodeIA()).substring(1, 3);}

        matricule=matricule.concat(codeIA).concat(order);
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
        // if (enseignant.getMatriculeEns() == null) 
        enseignant.setMatriculeEns(matricule);

        return enseignantRepository.save(enseignant);
    }

    public String getOrder(String id) {
        String order;

        if (id.length() == 1) {
            order = "00" + id;
        } else if (id.length() == 2) {
            order = "0" + id;
        
        } else if (id.length() == 3) {
            order = id;
        } else {
            order = id.substring(0, 3);
        }
        return order;
    }

    @Override
    public Long findOneByIdDesc() {
        return enseignantRepository.findOneByIdDesc();
    }

    @Override
    public Optional<Enseignant> partialUpdate(Enseignant enseignant) {
        log.debug("Request to partially update Enseignant : {}", enseignant);

        return enseignantRepository
            .findById(enseignant.getId())
            .map(existingEnseignant -> {
                if (enseignant.getMatriculeEns() != null) {
                    existingEnseignant.setMatriculeEns(enseignant.getMatriculeEns());
                }
                if (enseignant.getNom() != null) {
                    existingEnseignant.setNom(enseignant.getNom());
                }
                if (enseignant.getPrenom() != null) {
                    existingEnseignant.setPrenom(enseignant.getPrenom());
                }
                if (enseignant.getNumCNI() != null) {
                    existingEnseignant.setNumCNI(enseignant.getNumCNI());
                }
                if (enseignant.getAnneeDentree() != null) {
                    existingEnseignant.setAnneeDentree(enseignant.getAnneeDentree());
                }
                if (enseignant.getRegion() != null) {
                    existingEnseignant.setRegion(enseignant.getRegion());
                }
                if (enseignant.getDepartement() != null) {
                    existingEnseignant.setDepartement(enseignant.getDepartement());
                }
                if (enseignant.getAutreDep() != null) {
                    existingEnseignant.setAutreDep(enseignant.getAutreDep());
                }
                if (enseignant.getAutreRegion() != null) {
                    existingEnseignant.setAutreRegion(enseignant.getAutreRegion());
                }
                if (enseignant.getCodeIA() != null) {
                    existingEnseignant.setCodeIA(enseignant.getCodeIA());
                }
                if (enseignant.getAutreCodeIA() != null) {
                    existingEnseignant.setAutreCodeIA(enseignant.getAutreCodeIA());
                }
                if (enseignant.getSexe() != null) {
                    existingEnseignant.setSexe(enseignant.getSexe());
                }
                if (enseignant.getEmail() != null) {
                    existingEnseignant.setEmail(enseignant.getEmail());
                }

                return existingEnseignant;
            })
            .map(enseignantRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Enseignant> findAll(Pageable pageable) {
        log.debug("Request to get all Enseignants");
        return enseignantRepository.findAll(pageable);
    }

    public Page<Enseignant> findAllWithEagerRelationships(Pageable pageable) {
        return enseignantRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enseignant> findOne(Long id) {
        log.debug("Request to get Enseignant : {}", id);
        return enseignantRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enseignant : {}", id);
        enseignantRepository.deleteById(id);
    }
}
