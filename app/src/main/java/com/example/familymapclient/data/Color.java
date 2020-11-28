package com.example.familymapclient.data;

import com.example.familymapclient.utilities.ArrayHelpers;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;

/**
 * A set of colors based on {@link BitmapDescriptorFactory} constants.
 */
public enum Color {
	RED,
	ORANGE,
	YELLOW,
	GREEN,
	CYAN,
	AZURE,
	BLUE,
	VIOLET,
	MAGENTA,
	ROSE;
	
	/**
	 * @return an integer representing the color's value on the color wheel as a float value that is
	 * greater or equal to 0 and less than 360.
	 */
	public float value() {
		switch (this) {
			case RED: return BitmapDescriptorFactory.HUE_RED;
			case ORANGE: return BitmapDescriptorFactory.HUE_ORANGE;
			case YELLOW: return BitmapDescriptorFactory.HUE_YELLOW;
			case GREEN: return BitmapDescriptorFactory.HUE_GREEN;
			case CYAN: return BitmapDescriptorFactory.HUE_CYAN;
			case AZURE: return BitmapDescriptorFactory.HUE_AZURE;
			case BLUE: return BitmapDescriptorFactory.HUE_BLUE;
			case VIOLET: return BitmapDescriptorFactory.HUE_VIOLET;
			case MAGENTA: return BitmapDescriptorFactory.HUE_MAGENTA;
			case ROSE: return BitmapDescriptorFactory.HUE_ROSE;
			
			default: return 359F; // the highest acceptable whole value
		}
	}
	
	/**
	 * @return All {@link Color} values as an {@link ArrayList}.
	 */
	public static @NonNull ArrayList<Color> allValues() {
		return new ArrayList<>(Arrays.asList(Color.values()));
	}
	
	/**
	 * @return a random {@link Color} value.
	 */
	public static @NonNull Color random() {
		return ArrayHelpers.randomElementFromList(Color.allValues());
	}
}
