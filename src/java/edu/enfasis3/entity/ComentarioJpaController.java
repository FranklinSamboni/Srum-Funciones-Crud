/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.entity;

import edu.enfasis3.entity.exceptions.NonexistentEntityException;
import edu.enfasis3.entity.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Frank
 */
public class ComentarioJpaController implements Serializable {

    public ComentarioJpaController(EntityManagerFactory emf) {
        this.utx = null;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comentario comentario) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
         //   utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad idactividad = comentario.getIdactividad();
            if (idactividad != null) {
                idactividad = em.getReference(idactividad.getClass(), idactividad.getIdactividad());
                comentario.setIdactividad(idactividad);
            }
            em.persist(comentario);
            if (idactividad != null) {
                idactividad.getComentarioList().add(comentario);
                idactividad = em.merge(idactividad);
            }
          //  utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
               // utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comentario comentario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
         //   utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Comentario persistentComentario = em.find(Comentario.class, comentario.getIdcomentario());
            Actividad idactividadOld = persistentComentario.getIdactividad();
            Actividad idactividadNew = comentario.getIdactividad();
            if (idactividadNew != null) {
                idactividadNew = em.getReference(idactividadNew.getClass(), idactividadNew.getIdactividad());
                comentario.setIdactividad(idactividadNew);
            }
            comentario = em.merge(comentario);
            if (idactividadOld != null && !idactividadOld.equals(idactividadNew)) {
                idactividadOld.getComentarioList().remove(comentario);
                idactividadOld = em.merge(idactividadOld);
            }
            if (idactividadNew != null && !idactividadNew.equals(idactividadOld)) {
                idactividadNew.getComentarioList().add(comentario);
                idactividadNew = em.merge(idactividadNew);
            }
          //  utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
              //  utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comentario.getIdcomentario();
                if (findComentario(id) == null) {
                    throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
           // utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            
            Comentario comentario;
            try {
                comentario = em.getReference(Comentario.class, id);
                comentario.getIdcomentario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comentario with id " + id + " no longer exists.", enfe);
            }
            Actividad idactividad = comentario.getIdactividad();
            if (idactividad != null) {
                idactividad.getComentarioList().remove(comentario);
                idactividad = em.merge(idactividad);
            }
            em.remove(comentario);
         //   utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
               // utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comentario> findComentarioEntities() {
        return findComentarioEntities(true, -1, -1);
    }

    public List<Comentario> findComentarioEntities(int maxResults, int firstResult) {
        return findComentarioEntities(false, maxResults, firstResult);
    }

    private List<Comentario> findComentarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comentario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Comentario findComentario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comentario.class, id);
        } finally {
            em.close();
        }
    }

    public int getComentarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comentario> rt = cq.from(Comentario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
