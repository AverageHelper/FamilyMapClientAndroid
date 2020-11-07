package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familymapclient.auth.Auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {
	
	public Auth auth = Auth.shared();
	@Nullable Integer signedOutHandler = null;
	
	@Override
	public View onCreateView(
		@NonNull LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_second, container, false);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAuthListeners();
		
		if (!auth.isSignedIn()) {
			navigateToLoginFragment();
		}
	}
	
	private void setupAuthListeners() {
		signedOutHandler = auth.addAuthStateDidChangeHandler(user -> {
			if (user == null) {
				// Signed out
				this.navigateToLoginFragment();
			}
		});
	}
	
	private void prepareForNavigation() {
		if (signedOutHandler != null) {
			auth.removeAuthStateDidChangeHandler(signedOutHandler);
			signedOutHandler = null;
		}
	}
	
	private void navigateToLoginFragment() {
		prepareForNavigation();
		NavController navController = NavHostFragment.findNavController(SecondFragment.this);
		navController.navigate(R.id.action_SecondFragment_to_LoginFragment);
	}
	
}
