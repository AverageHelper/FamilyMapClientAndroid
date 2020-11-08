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
import com.example.familymapclient.transport.login.LoginException;
import com.example.familymapclient.transport.register.RegisterException;
import com.example.familymapclient.ui.CheckboxListener;
import com.example.familymapclient.ui.RadioGroupListener;
import com.example.familymapclient.ui.TextFieldListener;
import com.google.android.material.snackbar.Snackbar;

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
	@Nullable Integer loginFailureHandler = null;
	
	private @NonNull String hostName = "";
	private @NonNull String portNumber = "";
	private boolean prefersHTTPS = false;
	private @NonNull String username = "";
	private @NonNull String password = "";
	private @NonNull String firstName = "";
	private @NonNull String lastName = "";
	private @NonNull String email = "";
	private @Nullable Gender gender = null;
	
	private EditText hostField;
	private EditText portField;
	private CheckBox httpsSelector;
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
		connectInputFields();
		findButtons(view);
		connectButtons();
		
		prefillInputFields();
		updateButtonActivity();
		
		if (auth.isSignedIn()) {
			navigateToSecondFragment();
		}
	}
	
	private void setupAuthListeners() {
		signedInHandler = auth.addAuthStateDidChangeHandler(authToken -> {
			if (authToken != null) {
				// Signed in
				this.navigateToSecondFragment();
			}
		});
		loginFailureHandler = auth.addFailureHandler(error -> {
			if (error instanceof Exception) {
				Exception e = (Exception) error;
				if (e.getMessage() != null) {
					this.presentMessage(e.getMessage());
				} else {
					this.presentMessage(e.toString());
				}
				
			} else {
				this.presentMessage(error.toString());
			}
		});
	}
	
	private void prepareForNavigation() {
		if (signedInHandler != null) {
			auth.removeAuthStateDidChangeHandler(signedInHandler);
			signedInHandler = null;
		}
		if (loginFailureHandler != null) {
			auth.removeFailureHandler(loginFailureHandler);
			loginFailureHandler = null;
		}
	}
	
	private void navigateToSecondFragment() {
		prepareForNavigation();
		NavController navController = NavHostFragment.findNavController(LoginFragment.this);
		navController.navigate(R.id.action_LoginFragment_to_SecondFragment);
	}
	
	private void presentMessage(@NonNull String text) {
		View view = getView();
		if (view != null) {
			Snackbar
				.make(view, text, Snackbar.LENGTH_LONG)
				.setAction("Tapped Error", null)
				.show();
		}
	}
	
	private void findInputFields(@NonNull View view) {
		hostField = view.findViewById(R.id.input_server_host);
		portField = view.findViewById(R.id.input_server_port);
		httpsSelector = view.findViewById(R.id.input_uses_https);
		usernameField = view.findViewById(R.id.input_username);
		passwordField = view.findViewById(R.id.input_password);
		firstNameField = view.findViewById(R.id.input_first_name);
		lastNameField = view.findViewById(R.id.input_last_name);
		emailField = view.findViewById(R.id.input_email_address);
		genderField = view.findViewById(R.id.input_gender);
	}
	
	private void prefillInputFields() {
		if (!hostName.isEmpty()) {
			hostField.setText(hostName);
		} if (auth.getHostname() != null) {
			hostField.setText(auth.getHostname());
		}
		
		if (!portNumber.isEmpty()) {
			portField.setText(portNumber);
		} else if (auth.getPortNumber() != null) {
			portField.setText(String.format(Locale.getDefault(), "%1d", auth.getPortNumber()));
		}
		
		httpsSelector.setChecked(prefersHTTPS || auth.usesSecureProtocol());
		usernameField.setText(username);
		passwordField.setText(password);
		firstNameField.setText(firstName);
		lastNameField.setText(lastName);
		emailField.setText(email);
		
		if (gender == null) {
			genderField.check(-1);
			
		} else if (gender == Gender.MALE) {
			genderField.check(R.id.input_gender_male);
			
		} else if (gender == Gender.FEMALE) {
			genderField.check(R.id.input_gender_female);
		}
	}
	
	private void connectInputFields() {
		hostField.addTextChangedListener(new TextFieldListener(text -> {
			this.hostName = text;
			updateButtonActivity();
		}));
		portField.addTextChangedListener(new TextFieldListener(text -> {
			this.portNumber = text;
			updateButtonActivity();
		}));
		httpsSelector.setOnCheckedChangeListener(new CheckboxListener(isOn -> this.prefersHTTPS = isOn));
		usernameField.addTextChangedListener(new TextFieldListener(text -> {
			this.username = text;
			updateButtonActivity();
		}));
		passwordField.addTextChangedListener(new TextFieldListener(text -> {
			this.password = text;
			updateButtonActivity();
		}));
		firstNameField.addTextChangedListener(new TextFieldListener(text -> {
			this.firstName = text;
			updateButtonActivity();
		}));
		lastNameField.addTextChangedListener(new TextFieldListener(text -> {
			this.lastName = text;
			updateButtonActivity();
		}));
		emailField.addTextChangedListener(new TextFieldListener(text -> {
			this.email = text;
			updateButtonActivity();
		}));
		genderField.setOnCheckedChangeListener(new RadioGroupListener(id -> {
			if (id == -1) {
				this.gender = null;
			} else if (id == R.id.input_gender_male) {
				this.gender = Gender.MALE;
			} else if (id == R.id.input_gender_female) {
				this.gender = Gender.FEMALE;
			}
			updateButtonActivity();
		}));
	}
	
	private void findButtons(@NonNull View view) {
		loginButton = view.findViewById(R.id.button_sign_in);
		registerButton = view.findViewById(R.id.button_register);
	}
	
	private void connectButtons() {
		loginButton.setOnClickListener(button -> login());
		registerButton.setOnClickListener(button -> register());
	}
	
	private void updateButtonActivity() {
		loginButton.setEnabled(
			!hostName.isEmpty() &&
				!portNumber.isEmpty() &&
				!username.isEmpty() &&
				!password.isEmpty()
		);
		registerButton.setEnabled(
			!hostName.isEmpty() &&
				!portNumber.isEmpty() &&
				!username.isEmpty() &&
				!password.isEmpty() &&
				!firstName.isEmpty() &&
				!lastName.isEmpty() &&
				!email.isEmpty() &&
				gender != null
		);
	}
	
	
	
	// ** Actions
	
	private void login() {
		try {
			updateServerLocation();
			auth.signIn(username, password);
			
		} catch (LoginException e) {
			presentMessage(e.getReason().getMessage(getActivity()));
		}
	}
	
	private void register() {
		try {
			updateServerLocation();
			auth.register(username, password, email, firstName, lastName, gender);
			
		} catch (RegisterException e) {
			presentMessage(e.getReason().getMessage(getActivity()));
		}
	}
	
	
	private void updateServerLocation() {
		auth.setUsesSecureProtocol(httpsSelector.isChecked());
		
		if (hostName.isEmpty()) {
			auth.setHostname(null);
		} else {
			auth.setHostname(hostName);
		}
		
		if (portNumber.isEmpty()) {
			auth.setPortNumber(null);
		} else {
			auth.setPortNumber(Integer.parseInt(portNumber));
		}
	}
}
