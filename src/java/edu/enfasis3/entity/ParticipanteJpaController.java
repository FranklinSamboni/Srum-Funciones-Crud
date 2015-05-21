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
public class ParticipanteJpaController implements Serializable {

    public ParticipanteJpaController(EntityManagerFactory emf) {
        this.utx = null;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Participante participante) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            //utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto idproyecto = participante.getIdproyecto();
            if (idproyecto != null) {
                idproyecto = em.getReference(idproyecto.getClass(), idproyecto.getIdproyecto());
                participante.setIdproyecto(idproyecto);
            }
            User iduser = participante.getIduser();
            if (iduser != null) {
                iduser = em.getReference(iduser.getClass(), iduser.getIduser());
                participante.setIduser(iduser);
            }
            em.persist(participante);
            if (idproyecto != null) {
                idproyecto.getParticipanteList().add(participante);
                idproyecto = em.merge(idproyecto);
            }
            if (iduser != null) {
                iduser.getParticipanteList().add(participante);
                iduser = em.merge(iduser);
            }
           // utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
             //   utx.rollback();
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

    public void edit(Participante participante) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            //.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Participante persistentParticipante = em.find(Participante.class, participante.getIdparticipante());
            Proyecto idproyectoOld = persistentParticipante.getIdproyecto();
            Proyecto idproyectoNew = participante.getIdproyecto();
            User iduserOld = persistentParticipante.getIduser();
            User iduserNew = participante.getIduser();
            if (idproyectoNew != null) {
                idproyectoNew = em.getReference(idproyectoNew.getClass(), idproyectoNew.getIdproyecto());
                participante.setIdproyecto(idproyectoNew);
            }
            if (iduserNew != null) {
                iduserNew = em.getReference(iduserNew.getClass(), iduserNew.getIduser());
                participante.setIduser(iduserNew);
            }
            participante = em.merge(participante);
            if (idproyectoOld != null && !idproyectoOld.equals(idproyectoNew)) {
                idproyectoOld.getParticipanteList().remove(participante);
                idproyectoOld = em.merge(idproyectoOld);
            }
            if (idproyectoNew != null && !idproyectoNew.equals(idproyectoOld)) {
                idproyectoNew.getParticipanteList().add(participante);
                idproyectoNew = em.merge(idproyectoNew);
            }
            if (iduserOld != null && !iduserOld.equals(iduserNew)) {
                iduserOld.getParticipanteList().remove(participante);
                iduserOld = em.merge(iduserOld);
            }
            if (iduserNew != null && !iduserNew.equals(iduserOld)) {
                iduserNew.getParticipanteList().add(participante);
                iduserNew = em.merge(iduserNew);
            }
            //utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
               // utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = participante.getIdparticipante();
                if (findParticipante(id) == null) {
                    throw new NonexistentEntityException("The participante with id " + id + " no longer exists.");
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
            Participante participante;
            try {
                participante = em.getReference(Participante.class, id);
                participante.getIdparticipante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The participante with id " + id + " no longer exists.", enfe);
            }
            Proyecto idproyecto = participante.getIdproyecto();
            if (idproyecto != null) {
                idproyecto.getParticipanteList().remove(participante);
                idproyecto = em.merge(idproyecto);
            }
            User iduser = participante.getIduser();
            if (iduser != null) {
                iduser.getParticipanteList().remove(participante);
                iduser = em.merge(iduser);
            }
            em.remove(participante);
            //utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
             //   utx.rollback();
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

    public List<Participante> findParticipanteEntities() {
        return findParticipanteEntities(true, -1, -1);
    }

    public List<Participante> findParticipanteEntities(int maxResults, int firstResult) {
        return findParticipanteEntities(false, maxResults, firstResult);
    }

    private List<Participante> findParticipanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Participante.class));
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

    public Participante findParticipante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Participante.class, id);
        } finally {
            em.close();
        }
    }

    public int getParticipanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Participante> rt = cq.from(Participante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
