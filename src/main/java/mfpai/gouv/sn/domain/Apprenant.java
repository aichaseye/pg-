package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import mfpai.gouv.sn.domain.enumeration.Sexe;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Apprenant.
 */
@Entity
@Table(name = "apprenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Apprenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule_app", unique = true)
    private String matriculeApp;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "apprenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enseignant", "apprenant", "etablissement" }, allowSetters = true)
    private Set<Demande> demandes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "apprenants" }, allowSetters = true)
    private ChefEtablissement chefEtablissement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "apprenants", "enseignants", "matieres", "demandes", "bFPA" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Apprenant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeApp() {
        return this.matriculeApp;
    }

    public Apprenant matriculeApp(String matriculeApp) {
        this.setMatriculeApp(matriculeApp);
        return this;
    }

    public void setMatriculeApp(String matriculeApp) {
        this.matriculeApp = matriculeApp;
    }

    public String getNom() {
        return this.nom;
    }

    public Apprenant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Apprenant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Apprenant sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Apprenant telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public Apprenant email(String email) {
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
            this.demandes.forEach(i -> i.setApprenant(null));
        }
        if (demandes != null) {
            demandes.forEach(i -> i.setApprenant(this));
        }
        this.demandes = demandes;
    }

    public Apprenant demandes(Set<Demande> demandes) {
        this.setDemandes(demandes);
        return this;
    }

    public Apprenant addDemande(Demande demande) {
        this.demandes.add(demande);
        demande.setApprenant(this);
        return this;
    }

    public Apprenant removeDemande(Demande demande) {
        this.demandes.remove(demande);
        demande.setApprenant(null);
        return this;
    }

    public ChefEtablissement getChefEtablissement() {
        return this.chefEtablissement;
    }

    public void setChefEtablissement(ChefEtablissement chefEtablissement) {
        this.chefEtablissement = chefEtablissement;
    }

    public Apprenant chefEtablissement(ChefEtablissement chefEtablissement) {
        this.setChefEtablissement(chefEtablissement);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Apprenant etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Apprenant)) {
            return false;
        }
        return id != null && id.equals(((Apprenant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Apprenant{" +
            "id=" + getId() +
            ", matriculeApp='" + getMatriculeApp() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
