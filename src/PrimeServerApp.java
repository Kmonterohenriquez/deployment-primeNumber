import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * This class gets number from a client and sends back true if the number is
 * prime, and false otherwise.
 *
 */
public class PrimeServerApp extends Application {

	/**
	 * Main method to launch the Application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		TextArea ta = new TextArea();

		Scene scene = new Scene(new ScrollPane(ta));
		stage.setScene(scene);

		stage.setTitle("Server");
		stage.show();

		// start the server thread.
		new Thread(() -> {
			try {
				// Create server socket
				ServerSocket serverSocket = new ServerSocket(7000);
				Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));
				// Listen for client's connection request
				Socket socket = serverSocket.accept();

				// Create input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					// Receive number from the client
					int number = inputFromClient.readInt();

					// Check if the given number is prime
					boolean prime = isPrime(number);

					// Send result to the client
					outputToClient.writeBoolean(prime);

					Platform.runLater(() -> ta
							.appendText("Number received from client to check prime number is: " + number + '\n'));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	/**
	 * Return true if the given number is prime, otherwise, false.
	 * 
	 * @param number
	 * @return
	 */
	private static boolean isPrime(int number) {
		boolean isPrime = true;
		for (int i = 2; i <= number / 2; ++i) {
			// Check if the number is divisible WITHOUT REMINDER.
			if (number % i == 0) {
				// divisible WITHOUT REMINDER.
				// So, not prime.
				isPrime = false;
				break;
			}
		}
		return isPrime;
	}

}