package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.FilterType;
import com.example.familymapclient.data.PersonCache;
import com.example.familymapclient.data.Relationship;
import com.example.familymapclient.data.UISettings;
import com.example.familymapclient.ui.ListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

public class PersonFragment extends Fragment {
	
	private RecyclerView detailsList;
	private ExpandableListView lifeEventsList;
	
	private final RelatedRecordsAdapter relatedRecordsAdapter = new RelatedRecordsAdapter();
	
	private @Nullable Person selectedPerson = null;
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_person, container, false);
	}
	
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getActivityArguments();
		findListViews(view);
		setupDetailsList();
		setupConnectedRecordsList();
		fetchLifeEvents();
		fetchRelationships();
	}
	
	private void getActivityArguments() {
		if (getActivity() != null) {
			String personJSON = getActivity().getIntent().getStringExtra(PersonActivity.ARG_PERSON_JSON);
			if (personJSON == null) { return; }
			try {
				selectedPerson = JSONSerialization.fromJson(personJSON, Person.class);
			} catch (Exception e) {
				System.out.println("Failed to deserialize JSON: " + e);
			}
		}
	}
	
	private void findListViews(@NonNull View view) {
		detailsList = view.findViewById(R.id.details_list);
		lifeEventsList = view.findViewById(R.id.life_events_list);
	}
	
	private void setupDetailsList() {
		addDividersToList(detailsList);
		detailsList.setAdapter(new PersonDetailAdapter(selectedPerson));
	}
	
	private void setupConnectedRecordsList() {
		lifeEventsList.setAdapter(relatedRecordsAdapter);
		lifeEventsList.expandGroup(0);
		lifeEventsList.expandGroup(1);
		lifeEventsList.setOnChildClickListener(this::onChildClick);
	}
	
	
	private void addDividersToList(@NonNull RecyclerView listView) {
		if (getActivity() != null) {
			LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
			DividerItemDecoration dividerItemDecoration =
				new DividerItemDecoration(listView.getContext(), layoutManager.getOrientation());
			
			listView.addItemDecoration(dividerItemDecoration);
			listView.setLayoutManager(layoutManager);
		}
	}
	
	private @Nullable UISettings getUIPreferences() {
		if (getActivity() != null && UIPreferencesActivity.class.isAssignableFrom(getActivity().getClass())) {
			UIPreferencesActivity activity = (UIPreferencesActivity) getActivity();
			return Objects.requireNonNull(activity).getUISettings();
		}
		return null;
	}
	
	
	
	private void fetchLifeEvents() {
		if (selectedPerson != null) {
			Set<Event> events = new HashSet<>();
			List<Event> allEvents = eventCache.lifeEventsForPerson(selectedPerson);
			UISettings settings = getUIPreferences();
			
			for (Event event : allEvents) {
				Person person = personCache.getValueWithID(event.getPersonID());
				if (person != null) {
					// Check filters
					if (person.getGender().equals(Gender.MALE) &&
						settings != null &&
						!settings.isFilterEnabled(FilterType.GENDER_MALE)) {
						continue;
					}
					
					if (person.getGender().equals(Gender.FEMALE) &&
						settings != null &&
						!settings.isFilterEnabled(FilterType.GENDER_FEMALE)) {
						continue;
					}
				}
				
				events.add(event);
			}
			
			relatedRecordsAdapter.setEvents(events);
		}
	}
	
	private void fetchRelationships() {
		if (selectedPerson != null) {
			Set<Relationship> relationships = personCache.relationshipsForPerson(selectedPerson);
			relatedRecordsAdapter.setRelationships(relationships);
		}
	}
	
	private @NonNull String stringForGender(@NonNull Gender gender) {
		switch (gender) {
			case MALE: return getString(R.string.person_gender_male);
			case FEMALE: return getString(R.string.person_gender_female);
			default: return gender.name();
		}
	}
	
	private boolean onChildClick(
		ExpandableListView parent,
		View view,
		int groupPosition,
		int childPosition,
		long id
	) {
		RelatedRecordsAdapter adapter = (RelatedRecordsAdapter) parent.getExpandableListAdapter();
		@Nullable Object child = adapter.getChild(groupPosition, childPosition);
		
		if (child == null) { return false; }
		
		if (child.getClass().equals(Event.class)) {
			Event event = (Event) child;
			onEventClick(event);
			
		} else if (child.getClass().equals(Relationship.class)) {
			Relationship relationship = (Relationship) child;
			onRelationshipClick(relationship);
		}
		
		return true;
	}
	
	private void onEventClick(@NonNull Event event) {
		startEventActivity(event);
	}
	
	private void onRelationshipClick(@NonNull Relationship relationship) {
		startPersonActivity(relationship.getOther());
	}
	
	private void startPersonActivity(@NonNull Person person) {
		Intent personDetails = PersonActivity.newIntent(getActivity(), person);
		startActivity(personDetails);
	}
	
	private void startEventActivity(@NonNull Event event) {
		Intent personDetails = EventActivity.newIntent(getActivity(), event);
		startActivity(personDetails);
	}
	
	private class PersonDetailAdapter extends RecyclerView.Adapter<PersonDetailAdapter.ViewHolder> {
		
		private final @NonNull List<Pair<String, String>> details;
		
		public PersonDetailAdapter(@Nullable Person person) {
			details = new ArrayList<>();
			if (person != null) {
				details.add(new Pair<>(getString(R.string.person_first_name), person.getFirstName()));
				details.add(new Pair<>(getString(R.string.person_last_name), person.getLastName()));
				details.add(new Pair<>(getString(R.string.person_gender), stringForGender(person.getGender())));
			}
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
			
			public final TextView titleLabel;
			public final TextView detailLabel;
			
			public ViewHolder(View itemView) {
				super(itemView);
				this.titleLabel = itemView.findViewById(R.id.title_label);
				this.detailLabel = itemView.findViewById(R.id.detail_label);
			}
		}
		
	}
	
	private class RelatedRecordsAdapter extends BaseExpandableListAdapter {
		
		private @Nullable Set<Event> events = null;
		private @Nullable Set<Relationship> relationships = null;
		
		public void setEvents(@Nullable Set<Event> events) {
			this.events = events;
			notifyDataSetChanged();
		}
		
		public void setRelationships(@Nullable Set<Relationship> relationships) {
			this.relationships = relationships;
			notifyDataSetChanged();
		}
		
		private @Nullable List<Event> getSortedEvents() {
			if (events == null) {
				return null;
			}
			List<Event> result = new ArrayList<>(events);
			Collections.sort(result, (event1, event2) ->
				Integer.compare(event1.getYear(), event2.getYear()));
			return result;
		}
		
		private @Nullable List<Relationship> getSortedRelationships() {
			if (relationships == null) {
				return null;
			}
			return new ArrayList<>(relationships);
		}
		
		@Override
		public int getGroupCount() {
			return 2;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			switch (groupPosition) {
				case 0: // Life Events
					if (events == null) {
						return 0;
					}
					return events.size();
					
				case 1: // Family
					if (relationships == null) {
						return 0;
					}
					return relationships.size();
					
				default:
					return 0;
			}
		}
		
		@Override
		public @NonNull String getGroup(int groupPosition) {
			switch (groupPosition) {
				case 0:
					return getString(events == null
						? R.string.loading_state_fetching_events
						: R.string.person_header_life_events);
				case 1:
					return getString(relationships == null
						? R.string.loading_state_fetching_persons
						: R.string.person_header_relationships);
				default:
					return "Group";
			}
		}
		
		@Override
		public @Nullable Object getChild(int groupPosition, int childPosition) {
			switch (groupPosition) {
				case 0: // Life Events
					if (getSortedEvents() == null) {
						return null;
					}
					return getSortedEvents().get(childPosition);
					
				case 1: // Family
					if (getSortedRelationships() == null) {
						return null;
					}
					return getSortedRelationships().get(childPosition);
					
				default:
					return null;
			}
		}
		
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			@Nullable Object child = getChild(groupPosition, childPosition);
			if (child == null) {
				return -1;
				
			} else if (child.getClass().equals(Event.class)) {
				Event event = (Event) child;
				return event.getId().hashCode();
				
			} else if (child.getClass().equals(Relationship.class)) {
				Relationship relationship = (Relationship) child;
				return relationship.getSubject().getId().hashCode();
			}
			
			return -1;
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View headerView;
			if (convertView != null) {
				headerView = convertView;
			} else {
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				headerView = layoutInflater.inflate(R.layout.listview_header, parent, false);
			}
			
			TextView titleLabel = headerView.findViewById(R.id.title_label);
			titleLabel.setText(getGroup(groupPosition));
			
			return headerView;
		}
		
		@Override
		public ListItem getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			ListItem cell;
			if (convertView != null && convertView.getClass().equals(ListItem.class)) {
				cell = (ListItem) convertView;
			} else {
				cell = ListItem.inflate(parent);
			}
			
			Object value = getChild(groupPosition, childPosition);
			cell.setValue(value);
			
			return cell;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
	
}