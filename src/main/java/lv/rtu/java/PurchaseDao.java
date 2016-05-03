package lv.rtu.java;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PurchaseDao {

	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("purchases");
	
	public static List<Purchase> findPurchaseListByToken(String token) {
		final EntityManager em = FACTORY.createEntityManager();

		try {
			
			final CriteriaBuilder qb = em.getCriteriaBuilder();
			final CriteriaQuery<Purchase> cq = qb.createQuery(Purchase.class);
			
			final Root<Purchase> p = cq.from(Purchase.class);
			cq.where(qb.equal(p.get(Purchase_.token), token));
			
			return em.createQuery(cq).getResultList();
		}
		finally{
			em.close();
		}
	}
	
	public static void storePurchaseListForToken(String token, List<Purchase> list) {
		final EntityManager em = FACTORY.createEntityManager();

		try {
			em.getTransaction().begin();
			
			em.createQuery("delete from Purchase p where p.token = ?1")
			  .setParameter(1, token)
			  .executeUpdate();
			
			for(Purchase purchase : list) {
				em.persist(purchase);
			}
			
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}
}
