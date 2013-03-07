package org.tuxdna.play;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class Write implements Constants {
	private static Logger logger = Logger.getLogger(Write.class);

	private static void storeIt(DocumentStore store, Document doc) {
		if (store.write(doc)) {
			logger.info("Added document");
		} else {
			logger.info("Adding document failed!");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		BasicConfigurator.configure();

		Logger.getRootLogger();
		String corpusPath = args.length >= 1 ? args[0] : null;

		String path = "/tmp/test-index";
		DocumentStore store = new DocumentStore(path);

		if (corpusPath == null) {
			String text = "a b c d";

			Document doc = new Document();
			
			// store a normal text field analyzed
			Field fieldAll = new Field(FIELD_ALL, text, Field.Store.YES,
					Field.Index.ANALYZED, Field.TermVector.YES);
			doc.add(fieldAll);
			
			// store an array of bytes
			byte[] bArr = {0,2,3,4};
			Field byteArrayField = new Field("byte_array", bArr, Field.Store.YES);
			doc.add(byteArrayField);
			
			// store an array of integers
			int[] iArr = {0,1,2,3,4};
	        byte[] array = Utility.toByteArray(iArr);
			
			Field intArrayField = new Field("int_array", array, Field.Store.YES);
			doc.add(intArrayField);
			
			storeIt(store, doc);

		} else {

			File file = new File(corpusPath);
			if (!file.isDirectory())
				return;

			file.getAbsolutePath();
			for (String dataFile : file.list()) {

				String absPath = file.getAbsolutePath() + "/" + dataFile;
				InputStream is;
				try {
					is = new FileInputStream(absPath);
				} catch (FileNotFoundException e) {
					System.err.println("Cannot read file: " + absPath);
					e.printStackTrace();
					continue;
				}

				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				try {
					while (br.ready()) {
						sb.append(br.readLine()).append("\n");
					}
				} catch (IOException e) {
					System.err.println("Cannot read file: " + absPath);
					e.printStackTrace();
					continue;
				}

				String text = sb.toString();

				Document doc = new Document();
				Field fieldAll = new Field(FIELD_ALL, text, Field.Store.YES,
						Field.Index.ANALYZED, Field.TermVector.YES);
				doc.add(fieldAll);
				storeIt(store, doc);
			}
		}
	}
}
