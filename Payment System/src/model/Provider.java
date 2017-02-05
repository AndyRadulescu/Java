package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the provider database table.
 * 
 */
@Entity
@NamedQuery(name="Provider.findAll", query="SELECT p FROM Provider p")
public class Provider implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_provider")
	private int idProvider;

	@Column(name="account_value")
	private double accountValue;

	@Column(name="due_date")
	private int dueDate;

	private String email;

	@Column(name="fine_value")
	private int fineValue;

	private String name;

	//bi-directional many-to-one association to Bill
	@OneToMany(mappedBy="provider")
	private List<Bill> bills;

	//bi-directional many-to-one association to Notice
	@OneToMany(mappedBy="provider")
	private List<Notice> notices;

	//bi-directional many-to-one association to StatusClient
	@OneToMany(mappedBy="provider")
	private List<StatusClient> statusClients;

	public Provider() {
	}

	public int getIdProvider() {
		return this.idProvider;
	}

	public void setIdProvider(int idProvider) {
		this.idProvider = idProvider;
	}

	public double getAccountValue() {
		return this.accountValue;
	}

	public void setAccountValue(double accountValue) {
		this.accountValue = accountValue;
	}

	public int getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(int dueDate) {
		this.dueDate = dueDate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getFineValue() {
		return this.fineValue;
	}

	public void setFineValue(int fineValue) {
		this.fineValue = fineValue;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Bill> getBills() {
		return this.bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public Bill addBill(Bill bill) {
		getBills().add(bill);
		bill.setProvider(this);

		return bill;
	}

	public Bill removeBill(Bill bill) {
		getBills().remove(bill);
		bill.setProvider(null);

		return bill;
	}

	public List<Notice> getNotices() {
		return this.notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public Notice addNotice(Notice notice) {
		getNotices().add(notice);
		notice.setProvider(this);

		return notice;
	}

	public Notice removeNotice(Notice notice) {
		getNotices().remove(notice);
		notice.setProvider(null);

		return notice;
	}

	public List<StatusClient> getStatusClients() {
		return this.statusClients;
	}

	public void setStatusClients(List<StatusClient> statusClients) {
		this.statusClients = statusClients;
	}

	public StatusClient addStatusClient(StatusClient statusClient) {
		getStatusClients().add(statusClient);
		statusClient.setProvider(this);

		return statusClient;
	}

	public StatusClient removeStatusClient(StatusClient statusClient) {
		getStatusClients().remove(statusClient);
		statusClient.setProvider(null);

		return statusClient;
	}

}