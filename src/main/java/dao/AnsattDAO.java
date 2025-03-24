package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import entity.Ansatt;
import entity.Avdeling;

public class AnsattDAO {

    private EntityManagerFactory emf;
    
    public AnsattDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Ansatt finnAnsattMedId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Ansatt.class, id);
        } finally {
            em.close();
        }
    }
    
    public Ansatt finnAnsattMedBrukernavn(String brukernavn) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ansatt> query = em.createQuery(
                    "SELECT a FROM Ansatt a WHERE a.brukernavn = :brukernavn", Ansatt.class);
            query.setParameter("brukernavn", brukernavn);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Ansatt> finnAlleAnsatte() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ansatt> query = em.createQuery("SELECT a FROM Ansatt a ORDER BY a.id", Ansatt.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void lagreNyAnsatt(Ansatt ansatt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ansatt);
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
    
    public void oppdaterAnsatt(Ansatt ansatt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(ansatt);
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
    
    public void oppdaterStillingOgLonn(int ansattId, String nyStilling, Double nyLonn) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            Ansatt ansatt = em.find(Ansatt.class, ansattId);
            if (ansatt != null) {
                if (nyStilling != null && !nyStilling.isBlank()) {
                    ansatt.setStilling(nyStilling);
                }
                if (nyLonn != null) {
                    ansatt.setMaanedslonn(ansatt.getMaanedslonn().add(java.math.BigDecimal.valueOf(nyLonn)));
                }
            }
            
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
    
    public void oppdaterAvdeling(int ansattId, int nyAvdelingId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            Ansatt ansatt = em.find(Ansatt.class, ansattId);
            Avdeling nyAvdeling = em.find(Avdeling.class, nyAvdelingId);
            
            // Sjekk om ansatt er sjef i sin nåværende avdeling
            if (ansatt != null && nyAvdeling != null) {
                Avdeling gammelAvdeling = ansatt.getAvdeling();
                if (gammelAvdeling != null && gammelAvdeling.getSjef() != null 
                        && gammelAvdeling.getSjef().getId().equals(ansatt.getId())) {
                    throw new IllegalStateException("Kan ikke flytte en ansatt som er sjef!");
                }
                
                ansatt.setAvdeling(nyAvdeling);
            }
            
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
}