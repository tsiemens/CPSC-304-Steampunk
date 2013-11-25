package org.steampunk.mist.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
			sInstance.initDatabase();
		}
		
		return sInstance;
	}
	
	private DatabaseManager(){
		initJDBCDriver();
		startConnection();
	}
	
	/*
	 * Initializes the database, if it needs to be initialized. Otherwise, does nothing.
	 */
	private void initDatabase(){
		try {
			if(!isDatabaseValid()){
				cleanDatabase();
				createTables();
				DatabasePopulator.demoPopulate();
			}
		} catch (SQLException e) {
			System.out.println("Failed to check database validity: " + e);
			System.exit(-1);
		}
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
			System.err.println("Invalid option");
			System.exit(0);
		}
		
		try {  
			System.out.print("Connecting to Oracle DB... ");

			mDbConnection = DriverManager.getConnection(CS_DATABASE_URL, mDbUserId, mDbPassword);
			mDbConnection.setAutoCommit(true);
			
			System.out.println("connected.");

		} catch( SQLException e ) {
			System.err.println("Connection failed\n" + e);
			System.exit(-1);
		}
	}
	
	/**
	 * @return true if the database contains all tables in the database schema, false otherwise
	 * @throws SQLException
	 */
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
		
		List<String> views = DatabaseSchema.getViewNames();

		for (String view : views){
			rs = stmt.executeQuery("SELECT view_name FROM user_views WHERE UPPER(view_name) = '"
					+view.toUpperCase() + "'");
			if(!rs.next()){
				System.out.println("\nDatabase invalid: " + view.toUpperCase() + " view not found.");
				stmt.close();
				return false;
			}
		}

		System.out.println("done.");
		stmt.close();
		return true;
	}
	
	/**
	 * Drops all tables contained in the schema
	 * @return
	 */
	private boolean cleanDatabase()
	{
		try
		{
			System.out.println("Cleaning database...");
			// Drop all relevant tables
			Statement stmt = mDbConnection.createStatement();
			ResultSet rs;

			List<String> views = DatabaseSchema.getViewNames();
			Collections.reverse(views);
			for (String view : views){
				rs = stmt.executeQuery("SELECT view_name FROM user_views WHERE UPPER(view_name) = '"
						+ view.toUpperCase() + "'");
				if(rs.next()){
					System.out.println("Dropping " + view.toUpperCase() + " view");
					stmt.execute("DROP VIEW "+ view);
				}
			}
			
			List<String> tables = DatabaseSchema.getTableNames();
			Collections.reverse(tables);
			for (String table : tables){
				rs = stmt.executeQuery("SELECT table_name FROM user_tables WHERE UPPER(table_name) = '"
						+table.toUpperCase() + "'");
				if(rs.next()){
					System.out.println("Dropping " + table.toUpperCase() + " table");
					stmt.execute("DROP TABLE "+ table);
				}
			}
						
			
			stmt.close();
			System.out.println("done.");
		} catch (SQLException e) {
			System.err.println("\nFailed to clean database: " + e);
			return false;
		}	
		return true;
	}

	/**
	 * Builds the database. Assumes that no tables exist with the same name
	 * 
	 * @return true if succeeded in creating all tables
	 */
	private boolean createTables()
	{
		try
		{
			System.out.print("Building database...");
			Statement stmt = mDbConnection.createStatement();
			
			// Create tables
			List<String> createStatements = DatabaseSchema.getCreateTableStatements();
			for (String statement : createStatements){
				System.out.println(statement);
				stmt.executeUpdate(statement);
			}
			
			// Create views
			List<String> createViewStatements = DatabaseSchema.getCreateViewStatements();
			for (String viewStatement : createViewStatements){
				System.out.println(viewStatement);
				stmt.executeUpdate(viewStatement);
			}
			
			stmt.close();
			System.out.println("done.");
			
			if( !DatabasePopulator.addRequiredEntities() ){
				cleanDatabase();
				return false;
			}
		} catch (SQLException e) {
			System.err.println("\nFailed to create tables: " + e);
			return false;
		}
		return true;
	}
	
	/**
	 * Performs the query for the string passed in
	 * @param queryString
	 * @return the Result set for the query, or null if an error occurred.
	 * @throws SQLException
	 */
	public ResultSet query(String queryString) throws SQLException {
		Statement stmt = mDbConnection.createStatement();	
		return stmt.executeQuery(queryString);	
	}
	
	/**
	 * Performs the update on the database, for the passed statement string
	 * @param updateString
	 * @return the row count or 0 if update returns nothing
	 * @throws SQLException
	 */
	public int update(String updateString) throws SQLException{
		Statement stmt = mDbConnection.createStatement();	
		return stmt.executeUpdate(updateString);
	}
	
	/**
	 * Performs the query for the prepared query string passed in, and other parameters.
	 * The number of parameters in the query string, and number of parameters passed in must be the same
	 * @param queryString
	 * @param parameters : the query parameters. Currently only supports objects of type Integer,
	 *  Boolean, java.sql.Date, Float, Double, String
	 * @return the Result set for the query
	 * @throws SQLException
	 */
	public ResultSet queryPrepared(String queryString, Object... parameters) throws SQLException{
		PreparedStatement stmt = mDbConnection.prepareStatement(queryString);
		prepareStatement(stmt, parameters);
		return stmt.executeQuery();
	}
	
	/**
	 * Performs the update for the prepared update string passed in, and other parameters.
	 * The number of parameters in the update string, and number of parameters passed in must be the same
	 * @param updateString
	 * @param parameters : the query parameters. Currently only supports objects of type Integer,
	 *  Boolean, java.sql.Date, Float, Double, String
	 * @return the row count or 0 if update returns nothing
	 * @throws SQLException
	 */
	public int updatePrepared(String queryString, Object... parameters) throws SQLException{
			PreparedStatement stmt = mDbConnection.prepareStatement(queryString);
			prepareStatement(stmt, parameters);
			return stmt.executeUpdate();
	}
	
	/**
	 * Adds the parameters appropriately to the prepares statement
	 * Elements in parameters must be either Integer, Boolean, java.sql.Date, Float, Double, String
	 * @param ps
	 * @param parameters
	 * @throws SQLException : ps could be in an unknown state if this is thrown
	 */
	private void prepareStatement(PreparedStatement ps, Object[] parameters) throws SQLException {
		for (int i = 0; i < parameters.length; i++)
		{
			if( parameters[i] instanceof Integer ) {
				// Integer
				ps.setInt(i+1, ((Integer)parameters[i]).intValue());
			} else if( parameters[i] instanceof Double ) {
				// Double
				ps.setDouble(i+1, ((Double)parameters[i]).doubleValue());
			} else if( parameters[i] instanceof Float ) {
				// Float
				ps.setFloat(i+1, ((Float)parameters[i]).floatValue());
			} else if( parameters[i] instanceof java.sql.Date ) {
				// Date
				ps.setDate(i+1, ((java.sql.Date)parameters[i]));
			} else if( parameters[i] instanceof java.sql.Timestamp ) {
				// Date
				ps.setTimestamp(i+1, ((java.sql.Timestamp)parameters[i]));
			} else if( parameters[i] instanceof String ) {
				// String
				ps.setString(i+1, ((String)parameters[i]));
			}  else if( parameters[i] instanceof byte[] ) {
				// bytes
				ps.setBytes(i+1, ((byte[])parameters[i]));
			} else if( parameters[i] instanceof Boolean ) {
				// Boolean
				ps.setBoolean(i+1, ((Boolean)parameters[i]).booleanValue());
			} else {			
				throw new SQLException("Invalid parameter type: "+parameters[i].getClass().getSimpleName());
			}
		}
	}
}
