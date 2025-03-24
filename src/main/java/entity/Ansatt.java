package main.java.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema = "yourusername_oblig3", name = "ansatt")
public class Ansatt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String brukernavn;
    private String fornavn;
    private String etternavn;
    private LocalDate ansettelsesdato;
    private String stilling;
    private BigDecimal maanedslonn;
    
    @ManyToOne
    @JoinColumn(name = "avdeling_id")
    private Avdeling avdeling;
    
    @OneToMany(mappedBy = "ansatt")
    private List<ProsjektDeltakelse> prosjektdeltagelser;
    
    public Ansatt() {
    }
    
    public Ansatt(String brukernavn, String fornavn, String etternavn, 
                 LocalDate ansettelsesdato, String stilling, BigDecimal maanedslonn) {
        this.brukernavn = brukernavn;
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.ansettelsesdato = ansettelsesdato;
        this.stilling = stilling;
        this.maanedslonn = maanedslonn;
    }
    
    public void skrivUt() {
        System.out.printf("Ansatt: id=%d, brukernavn=%s, navn=%s %s, stilling=%s, lønn=%.2f, " +
                "ansatt siden=%s, avdeling=%s%n", 
                id, brukernavn, fornavn, etternavn, stilling, maanedslonn, 
                ansettelsesdato, avdeling != null ? avdeling.getNavn() : "Ingen");
    }
    
    public void skrivUtMedProsjekter() {
        skrivUt();
        if (prosjektdeltagelser != null && !prosjektdeltagelser.isEmpty()) {
            System.out.println("Deltar i følgende prosjekter:");
            for (ProsjektDeltakelse pd : prosjektdeltagelser) {
                System.out.printf("   %s, rolle=%s, timer=%d%n", 
                        pd.getProsjekt().getNavn(), pd.getRolle(), pd.getTimer());
            }
        } else {
            System.out.println("Deltar ikke i noen prosjekter.");
        }
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrukernavn() {
        return brukernavn;
    }

    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public LocalDate getAnsettelsesdato() {
        return ansettelsesdato;
    }

    public void setAnsettelsesdato(LocalDate ansettelsesdato) {
        this.ansettelsesdato = ansettelsesdato;
    }

    public String getStilling() {
        return stilling;
    }

    public void setStilling(String stilling) {
        this.stilling = stilling;
    }

    public BigDecimal getMaanedslonn() {
        return maanedslonn;
    }

    public void setMaanedslonn(BigDecimal maanedslonn) {
        this.maanedslonn = maanedslonn;
    }

    public Avdeling getAvdeling() {
        return avdeling;
    }

    public void setAvdeling(Avdeling avdeling) {
        this.avdeling = avdeling;
    }

    public List<ProsjektDeltakelse> getProsjektdeltagelser() {
        return prosjektdeltagelser;
    }

    public void setProsjektdeltagelser(List<ProsjektDeltakelse> prosjektdeltagelser) {
        this.prosjektdeltagelser = prosjektdeltagelser;
    }
}