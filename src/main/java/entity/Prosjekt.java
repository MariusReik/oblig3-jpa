package main.java.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema = "yourusername_oblig3", name = "prosjekt")
public class Prosjekt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String navn;
    private String beskrivelse;
    
    @OneToMany(mappedBy = "prosjekt")
    private List<ProsjektDeltakelse> deltagelser;
    
    public Prosjekt() {
    }
    
    public Prosjekt(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }
    
    public void skrivUt() {
        System.out.printf("Prosjekt: id=%d, navn=%s, beskrivelse=%s%n", 
                id, navn, beskrivelse);
    }
    
    public void skrivUtMedDeltagelser() {
        skrivUt();
        
        int totalTimer = 0;
        if (deltagelser != null && !deltagelser.isEmpty()) {
            System.out.println("Deltagere i prosjektet:");
            for (ProsjektDeltakelse pd : deltagelser) {
                System.out.printf("   %s %s, rolle=%s, timer=%d%n", 
                        pd.getAnsatt().getFornavn(), pd.getAnsatt().getEtternavn(),
                        pd.getRolle(), pd.getTimer());
                totalTimer += pd.getTimer();
            }
            System.out.println("Totalt antall timer: " + totalTimer);
        } else {
            System.out.println("Ingen deltagere i prosjektet.");
        }
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public List<ProsjektDeltakelse> getDeltagelser() {
        return deltagelser;
    }

    public void setDeltagelser(List<ProsjektDeltakelse> deltagelser) {
        this.deltagelser = deltagelser;
    }
}