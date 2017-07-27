package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the status_client database table.
 * 
 */
@Entity
@Table(name="status_client")
@NamedQuery(name="StatusClient.findAll", query="SELECT s FROM StatusClient s")
public class StatusClient implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="id_status_client")
	private int idStatusClient;

	private byte status;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="id_client")
	private Client client;

	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="id_provider")
	private Provider provider;

	public StatusClient() {
	}

	public int getIdStatusClient() {
		return this.idStatusClient;
	}

	public void setIdStatusClient(int idStatusClient) {
		this.idStatusClient = idStatusClient;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
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

}