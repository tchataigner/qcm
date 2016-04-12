package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import login.DataConnect;

public class LoginDAO {

	public static boolean validate(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select login, password, power from user where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				// result found, means valid inputs
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return false;
		} finally {
			DataConnect.close(con);
		}
		return false;
	}

	public static int getDataPower(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select power from user where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			boolean encore = rs.next();
			
			while (encore) {
				String text = rs.getString(1);
				int power = Integer.parseInt(text);
				return power;
			}

		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return 0;
		} finally {
			DataConnect.close(con);
		}
		return 0;
	}
	
	public static int getDataId(String user, String password) {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = DataConnect.getConnection();
			ps = con.prepareStatement("Select id from user where login = ? and password = ?");
			ps.setString(1, user);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			boolean encore = rs.next();
			
			while (encore) {
				String text = rs.getString(1);
				int power = Integer.parseInt(text);
				return power;
			}

		} catch (SQLException ex) {
			System.out.println("Login error -->" + ex.getMessage());
			return 0;
		} finally {
			DataConnect.close(con);
		}
		return 0;
	}
}