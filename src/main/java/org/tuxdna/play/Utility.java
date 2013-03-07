package org.tuxdna.play;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Utility {
	public static int[] toIntArray(byte[] byteArray) {
		if (null == byteArray)
			return null;
		ByteBuffer bbuf = ByteBuffer.wrap(byteArray);
		IntBuffer ibuf = bbuf.asIntBuffer();
		int[] intArray = new int[byteArray.length / (Integer.SIZE / Byte.SIZE)];
		ibuf.get(intArray);
		return intArray;
	}

	public static byte[] toByteArray(int[] intArray) {
		if (null == intArray)
			return null;
		ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length
				* Integer.SIZE / Byte.SIZE);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(intArray);
		byte[] byteArray = byteBuffer.array();
		return byteArray;
	}

	public static String join(int[] intArray, String sep) {
		StringBuilder sb = new StringBuilder();
		for (int ival : intArray) {
			sb.append(ival);
			sb.append(sep);
		}
		return sb.toString();
	}

	public static String join(byte[] byteArray, String sep) {
		StringBuilder sb = new StringBuilder();
		for (int ival : byteArray) {
			sb.append(ival);
			sb.append(sep);
		}
		return sb.toString();
	}
}
