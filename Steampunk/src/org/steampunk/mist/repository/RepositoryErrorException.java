package org.steampunk.mist.repository;

public class RepositoryErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3867803037389860987L;

	RepositoryErrorException(String reason) {
		super(reason);
	}
}
