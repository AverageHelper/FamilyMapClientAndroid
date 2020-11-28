package com.example.familymapclient.utilities;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;

public class ArrayHelpers {
	
	/**
	 * Returns a random element from the provided list.
	 *
	 * @param list The list of elements.
	 * @param <T> The type of list elements.
	 * @return A random element of the list.
	 */
	public static <T> T randomElementFromList(@NonNull List<T> list) {
		Random random = new Random();
		return list.get(
			random.nextInt(list.size())
		);
	}
	
}
