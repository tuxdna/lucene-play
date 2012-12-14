package org.tuxdna.play;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
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

	public Document read() {
		if (reader == null)
			return null;

		try {
			TermEnum terms = reader.terms();
			while (terms.next()) {
				Term term = terms.term();
				System.out.println(term.text());
			}

			int num = reader.numDocs();
			for (int i = 0; i < num; i++) {
				if (reader.isDeleted(i))
					continue;
				System.out.println("id: " + i);
				Document d = reader.document(i);
				for (Fieldable field : d.getFields()) {
					System.out.println("  " + field.name());
					if (field.isTermVectorStored()) {
						TermFreqVector tfvector = reader.getTermFreqVector(i,
								field.name());
						String[] doc_terms = tfvector.getTerms();
						int[] doc_freq = tfvector.getTermFrequencies();
						for (int vec_idx = 0; vec_idx < tfvector.size(); vec_idx++) {
							System.out.println(doc_terms[vec_idx] + ""
									+ doc_freq[vec_idx]);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}