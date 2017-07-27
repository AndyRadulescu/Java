package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import application.SavedItems;
import model.Bill;
import model.Client;
import model.Notice;
import model.Provider;
import model.StatusClient;
/**
 * This class executes all commands received from the client.
 */
public class ClientServerThread implements Runnable {
	
	
	private Socket socket = null;
	private Semaphore sm;
	private EntityManagerFactory emf;

	public ClientServerThread(Socket sock, Semaphore sm, EntityManagerFactory emf) throws IOException {
		this.socket = sock;
		this.sm=sm;
		this.emf=emf;
	}
	@Override
	public void run() {
		try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
			OperationDAO dao = new OperationDAO(emf);
			Message result = chooseOper(in, dao);
			out.writeObject(result);
			socket.close();
			emf.getCache().evictAll();
		} catch (SocketTimeoutException ste) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * returns all StatusCleint
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message getAllClientStatus(OperationDAO dao) {
		Message ms = new Message();
		List<StatusClient> stc = new ArrayList<>();
		stc.addAll(dao.getAllStatus());
		ms.setData(stc);
		return ms;
	}
	/**
	 * subscrition client (write)
	 * @param ms message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message updateSubscribe(Message ms, OperationDAO dao) {
		byte bt = 1;
		Message message = new Message();
		StatusClient sts = new StatusClient();
		sts = (StatusClient) ms.getData();
		dao.updateStatus(sts.getIdStatusClient(), bt);

		return message;
	}
	/**
	 * unsubscribe client (write)
	 * @param ms message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message updateUnsubscribe(Message ms, OperationDAO dao) {// dezabonare client (write)
		byte bt = 0;
		Message message = new Message();
		StatusClient sts = new StatusClient();
		sts = (StatusClient) ms.getData();
		dao.updateStatus(sts.getIdStatusClient(), bt);
		return message;
	}
	/**
	 * returns all providers(read)
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message getFurnizori(OperationDAO dao) {// returneaza toti furnizorii (read)
		Message ms = new Message();
		ArrayList<Provider> furn = new ArrayList<>();
		furn.addAll(dao.getProviders());
		ms.setData(furn);
		return ms;
	}
	/**
	 * returns all bills of a client(read)
	 * @param message message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message listFacturi(Message message, OperationDAO dao) {// returneaza toate facturile unui client (read)
		Message m = new Message();
		Client cl = (Client) message.getData();
		ArrayList<Bill> facturi = new ArrayList<>();
		facturi.addAll(dao.getBillsByIdClient(cl.getIdClient()));
		int result = 0;
		if (facturi.size() > 0) {
			result = 1;
		}
		m.setAction(result);
		m.setData(facturi);
		System.out.println("Client(" + cl.getIdClient() + ") checked facturi");
		return m;
	}
	/**
	 * checks if status actually changed
	 * @param message message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
			
	public Message implementStatus(Message message, OperationDAO dao) {// verifica daca schimbarea de status s-a efectuat(read)
		StatusClient sts = (StatusClient) message.getData();
		String result = "";
		Message ms = new Message();
		dao.addStatusClient(sts);
		if (dao.checkStatus(sts.getIdStatusClient()) != null) {
			result = "It's ok";
		} else {
			result = "Faliure";
		}
		StatusClient status = dao.checkStatus(sts.getIdStatusClient());
		ms.setAction(status.getIdStatusClient());
		ms.setData(result);
		return ms;
	}
	/**
	 * updates accountValue of a provider(write)
	 * @param msg message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */

	public Message updateProviderAccount(Message msg, OperationDAO dao) {// update cont furnizor(write)
		Message m = new Message();
		Provider fr = (Provider) msg.getData();
		dao.updateProviderAccount(fr);

		int result = 0;
		if (dao.getProviderById(fr.getIdProvider()) != null) {
			result = 1;
		}
		m.setAction(result);
		m.setData(dao.getProviderById(fr.getIdProvider()));
		System.out.println("Client(" + fr.getIdProvider() + ") added funds");
		return m;
	}
	/**
	 * updates the status of a bill(write)
	 * @param msg message received from client
	 * @param dao does the operation on the database
	 */
	public void setStatusFactura(Message msg, OperationDAO dao) {// update status factura(write)

		Bill fac = (Bill) msg.getData();
		dao.updateBill(fac.getIdBill(), fac.getStatus());
		System.out.println("Bill(" + fac.getIdBill() + ") changed status to" + fac.getStatus());
	}
	/**
	 * deletes StatusClient (write)
	 * @param message message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message deleteStatus(Message message, OperationDAO dao) {//sterge statusclient (write)
		StatusClient stss = (StatusClient) message.getData();
		Message ms = new Message();
		dao.deleteStatusClient(stss.getIdStatusClient());
		return ms;

	}
	/**
	 * checks if the client already exists in the db(read)
	 * @param message message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message login(Message message, OperationDAO dao) {//verifica daca clientul exista in db (read)
		Client user = (Client) message.getData();
		Message ms = new Message();
		int result = 0;
		Client cl = new Client();
		if (dao.getClientByEmailPass(user.getEmail(), user.getPassword()).size() > 0) {
			result = 1;
			cl = dao.getClientByEmailPass(user.getEmail(), user.getPassword()).get(0);
			ms.setData(cl);
		} else {
			result = 0;
		}
		ms.setAction(result);
		return ms;

	}
	/**
	 * adds a new client in the db(write)
	 * @param message message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message signup(Message message, OperationDAO dao) {// adauga client(write)
		Client user = (Client) message.getData();
		String result = "";
		Message ms = new Message();
		dao.addClient(user);
		if (dao.getClientByEmailPass(user.getEmail(), user.getPassword()).size() > 0) {
			result = "Signup succesful!";
		} else {
			result = "Faliure";
		}
		Client qrResult = (Client) dao.getClientByEmailPass(user.getEmail(), user.getPassword()).get(0);
		ms.setAction(qrResult.getIdClient());
		ms.setData(result);
		System.out.println("Client(" + qrResult.getIdClient() + ") signed up");
		return ms;
	}
	/**
	 * more efficient alternative to switch; chooses the operation that must be executed, dependant on message.getAction() 
	 * @param in ObjectInputStream from socket.accept()
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message chooseOper(ObjectInputStream in, OperationDAO dao) {//in loc de switch; alege operatia in functie de message.getAction()
		Message ms = new Message();
		Message result = new Message();
		Map<Integer, Operation> map = new HashMap<>();
		try {
			ms = (Message) in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		map.put(SavedItems.ADDFUNDS, new Operation() {

			@Override // adauga fonduri (write)
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message m = new Message();
				m = addFunds(message, dao);
				sm.release();
				return m;
			}
		});
		map.put(SavedItems.LOGIN, new Operation() {
			@Override // login (read)
			public Message doOperation(Message message) {
				Message resp = new Message();
				resp = login(message, dao);
				return resp;
			}

		});
		map.put(SavedItems.REGISTER, new Operation() {
			@Override //signup (write)
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message resp = new Message();
				resp = signup(message, dao);
				sm.release();
				return resp;
			}
		});
		map.put(SavedItems.GETBILLS, new Operation() {
			@Override // (read)
			public Message doOperation(Message message) {
				Message resp = new Message();
				resp = listFacturi(message, dao);
				return resp;
			}
		});
		map.put(SavedItems.MANUALPAYMENT, new Operation() {
			
			@Override//plata manual(write)
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message resp = manualPayment(message, dao);
				sm.release();
				return resp;
			}

		});
		map.put(SavedItems.GETNOTICES, new Operation() {

			@Override//read
			public Message doOperation(Message message) {
				Message resp = getNotices(message, dao);
				return resp;
			}

		});
		map.put(SavedItems.ARCHIVENOTICE, new Operation() {

			@Override//write
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				archiveNotice(message, dao);
				sm.release();
				return null;
			}

		});
		map.put(SavedItems.SETSTATUS, new Operation() {

			@Override//write
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message resp = new Message();
				setStatusFactura(message, dao);
				sm.release();
				return resp;
			}

		});
		map.put(SavedItems.UPDATEACCPROV, new Operation() {

			@Override//write
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message m = new Message();
				m = updateProviderAccount(message, dao);
				sm.release();
				return m;
			}

		});
		map.put(SavedItems.DELETESTATUS, new Operation() {

			@Override
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message m = new Message();
				m = deleteStatus(message, dao);
				sm.release();
				return m;

			}

		});
		map.put(SavedItems.SUBSCRIBE, new Operation() {

			@Override
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message m = new Message();
				m = updateSubscribe(message, dao);
				sm.release();
				return m;
			}

		});
		map.put(SavedItems.CHECKSTATUS, new Operation() {

			@Override
			public Message doOperation(Message message) {
				// TODO Auto-generated method stub
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message m = new Message();
				m = implementStatus(message, dao);
				sm.release();
				return m;
			}

		});
		map.put(SavedItems.GETPROVIDERS, new Operation() {

			@Override
			public Message doOperation(Message message) {
				Message m = new Message();
				m = getFurnizori(dao);
				return m;
			}

		});
		map.put(SavedItems.UNSUBSCRIBE, new Operation() {

			@Override
			public Message doOperation(Message message) {
				try {
					sm.acquire();
				} catch (InterruptedException e) {
					e.getMessage();
				}
				Message m = new Message();
				m = updateUnsubscribe(message, dao);
				sm.release();
				return m;
			}

		});
		map.put(SavedItems.GETSTATUS, new Operation() {

			@Override
			public Message doOperation(Message message) {
				Message m = new Message();
				m = getAllClientStatus(dao);
				return m;
			}

		});
		if (ms != null)
			result = map.get(ms.getAction()).doOperation(ms);

		else {
			result.setData("eroare");
		}
		return result;
	}
	/**
	 * adds funds: update accountValue from client(write)
	 * @param msg message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message addFunds(Message msg, OperationDAO dao) {// adaugare de fondur, update cont client (write)
		Message ms = new Message();
		Client cl = (Client) msg.getData();
		dao.updateClientAccount(cl);
		int result = 0;
		if (dao.getClientByIdAccount(cl.getIdClient(), cl.getAccountValue()).size() > 0) {
			result = 1;
		}
		ms.setAction(result);
		ms.setData(dao.getClientByIdAccount(cl.getIdClient(), cl.getAccountValue()).get(0));
		System.out.println("Client(" + cl.getIdClient() + ") added funds");
		return ms;
	}
	/**
	 * manually pays a bill that couldn't be paid by the automatic system(write)
	 * @param msg message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message manualPayment(Message msg, OperationDAO dao) {//plataeste manual o factura care nu a putut fi platita de functia automata (write)
		Message response = new Message();
		Bill fac = (Bill) msg.getData();
		byte status = 0;
		dao.updateBill(fac.getIdBill(), status);
		response.setAction(status);
		return response;
	}
	/**
	 * returns all the notifications of a client(read)
	 * @param msg message received from client
	 * @param dao does the operation on the database
	 * @return Message
	 */
	public Message getNotices(Message msg, OperationDAO dao) {//returneaza toate instiintarile unui client (read)
		Client client = (Client) msg.getData();
		ArrayList<Notice> instiintari = new ArrayList<>();
		instiintari.addAll(dao.getNoticesByClient(client));
		Message resp = new Message();
		resp.setData(instiintari);
		return resp;
	}
	/**
	 * adds notification to archive(notification=read) (write)
	 * @param msg message received from client
	 * @param dao does the operation on the database
	 */
	public void archiveNotice(Message msg, OperationDAO dao) {// arhiveaza instiintarea(devine citita) (write)
		Notice inst = (Notice) msg.getData();
		byte newStatus = 1;
		dao.updateNotices(inst.getIdNotice(), newStatus);
	}

}
