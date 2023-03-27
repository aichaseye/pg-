package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.NomDep;
import mfpai.gouv.sn.domain.enumeration.NomReg;
import mfpai.gouv.sn.domain.enumeration.TypeStructure;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule_matiere", unique = true)
    private String matriculeMatiere;

    @Column(name = "nom_matiere")
    private String nomMatiere;

    @Column(name = "reference")
    private String reference;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private NomReg region;

    @Column(name = "autre_region")
    private String autreRegion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "departement", nullable = false)
    private NomDep departement;

    @Column(name = "autre_dep")
    private String autreDep;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "code_ia", nullable = false)
    private CodeIA codeIA;

    @Column(name = "autre_code_ia")
    private String autreCodeIA;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_structure", nullable = false)
    private TypeStructure typeStructure;

    @Column(name = "autre_structure")
    private String autreStructure;

    @NotNull
    @Column(name = "annee_affectation", nullable = false)
    private Integer anneeAffectation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "matieres" }, allowSetters = true)
    private Referant referant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "matieres" }, allowSetters = true)
    private ComptableMatiere comptableMatiere;

    @ManyToOne
    @JsonIgnoreProperties(value = { "apprenants", "enseignants", "matieres", "demandes", "bFPA" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeMatiere() {
        return this.matriculeMatiere;
    }

    public Matiere matriculeMatiere(String matriculeMatiere) {
        this.setMatriculeMatiere(matriculeMatiere);
        return this;
    }

    public void setMatriculeMatiere(String matriculeMatiere) {
        this.matriculeMatiere = matriculeMatiere;
    }

    public String getNomMatiere() {
        return this.nomMatiere;
    }

    public Matiere nomMatiere(String nomMatiere) {
        this.setNomMatiere(nomMatiere);
        return this;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public String getReference() {
        return this.reference;
    }

    public Matiere reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Matiere image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Matiere imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public NomReg getRegion() {
        return this.region;
    }

    public Matiere region(NomReg region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(NomReg region) {
        this.region = region;
    }

    public String getAutreRegion() {
        return this.autreRegion;
    }

    public Matiere autreRegion(String autreRegion) {
        this.setAutreRegion(autreRegion);
        return this;
    }

    public void setAutreRegion(String autreRegion) {
        this.autreRegion = autreRegion;
    }

    public NomDep getDepartement() {
        return this.departement;
    }

    public Matiere departement(NomDep departement) {
        this.setDepartement(departement);
        return this;
    }

    public void setDepartement(NomDep departement) {
        this.departement = departement;
    }

    public String getAutreDep() {
        return this.autreDep;
    }

    public Matiere autreDep(String autreDep) {
        this.setAutreDep(autreDep);
        return this;
    }

    public void setAutreDep(String autreDep) {
        this.autreDep = autreDep;
    }

    public CodeIA getCodeIA() {
        return this.codeIA;
    }

    public Matiere codeIA(CodeIA codeIA) {
        this.setCodeIA(codeIA);
        return this;
    }

    public void setCodeIA(CodeIA codeIA) {
        this.codeIA = codeIA;
    }

    public String getAutreCodeIA() {
        return this.autreCodeIA;
    }

    public Matiere autreCodeIA(String autreCodeIA) {
        this.setAutreCodeIA(autreCodeIA);
        return this;
    }

    public void setAutreCodeIA(String autreCodeIA) {
        this.autreCodeIA = autreCodeIA;
    }

    public TypeStructure getTypeStructure() {
        return this.typeStructure;
    }

    public Matiere typeStructure(TypeStructure typeStructure) {
        this.setTypeStructure(typeStructure);
        return this;
    }

    public void setTypeStructure(TypeStructure typeStructure) {
        this.typeStructure = typeStructure;
    }

    public String getAutreStructure() {
        return this.autreStructure;
    }

    public Matiere autreStructure(String autreStructure) {
        this.setAutreStructure(autreStructure);
        return this;
    }

    public void setAutreStructure(String autreStructure) {
        this.autreStructure = autreStructure;
    }

    public Integer getAnneeAffectation() {
        return this.anneeAffectation;
    }

    public Matiere anneeAffectation(Integer anneeAffectation) {
        this.setAnneeAffectation(anneeAffectation);
        return this;
    }

    public void setAnneeAffectation(Integer anneeAffectation) {
        this.anneeAffectation = anneeAffectation;
    }

    public Referant getReferant() {
        return this.referant;
    }

    public void setReferant(Referant referant) {
        this.referant = referant;
    }

    public Matiere referant(Referant referant) {
        this.setReferant(referant);
        return this;
    }

    public ComptableMatiere getComptableMatiere() {
        return this.comptableMatiere;
    }

    public void setComptableMatiere(ComptableMatiere comptableMatiere) {
        this.comptableMatiere = comptableMatiere;
    }

    public Matiere comptableMatiere(ComptableMatiere comptableMatiere) {
        this.setComptableMatiere(comptableMatiere);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Matiere etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return id != null && id.equals(((Matiere) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", matriculeMatiere='" + getMatriculeMatiere() + "'" +
            ", nomMatiere='" + getNomMatiere() + "'" +
            ", reference='" + getReference() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", region='" + getRegion() + "'" +
            ", autreRegion='" + getAutreRegion() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", autreDep='" + getAutreDep() + "'" +
            ", codeIA='" + getCodeIA() + "'" +
            ", autreCodeIA='" + getAutreCodeIA() + "'" +
            ", typeStructure='" + getTypeStructure() + "'" +
            ", autreStructure='" + getAutreStructure() + "'" +
            ", anneeAffectation=" + getAnneeAffectation() +
            "}";
    }
}
