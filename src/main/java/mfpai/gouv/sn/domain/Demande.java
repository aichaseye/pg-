package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import mfpai.gouv.sn.domain.enumeration.Motif;
import mfpai.gouv.sn.domain.enumeration.NomEtab;
import mfpai.gouv.sn.domain.enumeration.TypeDemandeur;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Demande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "motif", nullable = false)
    private Motif motif;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_demandeur", nullable = false)
    private TypeDemandeur typeDemandeur;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(name = "nom_etab")
    private NomEtab nomEtab;

    @Column(name = "autre_nom_etab")
    private String autreNomEtab;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandes", "bFPA", "etablissement" }, allowSetters = true)
    private Enseignant enseignant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "demandes", "chefEtablissement", "etablissement" }, allowSetters = true)
    private Apprenant apprenant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "apprenants", "enseignants", "matieres", "demandes", "bFPA" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Demande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Motif getMotif() {
        return this.motif;
    }

    public Demande motif(Motif motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(Motif motif) {
        this.motif = motif;
    }

    public TypeDemandeur getTypeDemandeur() {
        return this.typeDemandeur;
    }

    public Demande typeDemandeur(TypeDemandeur typeDemandeur) {
        this.setTypeDemandeur(typeDemandeur);
        return this;
    }

    public void setTypeDemandeur(TypeDemandeur typeDemandeur) {
        this.typeDemandeur = typeDemandeur;
    }

    public String getNom() {
        return this.nom;
    }

    public Demande nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Demande prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public NomEtab getNomEtab() {
        return this.nomEtab;
    }

    public Demande nomEtab(NomEtab nomEtab) {
        this.setNomEtab(nomEtab);
        return this;
    }

    public void setNomEtab(NomEtab nomEtab) {
        this.nomEtab = nomEtab;
    }

    public String getAutreNomEtab() {
        return this.autreNomEtab;
    }

    public Demande autreNomEtab(String autreNomEtab) {
        this.setAutreNomEtab(autreNomEtab);
        return this;
    }

    public void setAutreNomEtab(String autreNomEtab) {
        this.autreNomEtab = autreNomEtab;
    }

    public String getEmail() {
        return this.email;
    }

    public Demande email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Enseignant getEnseignant() {
        return this.enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Demande enseignant(Enseignant enseignant) {
        this.setEnseignant(enseignant);
        return this;
    }

    public Apprenant getApprenant() {
        return this.apprenant;
    }

    public void setApprenant(Apprenant apprenant) {
        this.apprenant = apprenant;
    }

    public Demande apprenant(Apprenant apprenant) {
        this.setApprenant(apprenant);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Demande etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Demande)) {
            return false;
        }
        return id != null && id.equals(((Demande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Demande{" +
            "id=" + getId() +
            ", motif='" + getMotif() + "'" +
            ", typeDemandeur='" + getTypeDemandeur() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nomEtab='" + getNomEtab() + "'" +
            ", autreNomEtab='" + getAutreNomEtab() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
