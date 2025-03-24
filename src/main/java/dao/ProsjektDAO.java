package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import entity.Ansatt;
import entity.Prosjekt;
import entity.ProsjektDeltakelse;

public class ProsjektDAO {

    private EntityManagerFactory emf;
    
    public ProsjektDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public Prosjekt finnProsjektMedId(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Prosjekt.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Prosjekt> finnAlleProsjekter() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Prosjekt> query = em.createQuery("SELECT p FROM Prosjekt p ORDER BY p.id", Prosjekt.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public void lagreNyttProsjekt(Prosjekt prosjekt) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(prosjekt);
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
    
    public void registrerProsjektdeltakelse(Ansatt ansatt, Prosjekt prosjekt, String rolle) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Hent oppdaterte entiteter
            ansatt = em.merge(ansatt);
            prosjekt = em.merge(prosjekt);
            
            // Opprett ny deltagelse
            ProsjektDeltakelse pd = new ProsjektDeltakelse(ansatt, prosjekt, rolle);
            em.persist(pd);
            
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
    
    public void registrerTimer(int ansattId, int prosjektId, int timer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            // Finn prosjektdeltakelsen
            TypedQuery<ProsjektDeltakelse> query = em.createQuery(
                    "SELECT pd FROM ProsjektDeltakelse pd " +
                    "WHERE pd.ansatt.id = :ansattId AND pd.prosjekt.id = :prosjektId", 
                    ProsjektDeltakelse.class);
            query.setParameter("ansattId", ansattId);
            query.setParameter("prosjektId", prosjektId);
            
            ProsjektDeltakelse pd = query.getSingleResult();
            pd.leggTilTimer(timer);
            
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
    
    public ProsjektDeltakelse finnProsjektdeltakelse(int ansattId, int prosjektId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProsjektDeltakelse> query = em.createQuery(
                    "SELECT pd FROM ProsjektDeltakelse pd " +
                    "WHERE pd.ansatt.id = :ansattId AND pd.prosjekt.id = :prosjektId", 
                    ProsjektDeltakelse.class);
            query.setParameter("ansattId", ansattId);
            query.setParameter("prosjektId", prosjektId);
            
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public int finnTotaleTimerForProsjekt(int prosjektId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                    "SELECT SUM(pd.timer) FROM ProsjektDeltakelse pd WHERE pd.prosjekt.id = :prosjektId", 
                    Integer.class);
            query.setParameter("prosjektId", prosjektId);
            
            Integer sum = query.getSingleResult();
            return sum != null ? sum : 0;
        } finally {
            em.close();
        }
    }
}