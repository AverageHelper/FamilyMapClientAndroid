package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.auth.LoginException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class LoginFragment extends Fragment {
	
	private Auth auth;
	@NonNull String username = "";
	@NonNull String password = "";
	
	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_login, container, false);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setup();
		connectInputFields(view);
		connectButtons(view);
	}
	
	private void setup() {
		auth = Auth.shared();
	}
	
	private void connectInputFields(@NonNull View view) {
		EditText usernameField = view.findViewById(R.id.input_username);
		usernameField.setText(username);
		// TODO: Add listener
		
		EditText passwordField = view.findViewById(R.id.input_password);
		passwordField.setText(password);
		// TODO: Add listener
	}
	
	private void connectButtons(@NonNull View view) {
		view.findViewById(R.id.button_sign_in)
			.setOnClickListener(button -> login());
//			.navigate(R.id.action_FirstFragment_to_SecondFragment));
		
		view.findViewById(R.id.button_register)
			.setOnClickListener(button -> register());
	}
	
	private void login() {
		try {
			auth.signIn("", "");
		} catch (LoginException e) {
			// TODO: Log and display the error message
		}
	}
	
	private void register() {
	
	}
}
