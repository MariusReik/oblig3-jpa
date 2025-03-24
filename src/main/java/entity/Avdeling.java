package main.java.entity;

    
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "yourusername_oblig3", name = "avdeling")
public class Avdeling {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String navn;
    
    @OneToOne
    @JoinColumn(name = "sjef_id")
    private Ansatt sjef;
    
    @OneToMany(mappedBy = "avdeling")
    private List<Ansatt> ansatte;
    
    public Avdeling() {
    }
    
    public Avdeling(String navn) {
        this.navn = navn;
    }
    
    public void skrivUt() {
        System.out.printf("Avdeling: id=%d, navn=%s, sjef=%s %s%n", 
                id, navn, 
                sjef != null ? sjef.getFornavn() : "Ingen", 
                sjef != null ? sjef.getEtternavn() : "");
    }
    
    public void skrivUtMedAnsatte() {
        skrivUt();
        System.out.println("Ansatte i avdelingen:");
        if (ansatte != null) {
            for (Ansatt a : ansatte) {
                System.out.printf("   %s %s, %s, %s%n", 
                        a.getFornavn(), a.getEtternavn(), a.getStilling(),
                        a.getId().equals(sjef.getId()) ? "Avdelingssjef" : "");
            }
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

    public Ansatt getSjef() {
        return sjef;
    }

    public void setSjef(Ansatt sjef) {
        this.sjef = sjef;
    }

    public List<Ansatt> getAnsatte() {
        return ansatte;
    }

    public void setAnsatte(List<Ansatt> ansatte) {
        this.ansatte = ansatte;
    }
}