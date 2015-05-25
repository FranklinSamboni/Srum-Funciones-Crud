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
public class ListaJpaController implements Serializable {

    public ListaJpaController(EntityManagerFactory emf) {
        this.utx = null;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lista lista) throws RollbackFailureException, Exception {
        if (lista.getActividadList() == null) {
            lista.setActividadList(new ArrayList<Actividad>());
        }
        EntityManager em = null;
        try {
           // utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto idproyecto = lista.getIdproyecto();
            if (idproyecto != null) {
                idproyecto = em.getReference(idproyecto.getClass(), idproyecto.getIdproyecto());
                lista.setIdproyecto(idproyecto);
            }
            List<Actividad> attachedActividadList = new ArrayList<Actividad>();
            for (Actividad actividadListActividadToAttach : lista.getActividadList()) {
                actividadListActividadToAttach = em.getReference(actividadListActividadToAttach.getClass(), actividadListActividadToAttach.getIdactividad());
                attachedActividadList.add(actividadListActividadToAttach);
            }
            lista.setActividadList(attachedActividadList);
            em.persist(lista);
            if (idproyecto != null) {
                idproyecto.getListaList().add(lista);
                idproyecto = em.merge(idproyecto);
            }
            for (Actividad actividadListActividad : lista.getActividadList()) {
                Lista oldIdlistaOfActividadListActividad = actividadListActividad.getIdlista();
                actividadListActividad.setIdlista(lista);
                actividadListActividad = em.merge(actividadListActividad);
                if (oldIdlistaOfActividadListActividad != null) {
                    oldIdlistaOfActividadListActividad.getActividadList().remove(actividadListActividad);
                    oldIdlistaOfActividadListActividad = em.merge(oldIdlistaOfActividadListActividad);
                }
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
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lista lista) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
           // utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Lista persistentLista = em.find(Lista.class, lista.getIdlista());
            Proyecto idproyectoOld = persistentLista.getIdproyecto();
            Proyecto idproyectoNew = lista.getIdproyecto();
            List<Actividad> actividadListOld = persistentLista.getActividadList();
            List<Actividad> actividadListNew = lista.getActividadList();
            List<String> illegalOrphanMessages = null;
            for (Actividad actividadListOldActividad : actividadListOld) {
                if (!actividadListNew.contains(actividadListOldActividad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Actividad " + actividadListOldActividad + " since its idlista field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idproyectoNew != null) {
                idproyectoNew = em.getReference(idproyectoNew.getClass(), idproyectoNew.getIdproyecto());
                lista.setIdproyecto(idproyectoNew);
            }
            List<Actividad> attachedActividadListNew = new ArrayList<Actividad>();
            for (Actividad actividadListNewActividadToAttach : actividadListNew) {
                actividadListNewActividadToAttach = em.getReference(actividadListNewActividadToAttach.getClass(), actividadListNewActividadToAttach.getIdactividad());
                attachedActividadListNew.add(actividadListNewActividadToAttach);
            }
            actividadListNew = attachedActividadListNew;
            lista.setActividadList(actividadListNew);
            lista = em.merge(lista);
            if (idproyectoOld != null && !idproyectoOld.equals(idproyectoNew)) {
                idproyectoOld.getListaList().remove(lista);
                idproyectoOld = em.merge(idproyectoOld);
            }
            if (idproyectoNew != null && !idproyectoNew.equals(idproyectoOld)) {
                idproyectoNew.getListaList().add(lista);
                idproyectoNew = em.merge(idproyectoNew);
            }
            for (Actividad actividadListNewActividad : actividadListNew) {
                if (!actividadListOld.contains(actividadListNewActividad)) {
                    Lista oldIdlistaOfActividadListNewActividad = actividadListNewActividad.getIdlista();
                    actividadListNewActividad.setIdlista(lista);
                    actividadListNewActividad = em.merge(actividadListNewActividad);
                    if (oldIdlistaOfActividadListNewActividad != null && !oldIdlistaOfActividadListNewActividad.equals(lista)) {
                        oldIdlistaOfActividadListNewActividad.getActividadList().remove(actividadListNewActividad);
                        oldIdlistaOfActividadListNewActividad = em.merge(oldIdlistaOfActividadListNewActividad);
                    }
                }
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
                Integer id = lista.getIdlista();
                if (findLista(id) == null) {
                    throw new NonexistentEntityException("The lista with id " + id + " no longer exists.");
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
         //   utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Lista lista;
            try {
                lista = em.getReference(Lista.class, id);
                lista.getIdlista();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lista with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Actividad> actividadListOrphanCheck = lista.getActividadList();
            for (Actividad actividadListOrphanCheckActividad : actividadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Lista (" + lista + ") cannot be destroyed since the Actividad " + actividadListOrphanCheckActividad + " in its actividadList field has a non-nullable idlista field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proyecto idproyecto = lista.getIdproyecto();
            if (idproyecto != null) {
                idproyecto.getListaList().remove(lista);
                idproyecto = em.merge(idproyecto);
            }
            em.remove(lista);
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

    public List<Lista> findListaEntities() {
        return findListaEntities(true, -1, -1);
    }

    public List<Lista> findListaEntities(int maxResults, int firstResult) {
        return findListaEntities(false, maxResults, firstResult);
    }

    private List<Lista> findListaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lista.class));
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

    public Lista findLista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lista.class, id);
        } finally {
            em.close();
        }
    }

    public int getListaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lista> rt = cq.from(Lista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
