
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class DBConnector {
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:mysql://127.0.0.1:3306/HumanBenchMark?serverTimezone=Asia/Seoul&useSSL=false";
	String user = "root"; // 사용자 mysql 계정에 맞게 바꿔야 합니다!
	String passwd = "eowlrhd123"; // 사용자 mysql 계정에 맞게 바꿔야 합니다!

	DBConnector() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
			return;
		}
		try {
			con = DriverManager.getConnection(url, user, passwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean sendLoginInformation(String id, String pw) {
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM user WHERE id = '%s' and password = '%s'".formatted(id, pw));
			if (rs.next()) {
				System.out.println("value");
				return true;
			} else {
				System.out.println("값 없음");
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	boolean sendCreateAccountInfo(String Iid, String Ipw) {
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO user VALUES('%s','%s')".formatted(Iid, Ipw));
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	boolean sendRTScore(String id, String score) {
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO rtscore(id, reaction_time) VALUES('%s','%s')".formatted(id, score));
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	boolean sendNumScore(String id, String score) {
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO numscore(id, num_memory) VALUES('%s','%s')".formatted(id, score));
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}

	ArrayList<String> getRtRank() {
		ArrayList<String[]> doubleString = new ArrayList<>();
		Object[] oob=null;
		ArrayList<String> resStr = new ArrayList<>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT rank() over(order by reaction_time Asc) as '순위',id, reaction_time From rtscore ORDER BY reaction_time ASC;");
			while (rs.next()) {
				String[] o = new String[3];
				for (int i = 0; i < 3; i++) {
					o[i] = rs.getString(i + 1);
				}
				doubleString.add(o);
			}
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		oob = doubleString.toArray();
		String resultString = Arrays.deepToString(oob);
		resultString = resultString.replace(" ", "");
		resultString = resultString.replace("[", "");
		resultString = resultString.replace("]", "");
		StringTokenizer stk = new StringTokenizer(resultString, ",");
		while(stk.hasMoreTokens()) {
			resStr.add(stk.nextToken());
		}
		System.out.println(resStr);
		return resStr;
	}
	
	ArrayList<String> getNumRank() {
		ArrayList<String[]> doubleString = new ArrayList<>();
		Object[] oob=null;
		ArrayList<String> resStr = new ArrayList<>();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT rank() over(order by num_memory DESC) as \"순위\",id, num_memory From numscore ORDER BY num_memory Desc;");
			while (rs.next()) {
				String[] o = new String[3];
				for (int i = 0; i < 3; i++) {
					o[i] = rs.getString(i + 1);
				}
				doubleString.add(o);
			}
		} catch (SQLException e) {
			System.out.println(e);
			return null;
		}
		oob = doubleString.toArray();
		String resultString = Arrays.deepToString(oob);
		resultString = resultString.replace(" ", "");
		resultString = resultString.replace("[", "");
		resultString = resultString.replace("]", "");
		StringTokenizer stk = new StringTokenizer(resultString, ",");
		while(stk.hasMoreTokens()) {
			resStr.add(stk.nextToken());
		}
		System.out.println(resStr);
		return resStr;
	}

}
