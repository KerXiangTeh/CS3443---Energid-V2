package application.controller;

import java.io.IOException;

import application.model.Profile;
import application.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller class for managing the user login
 *
 */
public class LoginController extends BaseController{

	@FXML
	private Label lblMessage;

	@FXML
	private TextField txtUsername;

	@FXML
	private TextField txtPassphrase;

	@FXML
	private Button btnLogin;

	@FXML
	void onLoginClicked(ActionEvent event) throws IOException {
		String username = txtUsername.getText();
		String password = txtPassphrase.getText();
		User u = User.validate(username, password);
		if (u == null) {
			// the entered username or the passphrase did not match with any user data we
			// have show an error message
			lblMessage.setText("Invalid Username or Passphrase");
		} else {
			Profile.setUser(u);

			// clear any error messages
			lblMessage.setText("");
			// load the role view with the found user
			ControllerUtils.openStatsHomePage(primaryStage);

		}

	}


}
