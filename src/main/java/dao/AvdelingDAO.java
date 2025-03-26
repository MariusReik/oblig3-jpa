package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import entity.Ansatt;
import entity.Avdeling;

public class AvdelingDAO {

    private EntityManagerFactory emf;
    
    public AvdelingDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Avdeling finnAvdelingMedId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Avdeling> query = em.createQuery(
                "SELECT a FROM Avdeling a LEFT JOIN FETCH a.ansatte LEFT JOIN FETCH a.sjef WHERE a.id = :id",
                Avdeling.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Avdeling> finnAlleAvdelinger() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Avdeling> query = em.createQuery("SELECT a FROM Avdeling a ORDER BY a.id", Avdeling.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void lagreNyAvdeling(Avdeling avdeling, Ansatt sjef) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Opprett avdelingen først
            em.persist(avdeling);
            
            // Oppdater sjefen med den nye avdelingen
            sjef = em.merge(sjef); // Hent oppdatert versjon av sjefen
            Avdeling gammelAvdeling = sjef.getAvdeling();
            sjef.setAvdeling(avdeling);
            
            // Sett sjefen på den nye avdelingen
            avdeling.setSjef(sjef);
            
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public List<Ansatt> finnAnsattePaaAvdeling(int avdelingId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ansatt> query = em.createQuery(
                    "SELECT a FROM Ansatt a LEFT JOIN FETCH a.prosjektdeltagelser WHERE a.avdeling.id = :avdelingId", 
                    Ansatt.class);
            query.setParameter("avdelingId", avdelingId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}