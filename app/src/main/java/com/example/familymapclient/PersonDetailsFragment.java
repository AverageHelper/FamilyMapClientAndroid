package com.example.familymapclient;

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
import com.example.familymapclient.data.PersonCache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

public class PersonDetailsFragment extends Fragment {
	
	private RecyclerView detailsList;
	private ExpandableListView lifeEventsList;
	private ExpandableListView familyList;
	
	private final LifeEventsAdapter lifeEventsAdapter = new LifeEventsAdapter();
	
	private @Nullable Person selectedPerson = null;
	private @Nullable List<Event> lifeEvents = null;
	private @Nullable List<Person> relationships = null;
	
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
		setupEventsList();
		setupFamilyList();
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
		familyList = view.findViewById(R.id.family_list);
	}
	
	private void setupDetailsList() {
		if (getActivity() != null) {
			LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
			DividerItemDecoration dividerItemDecoration =
				new DividerItemDecoration(detailsList.getContext(), layoutManager.getOrientation());
			
			detailsList.addItemDecoration(dividerItemDecoration);
			detailsList.setLayoutManager(layoutManager);
			detailsList.setAdapter(new PersonDetailAdapter(selectedPerson));
		}
	}
	
	private void setupEventsList() {
		lifeEventsList.setAdapter(lifeEventsAdapter);
	}
	
	private void setupFamilyList() {
	
	}
	
	private void fetchLifeEvents() {
		// get all events related to this person
		if (selectedPerson != null) {
			lifeEvents = new ArrayList<>();
			for (Event event : eventCache.values()) {
				if (event.getPersonID().equals(selectedPerson.getId())) {
					lifeEvents.add(event);
				}
			}
//			lifeEvents = personCache.eventsForPerson(selectedPerson);
			lifeEventsAdapter.setEvents(new HashSet<>(lifeEvents));
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
	
	private class LifeEventsAdapter extends BaseExpandableListAdapter {
		
		private final Set<DataSetObserver> observers = new HashSet<>();
		private @Nullable Set<Event> events = null;
		
		public void setEvents(@Nullable Set<Event> events) {
			this.events = events;
			for (DataSetObserver observer : observers) {
				observer.onChanged();
			}
		}
		
		private @Nullable List<Event> getSortedEvents() {
			if (events == null) {
				return null;
			}
			return new ArrayList<>(events);
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
			return 1;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			if (events == null) {
				return 0;
			}
			return events.size();
		}
		
		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}
		
		@Override
		public @Nullable Event getChild(int groupPosition, int childPosition) {
			if (getSortedEvents() == null) {
				return null;
			}
			return getSortedEvents().get(childPosition);
		}
		
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			if (events == null) {
				return -1;
			}
			return getSortedEvents().get(childPosition).getId().hashCode();
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
			titleLabel.setText(
				events == null
					? R.string.loading_state_fetching_events
					: R.string.person_header_life_events
			);
			
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
			
			Event event = getChild(groupPosition, childPosition);
			if (event != null) {
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
			}
			
			return cell;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			return events != null;
		}
		
		@Override
		public boolean isEmpty() {
			return events == null || events.isEmpty();
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