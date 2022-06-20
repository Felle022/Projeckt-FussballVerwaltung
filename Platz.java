package Fussballverwaltung;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Platz {
	
	static public void createTable(Connection c, String tableName)
	{
		try {
			Statement st= c.createStatement();
			String sql="create table if not exists "+tableName+"(platzname varchar(255),kapazitaet int, primary key(platzname,kapazitaet))";
			st.executeUpdate(sql);
			st.close();
			System.out.println();
			//System.out.println("Table "+tableName+" wurde erstellt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static public void insertPlatz(Connection c, String platzname,int kapazitaet)
	{
		try {
			String sql ="insert ignore into platz(platzname,kapazitaet)values(?,?);";
			PreparedStatement st = c.prepareStatement(sql);
			st.setString(1, platzname);
			st.setInt(2, kapazitaet);
			st.executeUpdate();
			st.close();
			System.out.println(""+platzname+" wurde in das Sytem aufgenommen.");
		} catch (SQLException e) {
			System.out.println("Eingaben waren ungültig");
		}
		
	}

	static void CSVInsert(Connection c, String path) {
		File file = new File(path);
		String daten = "";
		
		try {
			Scanner sc = new Scanner(file);
			String firstLine = sc.nextLine();
			String[] insert = new String[1];
			

			while (sc.hasNextLine()) {
				daten = sc.nextLine();
				daten.trim();
				insert = daten.split(";");
				String sel="select * from platz where platzname='"+insert[0]+"' and kapazitaet="+insert[1]+";";
				
				String sql = "insert ignore into platz(platzname,kapazitaet) values (?, ?);";
				PreparedStatement st;
				try {
					
					st = c.prepareStatement(sql);
					st.setString(1, insert[0]);
					int kap = Integer.parseInt(insert[1]);
					st.setInt(2, kap);
					st.executeUpdate();
					st.close();
					System.out.println(
							"" + insert[0] + " wurde erfolgreich aus der CSV-Datei ausgelsen.");
					
				} catch (SQLException e) {
					System.out.println("Es gibt Fehler in der angegebenen CSV-Datei");
					
				}

			}
		} catch (FileNotFoundException e) {
			System.out.println("Der angegebene Pfad ist nicht auffindbar");
			
		}

	}

}
