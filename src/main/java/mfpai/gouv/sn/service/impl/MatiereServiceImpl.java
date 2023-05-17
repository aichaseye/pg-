package mfpai.gouv.sn.service.impl;

import java.util.Optional;
import mfpai.gouv.sn.domain.Matiere;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.TypeStructure;
import mfpai.gouv.sn.repository.MatiereRepository;
import mfpai.gouv.sn.service.MatiereService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Matiere}.
 */
@Service
@Transactional
public class MatiereServiceImpl implements MatiereService {

    private final Logger log = LoggerFactory.getLogger(MatiereServiceImpl.class);

    private final MatiereRepository matiereRepository;

    public MatiereServiceImpl(MatiereRepository matiereRepository) {
        this.matiereRepository = matiereRepository;
    }

    @Override
    public Matiere save(Matiere matiere) {
        log.debug("Request to save Matiere : {}", matiere);

        // le code de l'IA
        String codeIA = "";
        if (matiere.getCodeIA() == CodeIA.Autres) {
            codeIA = matiere.getAutreCodeIA().substring(1, 3);
        } else if (matiere.getCodeIA() == CodeIA.Autres && matiere.getAutreCodeIA() != null) {
            codeIA = String.valueOf(matiere.getCodeIA()).substring(1, 3);
        } else {
            codeIA = String.valueOf(matiere.getCodeIA()).substring(1, 3);
        }
        // ordre
        Long last_insert = matiereRepository.findOneByIdDesc();
        if (last_insert == null) {
            last_insert = 1L;
        }

        String order = getOrder(String.valueOf(last_insert));
        String matricule = codeIA + order + String.valueOf(matiere.getAnneeAffectation()).substring(2) + "FTP";
        TypeStructure structure;
        if (matiere.getTypeStructure() == TypeStructure.Autre) {
            structure = matiere.getTypeStructure();
        } else if (matiere.getTypeStructure() == TypeStructure.Autre && matiere.getAutreStructure() != null) {
            structure = matiere.getTypeStructure();
        } else {
            structure = matiere.getTypeStructure();
        }

        matricule = matricule + "/" + structure;

        matiere.setMatriculeMatiere(matricule);
        return matiereRepository.save(matiere);
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
        return matiereRepository.findOneByIdDesc();
    }

    @Override
    public Optional<Matiere> partialUpdate(Matiere matiere) {
        log.debug("Request to partially update Matiere : {}", matiere);

        return matiereRepository
            .findById(matiere.getId())
            .map(existingMatiere -> {
                if (matiere.getMatriculeMatiere() != null) {
                    existingMatiere.setMatriculeMatiere(matiere.getMatriculeMatiere());
                }
                if (matiere.getNomMatiere() != null) {
                    existingMatiere.setNomMatiere(matiere.getNomMatiere());
                }
                if (matiere.getReference() != null) {
                    existingMatiere.setReference(matiere.getReference());
                }
                if (matiere.getImage() != null) {
                    existingMatiere.setImage(matiere.getImage());
                }
                if (matiere.getImageContentType() != null) {
                    existingMatiere.setImageContentType(matiere.getImageContentType());
                }
                if (matiere.getRegion() != null) {
                    existingMatiere.setRegion(matiere.getRegion());
                }
                if (matiere.getAutreRegion() != null) {
                    existingMatiere.setAutreRegion(matiere.getAutreRegion());
                }
                if (matiere.getDepartement() != null) {
                    existingMatiere.setDepartement(matiere.getDepartement());
                }
                if (matiere.getAutreDep() != null) {
                    existingMatiere.setAutreDep(matiere.getAutreDep());
                }
                if (matiere.getCodeIA() != null) {
                    existingMatiere.setCodeIA(matiere.getCodeIA());
                }
                if (matiere.getAutreCodeIA() != null) {
                    existingMatiere.setAutreCodeIA(matiere.getAutreCodeIA());
                }
                if (matiere.getTypeStructure() != null) {
                    existingMatiere.setTypeStructure(matiere.getTypeStructure());
                }
                if (matiere.getAutreStructure() != null) {
                    existingMatiere.setAutreStructure(matiere.getAutreStructure());
                }
                if (matiere.getAnneeAffectation() != null) {
                    existingMatiere.setAnneeAffectation(matiere.getAnneeAffectation());
                }

                return existingMatiere;
            })
            .map(matiereRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Matiere> findAll(Pageable pageable) {
        log.debug("Request to get all Matieres");
        return matiereRepository.findAll(pageable);
    }

    public Page<Matiere> findAllWithEagerRelationships(Pageable pageable) {
        return matiereRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Matiere> findOne(Long id) {
        log.debug("Request to get Matiere : {}", id);
        return matiereRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
    }
}
