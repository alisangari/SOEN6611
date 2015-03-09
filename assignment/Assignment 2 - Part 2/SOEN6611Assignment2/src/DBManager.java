import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

	public boolean createIssuesTable(Issue issue) {
		Statement stmt = null;
		boolean success = false;
		if (conn == null) {
			dbConnect();
		}
		dbConnect();
		try {
			System.out.print("Creating " + Constants.ISSUES_TABLE_NAME
					+ " table...");
			stmt = conn.createStatement();

			String fields = "";
			for (String field : new Issue().getFields().keySet()) {
				fields += field + " VARCHAR(255), ";
			}
			String primaryKey = "PRIMARY KEY ( id )";
			String sql = "CREATE TABLE " + Constants.ISSUES_TABLE_NAME + " ("
					+ fields + primaryKey + ") ";
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

			String sql = "INSERT INTO " + Constants.ISSUES_TABLE_NAME + " ("
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

//			sql = "INSERT INTO filteredIssues SELECT * FROM rawIssues";
			sql = "INSERT INTO filteredIssues SELECT `cc`, STR_TO_DATE(`closeDate`, '%M %Y'), `owner`, `attachment`, `os`, STR_TO_DATE(`reportDate`, '%a %M %d %H:%i:%s %Y'), `id`, `priority`, `type`, `status` FROM rawIssues";
			stmt.executeUpdate(sql);

			sql = "delete from filteredIssues where closeDate = ''";
			stmt.executeUpdate(sql);

			sql = "ALTER TABLE filteredIssues ADD closeDateF VARCHAR(255)";
			stmt.executeUpdate(sql);

			sql = "ALTER TABLE filteredIssues ADD reportDateF VARCHAR(255)";
			stmt.executeUpdate(sql);

			sql = "ALTER TABLE filteredIssues ADD timeDelta VARCHAR(255)";
			stmt.executeUpdate(sql);
			
			sql = "delete from filteredissues where closeDate = '0000-00-00';";
			stmt.executeUpdate(sql);

			 //updates date to correct format
			sql = "update filteredIssues set closeDateF= SUBSTRING(closeDate,1,10)";
			stmt.executeUpdate(sql);
			
			 //updates date to correct format
			sql = "update filteredIssues set reportDateF= SUBSTRING(reportDate,1,10)";
			stmt.executeUpdate(sql);
			
			//in order to use datediff function we should have value for the day too, we suppose the close day end of the month.
			sql = "UPDATE filteredIssues SET closeDateF = REPLACE(closeDateF, '-00', '-28')";
			stmt.executeUpdate(sql);
			
			 // and finally the timeDelta
			sql = "update filteredIssues SET timeDelta = DATEDIFF (closeDateF, reportDateF)";
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

	public boolean createCcsTable() {
		Statement stmt = null;
		boolean success = false;
		if (conn == null) {
			dbConnect();
		}
		try {
			System.out.print("Creating " + Constants.CCS_TABLE_NAME
					+ " table...");
			stmt = conn.createStatement();

			String fields = "id INT(10) NOT NULL AUTO_INCREMENT, issueId VARCHAR(255), email VARCHAR(255), ";
			String primaryKey = "PRIMARY KEY ( id )";
			String sql = "CREATE TABLE " + Constants.CCS_TABLE_NAME + " ("
					+ fields + primaryKey + ") ";
			stmt.executeUpdate(sql);
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (success) {
			System.out.println(" done!");
			return true;
		}
		return false;
	}

	public void insertCcs(String issueId, List<String> ccList) {
		Statement stmt = null;
		if (conn == null) {
			dbConnect();
		}

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String fieldNames = "(issueId, email)";
		String fieldValues = "";

		for (String email : ccList) {
			fieldValues += "('" + issueId + "', '" + email + "'), ";
		}
		try {
			fieldValues = fieldValues.substring(0, fieldValues.length() - 2);
		} catch (Exception e) {
		}

		String sql = "INSERT INTO " + Constants.CCS_TABLE_NAME + fieldNames
				+ " VALUES " + fieldValues;
		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
		}
	}

}