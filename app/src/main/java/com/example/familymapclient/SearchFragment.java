package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.familymapclient.data.SearchManager;
import com.example.familymapclient.ui.ListItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import model.Event;
import model.Person;

public class SearchFragment extends Fragment {
	
	private SearchManager search;
	
	private SearchView searchBar;
	private RecyclerView resultsList;
	private ProgressBar loadingIndicator;
	
	@Override
	public View onCreateView(
		LayoutInflater inflater,
		ViewGroup container,
		Bundle savedInstanceState
	) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_search, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		search = new SearchManager();
		findChildViews(view);
		setupDetailsList();
		setupSearchBar();
	}
	
	@Override
	public void onDestroy() {
		search.setCallback(null);
		super.onDestroy();
	}
	
	private void findChildViews(@NonNull View view) {
		searchBar = view.findViewById(R.id.search_bar);
		resultsList = view.findViewById(R.id.results_list);
		loadingIndicator = view.findViewById(R.id.loading_search_results);
	}
	
	private void setupDetailsList() {
		addDividersToList(resultsList);
		SearchAdapter adapter = new SearchAdapter();
		resultsList.setAdapter(adapter);
		search.setCallback(adapter::setResults);
		search.runNewSearchWithQuery("");
	}
	
	private void setupSearchBar() {
		searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				updateSearch(query);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				updateSearch(newText);
				return true;
			}
		});
		searchBar.setQueryHint(getString(R.string.person_event_search_hint));
		searchBar.requestFocusFromTouch();
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
	
	private void setLoadingSearchResults(boolean isLoading) {
		loadingIndicator.setVisibility(isLoading
			? View.VISIBLE
			: View.INVISIBLE);
		resultsList.setVisibility(isLoading
			? View.INVISIBLE
			: View.VISIBLE);
	}
	
	private void updateSearch(@NonNull String query) {
		setLoadingSearchResults(true);
		search.runNewSearchWithQuery(query);
	}
	
	private void onChildClick(@Nullable Object value) {
		if (value == null) { return; }
		
		if (value.getClass().equals(Event.class)) {
			Event event = (Event) value;
			onEventClick(event);
			
		} else if (value.getClass().equals(Person.class)) {
			Person person = (Person) value;
			onPersonClick(person);
		}
	}
	
	private void onEventClick(@NonNull Event event) {
		startEventActivity(event);
	}
	
	private void onPersonClick(@NonNull Person person) {
		startPersonActivity(person);
	}
	
	private void startPersonActivity(@NonNull Person person) {
		Intent personDetails = PersonActivity.newIntent(getActivity(), person);
		startActivity(personDetails);
	}
	
	private void startEventActivity(@NonNull Event event) {
		Intent personDetails = EventActivity.newIntent(getActivity(), event);
		startActivity(personDetails);
	}
	
	/**
	 * An object that adapts search result data into rows for the search results list.
	 */
	private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
		private @NonNull
		List<Object> results;
		
		public SearchAdapter() {
			results = new ArrayList<>();
		}
		
		@Override
		public @NonNull SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			ListItem listItem = ListItem.inflate(parent);
			return new SearchAdapter.ViewHolder(listItem);
		}
		
		public void setResults(@NonNull List<Object> results) {
			this.results = results;
			notifyDataSetChanged();
			setLoadingSearchResults(false);
		}
		
		@Override
		public int getItemCount() {
			return results.size();
		}
		
		@Override
		public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
			Object value = results.get(position);
			ListItem listItem = holder.getListItem();
			listItem.setValue(value);
			listItem.setOnClickListener(view -> onChildClick(value));
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			private final @Nullable ListItem listItem;
			
			public ViewHolder(@Nullable ListItem listItem) {
				super(listItem);
				this.listItem = listItem;
			}
			
			public @Nullable ListItem getListItem() {
				return listItem;
			}
		}
	}
	
}
