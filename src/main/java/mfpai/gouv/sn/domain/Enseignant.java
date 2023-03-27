package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import mfpai.gouv.sn.domain.enumeration.CodeIA;
import mfpai.gouv.sn.domain.enumeration.NomDep;
import mfpai.gouv.sn.domain.enumeration.NomReg;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Enseignant.
 */
@Entity
@Table(name = "enseignant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule_ens", unique = true)
    private String matriculeEns;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "num_cni", nullable = false, unique = true)
    private Long numCNI;

    @NotNull
    @Column(name = "annee_dentree", nullable = false)
    private Integer anneeDentree;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private NomReg region;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "departement", nullable = false)
    private NomDep departement;

    @Column(name = "autre_dep")
    private String autreDep;

    @Column(name = "autre_region")
    private String autreRegion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "code_ia", nullable = false)
    private CodeIA codeIA;

    @Column(name = "autre_code_ia")
    private String autreCodeIA;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "enseignant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enseignant", "apprenant", "etablissement" }, allowSetters = true)
    private Set<Demande> demandes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "etablissements", "enseignants" }, allowSetters = true)
    private BFPA bFPA;

    @ManyToOne
    @JsonIgnoreProperties(value = { "apprenants", "enseignants", "matieres", "demandes", "bFPA" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enseignant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeEns() {
        return this.matriculeEns;
    }

    public Enseignant matriculeEns(String matriculeEns) {
        this.setMatriculeEns(matriculeEns);
        return this;
    }

    public void setMatriculeEns(String matriculeEns) {
        this.matriculeEns = matriculeEns;
    }

    public String getNom() {
        return this.nom;
    }

    public Enseignant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Enseignant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Long getNumCNI() {
        return this.numCNI;
    }

    public Enseignant numCNI(Long numCNI) {
        this.setNumCNI(numCNI);
        return this;
    }

    public void setNumCNI(Long numCNI) {
        this.numCNI = numCNI;
    }

    public Integer getAnneeDentree() {
        return this.anneeDentree;
    }

    public Enseignant anneeDentree(Integer anneeDentree) {
        this.setAnneeDentree(anneeDentree);
        return this;
    }

    public void setAnneeDentree(Integer anneeDentree) {
        this.anneeDentree = anneeDentree;
    }

    public NomReg getRegion() {
        return this.region;
    }

    public Enseignant region(NomReg region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(NomReg region) {
        this.region = region;
    }

    public NomDep getDepartement() {
        return this.departement;
    }

    public Enseignant departement(NomDep departement) {
        this.setDepartement(departement);
        return this;
    }

    public void setDepartement(NomDep departement) {
        this.departement = departement;
    }

    public String getAutreDep() {
        return this.autreDep;
    }

    public Enseignant autreDep(String autreDep) {
        this.setAutreDep(autreDep);
        return this;
    }

    public void setAutreDep(String autreDep) {
        this.autreDep = autreDep;
    }

    public String getAutreRegion() {
        return this.autreRegion;
    }

    public Enseignant autreRegion(String autreRegion) {
        this.setAutreRegion(autreRegion);
        return this;
    }

    public void setAutreRegion(String autreRegion) {
        this.autreRegion = autreRegion;
    }

    public CodeIA getCodeIA() {
        return this.codeIA;
    }

    public Enseignant codeIA(CodeIA codeIA) {
        this.setCodeIA(codeIA);
        return this;
    }

    public void setCodeIA(CodeIA codeIA) {
        this.codeIA = codeIA;
    }

    public String getAutreCodeIA() {
        return this.autreCodeIA;
    }

    public Enseignant autreCodeIA(String autreCodeIA) {
        this.setAutreCodeIA(autreCodeIA);
        return this;
    }

    public void setAutreCodeIA(String autreCodeIA) {
        this.autreCodeIA = autreCodeIA;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Enseignant sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getEmail() {
        return this.email;
    }

    public Enseignant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Demande> getDemandes() {
        return this.demandes;
    }

    public void setDemandes(Set<Demande> demandes) {
        if (this.demandes != null) {
            this.demandes.forEach(i -> i.setEnseignant(null));
        }
        if (demandes != null) {
            demandes.forEach(i -> i.setEnseignant(this));
        }
        this.demandes = demandes;
    }

    public Enseignant demandes(Set<Demande> demandes) {
        this.setDemandes(demandes);
        return this;
    }

    public Enseignant addDemande(Demande demande) {
        this.demandes.add(demande);
        demande.setEnseignant(this);
        return this;
    }

    public Enseignant removeDemande(Demande demande) {
        this.demandes.remove(demande);
        demande.setEnseignant(null);
        return this;
    }

    public BFPA getBFPA() {
        return this.bFPA;
    }

    public void setBFPA(BFPA bFPA) {
        this.bFPA = bFPA;
    }

    public Enseignant bFPA(BFPA bFPA) {
        this.setBFPA(bFPA);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Enseignant etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enseignant)) {
            return false;
        }
        return id != null && id.equals(((Enseignant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enseignant{" +
            "id=" + getId() +
            ", matriculeEns='" + getMatriculeEns() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", numCNI=" + getNumCNI() +
            ", anneeDentree=" + getAnneeDentree() +
            ", region='" + getRegion() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", autreDep='" + getAutreDep() + "'" +
            ", autreRegion='" + getAutreRegion() + "'" +
            ", codeIA='" + getCodeIA() + "'" +
            ", autreCodeIA='" + getAutreCodeIA() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
