import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DBManager {
	private Connection conn = null;

	public DBManager() {

	}

	public boolean createDB() {

		Statement stmt = null;
		boolean success = false;
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(Constants.DB_URL,
					Constants.USER, Constants.PASS);

			System.out.print("Creating database...");
			stmt = conn.createStatement();

			String sql;

			try {
				sql = "DROP DATABASE " + Constants.DB_NAME;
				stmt.executeUpdate(sql);
			} catch (Exception e) {
				// do nothing, it means the DB wasn't there previously.
			}

			sql = "CREATE DATABASE " + Constants.DB_NAME;
			stmt.executeUpdate(sql);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (success) {
			System.out.println(" done!");
			return true;
		}
		System.out.println(" failed!");
		return false;
	}

	private void dbConnect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.print("Connecting to database...");
			conn = DriverManager.getConnection(Constants.DB_URL
					+ Constants.DB_NAME, Constants.USER, Constants.PASS);
			System.out.println(" done!");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean createTable(Issue issue) {

		Statement stmt = null;
		boolean success = false;
		if (conn == null) {
			dbConnect();
		}
		dbConnect();
		try {
			System.out.print("Creating " + Constants.TABLE_NAME + " table...");
			stmt = conn.createStatement();

			String fields = "";
			for (String field : new Issue().getFields().keySet()) {
				fields += field + " VARCHAR(255), ";
			}
			String primaryKey = "PRIMARY KEY ( id )";
			String sql = "CREATE TABLE " + Constants.TABLE_NAME + " (" + fields
					+ primaryKey + ") ";
			stmt.executeUpdate(sql);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (success) {
			System.out.println(" done!");
			return true;
		}
		System.out.println(" failed!");
		return false;
	}

	public void sendIssuesToDb(ArrayList<Issue> issues) {
		Statement stmt = null;
		if (conn == null) {
			dbConnect();
		}

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Issue issue : issues) {
			Map<String, String> fields = issue.getFields();
			String fieldNames = "";
			String fieldValues = "";
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			Iterator it = fields.entrySet().iterator();
			while (it.hasNext()) {
				Entry field = (Entry) it.next();
				names.add((String) field.getKey());
				values.add((String) field.getValue());
			}
			for (int i = 0; i < names.size() - 1; i++) {
				fieldNames += names.get(i) + ", ";
				fieldValues += "'" + values.get(i) + "', ";
			}
			fieldNames += names.get(names.size() - 1);
			fieldValues += "'" + values.get(values.size() - 1) + "'";

			String sql = "INSERT INTO " + Constants.TABLE_NAME + " ("
					+ fieldNames + ")" + " VALUES (" + fieldValues + ")";
			try {
				stmt.executeUpdate(sql);

			} catch (SQLException e) {
				System.out.println(sql + " failed!");
				e.printStackTrace();
			}

		}
	}

	public void performPostExtractionActivities() {
		Statement stmt = null;
		if (conn == null) {
			dbConnect();
		}

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "";
		try {
			sql = "CREATE TABLE filteredIssues LIKE rawIssues";
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO filteredIssues SELECT * FROM rawIssues";
			stmt.executeUpdate(sql);
			
			sql = "delete from filteredIssues where closeDate = ''";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			System.out.println(sql + " failed!");
			e.printStackTrace();
		}

	}

	protected void finalize() throws Throwable {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}