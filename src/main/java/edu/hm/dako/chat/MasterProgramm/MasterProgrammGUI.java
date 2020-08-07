package edu.hm.dako.chat.MasterProgramm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * GUI zur Veranschaulichung des Ausgewerteten AuditLogs.
 * 
 * @author OMMS
 */
public class MasterProgrammGUI extends Application {
	
	final VBox pane = new VBox(5);
	
	//Klasse, die die Auswetung uebernimmt
	private AuditLogEvaluator evaluator;
	
	//Update Button
	private Button updateButton;
	
	//Textfields fuer Update der Asuwertung
	private TextField countAllPdu;
	private TextField countClients;
	private TextField countMessages;
	private TextField countLogins;
	private TextField countLogouts;
	private TextField firstSent;
	private TextField lastSent;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("AuditLog-Auswertung");
		stage.setScene(new Scene(pane, 500, 345));
		stage.show();

		pane.setStyle("-fx-background-color: cornsilk");
		pane.setPadding(new Insets(4, 4, 4, 4));
		
		evaluator = new AuditLogEvaluator();

		pane.getChildren().add(createSeperator("Daten der Auswertung", 370));
		pane.getChildren().add(createInformationPane());
		pane.getChildren().add(createSeperator("", 488));
		pane.getChildren().add(createUpdateButtonPane());
		
		updateEvaluator();
	}
	
	/**
	 * Erzeugt ein Pane, dass alle Information visuell darstellt.
	 * 
	 * @return HBox mit Labels und Textfields.
	 */
	private HBox createInformationPane() {
		final HBox pane = new HBox(50);
		pane.setAlignment(Pos.CENTER);
		
		Label countAllPduLabel = createLabel("Anzahl aller PDUs: ");
		countAllPdu= createNotEditableTextfield(evaluator.getCountAllPDU() + "");
		
		Label countClientsLabel = createLabel("Anzahl Clients: ");
		countClients = createNotEditableTextfield(evaluator.getCountClients() + "");
		
		Label countMessagesLabel = createLabel("Anzahl Nachrichten: ");
		countMessages = createNotEditableTextfield(evaluator.getCountChatEvents() + "");
		
		Label countLoginsLabel = createLabel("Anzahl Logins: ");
		countLogins = createNotEditableTextfield(evaluator.getCountLoginEvents() + "");
		
		Label countLogoutsLabel = createLabel("Anzahl Logouts: ");
		countLogouts = createNotEditableTextfield(evaluator.getCountLogoutEvents() + "");
		
		Label firstSentLabel = createLabel("Erste Nachricht gesendet um: ");
		firstSent = createNotEditableTextfield(evaluator.getFirstDate() + "");
		
		Label lastSentLabel = createLabel("Letzte Nachricht gesendet um: ");
		lastSent = createNotEditableTextfield(evaluator.getLastDate() + "");
		
		final VBox labelBox = new VBox(10);
		labelBox.setAlignment(Pos.CENTER);
		labelBox.getChildren().add(countAllPduLabel);
		labelBox.getChildren().add(countClientsLabel);
		labelBox.getChildren().add(countMessagesLabel);
		labelBox.getChildren().add(countLoginsLabel);
		labelBox.getChildren().add(countLogoutsLabel);
		labelBox.getChildren().add(firstSentLabel);
		labelBox.getChildren().add(lastSentLabel);
		
		final VBox infoBox = new VBox(10);
		infoBox.setAlignment(Pos.CENTER);
		infoBox.getChildren().add(countAllPdu);
		infoBox.getChildren().add(countClients);
		infoBox.getChildren().add(countMessages);
		infoBox.getChildren().add(countLogins);
		infoBox.getChildren().add(countLogouts);
		infoBox.getChildren().add(firstSent);
		infoBox.getChildren().add(lastSent);
		
		pane.getChildren().addAll(labelBox, infoBox);
		
		return pane;
	}
	
	/**
	 * Erzeugen eines Panes mit einem Button zum Updaten.
	 * 
	 * @return UpdateButton-Pane
	 */
	private HBox createUpdateButtonPane() {
		final HBox pane = new HBox(50);
		pane.setAlignment(Pos.CENTER);
		
		updateButton = new Button("Update");
		pane.getChildren().add(updateButton);
		
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				updateEvaluator();
			}
		});
		return pane;
	}
	
	/**
	 * Updatet alle Felder mit neuen AuditLog-Saetzen.
	 */
	private void updateEvaluator() {
		evaluator.update(AuditLogFileReader.getAllLinesAsList());
		evaluator.evaluate();
		
		countAllPdu.setText(evaluator.getCountAllPDU() + "");
		countClients.setText(evaluator.getCountClients() + "");
		countMessages.setText(evaluator.getCountChatEvents() + "");
		countLogins.setText(evaluator.getCountLoginEvents() + "");
		countLogouts.setText(evaluator.getCountLogoutEvents() + "");
		firstSent.setText(evaluator.getFirstDate() + "");
		lastSent.setText(evaluator.getLastDate() + "");
	}
	
	/**
	 * Label erzeugen
	 * 
	 * @param value
	 * @return Label
	 */
	private Label createLabel(String value) {
		final Label label = new Label(value);
		label.setMinSize(200, 28);
		label.setMaxSize(200, 28);
		return label;
	}
	
	/**
	 * Nicht editierbares Feld erzeugen
	 * 
	 * @param value
	 *          Feldinhalt
	 * @return Textfeld
	 */
	private TextField createNotEditableTextfield(String value) {
		TextField textField = new TextField(value);
		textField.setMaxSize(180, 28);
		textField.setMinSize(180, 28);
		textField.setEditable(false);
		textField.setStyle(
				"-fx-background-color: white; -fx-border-color: lightgrey; -fx-border-radius: 5px, 5px, 5px, 5px");
		return textField;
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
}