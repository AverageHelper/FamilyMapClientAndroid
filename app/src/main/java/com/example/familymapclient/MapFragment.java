package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import model.Event;
import model.Gender;
import model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback {
	
	public Auth auth = Auth.shared();
	private @Nullable Integer signedOutHandler = null;
	private @Nullable Integer eventCacheHandler = null;
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	private MapView mapView;
	private @Nullable GoogleMap map = null;
	private ConstraintLayout imageContainer;
	private ProgressBar loadingIndicator;
	private ImageView femaleImage;
	private ImageView maleImage;
	private TextView footerText;
	
	private @Nullable Event selectedEvent = null;
	private @Nullable Person personForEvent = null;
	
	
	// ** Lifecycle Events
	
	@Override
	public View onCreateView(
		@NonNull LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_map, container, false);
	}
	
	@Override
	public void onDestroy() {
		mapView.onDestroy();
		prepareForNavigation();
		super.onDestroy();
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setupAuthListeners();
		setupCacheListeners();
		
		findMapView(view, savedInstanceState);
		findFooterViews(view);
		
		if (!auth.isSignedIn()) {
			navigateToLoginFragment();
		}
		
		initializeMaps();
		mapView.getMapAsync(this);
		
		setEvent(null);
	}
	
	private void setupAuthListeners() {
		signedOutHandler = auth.addAuthStateDidChangeHandler(authToken -> {
			if (authToken == null) {
				// Signed out
				this.navigateToLoginFragment();
			}
		});
	}
	
	private void setupCacheListeners() {
		eventCacheHandler = eventCache.addUpdateHandler(cache -> this.updateMapContents());
	}
	
	private void findMapView(@NonNull View view, Bundle savedInstanceState) {
		mapView = view.findViewById(R.id.map_view);
		mapView.onCreate(savedInstanceState);
	}
	
	private void findFooterViews(@NonNull View view) {
		imageContainer = view.findViewById(R.id.image_container);
		loadingIndicator = view.findViewById(R.id.image_loading_indicator);
		femaleImage = view.findViewById(R.id.image_view_female);
		maleImage = view.findViewById(R.id.image_view_male);
		footerText = view.findViewById(R.id.footer_text_view);
	}
	
	private void initializeMaps() {
		if (this.getActivity() != null) {
			int result = MapsInitializer.initialize(this.getActivity());
			if (result != ConnectionResult.SUCCESS) {
				switch (result) {
					case ConnectionResult.API_UNAVAILABLE:
					case ConnectionResult.SERVICE_DISABLED:
						presentSnackbar(getString(R.string.maps_error_api_unavailable));
						break;
					
					case ConnectionResult.CANCELED:
						presentSnackbar(getString(R.string.maps_error_canceled));
						break;
					
					case ConnectionResult.DEVELOPER_ERROR:
						presentSnackbar(getString(R.string.maps_error_developer_error));
						break;
						
					case ConnectionResult.INTERNAL_ERROR:
						presentSnackbar(getString(R.string.maps_error_internal_error));
						break;
					
					case ConnectionResult.LICENSE_CHECK_FAILED:
						presentSnackbar(getString(R.string.maps_error_license_check_failed));
						break;
					
					case ConnectionResult.NETWORK_ERROR:
						presentSnackbar(getString(R.string.maps_error_network_error));
						break;
					
					case ConnectionResult.SERVICE_INVALID:
						presentSnackbar(getString(R.string.maps_error_service_invalid));
						break;
					
					case ConnectionResult.SERVICE_MISSING:
						presentSnackbar(getString(R.string.maps_error_service_not_found));
						break;
						
					case ConnectionResult.TIMEOUT:
						presentSnackbar(getString(R.string.maps_error_timeout));
						break;
					
					default:
						presentSnackbar(getString(R.string.maps_error_unknown));
				}
			}
		}
	}
	
	
	// ** Events
	
	private void setEvent(@Nullable Event event) {
		this.selectedEvent = event;
		
		if (event == null) {
			footerText.setText(R.string.event_navigation_hint);
			setPerson(null);
			imageContainer.setVisibility(View.GONE);
			loadingIndicator.setVisibility(View.GONE);
			return;
		}
		
		imageContainer.setVisibility(View.VISIBLE);
		Person person = personCache.getValueWithID(event.getPersonID());
		setPerson(person);
	}
	
	private void setPerson(@Nullable Person person) {
		this.personForEvent = person;
		
		if (person == null) {
			loadingIndicator.setVisibility(selectedEvent != null ? View.VISIBLE : View.GONE);
			maleImage.setVisibility(View.GONE);
			femaleImage.setVisibility(View.GONE);
		} else {
			loadingIndicator.setVisibility(View.GONE);
			maleImage.setVisibility(person.getGender().equals(Gender.MALE) ? View.VISIBLE : View.GONE);
			femaleImage.setVisibility(person.getGender().equals(Gender.FEMALE) ? View.VISIBLE : View.GONE);
		}
	}
	
	
	// ** Map Lifecycle
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.map = googleMap;
		presentToast("Google Maps loaded.");
		updateMapContents();
	}
	
	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}
	
	@Override
	public void onLowMemory() {
		mapView.onLowMemory();
		super.onLowMemory();
	}
	
	private void updateMapContents() {
		if (this.map == null) {
			return;
		}
		
		map.clear();
		// Add markers and lines
		
		
		LatLng sydney = new LatLng(-34, 151);
		map.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
	}
	
	
	// ** Utility
	
	private void prepareForNavigation() {
		if (signedOutHandler != null) {
			auth.removeAuthStateDidChangeHandler(signedOutHandler);
			signedOutHandler = null;
		}
		if (eventCacheHandler != null) {
			eventCache.removeUpdateHandler(eventCacheHandler);
			eventCacheHandler = null;
		}
	}
	
	private void navigateToLoginFragment() {
		prepareForNavigation();
		NavController navController = NavHostFragment.findNavController(MapFragment.this);
		navController.navigate(R.id.action_MapFragment_to_LoginFragment);
	}
	
	private void presentToast(@NonNull String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
	}
	
	private void presentSnackbar(@NonNull String text) {
		View view = getView();
		if (view != null) {
			Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
		}
	}
	
}
