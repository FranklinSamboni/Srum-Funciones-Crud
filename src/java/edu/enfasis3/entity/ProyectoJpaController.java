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
public class ProyectoJpaController implements Serializable {

    public ProyectoJpaController(EntityManagerFactory emf) {
        this.utx = null;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proyecto proyecto) throws RollbackFailureException, Exception {
        if (proyecto.getListaList() == null) {
            proyecto.setListaList(new ArrayList<Lista>());
        }
        if (proyecto.getParticipanteList() == null) {
            proyecto.setParticipanteList(new ArrayList<Participante>());
        }
        EntityManager em = null;
        try {
          //  utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            User manager = proyecto.getManager();
            if (manager != null) {
                manager = em.getReference(manager.getClass(), manager.getIduser());
                proyecto.setManager(manager);
            }
            List<Lista> attachedListaList = new ArrayList<Lista>();
            for (Lista listaListListaToAttach : proyecto.getListaList()) {
                listaListListaToAttach = em.getReference(listaListListaToAttach.getClass(), listaListListaToAttach.getIdlista());
                attachedListaList.add(listaListListaToAttach);
            }
            proyecto.setListaList(attachedListaList);
            List<Participante> attachedParticipanteList = new ArrayList<Participante>();
            for (Participante participanteListParticipanteToAttach : proyecto.getParticipanteList()) {
                participanteListParticipanteToAttach = em.getReference(participanteListParticipanteToAttach.getClass(), participanteListParticipanteToAttach.getIdparticipante());
                attachedParticipanteList.add(participanteListParticipanteToAttach);
            }
            proyecto.setParticipanteList(attachedParticipanteList);
            em.persist(proyecto);
            if (manager != null) {
                manager.getProyectoList().add(proyecto);
                manager = em.merge(manager);
            }
            for (Lista listaListLista : proyecto.getListaList()) {
                Proyecto oldIdproyectoOfListaListLista = listaListLista.getIdproyecto();
                listaListLista.setIdproyecto(proyecto);
                listaListLista = em.merge(listaListLista);
                if (oldIdproyectoOfListaListLista != null) {
                    oldIdproyectoOfListaListLista.getListaList().remove(listaListLista);
                    oldIdproyectoOfListaListLista = em.merge(oldIdproyectoOfListaListLista);
                }
            }
            for (Participante participanteListParticipante : proyecto.getParticipanteList()) {
                Proyecto oldIdproyectoOfParticipanteListParticipante = participanteListParticipante.getIdproyecto();
                participanteListParticipante.setIdproyecto(proyecto);
                participanteListParticipante = em.merge(participanteListParticipante);
                if (oldIdproyectoOfParticipanteListParticipante != null) {
                    oldIdproyectoOfParticipanteListParticipante.getParticipanteList().remove(participanteListParticipante);
                    oldIdproyectoOfParticipanteListParticipante = em.merge(oldIdproyectoOfParticipanteListParticipante);
                }
            }
           // utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
            //    utx.rollback();
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

    public void edit(Proyecto proyecto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
          //  utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto persistentProyecto = em.find(Proyecto.class, proyecto.getIdproyecto());
            User managerOld = persistentProyecto.getManager();
            User managerNew = proyecto.getManager();
            List<Lista> listaListOld = persistentProyecto.getListaList();
            List<Lista> listaListNew = proyecto.getListaList();
            List<Participante> participanteListOld = persistentProyecto.getParticipanteList();
            List<Participante> participanteListNew = proyecto.getParticipanteList();
            List<String> illegalOrphanMessages = null;
            for (Lista listaListOldLista : listaListOld) {
                if (!listaListNew.contains(listaListOldLista)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Lista " + listaListOldLista + " since its idproyecto field is not nullable.");
                }
            }
            for (Participante participanteListOldParticipante : participanteListOld) {
                if (!participanteListNew.contains(participanteListOldParticipante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Participante " + participanteListOldParticipante + " since its idproyecto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (managerNew != null) {
                managerNew = em.getReference(managerNew.getClass(), managerNew.getIduser());
                proyecto.setManager(managerNew);
            }
            List<Lista> attachedListaListNew = new ArrayList<Lista>();
            for (Lista listaListNewListaToAttach : listaListNew) {
                listaListNewListaToAttach = em.getReference(listaListNewListaToAttach.getClass(), listaListNewListaToAttach.getIdlista());
                attachedListaListNew.add(listaListNewListaToAttach);
            }
            listaListNew = attachedListaListNew;
            proyecto.setListaList(listaListNew);
            List<Participante> attachedParticipanteListNew = new ArrayList<Participante>();
            for (Participante participanteListNewParticipanteToAttach : participanteListNew) {
                participanteListNewParticipanteToAttach = em.getReference(participanteListNewParticipanteToAttach.getClass(), participanteListNewParticipanteToAttach.getIdparticipante());
                attachedParticipanteListNew.add(participanteListNewParticipanteToAttach);
            }
            participanteListNew = attachedParticipanteListNew;
            proyecto.setParticipanteList(participanteListNew);
            proyecto = em.merge(proyecto);
            if (managerOld != null && !managerOld.equals(managerNew)) {
                managerOld.getProyectoList().remove(proyecto);
                managerOld = em.merge(managerOld);
            }
            if (managerNew != null && !managerNew.equals(managerOld)) {
                managerNew.getProyectoList().add(proyecto);
                managerNew = em.merge(managerNew);
            }
            for (Lista listaListNewLista : listaListNew) {
                if (!listaListOld.contains(listaListNewLista)) {
                    Proyecto oldIdproyectoOfListaListNewLista = listaListNewLista.getIdproyecto();
                    listaListNewLista.setIdproyecto(proyecto);
                    listaListNewLista = em.merge(listaListNewLista);
                    if (oldIdproyectoOfListaListNewLista != null && !oldIdproyectoOfListaListNewLista.equals(proyecto)) {
                        oldIdproyectoOfListaListNewLista.getListaList().remove(listaListNewLista);
                        oldIdproyectoOfListaListNewLista = em.merge(oldIdproyectoOfListaListNewLista);
                    }
                }
            }
            for (Participante participanteListNewParticipante : participanteListNew) {
                if (!participanteListOld.contains(participanteListNewParticipante)) {
                    Proyecto oldIdproyectoOfParticipanteListNewParticipante = participanteListNewParticipante.getIdproyecto();
                    participanteListNewParticipante.setIdproyecto(proyecto);
                    participanteListNewParticipante = em.merge(participanteListNewParticipante);
                    if (oldIdproyectoOfParticipanteListNewParticipante != null && !oldIdproyectoOfParticipanteListNewParticipante.equals(proyecto)) {
                        oldIdproyectoOfParticipanteListNewParticipante.getParticipanteList().remove(participanteListNewParticipante);
                        oldIdproyectoOfParticipanteListNewParticipante = em.merge(oldIdproyectoOfParticipanteListNewParticipante);
                    }
                }
            }
           // utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
            //    utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proyecto.getIdproyecto();
                if (findProyecto(id) == null) {
                    throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.");
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
            //utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            Proyecto proyecto;
            try {
                proyecto = em.getReference(Proyecto.class, id);
                proyecto.getIdproyecto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proyecto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Lista> listaListOrphanCheck = proyecto.getListaList();
            for (Lista listaListOrphanCheckLista : listaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proyecto (" + proyecto + ") cannot be destroyed since the Lista " + listaListOrphanCheckLista + " in its listaList field has a non-nullable idproyecto field.");
            }
            List<Participante> participanteListOrphanCheck = proyecto.getParticipanteList();
            for (Participante participanteListOrphanCheckParticipante : participanteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proyecto (" + proyecto + ") cannot be destroyed since the Participante " + participanteListOrphanCheckParticipante + " in its participanteList field has a non-nullable idproyecto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            User manager = proyecto.getManager();
            if (manager != null) {
                manager.getProyectoList().remove(proyecto);
                manager = em.merge(manager);
            }
            em.remove(proyecto);
           // utx.commit();
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

    public List<Proyecto> findProyectoEntities() {
        return findProyectoEntities(true, -1, -1);
    }

    public List<Proyecto> findProyectoEntities(int maxResults, int firstResult) {
        return findProyectoEntities(false, maxResults, firstResult);
    }

    private List<Proyecto> findProyectoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proyecto.class));
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

    public Proyecto findProyecto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proyecto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProyectoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proyecto> rt = cq.from(Proyecto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
