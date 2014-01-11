package org.tuxdna.play;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import junit.framework.TestCase;

// http://wiki.apache.org/lucene-java/LuceneFAQ#How_do_I_delete_documents_from_the_index.3F

public class DeleteTest extends TestCase {
	public void testDelete() {
		String path = "/tmp/test-index";
		DocumentStore store = new DocumentStore(path);
		boolean deleted;
		try {
			store.deleteAll();
			deleted = true;
		} catch (IOException e) {
			deleted = false;
		}
		assertTrue(deleted);
		assertEquals(0, store.count());
		try {
			store.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		store = new DocumentStore(path);
		assertEquals(0, store.count());
		Document doc = null;
		doc = Utility.getSampleDocument("0");
		store.write(doc);
		assertEquals(1, store.count());
		doc = Utility.getSampleDocument("1");
		store.write(doc);
		assertEquals(2, store.count());

		// update field value in document
		{
			Field f = doc.getField(Constants.FIELD_ALL);
			f.setValue("q w e r");
			doc.removeField(Constants.FIELD_ALL);
			doc.add(f);
		}
		// update into lucene
		store.update(doc);
		
		// once we optimize, the document actually gets deleted
		store.optimize();
		assertEquals(2, store.count());
	}
}
