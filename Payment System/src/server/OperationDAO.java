package server;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Client;
import model.Bill;
import model.Provider;
import model.Notice;
import model.StatusClient;

/**
 * 
 * Contains all the operations that are executed on the database.
 *
 */
public class OperationDAO {// toate operatile pe baza de date
	private EntityManagerFactory emf;

	private EntityManagerFactory getEntityManagerFactory() {
		// EntityManagerFactory emf
		// =Persistence.createEntityManagerFactory("Sist_bancar");
		return emf;
	}

	public OperationDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public List<StatusClient> getAllStatus() {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qrStat = em.createQuery("SELECT s FROM StatusClient s");
		List<StatusClient> list = qrStat.getResultList();
		em.close();
		return list;
	}

	public List<Bill> getBillByID(Client cl, Provider fr) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qrFac = em.createQuery("SELECT b FROM Bill b WHERE b.client = :client AND b.provider = :furn");
		qrFac.setParameter("client", cl);
		qrFac.setParameter("furn", fr);
		List<Bill> list = qrFac.getResultList();
		em.close();
		return list;
	}

	public List<Bill> getBillByIdStatus(Client cl, Provider fr) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qrFac = em
				.createQuery("SELECT b FROM Bill b WHERE b.client = :client AND b.provider = :furn AND b.status>0");
		qrFac.setParameter("client", cl);
		qrFac.setParameter("furn", fr);
		List<Bill> list = qrFac.getResultList();
		em.close();
		return list;
	}

	public void updateBill(int id, byte newStatus) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Bill fac = em.find(Bill.class, id);
		fac.setStatus(newStatus);
		em.persist(fac);
		em.getTransaction().commit();
		em.close();
	}

	public void addBill(Bill fac) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(fac);
		em.getTransaction().commit();
		em.close();
	}

	public void addNotice(Notice inst) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(inst);
		em.getTransaction().commit();
		em.close();
	}

	public void updateStatus(int id, byte status) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		StatusClient stat = em.find(StatusClient.class, id);
		stat.setStatus(status);
		em.persist(stat);
		em.getTransaction().commit();
		em.close();
	}

	public void updateFineStatus(int id, byte status, double valoare) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Bill fac = em.find(Bill.class, id);
		fac.setFineStatus(status);
		fac.setFineValue(valoare);
		em.persist(fac);
		em.getTransaction().commit();
		em.close();
	}

	public void updateClientAccount(int id, double total) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Client cl = em.find(Client.class, id);
		cl.setAccountValue(cl.getAccountValue() - total);
		em.persist(cl);
		em.getTransaction().commit();
		em.close();
	}

	public void updateProviderAccount(int id, double total) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Provider fr = em.find(Provider.class, id);
		fr.setAccountValue(fr.getAccountValue() + total);
		em.persist(fr);
		em.getTransaction().commit();
		em.close();
	}

	public void updateBillStatus(int id, byte status) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Bill fac = em.find(Bill.class, id);
		fac.setStatus(status);
		fac.setFineStatus(status);
		em.persist(fac);
		em.getTransaction().commit();
		em.close();
	}

	public List<Provider> getProviders() {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qr = em.createQuery("SELECT p FROM Provider p");
		List<Provider> list = qr.getResultList();
		em.close();
		return list;

	}

	public List<Bill> getBillsByIdClient(int idClient) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Client cl = em.find(Client.class, idClient);
		List<Bill> list = cl.getBills();
		em.close();
		return list;
	}

	public void addStatusClient(StatusClient sts) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(sts);
		em.getTransaction().commit();
		em.close();
	}

	public StatusClient checkStatus(int id) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		StatusClient stat = em.find(StatusClient.class, id);
		em.close();
		return stat;
	}

	public void updateProviderAccount(Provider furn) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Provider fr = em.find(Provider.class, furn.getIdProvider());
		fr.setAccountValue(furn.getAccountValue());
		em.persist(fr);
		em.getTransaction().commit();
		em.close();
	}

	public Provider getProviderById(int id) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Provider fr = em.find(Provider.class, id);
		return fr;
	}

	public void deleteStatusClient(int id) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		StatusClient fac = em.find(StatusClient.class, id);
		em.remove(fac);
		em.getTransaction().commit();
		em.close();
	}

	public List<Client> getClients() {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qr = em.createQuery("Select c FROM Client c");
		List<Client> list = qr.getResultList();
		em.close();
		return list;
	}

	public List<Client> getClientByEmailPass(String email, String pass) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qr = em.createQuery("SELECT c FROM Client c WHERE c.email = :email AND c.password = :password");
		qr.setParameter("email", email);
		qr.setParameter("password", pass);
		List<Client> list = qr.getResultList();
		em.close();
		return list;
	}

	public void addClient(Client cl) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		em.persist(cl);
		em.getTransaction().commit();
		em.close();
	}

	public void updateClientAccount(Client cl) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Client client = em.find(Client.class, cl.getIdClient());
		em.getTransaction().begin();
		client.setAccountValue(cl.getAccountValue());
		em.persist(client);
		em.getTransaction().commit();
		em.close();
	}

	public Bill getBillById(int id) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Bill fac = em.find(Bill.class, id);
		return fac;
	}

	public List<Notice> getNoticesByClient(Client cl) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Client client = em.find(Client.class, cl.getIdClient());
		return client.getNotices();
	}

	public void updateNotices(int id, byte status) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Notice inst = em.find(Notice.class, id);
		inst.setStatus(status);
		em.persist(inst);
		em.getTransaction().commit();
		em.close();
	}

	public List<Client> getClientByIdAccount(int id, double valoare) {
		EntityManager em = this.getEntityManagerFactory().createEntityManager();
		Query qr = em.createQuery("SELECT c FROM Client c WHERE c.idClient = :id AND c.accountValue = :valoare");
		qr.setParameter("id", id);
		qr.setParameter("valoare", valoare);
		List<Client> list = qr.getResultList();
		em.close();
		return list;
	}
}
