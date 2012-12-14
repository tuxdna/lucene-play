package org.tuxdna.play;

import org.apache.log4j.Logger;

public class App implements Constants {
	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("starting up...");
		System.out.println("Hello App!");
	}
}
