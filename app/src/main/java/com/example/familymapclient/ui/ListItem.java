package com.example.familymapclient.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.data.Color;
import com.example.familymapclient.data.EventCache;
import com.example.familymapclient.data.PersonCache;
import com.example.familymapclient.data.Relationship;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import model.Event;
import model.Person;

public class ListItem extends ConstraintLayout {
	
	// ** View Inflation
	
	private final @NonNull TextView titleLabel;
	private final @NonNull TextView detailLabel;
	private final @NonNull ImageView imageView;
	
	public static ListItem inflate(@NonNull ViewGroup parent) {
		return (ListItem) LayoutInflater.from(parent.getContext())
			.inflate(R.layout.listview_title_detail_icon, parent, false);
	}
	
	public ListItem(@NonNull Context context) {
		this(context, null, 0);
	}
	
	public ListItem(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ListItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context)
			.inflate(R.layout.listview_title_detail_icon_content, this, true);
		titleLabel = findViewById(R.id.title_label);
		detailLabel = findViewById(R.id.detail_label);
		imageView = findViewById(R.id.image_view);
	}
	
	public @NonNull TextView getTitleLabel() {
		return titleLabel;
	}
	
	public @NonNull TextView getDetailLabel() {
		return detailLabel;
	}
	
	public @NonNull ImageView getImageView() {
		return imageView;
	}
	
	
	// ** Data
	
	private final PersonCache personCache = PersonCache.shared();
	private final EventCache eventCache = EventCache.shared();
	
	/**
	 * Updates the cell's UI elements to reflect details about the provided data.
	 * @param value The data that the cell should describe.
	 */
	public void setValue(@Nullable Object value) {
		if (value == null) { return; }
		
		if (value.getClass().equals(Event.class)) {
			setEvent((Event) value);
			
		} else if (value.getClass().equals(Person.class)) {
			setPerson((Person) value);
			
		} else if (value.getClass().equals(Relationship.class)) {
			setRelationship((Relationship) value);
		}
	}
	
	/**
	 * Updates the cell's UI elements to reflect details about the provided <code>event</code>.
	 * @param event The event that the cell should describe.
	 */
	public void setEvent(@NonNull Event event) {
		Person person = personCache.getValueWithID(event.getPersonID());
		
		getTitleLabel().setText(getContext().getString(
			R.string.arg_event_description_short,
			event.getEventType().toUpperCase(Locale.ROOT),
			event.getCity(),
			event.getCountry(),
			event.getYear()
		));
		Color color = eventCache.colorForEvent(event);
		getImageView().setImageResource(R.drawable.ic_map_marker);
		getImageView().setColorFilter(color.colorValue(), android.graphics.PorterDuff.Mode.SRC_IN);
		
		if (person != null) {
			getDetailLabel().setText(getContext().getString(
				R.string.arg_full_name,
				person.getFirstName(),
				person.getLastName()
			));
		}
	}
	
	/**
	 * Updates the cell's UI elements to reflect details about the provided <code>person</code>.
	 * @param person The relationship that the cell should describe.
	 */
	public void setPerson(@NonNull Person person) {
		getTitleLabel().setText(getContext().getString(
			R.string.arg_full_name,
			person.getFirstName(),
			person.getLastName()
		));
		switch (person.getGender()) {
			case MALE:
				getImageView().setImageResource(R.drawable.ic_iconmonstr_male);
				break;
			case FEMALE:
				getImageView().setImageResource(R.drawable.ic_iconmonstr_female);
				break;
		}
	}
	
	/**
	 * Updates the cell's UI elements to reflect details about the provided <code>relationship</code>.
	 * @param relationship The relationship that the cell should describe.
	 */
	public void setRelationship(@NonNull Relationship relationship) {
		Person person = relationship.getOther();
		
		setPerson(person);
		
		switch (relationship.getRelationshipType()) {
			case FATHER:
				getDetailLabel().setText(R.string.person_relationship_father);
				break;
			
			case MOTHER:
				getDetailLabel().setText(R.string.person_relationship_mother);
				break;
			
			case SPOUSE:
				getDetailLabel().setText(R.string.person_relationship_spouse);
				break;
			
			case CHILD:
				getDetailLabel().setText(R.string.person_relationship_child);
				break;
			
			default:
				getDetailLabel().setText(relationship.getRelationshipType().name());
				break;
		}
	}
	
}
