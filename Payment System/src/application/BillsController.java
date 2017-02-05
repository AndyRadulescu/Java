package application;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import model.Client;
import model.Bill;
import model.Provider;
import model.StatusClient;
import server.Message;
/***
 * 
 * Controller for the bills frame.
 *
 */
public class BillsController implements Initializable {//controller pt BillsScene

	@FXML
	private Button payButton;

	@FXML
	private Label lb_Ban;

	@FXML
	private Label lb_ValBan;

	@FXML
	private Label lb_Verif;

	@FXML
	private Label lb_Cont;

	@FXML
	private ListView<Bill> paid;


	@FXML
	private ListView<Bill> unpaid;

	static Client cli = LoginController.takeClient();

	private ArrayList<Bill> respBills = new ArrayList<>();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		lb_Cont.setText(String.valueOf(cli.getAccountValue()));
		try {
			String email = cli.getEmail();
			String pass = cli.getPassword();
			Client cl = new Client();
			cl.setEmail(email);
			cl.setPassword(pass);
			Message ms = new Message(SavedItems.LOGIN, cl);
			ClientThread clTh = new ClientThread(ms);
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Future<Message> future = ex.submit(clTh);
			Message resp = (Message) future.get();
			ex.shutdown();
			Client respCl = (Client) resp.getData();
			int result = resp.getAction();
			if (result == 1) {
				lb_Cont.setText(String.valueOf(respCl.getAccountValue()));
				cli = respCl;
			}
		} catch (ExecutionException | InterruptedException ex) {
			System.out.println("Error talking to " + SavedItems.HOST + ":" + SavedItems.PORT);
		}
		try {
			Client client = new Client();
			client.setIdClient(cli.getIdClient());
			Message ms = new Message(SavedItems.GETBILLS, client);
			ClientThread clTh = new ClientThread(ms);
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Future<Message> future = ex.submit(clTh);
			Message resp = (Message) future.get();
			ex.shutdown();
			respBills = (ArrayList<Bill>) resp.getData();
			int action = resp.getAction();
			paid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			if (action == 1)
				for (int i = 0; i < respBills.size(); i++)
					if (respBills.get(i).getStatus() != 0) {
						paid.getItems().add(respBills.get(i));
					} else {
						unpaid.getItems().add(respBills.get(i));
					}

			payButton.setOnAction(e -> buttonClicked());

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	private void buttonClicked() {
		ObservableList<Bill> movies;
		movies = paid.getSelectionModel().getSelectedItems();
		if (movies.isEmpty())
			lb_Verif.setVisible(true);
		else {
			lb_Verif.setVisible(false);
			double total = cli.getAccountValue();
			double value = 0;
			for (Bill fac : movies) {
				value += fac.getValue() + fac.getFineValue();
			}
			// ..................................................... statusuri
			ArrayList<StatusClient> statusuri = new ArrayList<>();
			Message out = new Message(SavedItems.GETSTATUS, statusuri);
			ClientThread clTH = new ClientThread(out);
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Future<Message> futureObj = ex.submit(clTH);
			Message messagge = new Message();
			try {
				messagge = futureObj.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ex.shutdown();
			statusuri = (ArrayList<StatusClient>) messagge.getData();

			// -----------------------------------client
			for (Bill fac : movies) {

				for (int i = 0; i < statusuri.size(); i++)
					if (statusuri.get(i).getClient().getIdClient() == cli.getIdClient()
							&& fac.getProvider().getName().compareTo(statusuri.get(i).getProvider().getName()) == 0)
						if (statusuri.get(i).getStatus() == 2) {
							lb_Ban.setVisible(true);
							lb_ValBan.setVisible(true);
							lb_ValBan.setText(statusuri.get(i).getProvider().getName());
							break;
						} else {
							lb_Ban.setVisible(false);
							lb_ValBan.setVisible(false);
							if (total - value >= 0) {
								try {
									DecimalFormat df = new DecimalFormat("#.00");
									lb_Cont.setText(String.valueOf(Double.valueOf(df.format(total - value))));
									// ..................................................
									Client clin = new Client();
									clin.setAccountValue(Double.valueOf(df.format(total - value)));
									clin.setIdClient(cli.getIdClient());
									Message ms = new Message(SavedItems.ADDFUNDS, clin);
									ClientThread clTh = new ClientThread(ms);
									ExecutorService exAdd = Executors.newSingleThreadExecutor();
									Future<Message> ftm = exAdd.submit(clTh);
									Message message = (Message) ftm.get();
									exAdd.shutdown();
									Client response = (Client) message.getData();
									cli = response;

									// ............................................

									for (Bill bill : movies) {

										// System.out.println("m este : " + m);
										for (int j = 0; j < respBills.size(); j++) {
											if (bill.getIdBill() == respBills.get(j).getIdBill()) {
												System.out.println(" j = " + j);
												System.out.println(respBills.get(j).getIdBill());
												// ---------------------------------------------------------
												Bill fact = new Bill();
												fact.setStatus(Byte.valueOf("0"));
												fact.setIdBill(respBills.get(j).getIdBill());
												Message mesaj = new Message(SavedItems.SETSTATUS, fact);
												ClientThread clTh2 = new ClientThread(mesaj);
												ExecutorService ex2 = Executors.newSingleThreadExecutor();
												Future<Message> ftm2 = ex2.submit(clTh2);
												Message message2 = (Message) ftm2.get();
												ex2.shutdown();

												// ---------------------------------------------------------------
												// System.out.println("str[0]= "
												// + str[0]);
												unpaid.getItems().add(respBills.get(i));
												Provider fr = new Provider();
												fr.setIdProvider(respBills.get(j).getProvider().getIdProvider());
												fr.setAccountValue(Double.valueOf(
														df.format(respBills.get(j).getProvider().getAccountValue()
																+ bill.getValue())));
												Message mssg = new Message(SavedItems.UPDATEACCPROV, fr);
												ClientThread clFr = new ClientThread(mssg);
												ExecutorService exx2 = Executors.newSingleThreadExecutor();
												Future<Message> ftr = exx2.submit(clFr);
												Message messsage2 = (Message) ftr.get();
												ex.shutdown();
												Provider response2 = (Provider) messsage2.getData();
												fr = response2;
												System.out.println(
														fr.getName() + "are valoarea de" + fr.getAccountValue());
												paid.getItems().removeAll(bill);
											}
										}
									}

								} catch (InterruptedException | ExecutionException e) {
									System.out.println("Error talking to " + SavedItems.HOST + ":" + SavedItems.PORT);
								}
							} else {
								lb_Verif.setVisible(true);
								lb_Verif.setText("Bani insuficienti in cont");
							}

						}
			}
		}
	}

	@FXML
	void onBack(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainFrame.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(scene);
		stg.show();
	}

}
