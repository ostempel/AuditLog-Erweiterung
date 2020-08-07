package edu.hm.dako.chat.AuditLogServer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.common.SystemConstants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * GUI fuer das Starten/ Stoppen/ Beenden des Servers.
 * 
 * @author Oliver
 */
public class AuditLogServerGUI extends Application {

	private static Log log = LogFactory.getLog(AuditLogServerGUI.class);

	final VBox pane = new VBox(5);

	// Interface der Chat-Server-Implementierung
	private static AuditLogServerInterface auditLogServer;

	//ChoiceBox -> TCP und UDP auswaehlbar
	private ChoiceBox<String> choiceBox;

	//Kontroll-Buttons des AuditLog-Servers
	private Button startButton;
	private Button stopButton;
	private Button finishButton;

	// Moegliche Belegungen des Implementierungsfeldes in der GUI
	ObservableList<String> implTypeOptions = FXCollections.observableArrayList(
			SystemConstants.AUDIT_LOG_SERVER_TCP_IMPL, SystemConstants.AUDIT_LOG_SERVER_UDP_IMPL);

	public AuditLogServerGUI() {
	}

	public static void main(String[] args) {
		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_tcp.properties", 60 * 1000);
		launch(args);
	}
	
	/**
	 * Erstellt und Startet die GUI.
	 */
	@Override
	public void start(final Stage stage) throws Exception {

		stage.setTitle("AuditLogServerGUI");
		stage.setScene(new Scene(pane, 	415, 100));
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					AuditLogServerGUI.auditLogServer.stop();
				} catch (Exception ex) {
					AuditLogServerGUI.log.error("Fehler beim Stoppen des AuditLog-Servers");
					ExceptionHandler.logException(ex);
				}
			}
		});

		pane.setStyle("-fx-background-color: cornsilk");
		pane.setPadding(new Insets(4, 4, 4, 4));
		
		pane.getChildren().add(createInput());

		pane.getChildren().add(createSeperator("", 415));
		pane.getChildren().add(createButtonPane());

		reactOnStartButton();
		reactOnStopButton();
		reactOnFinishButton();
		stopButton.setDisable(true);
	}
	
	/**
	 * Gib das Layout fuer den GUI zurueck.
	 * 
	 * @return Layout des GUI
	 */
	private GridPane createInput() {
		final GridPane inputPane = new GridPane();
		
		inputPane.setPadding(new Insets(5, 5, 5, 5));
		Label impl = createLabel("Servertyp:");
		choiceBox = createChoiceBox();
		
		inputPane.add(impl, 1, 3);
		inputPane.add(choiceBox, 3, 3);
		
		return inputPane;
	}
	
	/**
	 * Erstellt eine ChoiceBox um den Implementierungstyp auszuwählen.
	 * 
	 * @return ChoiceBox<String>
	 */
	private ChoiceBox<String> createChoiceBox() {
		ChoiceBox<String> box = new ChoiceBox<String>(implTypeOptions);
		box.setMinSize(200, 25);
		box.setMaxSize(200, 25);
		box.setValue(implTypeOptions.get(0));
		box.setStyle(
				"-fx-background-color: white; -fx-border-color: lightgrey; -fx-border-radius: 5px, 5px, 5px, 5px");
		return box;
	}

	/**
	 * Pane fuer Buttons erzeugen
	 * 
	 * @return HBox
	 */
	private HBox createButtonPane() {
		final HBox buttonPane = new HBox(5);

		startButton = new Button("Server starten");
		stopButton = new Button("Server stoppen");
		finishButton = new Button("Beenden");

		buttonPane.getChildren().addAll(startButton, stopButton, finishButton);
		buttonPane.setAlignment(Pos.CENTER);
		return buttonPane;
	}

	/**
	 * Label erzeugen
	 * 
	 * @param value
	 * @return Label
	 */
	private Label createLabel(String value) {
		final Label label = new Label(value);
		label.setMinSize(200, 25);
		label.setMaxSize(200, 25);
		return label;
	}


	/**
	 * Trennlinie erstellen
	 * 
	 * @param value
	 *          Text der Trennlinie
	 * @param size
	 *          Groesse der Trennlinie
	 * @return Trennlinie
	 */
	private HBox createSeperator(String value, int size) {
		// Separator erstellen
		final HBox labeledSeparator = new HBox();
		final Separator rightSeparator = new Separator(Orientation.HORIZONTAL);
		final Label textOnSeparator = new Label(value);

		textOnSeparator.setFont(Font.font(12));

		rightSeparator.setMinWidth(size);
		rightSeparator.setMaxWidth(size);

		labeledSeparator.getChildren().add(textOnSeparator);
		labeledSeparator.getChildren().add(rightSeparator);
		labeledSeparator.setAlignment(Pos.BASELINE_LEFT);

		return labeledSeparator;
	}

	/**
	 * Reaktion auf das Betaetigen des Start-Buttons
	 */
	private void reactOnStartButton() {
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// Implementierungstyp, der zu starten ist, ermitteln und
				// AuditLog-Server starten
				String implType = readImplTypeChoiceBox();
				
				try {
					
					//AuditLogServer starten
					startAuditLogServer(implType);
				} catch (Exception e) {
					setAlert(
							"Der Server konnte nicht gestartet werden, evtl. laeuft ein anderer Server mit dem Port");
					return;
				}
				
				startButton.setDisable(true);
				stopButton.setDisable(false);
				finishButton.setDisable(true);

			}
		});
	}

	/**
	 * Reaktion auf das Betaetigen des Stop-Buttons
	 */
	private void reactOnStopButton() {

		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					auditLogServer.stop();
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Fehler beim Stoppen des AuditLog-Servers");
					ExceptionHandler.logException(e);
				}

				startButton.setDisable(false);
				stopButton.setDisable(true);
				finishButton.setDisable(false);
			}
		});
	}

	/**
	 * Reaktion auf das Betaetigen des Finish-Buttons
	 */
	private void reactOnFinishButton() {
		finishButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (auditLogServer != null) {
					try {
						AuditLogServerGUI.auditLogServer.stop();
					} catch (Exception var3) {
						AuditLogServerGUI.log.error("Fehler beim Stoppen des AuditLog-Servers");
						ExceptionHandler.logException(var3);
					}
				}
				System.out.println("AuditLogServer-GUI ordnungsgemaess beendet");
				log.debug("Schliessen-Button betaetigt");
				System.exit(0);
			}
		});
	}

	/**
	 * Implementierungstyp aus GUI auslesen
	 */
	private String readImplTypeChoiceBox() {
		String implType = new String(choiceBox.getValue());
		
		return implType;
	}
	
	/**
	 * Startet den Server (Entweder TCP oder UDP).
	 * 
	 * @param implType
	 * @throws Exception
	 */
	private void startAuditLogServer(String implType) throws Exception {
		
		if (implType.equals(SystemConstants.AUDIT_LOG_SERVER_TCP_IMPL)) {
			auditLogServer = AuditLogServerFactory.getTcpServer();
		} else if (implType.equals(SystemConstants.AUDIT_LOG_SERVER_UDP_IMPL)) {
			auditLogServer = AuditLogServerFactory.getUdpServer();
		}
		// Server starten
		auditLogServer.start();
	}

	/**
	 * Oeffnen eines Dialogfensters, wenn ein Fehler bei der Eingabe auftritt
	 *
	 * @param message
	 */
	private void setAlert(String message) {
		;
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Fehler!");
		alert.setHeaderText(
				"Bei den von ihnen eingegebenen Parametern ist ein Fehler aufgetreten:");
		alert.setContentText(message);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				alert.showAndWait();
			}
		});
	}
}