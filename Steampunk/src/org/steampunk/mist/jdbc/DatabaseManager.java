package org.steampunk.mist.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {

	private static final String CS_DATABASE_URL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
	
	private static DatabaseManager sInstance;
	
	private String mDbUserId;
	private String mDbPassword;
	
	private Connection mDbConnection;
	
	public static DatabaseManager getInstance(){
		if(sInstance == null){
			sInstance = new DatabaseManager();
		}
		
		return sInstance;
	}
	
	private DatabaseManager(){
		initJDBCDriver();
		startConnection();
		
		try {
			if(!isDatabaseValid()){
				createTables();
			}
		} catch (SQLException e) {
			System.out.println("Failed to check database validity: " + e);
			System.exit(-1);
		}
		// TODO: write rest of constructor
	}
	
	/**
	 * Loads the JDBC drivers. Must be called before any connection can be established.
	 */
	private void initJDBCDriver(){
		try {
			System.out.print("Loading JDBC driver ... ");

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			System.out.println("loaded.");
		} catch (SQLException e) {
			System.out.println("Unable to load driver\n" + e);
			System.exit(-1);
		}
	}
	
	/**
	 *	Establishes a connection to the database, getting the username and password
	 */
	private void startConnection(){
		Scanner scanner = new Scanner( System.in );
		System.out.println("Enter the corresponsing number for your database:\n"+
				"1 : Joey\n"+
				"2 : Lizann\n"+
				"3 : Sudi\n"+
				"4 : Trevor");
		String option = scanner.next();
		scanner.close();
		
		if(option.contentEquals("1")){
			// Joey's db
			mDbUserId = "ora_a9b8";
			mDbPassword = "a50998103";
		} else if(option.contentEquals("2")){
			// Lizann's db
			mDbUserId = "ora_i8r7";
			mDbPassword = "a51655108";
		} else if(option.contentEquals("3")){
			// Sudi's db
			mDbUserId = "ora_e4k8";
			mDbPassword = "a55211122";
		} else if(option.contentEquals("4")){
			// Trevor's db
			mDbUserId = "ora_x6a8";
			mDbPassword = "a43224104";
		} else {
			System.out.println("Invalid option");
			System.exit(0);
		}
		
		try {  
			System.out.print("Connecting to Oracle DB... ");

			mDbConnection = DriverManager.getConnection(CS_DATABASE_URL, mDbUserId, mDbPassword);

			System.out.println("connected.");

		} catch( SQLException e ) {
			System.out.println("Connection failed\n" + e);
			System.exit(-1);
		}
	}
	
	private boolean isDatabaseValid() throws SQLException{
		System.out.print("Checking tables... ");

		Statement stmt = mDbConnection.createStatement();

		List<String> tables = DatabaseSchema.getTableNames();
		ResultSet rs;

		for (String table : tables){
			rs = stmt.executeQuery("SELECT table_name FROM user_tables WHERE UPPER(table_name) = '"
					+table.toUpperCase() + "'");
			if(!rs.next()){
				System.out.println("\nDatabase invalid: " + table.toUpperCase() + " table not found.");
				stmt.close();
				return false;
			}
		}

		System.out.println("done.");
		stmt.close();
		return true;
	}

	/**
	 * Builds or rebuilds the database. If any tables exist with the same names,
	 * they will be overwritten, and data will be lost.
	 * 
	 * @return if succeeded in creating all tables
	 */
	private boolean createTables()
	{
		try
		{
			System.out.println("Cleaning database...");
			// Drop all relevant tables
			Statement stmt = mDbConnection.createStatement();
			ResultSet rs;
			
			List<String> tables = DatabaseSchema.getTableNames();
			Collections.reverse(tables);
			for (String table : tables){
				rs = stmt.executeQuery("SELECT table_name FROM user_tables WHERE UPPER(table_name) = '"
						+table.toUpperCase() + "'");
				if(rs.next()){
					System.out.println("Dropping " + table.toUpperCase() + " table");
					stmt.execute("DROP TABLE "+table);
				}
			}
			
			// Create all tables
			System.out.println("Building database...");
			
			List<String> createStatements = DatabaseSchema.getCreateTableStatements();
			for (String statement : createStatements){
				stmt.executeUpdate(statement);
			}
			
			stmt.close();
			System.out.println("done.");
		} catch (SQLException e) {
			System.out.println("FAILED WITH ERROR: " + e);
			return false;
		}
		return true;
	}
}
