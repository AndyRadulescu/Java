package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the client database table.
 * 
 */
@Entity
@NamedQuery(name="Client.findAll", query="SELECT c FROM Client c")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_client")
	private int idClient;

	@Column(name="account_value")
	private double accountValue;

	private String address;

	private String cnp;

	private String email;

	private String name;

	private String password;

	//bi-directional many-to-one association to StatusClient
	@OneToMany(mappedBy="client")
	private List<StatusClient> statusClients;

	//bi-directional many-to-one association to Bill
	@OneToMany(mappedBy="client")
	private List<Bill> bills;

	//bi-directional many-to-one association to Notice
	@OneToMany(mappedBy="client")
	private List<Notice> notices;

	public Client() {
	}

	public int getIdClient() {
		return this.idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public double getAccountValue() {
		return this.accountValue;
	}

	public void setAccountValue(double accountValue) {
		this.accountValue = accountValue;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCnp() {
		return this.cnp;
	}

	public void setCnp(String cnp) {
		this.cnp = cnp;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<StatusClient> getStatusClients() {
		return this.statusClients;
	}

	public void setStatusClients(List<StatusClient> statusClients) {
		this.statusClients = statusClients;
	}

	public StatusClient addStatusClient(StatusClient statusClient) {
		getStatusClients().add(statusClient);
		statusClient.setClient(this);

		return statusClient;
	}

	public StatusClient removeStatusClient(StatusClient statusClient) {
		getStatusClients().remove(statusClient);
		statusClient.setClient(null);

		return statusClient;
	}

	public List<Bill> getBills() {
		return this.bills;
	}

	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}

	public Bill addBill(Bill bill) {
		getBills().add(bill);
		bill.setClient(this);

		return bill;
	}

	public Bill removeBill(Bill bill) {
		getBills().remove(bill);
		bill.setClient(null);

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
		notice.setClient(this);

		return notice;
	}

	public Notice removeNotice(Notice notice) {
		getNotices().remove(notice);
		notice.setClient(null);

		return notice;
	}

}