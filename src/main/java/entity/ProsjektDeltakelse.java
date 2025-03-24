package main.java.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "h591443_oblig3", name = "prosjektdeltakelse")
public class ProsjektDeltakelse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "ansatt_id")
    private Ansatt ansatt;
    
    @ManyToOne
    @JoinColumn(name = "prosjekt_id")
    private Prosjekt prosjekt;
    
    private String rolle;
    private Integer timer;
    
    public ProsjektDeltakelse() {
    }
    
    public ProsjektDeltakelse(Ansatt ansatt, Prosjekt prosjekt, String rolle) {
        this.ansatt = ansatt;
        this.prosjekt = prosjekt;
        this.rolle = rolle;
        this.timer = 0;
    }
    
    public void skrivUt() {
        System.out.printf("ProsjektDeltakelse: id=%d, ansatt=%s %s, prosjekt=%s, rolle=%s, timer=%d%n",
                id, ansatt.getFornavn(), ansatt.getEtternavn(), 
                prosjekt.getNavn(), rolle, timer);
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ansatt getAnsatt() {
        return ansatt;
    }

    public void setAnsatt(Ansatt ansatt) {
        this.ansatt = ansatt;
    }

    public Prosjekt getProsjekt() {
        return prosjekt;
    }

    public void setProsjekt(Prosjekt prosjekt) {
        this.prosjekt = prosjekt;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }
    
    public void leggTilTimer(int antall) {
        this.timer += antall;
    }
}