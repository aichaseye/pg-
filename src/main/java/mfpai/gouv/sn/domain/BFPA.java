package mfpai.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BFPA.
 */
@Entity
@Table(name = "bfpa")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BFPA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_prenom", nullable = false)
    private String nomPrenom;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "bFPA")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "apprenants", "enseignants", "matieres", "demandes", "bFPA" }, allowSetters = true)
    private Set<Etablissement> etablissements = new HashSet<>();

    @OneToMany(mappedBy = "bFPA")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandes", "bFPA", "etablissement" }, allowSetters = true)
    private Set<Enseignant> enseignants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BFPA id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPrenom() {
        return this.nomPrenom;
    }

    public BFPA nomPrenom(String nomPrenom) {
        this.setNomPrenom(nomPrenom);
        return this;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BFPA user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.setBFPA(null));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.setBFPA(this));
        }
        this.etablissements = etablissements;
    }

    public BFPA etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public BFPA addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.setBFPA(this);
        return this;
    }

    public BFPA removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.setBFPA(null);
        return this;
    }

    public Set<Enseignant> getEnseignants() {
        return this.enseignants;
    }

    public void setEnseignants(Set<Enseignant> enseignants) {
        if (this.enseignants != null) {
            this.enseignants.forEach(i -> i.setBFPA(null));
        }
        if (enseignants != null) {
            enseignants.forEach(i -> i.setBFPA(this));
        }
        this.enseignants = enseignants;
    }

    public BFPA enseignants(Set<Enseignant> enseignants) {
        this.setEnseignants(enseignants);
        return this;
    }

    public BFPA addEnseignant(Enseignant enseignant) {
        this.enseignants.add(enseignant);
        enseignant.setBFPA(this);
        return this;
    }

    public BFPA removeEnseignant(Enseignant enseignant) {
        this.enseignants.remove(enseignant);
        enseignant.setBFPA(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BFPA)) {
            return false;
        }
        return id != null && id.equals(((BFPA) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BFPA{" +
            "id=" + getId() +
            ", nomPrenom='" + getNomPrenom() + "'" +
            "}";
    }
}
