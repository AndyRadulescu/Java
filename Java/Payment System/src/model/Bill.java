package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the bill database table.
 * 
 */
@Entity
@NamedQuery(name="Bill.findAll", query="SELECT b FROM Bill b")
public class Bill implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_bill")
	private int idBill;

	@Column(name="fine_status")
	private byte fineStatus;

	@Column(name="fine_value")
	private double fineValue;

	private byte status;

	private double value;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="id_client")
	private Client client;

	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="id_provider")
	private Provider provider;

	//bi-directional many-to-one association to Notice
	@OneToMany(mappedBy="bill")
	private List<Notice> notices;

	public Bill() {
	}

	public int getIdBill() {
		return this.idBill;
	}

	public void setIdBill(int idBill) {
		this.idBill = idBill;
	}

	public byte getFineStatus() {
		return this.fineStatus;
	}

	public void setFineStatus(byte fineStatus) {
		this.fineStatus = fineStatus;
	}

	public double getFineValue() {
		return this.fineValue;
	}

	public void setFineValue(double fineValue) {
		this.fineValue = fineValue;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
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

	public List<Notice> getNotices() {
		return this.notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public Notice addNotice(Notice notice) {
		getNotices().add(notice);
		notice.setBill(this);

		return notice;
	}

	public Notice removeNotice(Notice notice) {
		getNotices().remove(notice);
		notice.setBill(null);

		return notice;
	}
	@Override
	public String toString() {
		return idBill + "                " + provider.getName() + "                " + value + "                "
				+ fineValue;
	}

}