package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the notice database table.
 * 
 */
@Entity
@NamedQuery(name="Notice.findAll", query="SELECT n FROM Notice n")
public class Notice implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_notice")
	private int idNotice;

	

	private String description;

	private byte status;

	//bi-directional many-to-one association to Bill
	@ManyToOne
	@JoinColumn(name="id_bill")
	private Bill bill;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="id_client")
	private Client client;

	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="id_provider")
	private Provider provider;

	public Notice() {
	}

	public int getIdNotice() {
		return this.idNotice;
	}

	public void setIdNotice(int idNotice) {
		this.idNotice = idNotice;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Bill getBill() {
		return this.bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Provider getProvider() {
		return this.provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	@Override
	public String toString() {
		return description;
	}

}