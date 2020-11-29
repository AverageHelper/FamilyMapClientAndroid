package com.example.familymapclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import model.Person;
import transport.JSONSerialization;

public class PersonDetailsFragment extends Fragment {
	
	private RecyclerView detailsList;
	private @Nullable Person selectedPerson = null;
	
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
	
	private class PersonDetailAdapter extends RecyclerView.Adapter<PersonDetailAdapter.ViewHolder> {
		
		private final List<Pair<String, String>> listData;
		
		public PersonDetailAdapter(@Nullable Person person) {
			listData = new ArrayList<>();
			if (person == null) {
				listData.add(new Pair<>(getString(R.string.person_first_name), "John"));
				listData.add(new Pair<>(getString(R.string.person_last_name), "Doe"));
				listData.add(new Pair<>(getString(R.string.person_gender), "no gender"));
			} else {
				listData.add(new Pair<>(getString(R.string.person_first_name), person.getFirstName()));
				listData.add(new Pair<>(getString(R.string.person_last_name), person.getLastName()));
				listData.add(new Pair<>(getString(R.string.person_gender), person.getGender().name()));
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
			final Pair<String, String> data = listData.get(position);
			holder.titleLabel.setText(data.first);
			holder.detailLabel.setText(data.second);
		}
		
		@Override
		public int getItemCount() {
			return listData.size();
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