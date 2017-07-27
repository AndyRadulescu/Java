package server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Bill;
import model.Client;
import model.Notice;
import model.Provider;
import model.StatusClient;
/***
 * 
 * This thread represents the automatic payment system that runs on the server side.
 *
 */
public class DbOnlyThread implements Runnable {
	private Semaphore sm;
	private EntityManagerFactory emf;
	public DbOnlyThread(Semaphore sm, EntityManagerFactory emf){
		this.sm=sm;
		this.emf=emf;
	}
	
	@Override
	public void run() {
		int date = 1;
		int month = 1;
		int sleepTime = 3;
		//EntityManagerFactory emf;
		while (true) {
			try {
				//emf=Persistence.createEntityManagerFactory("Sist_bancar");
				OperationDAO dao = new OperationDAO(emf);
				TimeUnit.SECONDS.sleep(sleepTime);
				this.sm.acquire();
				createBills(dao, date);
				automaticPayment(date,dao);
				emf.getCache().evictAll();
				this.sm.release();
				System.out.println("slept for " + sleepTime + " seconds; Date: " + date+ "; Month: "+month);
				if (date == 10) {
					date = 1;
					month++;
				} else
					date++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void createBills(OperationDAO dao, int date) {
		List<StatusClient> statusList = new ArrayList<>();
		statusList.addAll(dao.getAllStatus());
		List<Bill> bills = new ArrayList<>();
		int adaos = 100;
		for (StatusClient statusClient : statusList) {
			if (statusClient.getProvider().getDueDate() == date) {
				if (statusClient.getStatus() < 2) {
					bills.clear();
					bills.addAll(dao.getBillByIdStatus(statusClient.getClient(), statusClient.getProvider()));
					if (!bills.isEmpty()) {
						byte oldStatus = 0;
						for (Bill bill : bills) {
							Bill newBill = new Bill();
							newBill.setClient(bill.getClient());
							newBill.setProvider(bill.getProvider());
							if (bill.getStatus() == 1 || bill.getStatus() == 2) {
								int newStatus = (int) bill.getStatus() + 1;
								newBill.setValue(bill.getValue() + adaos);
								newBill.setStatus((byte) newStatus);
								dao.addBill(newBill);
								dao.updateBill(bill.getIdBill(), oldStatus);
							}
							if (bill.getStatus() == 3) {
								if (bill.getFineStatus() == 0) {
									dao.updateBillStatus(bill.getIdBill(), oldStatus);
									byte fineStatus = 1;
									newBill.setFineStatus(fineStatus);
									newBill.setFineValue(randDouble(20, 200));
									newBill.setValue(bill.getValue() + adaos);
									newBill.setStatus(bill.getStatus());
									dao.addBill(newBill);
									byte clientStatus = 2;
									dao.updateStatus(statusClient.getIdStatusClient(), clientStatus);
									Notice notice=new Notice();
									notice.setClient(bill.getClient());
									notice.setProvider(bill.getProvider());
									notice.setStatus(oldStatus);
									notice.setDescription("Serviciile furnizorului " + notice.getProvider().getName()
											+ " au fost suspendate din motive de neplata!");
									dao.addNotice(notice);
								}
							}
						}
					} else {
						byte newStatus = 1;
						Bill newBill = new Bill();
						newBill.setStatus(newStatus);
						newBill.setClient(statusClient.getClient());
						newBill.setProvider(statusClient.getProvider());
						newBill.setValue(randDouble(20, 500));
						dao.addBill(newBill);
					}
				}
			}
		}
	}

	public static double randDouble(double min, double max) {
		Random rand = new Random();
		double randomNum = min + (max - min) * rand.nextDouble();
		DecimalFormat df = new DecimalFormat("#.00");
		randomNum = Double.valueOf(df.format(randomNum));
		return randomNum;
	}
	private void automaticPayment(int date, OperationDAO dao) {//plata facturilor 
		ArrayList<StatusClient> statusuri = new ArrayList<>();
		statusuri.addAll(dao.getAllStatus());
		byte noticeStat = 0;
		for (StatusClient statusClient : statusuri) {
			if (statusClient.getProvider().getDueDate() == date) {
				Client client = statusClient.getClient();
				Provider furn = statusClient.getProvider();
				List<Bill> facturi = dao.getBillByID(client, furn);
				if (statusClient.getStatus() == 1) {
					if (!facturi.isEmpty()) {
						for (Bill bill : facturi) {
							if (bill.getStatus() != 0) {
								Notice inst = new Notice();
								inst.setClient(statusClient.getClient());
								inst.setProvider(statusClient.getProvider());
								inst.setStatus(noticeStat);
								inst.setBill(bill);
								double total = bill.getValue() + bill.getFineValue();
								if (statusClient.getClient().getAccountValue() >= total) {
									byte a = 0;
									dao.updateBillStatus(bill.getIdBill(), a);

									dao.updateClientAccount(statusClient.getClient().getIdClient(), total);
									dao.updateProviderAccount(statusClient.getProvider().getIdProvider(), total);

									inst.setDescription("Bill (" + bill.getIdBill() + ") la "
											+ bill.getProvider().getName() + " cu valoarea totala de " + total
											+ " a fost platita cu succes!");
								} else {
									inst.setDescription("Bill (" + bill.getIdBill() + ") la "
											+ bill.getProvider().getName() + " cu valoarea totala de " + total
											+ " nu a putut fi platita, fonduri insuficiente!");

								}

								dao.addNotice(inst);

							}
						}
					}
				}
			}
		}

	}

}
