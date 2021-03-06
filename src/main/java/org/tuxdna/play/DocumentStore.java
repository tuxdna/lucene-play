package org.tuxdna.play;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

class DocumentStore {
	private static Logger logger = Logger.getLogger(DocumentStore.class);
	IndexReader reader = null;
	Analyzer analyzer = null;
	IndexWriter writer = null;
	Directory dir = null;

	DocumentStore(String path) {
		logger.debug("Starting DocumentStore: " + path);
		try {
			File file = new File(path);
			if (!file.exists()) {
				logger.debug("Path doesn't exist. Creating it now: " + path);
				file.mkdirs();
			}
			dir = FSDirectory.open(new File(path));
			analyzer = new SimpleAnalyzer();
			writer = new IndexWriter(dir, analyzer,
					IndexWriter.MaxFieldLength.UNLIMITED);
			reader = IndexReader.open(dir, true);
		} catch (IOException e) {
			logger.fatal(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	public boolean write(Document doc) {
		try {
			writer.addDocument(doc);
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean update(Document doc) {
		try {
			Field field = doc.getField(Constants.FIELD_ID);
			String txt = doc.get(field.name());
			Term t = new Term(field.name(), txt);
			writer.updateDocument(t, doc);
			writer.commit();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	public Document read() {
		if (reader == null)
			return null;

		try {

			// enlist all the terms in corpus ( or the index )
			TermEnum terms = reader.terms();
			System.out.print("Corups Terms: ");
			while (terms.next()) {
				Term term = terms.term();
				System.out.print(term.text() + ", ");
			}
			System.out.println();

			// enlist terms per document
			int num = reader.numDocs();
			for (int i = 0; i < num; i++) {
				if (reader.isDeleted(i))
					continue;
				System.out.println("  Doc Id: " + i);
				Document d = reader.document(i);
				for (Fieldable field : d.getFields()) {
					System.out.println("    " + field.name());
					if ("byte_array".equals(field.name())) {
						byte[] bArr = field.getBinaryValue();
						System.out.println(Utility.join(bArr, ", "));
					} else if ("int_array".equals(field.name())) {
						byte[] v = field.getBinaryValue();
						int[] iArr = Utility.toIntArray(v);
						System.out.println(Utility.join(iArr, ", "));
					}
					if (field.isTermVectorStored()) {
						TermFreqVector tfvector = reader.getTermFreqVector(i,
								field.name());
						String[] doc_terms = tfvector.getTerms();
						int[] doc_freq = tfvector.getTermFrequencies();
						System.out.print("      ");
						for (int vec_idx = 0; vec_idx < tfvector.size(); vec_idx++) {
							System.out.print(doc_terms[vec_idx] + ":"
									+ doc_freq[vec_idx] + ", ");
						}
						System.out.println();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteAll() throws IOException {
		writer.deleteAll();
	}
	
	public int count() {
		return writer.maxDoc();
	}
	
	public void close() throws IOException {
		reader.close();
		writer.close();
		dir.close();
	}

	public void optimize() {
		try {
			writer.optimize();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}