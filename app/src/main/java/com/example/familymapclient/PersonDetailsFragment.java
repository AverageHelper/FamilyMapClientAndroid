package com.example.familymapclient;

import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.data.Color;
import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.FilterType;
import com.example.familymapclient.data.PersonCache;
import com.example.familymapclient.data.Relationship;
import com.example.familymapclient.data.UISettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import model.Event;
import model.Gender;
import model.Person;
import transport.JSONSerialization;

public class PersonDetailsFragment extends Fragment {
	
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
		return inflater.inflate(R.layout.fragment_person_details, container, false);
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
	
	private @NonNull UISettings getUIPreferences() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		UISettings settings = new UISettings();
		settings.setFiltersEnabled(getActivity(), preferences);
		settings.setLineTypesEnabled(getActivity(), preferences);
		return settings;
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
						!settings.isFilterEnabled(FilterType.GENDER_MALE)) {
						continue;
					}
					
					if (person.getGender().equals(Gender.FEMALE) &&
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
	
	private class RelatedRecordsAdapter extends BaseExpandableListAdapter {
		
		private final Set<DataSetObserver> observers = new HashSet<>();
		private @Nullable Set<Event> events = null;
		private @Nullable Set<Relationship> relationships = null;
		
		public void setEvents(@Nullable Set<Event> events) {
			this.events = events;
			sendUpdates();
		}
		
		public void setRelationships(@Nullable Set<Relationship> relationships) {
			this.relationships = relationships;
			sendUpdates();
		}
		
		private void sendUpdates() {
			for (DataSetObserver observer : observers) {
				observer.onChanged();
			}
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
		public void registerDataSetObserver(DataSetObserver observer) {
			observers.add(observer);
		}
		
		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			observers.remove(observer);
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
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			View cell;
			if (/*convertView != null &&*/ false) {
				cell = convertView;
			} else {
				LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
				cell = layoutInflater.inflate(R.layout.listview_title_detail_icon, parent, false);
			}
			
			TextView titleLabel = cell.findViewById(R.id.title_label);
			TextView detailLabel = cell.findViewById(R.id.detail_label);
			ImageView imageView = cell.findViewById(R.id.image_view);
			
			@Nullable Object child = getChild(groupPosition, childPosition);
			
			if (child == null) {
				return cell;
			}
			
			if (child.getClass().equals(Event.class)) {
				Event event = (Event) child;
				Person person = personCache.getValueWithID(event.getPersonID());
				
				titleLabel.setText(getString(
					R.string.arg_event_description_short,
					event.getEventType().toUpperCase(),
					event.getCity(),
					event.getCountry(),
					event.getYear()
				));
				Color color = eventCache.colorForEvent(event);
				// FIXME: How do we properly pass this in?
				imageView.setColorFilter((int) color.value());
				
				if (person != null) {
					detailLabel.setText(getString(
						R.string.arg_full_name,
						person.getFirstName(),
						person.getLastName()
					));
				}
				
			} else if (child.getClass().equals(Relationship.class)) {
				Relationship relationship = (Relationship) child;
				Person person = relationship.getOther();
				
				titleLabel.setText(getString(
					R.string.arg_full_name,
					person.getFirstName(),
					person.getLastName()
				));
				
				switch (relationship.getRelationshipType()) {
					case FATHER:
						detailLabel.setText(R.string.person_relationship_father);
						break;
						
					case MOTHER:
						detailLabel.setText(R.string.person_relationship_mother);
						break;
						
					case SPOUSE:
						detailLabel.setText(R.string.person_relationship_spouse);
						break;
						
					case CHILD:
						detailLabel.setText(R.string.person_relationship_child);
						break;
						
					default:
						detailLabel.setText(relationship.getRelationshipType().name());
						break;
				}
				
				switch (person.getGender()) {
					case MALE:
						imageView.setImageResource(R.drawable.ic_iconmonstr_male);
						break;
					case FEMALE:
						imageView.setImageResource(R.drawable.ic_iconmonstr_female);
						break;
				}
			}
			
			return cell;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return events != null && relationships != null;
		}
		
		@Override
		public boolean isEmpty() {
			return (events == null || events.isEmpty()) &&
				(relationships == null || relationships.isEmpty());
		}
		
		@Override
		public void onGroupExpanded(int groupPosition) {
		
		}
		
		@Override
		public void onGroupCollapsed(int groupPosition) {
		
		}
		
		@Override
		public long getCombinedChildId(long groupId, long childId) {
			return groupId & childId;
		}
		
		@Override
		public long getCombinedGroupId(long groupId) {
			return groupId;
		}
	}
	
}