package nz.co.solnet.helper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseHelper {

	// Hide the default constructor to prevent erroneous initialisation
	private DatabaseHelper() {

	}

	private static final String DATABASE_URL = "jdbc:derby:applicationdb;create=true";
	private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);

	/**
	 * Create a derby database if it doesn't exist and insert seed data.
	 */
	public static void initialiseDB() {

		try (Connection conn = DriverManager.getConnection(DATABASE_URL)) { // note: creating DB without username/password

			insertInitialData(conn);
		} catch (SQLException e) {
			logger.error("Error in inserting initial data", e);
		}
	}

	/**
	 * Insert sample seed data into the database.
	 * 
	 * @param conn
	 * @throws SQLException
	 */
	private static void insertInitialData(Connection conn) throws SQLException {

		try (Statement statement = conn.createStatement()) { // create statement for our initial table

			if (!doesTableExist("tasks", conn)) {

				StringBuilder sqlSB = new StringBuilder();
				sqlSB.append("CREATE TABLE tasks (id int not null generated always as identity,");
				sqlSB.append(" title varchar(256) not null,");
				sqlSB.append(" description varchar(1024),");
				sqlSB.append(" due_date date,");
				sqlSB.append(" status varchar(10),");
				sqlSB.append(" creation_date date not null,");
				sqlSB.append(" primary key (id))");
				statement.execute(sqlSB.toString()); // execute the statement

				// NOTE: added initial data for testing purposes
				testInitialData(conn);

				logger.info("Table created.");
			} else {
				logger.info("Table already exists");
			}
		}
	}

	/**
	 * Utility method to populate the database for testing purposes
	 *
	 * @param conn
	 */
	private static void testInitialData(Connection conn) throws SQLException {
		try (Statement statement = conn.createStatement()) { // create statement for our initial table

			if (doesTableExist("tasks", conn)) {

				StringBuilder sqlSB = new StringBuilder();
				sqlSB.append(addTask);
				statement.execute(sqlSB.toString());

				logger.info("Test data added");
			} else {
				logger.info("Table does not exist");
			}
		}
	}


	// sample SQL query templates for use by the key API tasks
	private static String selectAll = "SELECT * FROM tasks";
	private static String selectOverdue = "SELECT * FROM tasks" +
			"WHERE due_date < NOW()" +
			"ORDER BY due_date ASC";
	private static String selectId = "SELECT * FROM tasks" +
			"WHERE id = $id_input";
	private static String addTask = "INSERT INTO tasks (title, description, due_date, status, creation_date)" +
			"VALUES ('title', 'description', '12/7/2022', 'good', '12/5/2022')";
	private static String deleteTask = "DELETE FROM tasks" +
			"WHERE id = $id_input";
	private static String updateTask = "UPDATE tasks" +
			"SET column1 = value1, column2 = value2" +
			"WHERE id = $id_input";


	/**
	 * TODO Utility method to insert into the database
	 */
	public static void insertData() {

		try (Connection conn = DriverManager.getConnection(DATABASE_URL)) { // note: creating DB without username/password

			logger.info("TESTING DatabaseHelper Insert method");

			// call fetch method TODO
			//putData("TEST", conn);

			// NOTE: added initial data for testing purposes
			testInitialData(conn);

		} catch (SQLException e) {
			logger.error("Error in inserting data into database", e);
		}
	}

	/**
	 * Utility method to query the database
	 */
	public static void queryData() {

		try (Connection conn = DriverManager.getConnection(DATABASE_URL)) { // note: creating DB without username/password

			logger.info("TESTING DatabaseHelper Query method");

			// call fetch method
			fetchData("TEST", conn);

		} catch (SQLException e) {
			logger.error("Error in loading data from database", e);
		}
	}

	/**
	 * TODO: add functionality for API of put/pull/delete/edit/etc.
	 */
	private static void fetchData(String data, Connection conn) throws SQLException {

		try (Statement statement = conn.createStatement()) { // create query statement from our connection

			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append(selectAll);

			ResultSet resultSet = statement.executeQuery(sqlSB.toString()); // execute this query with our connection

			// check for data in the database
			if (!resultSet.next()) {

				logger.info("No data found in database.");

			} else {

				// go through the rows in the ResultSet
				while (resultSet.next()) {
					// ... get the various data out of this row in the database
					int id = resultSet.getInt("id");
					String title = resultSet.getString("title");
					String description = resultSet.getString("description");
					String due_date = resultSet.getString("due_date");
					String status = resultSet.getString("status");
					String creation_date = resultSet.getString("creation_date");

					String theFieldValues = String.format("Id: %s, title: %s, description: %s, due_date: %s, status: %s, creation_date: %s", id, title, description, due_date, status, creation_date);

					logger.info("DATA: " + theFieldValues);
				}
			}

			// don't forget to close what you've done
			resultSet.close();
			statement.close();
			conn.close();
		}
	}



	/**
	 * Checks if the table exists in the database.
	 * 
	 * @param tableName - table name to be checked in the database
	 * @param conn      - database connection to use
	 * @return - boolean to indicate of the table exists or not
	 * @throws SQLException
	 */
	private static boolean doesTableExist(String tableName, Connection conn) throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);

		return result.next(); // boolean value which returns if there is a next row in the result set
	}

	/**
	 * Utility method to drop the table.
	 */
	public static void cleanDatabase() {

		try (Connection conn = DriverManager.getConnection(DATABASE_URL);
				Statement statement = conn.createStatement()) {
			String sql1 = "DROP TABLE tasks";
			statement.execute(sql1);
			logger.info("Table dropped successfully");
		} catch (SQLException e) {
			logger.error("Error in dropping table", e);
		}
	}

	/**
	 * This method does a graceful shutdown for the database.
	 */
	public static void cleanupDB() {

		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {

			if (e.getSQLState().equals("XJ015")) {
				logger.info("Database shutdown successfully");
			} else {
				logger.error("Error in database shutdown", e);
			}
		}
	}
}
