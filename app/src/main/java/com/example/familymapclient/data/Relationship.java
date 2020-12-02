package com.example.familymapclient.data;

import android.os.Build;

import java.util.Objects;

import androidx.annotation.NonNull;
import model.Person;

/**
 * An object that represents a familial relationship between two persons.
 */
public class Relationship {
	private final @NonNull Person subject;
	private final @NonNull Person other;
	private final @NonNull RelationshipType relationshipType;
	
	public Relationship(
		@NonNull Person subject,
		@NonNull Person other,
		@NonNull RelationshipType relationshipType
	) {
		this.subject = subject;
		this.other = other;
		this.relationshipType = relationshipType;
	}
	
	public @NonNull Person getSubject() {
		return subject;
	}
	
	public @NonNull Person getOther() {
		return other;
	}
	
	public @NonNull RelationshipType getRelationshipType() {
		return relationshipType;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Relationship that = (Relationship) o;
		return subject.equals(that.subject) &&
			other.equals(that.other) &&
			relationshipType == that.relationshipType;
	}
	
	@Override
	public int hashCode() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return Objects.hash(subject, other, relationshipType);
		} else {
			return subject.hashCode() & other.hashCode() & relationshipType.hashCode();
		}
	}
}
