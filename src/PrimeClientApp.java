import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 
 *  @author Kevin Wilding Montero Henriquez
 *  
 */

/**
 * This class takes the user input and sends it to the server to check if the
 * given number is prime or not.
 * 
 *
 */
public class PrimeClientApp extends Application {

	// Input and output streams
	DataOutputStream outStream;
	DataInputStream inStream;

	/**
	 * Main method to launch the Application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Panel to hold the label and text field
		BorderPane tfPane = new BorderPane();
		tfPane.setPadding(new Insets(5, 5, 5, 5));
		tfPane.setStyle("-fx-border-color: green");
		tfPane.setLeft(new Label("Enter a number to check prime: "));

		TextField tf = new TextField();
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		tfPane.setCenter(tf);

		BorderPane mainPane = new BorderPane();
		// Text area to display user input and result
		TextArea ta = new TextArea();
		mainPane.setCenter(new ScrollPane(ta));
		mainPane.setTop(tfPane);

		// Create a scene and place it in the stage
		Scene scene = new Scene(mainPane, 450, 200);
		primaryStage.setTitle("Check Prime Number - Client"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		tf.setOnAction(e -> {
			String text = tf.getText();
			int number = Integer.parseInt(text);
			// Send number to the server
			try {
				outStream.writeInt(number);
				outStream.flush();

				// Get result from the server
				boolean prime = inStream.readBoolean();

				// Add to the text area display
				ta.appendText("number is " + number + "\n");
				if (prime) {
					ta.appendText(number + " is prime." + '\n');
				} else {
					ta.appendText(number + " is not prime." + '\n');
				}
			} catch (IOException ex) {
				Platform.runLater(() -> ta.appendText("-----\nError: " + ex.toString() + '\n'));
			}
		});

		try {
			// client socket to connect to the server
			Socket socket = new Socket("localhost", 7000);

			// input stream to receive data from the server
			inStream = new DataInputStream(socket.getInputStream());

			// output stream to send data to the server
			outStream = new DataOutputStream(socket.getOutputStream());

		} catch (IOException ex) {
			ta.appendText("Error: " + ex.toString() + '\n');
		}
	}
}