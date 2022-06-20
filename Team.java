package Fussballverwaltung;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Team {
	
	static public void createTable(Connection c, String tableName)
	{
		try {
			Statement st= c.createStatement();
			String sql="create table if not exists "+tableName+"(teamname varchar(255),liga varchar(255),gruendungsjahr int, primary key(teamname,liga,gruendungsjahr))";
			st.executeUpdate(sql);
			st.close();
			System.out.println();
			//System.out.println("Table "+tableName+" wurde erstellt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static public void insertTeam(Connection c, String teamname,String liga,int gruendungsjahr)
	{
		try {
			String sql ="insert ignore into Team(teamname,liga,gruendungsjahr)values(?,?,?);";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, teamname);
			st.setString(2,liga);
			st.setInt(3, gruendungsjahr);
			st.executeUpdate();
			st.close();
			System.out.println(""+teamname+" wurde in das Sytem aufgenommen.");
		} catch (SQLException e) {
			System.out.println("Einagaben waren ungültig");
		}
		
	}

	static void CSVInsert(Connection c, String path) {
		File file = new File(path);
		String daten = "";
		
		try {
			Scanner sc = new Scanner(file);
			String firstLine = sc.nextLine();
			String[] insert = new String[2];
			
			while (sc.hasNextLine()) {
				daten = sc.nextLine();
				daten.trim();
				insert = daten.split(";");
				
				String sql = "insert ignore into team(teamname,liga,gruendungsjahr) values (?, ?, ?);";
				PreparedStatement st;
				try {
					
					
					st = c.prepareStatement(sql);
					st.setString(1, insert[0]);
					st.setString(2, insert[1]);
					int age = Integer.parseInt(insert[2]);
					st.setInt(3, age);
					st.executeUpdate();
					st.close();
					System.out.println(
							"Die Mannschaft " + insert[0] + " wurde erfolgreich aus der CSV-Datei ausgelsen.");
					
				} catch (SQLException e) {
					System.out.println("Es gibt Fehler in der angegebenen CSV-Datei");
				}

			}
		} catch (FileNotFoundException e) {
			System.out.println("Der angegebene Pfad ist nicht auffindbar");
		}

	}
}
