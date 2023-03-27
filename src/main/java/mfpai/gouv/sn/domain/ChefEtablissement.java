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
 * A ChefEtablissement.
 */
@Entity
@Table(name = "chef_etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChefEtablissement implements Serializable {

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

    @OneToMany(mappedBy = "chefEtablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demandes", "chefEtablissement", "etablissement" }, allowSetters = true)
    private Set<Apprenant> apprenants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChefEtablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPrenom() {
        return this.nomPrenom;
    }

    public ChefEtablissement nomPrenom(String nomPrenom) {
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

    public ChefEtablissement user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Apprenant> getApprenants() {
        return this.apprenants;
    }

    public void setApprenants(Set<Apprenant> apprenants) {
        if (this.apprenants != null) {
            this.apprenants.forEach(i -> i.setChefEtablissement(null));
        }
        if (apprenants != null) {
            apprenants.forEach(i -> i.setChefEtablissement(this));
        }
        this.apprenants = apprenants;
    }

    public ChefEtablissement apprenants(Set<Apprenant> apprenants) {
        this.setApprenants(apprenants);
        return this;
    }

    public ChefEtablissement addApprenant(Apprenant apprenant) {
        this.apprenants.add(apprenant);
        apprenant.setChefEtablissement(this);
        return this;
    }

    public ChefEtablissement removeApprenant(Apprenant apprenant) {
        this.apprenants.remove(apprenant);
        apprenant.setChefEtablissement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChefEtablissement)) {
            return false;
        }
        return id != null && id.equals(((ChefEtablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChefEtablissement{" +
            "id=" + getId() +
            ", nomPrenom='" + getNomPrenom() + "'" +
            "}";
    }
}
