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
 * A Referant.
 */
@Entity
@Table(name = "referant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Referant implements Serializable {

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

    @OneToMany(mappedBy = "referant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "referant", "comptableMatiere", "etablissement" }, allowSetters = true)
    private Set<Matiere> matieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Referant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPrenom() {
        return this.nomPrenom;
    }

    public Referant nomPrenom(String nomPrenom) {
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

    public Referant user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Matiere> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matiere> matieres) {
        if (this.matieres != null) {
            this.matieres.forEach(i -> i.setReferant(null));
        }
        if (matieres != null) {
            matieres.forEach(i -> i.setReferant(this));
        }
        this.matieres = matieres;
    }

    public Referant matieres(Set<Matiere> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public Referant addMatiere(Matiere matiere) {
        this.matieres.add(matiere);
        matiere.setReferant(this);
        return this;
    }

    public Referant removeMatiere(Matiere matiere) {
        this.matieres.remove(matiere);
        matiere.setReferant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Referant)) {
            return false;
        }
        return id != null && id.equals(((Referant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Referant{" +
            "id=" + getId() +
            ", nomPrenom='" + getNomPrenom() + "'" +
            "}";
    }
}
