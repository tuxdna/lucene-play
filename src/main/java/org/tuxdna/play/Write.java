package org.tuxdna.play;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class Write implements Constants  {
	private static Logger logger = Logger.getLogger(Write.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		BasicConfigurator.configure();
		
		Logger.getRootLogger();
		
		String path = "/tmp/test-index";
		DocumentStore store = new DocumentStore(path);

		String text = "a b c d";

		Document doc = new Document();
		Field fieldAll = new Field(FIELD_ALL, text, Field.Store.NO,
				Field.Index.ANALYZED, Field.TermVector.NO);
		doc.add(fieldAll);

		if (store.write(doc)) {
			logger.info("Added document");
		} else {
			logger.info("Adding document failed!");
		}
	}

}
