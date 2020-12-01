package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import model.Event;
import model.Gender;
import model.Person;
import transport.JSONSerialization;

public class PersonDetailsFragment extends Fragment {
	
	private RecyclerView detailsList;
	private @Nullable Person selectedPerson = null;
	
	private @Nullable List<Event> lifeEvents = null;
	private @Nullable List<Person> relationships = null;
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_person_details, container, false);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getActivityArguments();
		findListView(view);
		setupList();
		fetchLifeEvents();
		fetchRelationships();
	}
	
	private void getActivityArguments() {
		if (getActivity() != null) {
			String personJSON = getActivity().getIntent().getStringExtra(PersonActivity.ARG_PERSON_JSON);
			try {
				selectedPerson = JSONSerialization.fromJson(personJSON, Person.class);
			} catch (Exception e) {
				System.out.println("Failed to deserialize JSON: " + e);
			}
		}
	}
	
	private void findListView(@NonNull View view) {
		detailsList = view.findViewById(R.id.details_list);
	}
	
	private void setupList() {
		if (getActivity() != null) {
			LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
			DividerItemDecoration dividerItemDecoration =
				new DividerItemDecoration(detailsList.getContext(), layoutManager.getOrientation());
			
			detailsList.addItemDecoration(dividerItemDecoration);
			detailsList.setLayoutManager(layoutManager);
			detailsList.setAdapter(new PersonDetailAdapter(selectedPerson));
		}
	}
	
	private void fetchLifeEvents() {
		// get all events related to this person
		if (selectedPerson != null) {
			lifeEvents = personCache.eventsForPerson(selectedPerson);
		}
	}
	
	private void fetchRelationships() {
		// get all persons related to this person
	}
	
	private @NonNull String stringForGender(@NonNull Gender gender) {
		switch (gender) {
			case MALE: return getString(R.string.person_gender_male);
			case FEMALE: return getString(R.string.person_gender_female);
			default: return gender.name();
		}
	}
	
	private class PersonDetailAdapter extends RecyclerView.Adapter<PersonDetailAdapter.ViewHolder> {
		
		private final @NonNull List<Pair<String, String>> details;
		
		public PersonDetailAdapter(@Nullable Person person) {
			// Add person details
			details = new ArrayList<>();
			if (person == null) {
				details.add(new Pair<>(getString(R.string.person_first_name), "John"));
				details.add(new Pair<>(getString(R.string.person_last_name), "Doe"));
				details.add(new Pair<>(getString(R.string.person_gender), "no gender"));
			} else {
				details.add(new Pair<>(getString(R.string.person_first_name), person.getFirstName()));
				details.add(new Pair<>(getString(R.string.person_last_name), person.getLastName()));
				details.add(new Pair<>(getString(R.string.person_gender), stringForGender(person.getGender())));
			}
			
			// Add life events
			
			// Add family members
		}
		
		@Override
		public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
			View listItem = layoutInflater.inflate(R.layout.listview_title_detail, parent, false);
			return new ViewHolder(listItem);
		}
		
		@Override
		public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
			final Pair<String, String> data = details.get(position);
			holder.titleLabel.setText(data.first);
			holder.detailLabel.setText(data.second);
		}
		
		@Override
		public int getItemCount() {
			return details.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			
			public TextView titleLabel;
			public TextView detailLabel;
			
			public ViewHolder(View itemView) {
				super(itemView);
				this.titleLabel = itemView.findViewById(R.id.title_label);
				this.detailLabel = itemView.findViewById(R.id.detail_label);
			}
		}
		
	}
	
}