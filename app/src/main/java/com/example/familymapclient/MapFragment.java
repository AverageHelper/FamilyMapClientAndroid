package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.familymapclient.auth.Auth;
import com.example.familymapclient.data.Color;
import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;
import com.example.familymapclient.data.fetch.PersonRequester;
import com.example.familymapclient.transport.ServerLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

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
	private @Nullable Integer eventCacheHandler = null;
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	private MapView mapView;
	private @Nullable GoogleMap map = null;
	private ConstraintLayout imageContainer;
	private ConstraintLayout eventInfo;
	private ProgressBar loadingIndicator;
	private ImageView femaleImage;
	private ImageView maleImage;
	private TextView footerText;
	
	private @Nullable Event selectedEvent = null;
	private @Nullable Person personForEvent = null;
	
	
	// ** Lifecycle Events
	
//	public static final String ARG_EVENTS = "events";
//
//	public static @NonNull MapFragment newInstance(@NonNull ArrayList<String> eventIDs) {
//		Bundle args = new Bundle();
//		args.putStringArrayList(ARG_EVENTS, eventIDs);
//
//		MapFragment fragment = new MapFragment();
//		fragment.setArguments(args);
//		return fragment;
//	}
	
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
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// get new event data
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findMapView(view, savedInstanceState);
		findFooterViews(view);
		attachClickListeners();
		
		initializeMaps();
		mapView.getMapAsync(this);
		
		setEvent(null);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		setupCacheListeners();
		
		if (!auth.isSignedIn()) {
			// Signed out. Get out of here.
			navigateToLoginFragment();
		}
	}
	
	@Override
	public void onStop() {
		if (eventCacheHandler != null) {
			eventCache.removeUpdateHandler(eventCacheHandler);
			eventCacheHandler = null;
		}
		mapView.onStop();
		super.onStop();
	}
	
	private void setupCacheListeners() {
		eventCacheHandler = eventCache.addUpdateHandler(cache -> this.updateMapContents());
	}
	
	private void findMapView(@NonNull View view, Bundle savedInstanceState) {
		mapView = view.findViewById(R.id.map_view);
		mapView.onCreate(savedInstanceState);
	}
	
	private void findFooterViews(@NonNull View view) {
		eventInfo = view.findViewById(R.id.event_info);
		imageContainer = view.findViewById(R.id.image_container);
		loadingIndicator = view.findViewById(R.id.image_loading_indicator);
		femaleImage = view.findViewById(R.id.image_view_female);
		maleImage = view.findViewById(R.id.image_view_male);
		footerText = view.findViewById(R.id.footer_text_view);
	}
	
	private void attachClickListeners() {
		eventInfo.setOnClickListener(view -> {
			if (personForEvent != null) {
				startPersonActivity(personForEvent);
			}
		});
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
	
	
	// ** Persons
	
	private @Nullable PersonRequester personFetch = null;
	
	private void fetchPerson(@NonNull String personID) {
		if (auth.getAuthToken() == null) {
			return;
		}
		ServerLocation server = new ServerLocation(
			auth.getHostname(),
			auth.getPortNumber(),
			auth.usesSecureProtocol()
		);
		personFetch = personCache.fetchPersonWithID(
			server,
			personID,
			auth.getAuthToken(),
			person -> {
				if (personFetch != null) {
					personFetch = null;
					setPerson(person);
				}
			},
			error -> {
				handleAsyncFailure(error);
				personFetch = null;
			}
		);
	}
	
	private void handleAsyncFailure(@NonNull Throwable error) {
		// Toast the error message
		if (error instanceof Exception) {
			Exception e = (Exception) error;
			if (e.getMessage() != null) {
				presentSnackbar(e.getMessage());
			} else {
				presentSnackbar(e.toString());
			}
			
		} else {
			presentSnackbar(error.toString());
		}
		
		// Stop our async handler and deselect the event
		this.personFetch = null;
		setEvent(null);
	}
	
	
	// ** Events
	
	private void setEvent(@Nullable Event event) {
		this.selectedEvent = event;
		
		if (event == null) {
			setPerson(null);
			imageContainer.setVisibility(View.GONE);
			loadingIndicator.setVisibility(View.GONE);
			return;
		}
		
		imageContainer.setVisibility(View.VISIBLE);
		
		// Select the person record if we have it, or fetch it if we don't
		String personID = event.getPersonID();
		@Nullable Person person = personCache.getValueWithID(personID);
		setPerson(person);
		
		if (person == null) {
			fetchPerson(personID);
		}
	}
	
	private void setPerson(@Nullable Person person) {
		this.personForEvent = person;
		
		if (person == null) {
			if (selectedEvent == null) {
				footerText.setText(R.string.event_navigation_hint);
			} else {
				footerText.setText(R.string.event_loading_hint);
			}
			loadingIndicator.setVisibility(selectedEvent != null ? View.VISIBLE : View.GONE);
			maleImage.setVisibility(View.GONE);
			femaleImage.setVisibility(View.GONE);
		} else {
			if (selectedEvent == null) {
				footerText.setText(getString(
					R.string.arg_full_name,
					person.getFirstName(),
					person.getLastName()
				));
			} else {
				footerText.setText(getString(
					R.string.arg_event_description,
					person.getFirstName(),
					person.getLastName(),
					selectedEvent.getEventType().toUpperCase(Locale.ROOT),
					selectedEvent.getCity(),
					selectedEvent.getCountry(),
					selectedEvent.getYear()
				));
			}
			loadingIndicator.setVisibility(View.GONE);
			maleImage.setVisibility(person.getGender().equals(Gender.MALE) ? View.VISIBLE : View.GONE);
			femaleImage.setVisibility(person.getGender().equals(Gender.FEMALE) ? View.VISIBLE : View.GONE);
		}
		
		updateMapContents();
	}
	
	
	// ** Map Lifecycle
	
	private boolean onMapMarkerTapped(@NonNull Marker marker) {
		if (map == null) {
			return false;
		}
		
		map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
		if (marker.getTag() != null) { // The event is stored in the tag
			setEvent((Event) marker.getTag());
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.map = googleMap;
		this.map.setOnMarkerClickListener(this::onMapMarkerTapped);
		updateMapContents();
	}
	
	private void updateMapContents() {
		if (this.map == null) {
			return;
		}
		
		map.clear();
		
		// Add markers and lines
		for (Event event : eventCache.values()) {
			if (event.getLatitude() == null || event.getLongitude() == null) {
				break;
			}
			LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
			Color color = eventCache.colorForEvent(event);
			
			MarkerOptions options = new MarkerOptions()
				.position(location)
				.icon(BitmapDescriptorFactory.defaultMarker(color.value()));
			
			Marker marker = map.addMarker(options);
			marker.setTag(event);
		}
	}
	
	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		mapView.onLowMemory();
		super.onLowMemory();
	}
	
	
	// ** Navigation
	
	private void presentSnackbar(@NonNull String text) {
		View view = getView();
		if (view != null) {
			Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
		}
	}
	
	private void navigateToLoginFragment() {
		NavController navController = NavHostFragment.findNavController(MapFragment.this);
		navController.navigate(R.id.action_MapFragment_to_LoginFragment);
	}
	
	private void startPersonActivity(@NonNull Person person) {
		Intent personDetails = new Intent(getActivity(), PersonActivity.class);
		personDetails.putExtra(PersonActivity.ARG_PERSON_JSON, person.toJson());
		startActivity(personDetails);
	}
	
}
