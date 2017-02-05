package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
 * Controller for the providers frame.
 *
 */
public class ProvidersController implements Initializable {

	@FXML
	private Button btn_Delete;

	@FXML
	private Button onAboneaza;

	@FXML
	private Button onDezaboneaza;

	@FXML
	private Label lb_error;

	@FXML
	private ListView<String> list_FurnServ;

	@FXML
	private Button onInregistreaza;

	@FXML
	private ListView<String> list_Furn;

	List<Bill> respCl = new ArrayList<>();

	static Client cli = LoginController.takeClient();
	public static ArrayList<Provider> furn = new ArrayList<>();

	@FXML
	void onBack(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainFrame.fxml"));
		Scene scene = new Scene(root);
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		scene.getStylesheets().add("/Images/design.css");
		stg.setScene(scene);
		stg.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		list_FurnServ.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		list_Furn.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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

		} catch (ExecutionException | InterruptedException ex) {
			System.out.println("Error talking to " + SavedItems.HOST + ":" + SavedItems.PORT);
		}
		// -------------------------------------------------------furnizorii
		ArrayList<Provider> providers = new ArrayList<>();
		Message mss = new Message(SavedItems.GETPROVIDERS, null);
		ClientThread clThh = new ClientThread(mss);
		ExecutorService exx = Executors.newSingleThreadExecutor();
		Future<Message> future2 = exx.submit(clThh);
		Message response;
		try {
			response = future2.get();
			providers.addAll((ArrayList<Provider>) response.getData());
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		exx.shutdown();

		furn.addAll(providers);
		// ............................................................statusurile
		ArrayList<StatusClient> statusuri = new ArrayList<>();
		Message msgg = new Message(SavedItems.GETSTATUS, statusuri);
		ClientThread clTH = new ClientThread(msgg);
		ExecutorService eX = Executors.newSingleThreadExecutor();
		Future<Message> futureObj = eX.submit(clTH);
		Message messagge = new Message();
		try {
			messagge = futureObj.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eX.shutdown();
		statusuri = (ArrayList<StatusClient>) messagge.getData();
		for (int i = 0; i < statusuri.size(); i++)
			if (statusuri.get(i).getClient().getIdClient() == cli.getIdClient()) {
				if (statusuri.get(i).getStatus() == 1)
					list_FurnServ.getItems()
							.add(String.valueOf(statusuri.get(i).getProvider().getIdProvider()) + "                    "
									+ String.valueOf(statusuri.get(i).getProvider().getName()) + "           "
									+ "ABONAT");
				if (statusuri.get(i).getStatus() == 0)
					list_FurnServ.getItems()
							.add(String.valueOf(statusuri.get(i).getProvider().getIdProvider()) + "                    "
									+ String.valueOf(statusuri.get(i).getProvider().getName()) + "           "
									+ "NEABONAT");
				if (statusuri.get(i).getStatus() == 2)
					list_FurnServ.getItems()
							.add(String.valueOf(statusuri.get(i).getProvider().getIdProvider()) + "                    "
									+ String.valueOf(statusuri.get(i).getProvider().getName()) + "           "
									+ "BANAT");
			}
		for (int i = 0; i < providers.size(); i++) {
			int ok = 0;
			for (int j = 0; j < statusuri.size(); j++)
				if (statusuri.get(j).getProvider().getIdProvider() == providers.get(i).getIdProvider())
					ok = 1;
			if (ok == 0)
				list_Furn.getItems().add(String.valueOf(providers.get(i).getIdProvider()) + "                    "
						+ String.valueOf(providers.get(i).getName()));
		}
		btn_Delete.setOnAction(e -> buttonSterge());

		onDezaboneaza.setOnAction(e -> buttonDezaboneaza());

		onAboneaza.setOnAction(e -> buttonAboneaza());

		onInregistreaza.setOnAction(e -> buttonClicked());

	}

	public void buttonDezaboneaza() {
		ObservableList<String> movies = list_FurnServ.getSelectionModel().getSelectedItems();
		ArrayList<StatusClient> statusuri = new ArrayList<>();
		Message msgg = new Message(SavedItems.GETSTATUS, statusuri);
		ClientThread clTH = new ClientThread(msgg);
		ExecutorService eX = Executors.newSingleThreadExecutor();
		Future<Message> futureObj = eX.submit(clTH);
		Message messagge = new Message();
		try {
			messagge = futureObj.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eX.shutdown();
		statusuri = (ArrayList<StatusClient>) messagge.getData();
		for (String m : movies) {
			String[] str = m.split("\\s+");
			for (int i = 0; i < statusuri.size(); i++)
				if (Integer.parseInt(str[0]) == statusuri.get(i).getProvider().getIdProvider()) {

					StatusClient status = new StatusClient();
					status.setIdStatusClient(statusuri.get(i).getIdStatusClient());
					Message message = new Message(SavedItems.UNSUBSCRIBE, status);
					ClientThread clTh = new ClientThread(message);
					ExecutorService ex = Executors.newSingleThreadExecutor();
					Future<Message> future = ex.submit(clTh);
					Message msg = new Message();
					list_FurnServ.getItems().remove(m);
					list_FurnServ.getItems()
							.add(String.valueOf(statusuri.get(i).getProvider().getIdProvider()) + "                    "
									+ String.valueOf(statusuri.get(i).getProvider().getName()) + "           "
									+ "NEABONAT");
					try {
						msg = future.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ex.shutdown();

				}
		}

	}

	public void buttonAboneaza() {
		ObservableList<String> movies = list_FurnServ.getSelectionModel().getSelectedItems();
		ArrayList<StatusClient> statusuri = new ArrayList<>();
		Message msgg = new Message(SavedItems.GETSTATUS, statusuri);
		ClientThread clTH = new ClientThread(msgg);
		ExecutorService eX = Executors.newSingleThreadExecutor();
		Future<Message> futureObj = eX.submit(clTH);
		Message messagge = new Message();
		try {
			messagge = futureObj.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eX.shutdown();
		statusuri = (ArrayList<StatusClient>) messagge.getData();
		for (String m : movies) {
			String[] str = m.split("\\s+");
			for (int i = 0; i < statusuri.size(); i++)
				if (Integer.parseInt(str[0]) == statusuri.get(i).getProvider().getIdProvider()) {

					StatusClient status = new StatusClient();
					status.setIdStatusClient(statusuri.get(i).getIdStatusClient());
					Message message = new Message(SavedItems.SUBSCRIBE, status);
					ClientThread clTh = new ClientThread(message);
					ExecutorService ex = Executors.newSingleThreadExecutor();
					Future<Message> future = ex.submit(clTh);
					Message msg = new Message();
					list_FurnServ.getItems().remove(m);
					list_FurnServ.getItems()
							.add(String.valueOf(statusuri.get(i).getProvider().getIdProvider()) + "                    "
									+ String.valueOf(statusuri.get(i).getProvider().getName()) + "           "
									+ "ABONAT");
					try {
						msg = future.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ex.shutdown();

				}
		}
	}

	public void buttonSterge() {
		ObservableList<String> movies = list_FurnServ.getSelectionModel().getSelectedItems();

		lb_error.setVisible(false);
		// ................................................statusuri

		Client client = new Client();
		client.setIdClient(cli.getIdClient());
		Message ms = new Message(SavedItems.GETBILLS, client);
		ClientThread clTh = new ClientThread(ms);
		ExecutorService ex = Executors.newSingleThreadExecutor();
		Future<Message> future = ex.submit(clTh);
		Message resp = new Message();
		try {
			resp = (Message) future.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ex.shutdown();
		respCl = (ArrayList<Bill>) resp.getData();
		for (String m : movies) {
			String[] string = m.split("\\s+");

			boolean ok = false;
			for (int i = 0; i < respCl.size(); i++)
				if (Integer.parseInt(string[0]) == respCl.get(i).getProvider().getIdProvider()
						&& respCl.get(i).getStatus() != 0) {
					ok = true;
					lb_error.setVisible(true);
					break;
				} else {
					StatusClient sts = new StatusClient();
					sts.setClient(cli);
					sts.setProvider(respCl.get(i).getProvider());
				}

			if (ok == false)
			// .................................................
			{
				StatusClient sts = new StatusClient();

				ArrayList<StatusClient> statusuri = new ArrayList<>();
				Message msgg = new Message(SavedItems.GETSTATUS, statusuri);
				ClientThread clTH = new ClientThread(msgg);
				ExecutorService eX = Executors.newSingleThreadExecutor();
				Future<Message> futureObj = eX.submit(clTH);
				Message messagge = new Message();
				try {
					messagge = futureObj.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				eX.shutdown();
				statusuri = (ArrayList<StatusClient>) messagge.getData();

				for (int i = 0; i < statusuri.size(); i++)
					if (statusuri.get(i).getClient().getIdClient() == cli.getIdClient()
							&& statusuri.get(i).getProvider().getIdProvider() == Integer.parseInt(string[0]))
						sts.setIdStatusClient(statusuri.get(i).getIdStatusClient());

				Message msg = new Message(SavedItems.DELETESTATUS, sts);
				ClientThread clTh2 = new ClientThread(msg);
				ExecutorService ex2 = Executors.newSingleThreadExecutor();
				Future<Message> future2 = ex2.submit(clTh2);
				// Message messgg = new Message();
				try {
					future2.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ex2.shutdown();

				for (int i = 0; i < statusuri.size(); i++)
					if (statusuri.get(i).getProvider().getIdProvider() == Integer.parseInt(string[0]))
						list_Furn.getItems().add(String.valueOf(statusuri.get(i).getProvider().getIdProvider())
								+ "                    " + String.valueOf(statusuri.get(i).getProvider().getName()));
				list_FurnServ.getItems().remove(m);
			}

		}
	}

	public void buttonClicked() {

		ObservableList<String> movies = list_Furn.getSelectionModel().getSelectedItems();
		for (String m : movies) {
			list_FurnServ.getItems().add(m + "          NEABONAT");
			String[] str = m.split("\\s+");

			StatusClient status = new StatusClient();
			status.setClient(cli);
			for (int i = 0; i < furn.size(); i++)
				if (Integer.parseInt(str[0]) == furn.get(i).getIdProvider())
					status.setProvider(furn.get(i));
			status.setStatus(Byte.valueOf("0"));
			Message msg = new Message(SavedItems.CHECKSTATUS, status);
			ClientThread clTH = new ClientThread(msg);
			ExecutorService eX = Executors.newSingleThreadExecutor();
			Future<Message> futureObj = eX.submit(clTH);
			// Message messagge = new Message();
			try {
				futureObj.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			eX.shutdown();
		}
		list_Furn.getItems().removeAll(movies);

	}
}
