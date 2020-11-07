package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.auth.LoginException;
import com.example.familymapclient.auth.RegisterException;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import model.Gender;

public class LoginFragment extends Fragment {
	
	public Auth auth = Auth.shared();
	@Nullable Integer signedInHandler = null;
	
	private EditText hostField;
	private EditText portField;
	private CheckBox prefersHTTPS;
	private EditText usernameField;
	private EditText passwordField;
	private EditText firstNameField;
	private EditText lastNameField;
	private EditText emailField;
	private RadioGroup genderField;
	
	private Button loginButton;
	private Button registerButton;
	
	@Override
	public View onCreateView(
		@NonNull LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_login, container, false);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAuthListeners();
		findInputFields(view);
		prefillInputFields();
		findButtons(view);
		connectButtons();
		
		if (auth.isSignedIn()) {
			navigateToSecondFragment();
		}
	}
	
	private void setupAuthListeners() {
		signedInHandler = auth.addAuthStateDidChangeHandler(user -> {
			if (user != null) {
				// Signed in
				this.navigateToSecondFragment();
			}
		});
	}
	
	private void prepareForNavigation() {
		if (signedInHandler != null) {
			auth.removeAuthStateDidChangeHandler(signedInHandler);
			signedInHandler = null;
		}
	}
	
	private void navigateToSecondFragment() {
		prepareForNavigation();
		NavController navController = NavHostFragment.findNavController(LoginFragment.this);
		navController.navigate(R.id.action_LoginFragment_to_SecondFragment);
	}
	
	private void findInputFields(@NonNull View view) {
		hostField = view.findViewById(R.id.input_server_host);
		portField = view.findViewById(R.id.input_server_port);
		prefersHTTPS = view.findViewById(R.id.input_uses_https);
		usernameField = view.findViewById(R.id.input_username);
		passwordField = view.findViewById(R.id.input_password);
		firstNameField = view.findViewById(R.id.input_first_name);
		lastNameField = view.findViewById(R.id.input_last_name);
		emailField = view.findViewById(R.id.input_email_address);
		genderField = view.findViewById(R.id.input_gender);
	}
	
	private void prefillInputFields() {
		if (auth.getHostname() != null) {
			hostField.setText(auth.getHostname());
		}
		if (auth.getPortNumber() != null) {
			portField.setText(String.format(Locale.getDefault(), "%1d", auth.getPortNumber()));
		}
		prefersHTTPS.setChecked(auth.usesSecureProtocol());
	}
	
	private void findButtons(@NonNull View view) {
		loginButton = view.findViewById(R.id.button_sign_in);
		registerButton = view.findViewById(R.id.button_register);
	}
	
	private void connectButtons() {
		loginButton.setOnClickListener(button -> login());
		registerButton.setOnClickListener(button -> register());
	}
	
	private @Nullable Gender selectedGender() {
		int selected = genderField.getCheckedRadioButtonId();
		if (selected == R.id.input_gender_male) {
			return Gender.MALE;
			
		} else if (selected == R.id.input_gender_female) {
			return Gender.FEMALE;
		}
		
		return null;
	}
	
	
	
	// ** Actions
	
	private void login() {
		try {
			String username = usernameField.getText().toString();
			String password = passwordField.getText().toString();
			
			updateServerLocation();
			
			auth.signIn(username, password);
		} catch (LoginException e) {
			// TODO: Log and display the error message
		}
	}
	
	private void register() {
		try {
			String username = usernameField.getText().toString();
			String password = passwordField.getText().toString();
			String email = emailField.getText().toString();
			String firstName = firstNameField.getText().toString();
			String lastName = lastNameField.getText().toString();
			
			updateServerLocation();
			
			auth.register(username, password, email, firstName, lastName, selectedGender());
		} catch (RegisterException e) {
			// TODO: Log and display the error message
		}
	}
	
	
	private void updateServerLocation() {
		auth.setUsesSecureProtocol(prefersHTTPS.isChecked());
		
		if (hostField.getText().toString().isEmpty()) {
			auth.setHostname(null);
		} else {
			auth.setHostname(hostField.getText().toString());
		}
		
		if (portField.getText().toString().isEmpty()) {
			auth.setPortNumber(null);
		} else {
			auth.setPortNumber(Integer.parseInt(portField.getText().toString()));
		}
	}
}
