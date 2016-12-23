package com.github.nio3.util;

public class ArrayUtil {

	public static byte[] byteArray(int arraySize) {
		byte[] result = new byte[arraySize];
		byte a = "a".getBytes()[0];
		for (int i = 0; i < arraySize; i++) {
			result[i] = a;
		}
		return result;
	}

	public static char[] charArray(int arraySize) {
		char[] result = new char[arraySize];
		for (int i = 0; i < arraySize; i++) {
			result[i] = 'a';
		}
		return result;
	}

	public static String string(int arraySize) {
		return new String(charArray(arraySize));
	}

}
