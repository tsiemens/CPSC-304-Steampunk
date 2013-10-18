package org.steampunk.mist.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		// TODO: write rest of constructor
	}
	
	/**
	 * Loads the JDBC drivers. Must be called before any connection can be established.
	 */
	private void initJDBCDriver(){
		try
		{
			System.out.print("Loading JDBC driver ... ");

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			System.out.println("loaded.");
		}
		catch (SQLException e)
		{
			System.out.println("Unable to load driver\n" + e);
			System.exit(-1);
		}
	}
	
	/**
	 *	Establishes a connection to the database, getting the username and password
	 */
	private void startConnection(){
		Scanner scanner = new Scanner( System.in );
		System.out.println("Enter oracle sql username: ");
		mDbUserId = scanner.next();
		System.out.println("Enter oracle sql password: ");
		mDbPassword = scanner.next();
		
		try 
		{  
			System.out.print("Connecting to Oracle DB... ");

			mDbConnection = DriverManager.getConnection(CS_DATABASE_URL, mDbUserId, mDbPassword);

			System.out.println("connected.");

		} 
		catch( SQLException e ) 
		{
			System.out.println("Connection failed\n" + e);
			System.exit(-1);
		}
	}
	
	private boolean isDatabaseValid(){
		// TODO this probably needs to change somewhat
		try
		{
			System.out.print("Checking tables... ");

			Statement stmt = mDbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT table_name FROM user_tables");
			
			boolean tablesCreated = false;
			while(rs.next())
			{
				if (rs.getString(1).toLowerCase().contentEquals("games")){
					tablesCreated = true;
					System.out.println("found.");
				}
			}
			
			return tablesCreated;
			
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}

	private boolean createTables(Connection con)
	{
		System.out.println("Creating tables");
		try
		{
			Statement stmt = con.createStatement();
			stmt.execute("CREATE TABLE branch ("
					+"bid INTEGER PRIMARY KEY,"
					+"branch_name CHAR(20))" );
			stmt.close();
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}
}
