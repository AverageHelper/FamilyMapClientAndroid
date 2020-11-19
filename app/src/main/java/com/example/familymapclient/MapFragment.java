package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familymapclient.auth.Auth;
import com.google.android.gms.maps.MapView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class MapFragment extends Fragment {
	
	public Auth auth = Auth.shared();
	@Nullable Integer signedOutHandler = null;
	
	private MapView mapView;
	
	@Override
	public View onCreateView(
		@NonNull LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_map, container, false);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAuthListeners();
		
		findMapView(view);
		setupMapView();
		
		if (!auth.isSignedIn()) {
			navigateToLoginFragment();
		}
	}
	
	private void setupAuthListeners() {
		signedOutHandler = auth.addAuthStateDidChangeHandler(authToken -> {
			if (authToken == null) {
				// Signed out
				this.navigateToLoginFragment();
			}
		});
	}
	
	private void findMapView(@NonNull View view) {
		mapView = view.findViewById(R.id.map_view);
	}
	
	private void setupMapView() {
		mapView.getMapAsync(googleMap -> {
			// Add markers and lines
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
		NavController navController = NavHostFragment.findNavController(MapFragment.this);
		navController.navigate(R.id.action_MapFragment_to_LoginFragment);
	}
	
}
