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
	 * @return All {@link Color} values as an {@link ArrayList}.
	 */
	public static @NonNull ArrayList<Color> allValues() {
		return new ArrayList<>(Arrays.asList(Color.values()));
	}
	
	public int colorValue() {
		switch (this) {
			case RED: return android.graphics.Color.RED;
			case ORANGE:
				return android.graphics.Color.parseColor("#FFA500");
			case YELLOW:
				return android.graphics.Color.parseColor("#F4E900");
			case GREEN:
				return android.graphics.Color.parseColor("#024000");
			case CYAN: return android.graphics.Color.CYAN;
			case AZURE:
				return android.graphics.Color.parseColor("#BB86FC");
			case BLUE: return android.graphics.Color.BLUE;
			case VIOLET:
				return android.graphics.Color.parseColor("#6200EE");
			case MAGENTA: return android.graphics.Color.MAGENTA;
			case ROSE:
				return android.graphics.Color.parseColor("#E10021");
			
			default: return android.graphics.Color.BLACK;
		}
	}
	
	/**
	 * @return a random {@link Color} value.
	 */
	public static @NonNull Color random() {
		return ArrayHelpers.randomElementFromList(Color.allValues());
	}
}
