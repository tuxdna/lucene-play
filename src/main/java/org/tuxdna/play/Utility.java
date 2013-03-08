package org.tuxdna.play;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

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

	public static Document getSampleDocument(String id) {
		Document doc = new Document();

		{
			id = (null == id ? "0" : id);
			Field fieldId = new Field(Constants.FIELD_ID, id,
					Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO);
			doc.add(fieldId);
		}

		{
			String text = "a b c d";

			// store a normal text field analyzed
			Field fieldAll = new Field(Constants.FIELD_ALL, text,
					Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
			doc.add(fieldAll);
		}
		{
			// store an array of bytes
			byte[] bArr = { 0, 2, 3, 4 };
			Field byteArrayField = new Field("byte_array", bArr,
					Field.Store.YES);
			doc.add(byteArrayField);
		}

		{ // store an array of integers
			int[] iArr = { 0, 1, 2, 3, 4 };
			byte[] array = Utility.toByteArray(iArr);

			Field intArrayField = new Field("int_array", array, Field.Store.YES);
			doc.add(intArrayField);
		}

		return doc;
	}
}
