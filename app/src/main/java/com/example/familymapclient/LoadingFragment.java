package com.example.familymapclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import model.Person;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;
import com.example.familymapclient.data.fetch.EventsRequester;
import com.example.familymapclient.data.fetch.PersonsRequester;
import com.example.familymapclient.transport.ServerLocation;

/**
 * A simple {@link Fragment} subclass that displays the application's initial loading status.
 * This fragment downloads user data from the Family Map server if there is a stored auth token.
 * If the download fails, then the auth token is forgotten and the fragment navigates to the Login
 * fragment.
 */
public class LoadingFragment extends Fragment {
	
	public Auth auth = Auth.shared();
	private @Nullable Integer signedInHandler = null;
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_loading, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setupAuthListeners();
		
		if (auth.isSignedIn()) {
			// Signed in.
			fetchPersonAndEvents();
			
		} else {
			// Signed out. The download might've failed.
			navigateToLoginFragment();
		}
	}
	
	@Override
	public void onStop() {
		if (signedInHandler != null) {
			auth.removeAuthStateDidChangeHandler(signedInHandler);
			signedInHandler = null;
		}
		super.onStop();
	}
	
	
	// ** Data Cache
	
	private void setupAuthListeners() {
		signedInHandler = auth.addAuthStateDidChangeHandler(this::handleAuthChange);
	}
	
	private void handleAuthChange(@Nullable String authToken) {
		if (authToken == null) {
			// Signed out. The download might've failed.
			presentToast("Download failed. Please log in again.");
			navigateToLoginFragment();
			
		} else {
			// Signed in.
			fetchPersonAndEvents();
		}
	}
	
	
	// ** Data Cache
	
	private @Nullable PersonsRequester personsFetch = null;
	private @Nullable EventsRequester eventsFetch = null;
	
	private void fetchPersonAndEvents() {
		if (auth.getPersonID() == null || auth.getAuthToken() == null ||
				(!personCache.isEmpty() && !eventCache.isEmpty())) {
			// No need to load data
			navigateToMapFragment();
			return;
		}
		
		// Load data
		ServerLocation server = new ServerLocation(
			auth.getHostname(),
			auth.getPortNumber(),
			auth.usesSecureProtocol()
		);
		personsFetch = personCache.fetchAllPersons(
			server,
			auth.getAuthToken(),
			persons -> {
				if (personsFetch != null) {
					if (!MainActivity.didWelcomeUser) {
						Person currentUser = personCache.getValueWithID(auth.getPersonID());
						if (currentUser != null) {
							presentToast("Welcome, " +
								currentUser.getFirstName() + " " +
								currentUser.getLastName() + "!");
						} else {
							presentToast("Welcome, User!");
						}
						MainActivity.didWelcomeUser = true;
					}
					personsFetch = null;
					if (eventsFetch == null) {
						navigateToMapFragment();
					}
				}
			},
			error -> {
				handleAsyncFailure(error);
				personsFetch = null;
			}
		);
		eventsFetch = eventCache.fetchAllEvents(
			server,
			auth.getAuthToken(),
			events -> {
				if (eventsFetch != null) {
					eventsFetch = null;
					if (personsFetch == null) {
						navigateToMapFragment();
					}
				}
			},
			error -> {
				handleAsyncFailure(error);
				eventsFetch = null;
			}
		);
	}
	
	private void handleAsyncFailure(@NonNull Throwable error) {
		// Toast the error message
		if (error instanceof Exception) {
			Exception e = (Exception) error;
			if (e.getMessage() != null) {
				presentToast(e.getMessage());
			} else {
				presentToast(e.toString());
			}
			
		} else {
			presentToast(error.toString());
		}
		
		// Stop our async handlers and log out
		this.personsFetch = null;
		this.eventsFetch = null;
		auth.logOut();
	}
	
	
	// ** Navigation
	
	private void presentToast(@NonNull String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	private void navigateToLoginFragment() {
		NavController navController = NavHostFragment.findNavController(LoadingFragment.this);
		navController.navigate(R.id.action_LoadingFragment_to_LoginFragment);
	}
	
	private void navigateToMapFragment() {
		NavController navController = NavHostFragment.findNavController(LoadingFragment.this);
		navController.navigate(R.id.action_LoadingFragment_to_MapFragment);
	}
	
}