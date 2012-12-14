package org.tuxdna.play;

import org.apache.log4j.BasicConfigurator;

public class Read {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		String indexPath = "/tmp/test-index/";
		if(args.length >= 1) {
			indexPath= args[0];
		}

		DocumentStore store = new DocumentStore(indexPath);
		store.read();
	}

}
