package org.tuxdna.play;

import org.apache.log4j.BasicConfigurator;

public class Read {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		DocumentStore store = new DocumentStore("/tmp/test-index/");
		store.read();
	}

}
