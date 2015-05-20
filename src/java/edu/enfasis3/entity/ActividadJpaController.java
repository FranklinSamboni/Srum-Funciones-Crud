/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.entity;

import edu.enfasis3.entity.exceptions.IllegalOrphanException;
import edu.enfasis3.entity.exceptions.NonexistentEntityException;
import edu.enfasis3.entity.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Frank
 */
public class ActividadJpaController implements Serializable {

    public ActividadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) throws RollbackFailureException, Exception {
        if (actividad.getComentarioList() == null) {
            actividad.setComentarioList(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Lista idlista = actividad.getIdlista();
            if (idlista != null) {
                idlista = em.getReference(idlista.getClass(), idlista.getIdlista());
                actividad.setIdlista(idlista);
            }
            List<Comentario> attachedComentarioList = new ArrayList<Comentario>();
            for (Comentario comentarioListComentarioToAttach : actividad.getComentarioList()) {
                comentarioListComentarioToAttach = em.getReference(comentarioListComentarioToAttach.getClass(), comentarioListComentarioToAttach.getIdcomentario());
                attachedComentarioList.add(comentarioListComentarioToAttach);
            }
            actividad.setComentarioList(attachedComentarioList);
            em.persist(actividad);
            if (idlista != null) {
                idlista.getActividadList().add(actividad);
                idlista = em.merge(idlista);
            }
            for (Comentario comentarioListComentario : actividad.getComentarioList()) {
                Actividad oldIdactividadOfComentarioListComentario = comentarioListComentario.getIdactividad();
                comentarioListComentario.setIdactividad(actividad);
                comentarioListComentario = em.merge(comentarioListComentario);
                if (oldIdactividadOfComentarioListComentario != null) {
                    oldIdactividadOfComentarioListComentario.getComentarioList().remove(comentarioListComentario);
                    oldIdactividadOfComentarioListComentario = em.merge(oldIdactividadOfComentarioListComentario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
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

    public void edit(Actividad actividad) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Actividad persistentActividad = em.find(Actividad.class, actividad.getIdactividad());
            Lista idlistaOld = persistentActividad.getIdlista();
            Lista idlistaNew = actividad.getIdlista();
            List<Comentario> comentarioListOld = persistentActividad.getComentarioList();
            List<Comentario> comentarioListNew = actividad.getComentarioList();
            List<String> illegalOrphanMessages = null;
            for (Comentario comentarioListOldComentario : comentarioListOld) {
                if (!comentarioListNew.contains(comentarioListOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioListOldComentario + " since its idactividad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idlistaNew != null) {
                idlistaNew = em.getReference(idlistaNew.getClass(), idlistaNew.getIdlista());
                actividad.setIdlista(idlistaNew);
            }
            List<Comentario> attachedComentarioListNew = new ArrayList<Comentario>();
            for (Comentario comentarioListNewComentarioToAttach : comentarioListNew) {
                comentarioListNewComentarioToAttach = em.getReference(comentarioListNewComentarioToAttach.getClass(), comentarioListNewComentarioToAttach.getIdcomentario());
                attachedComentarioListNew.add(comentarioListNewComentarioToAttach);
            }
            comentarioListNew = attachedComentarioListNew;
            actividad.setComentarioList(comentarioListNew);
            actividad = em.merge(actividad);
            if (idlistaOld != null && !idlistaOld.equals(idlistaNew)) {
                idlistaOld.getActividadList().remove(actividad);
                idlistaOld = em.merge(idlistaOld);
            }
            if (idlistaNew != null && !idlistaNew.equals(idlistaOld)) {
                idlistaNew.getActividadList().add(actividad);
                idlistaNew = em.merge(idlistaNew);
            }
            for (Comentario comentarioListNewComentario : comentarioListNew) {
                if (!comentarioListOld.contains(comentarioListNewComentario)) {
                    Actividad oldIdactividadOfComentarioListNewComentario = comentarioListNewComentario.getIdactividad();
                    comentarioListNewComentario.setIdactividad(actividad);
                    comentarioListNewComentario = em.merge(comentarioListNewComentario);
                    if (oldIdactividadOfComentarioListNewComentario != null && !oldIdactividadOfComentarioListNewComentario.equals(actividad)) {
                        oldIdactividadOfComentarioListNewComentario.getComentarioList().remove(comentarioListNewComentario);
                        oldIdactividadOfComentarioListNewComentario = em.merge(oldIdactividadOfComentarioListNewComentario);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = actividad.getIdactividad();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getIdactividad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The actividad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Comentario> comentarioListOrphanCheck = actividad.getComentarioList();
            for (Comentario comentarioListOrphanCheckComentario : comentarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Actividad (" + actividad + ") cannot be destroyed since the Comentario " + comentarioListOrphanCheckComentario + " in its comentarioList field has a non-nullable idactividad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Lista idlista = actividad.getIdlista();
            if (idlista != null) {
                idlista.getActividadList().remove(actividad);
                idlista = em.merge(idlista);
            }
            em.remove(actividad);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
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

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }

    public int getActividadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
