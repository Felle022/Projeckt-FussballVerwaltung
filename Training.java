package Fussballverwaltung;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;

public class Training {
	
	static void createTable(Connection c, String tableName) {
		try {
			Statement stmt = c.createStatement();
			String sql = "create table if not exists " + tableName +	// 	in Zukunft keine ID einbauen(KundenID&ArtikelID zum PrimaryKey machen)
			" (teamname varchar(255),liga varchar(255),gruendungsjahr int,platzname varchar(255),kapazitaet int, datum DATE, primary key(teamname,liga,gruendungsjahr,platzname,kapazitaet,Datum), foreign key (teamname,liga,gruendungsjahr) references Team (teamname,liga,gruendungsjahr), foreign key (platzname,kapazitaet) references platz (platzname,kapazitaet));";
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println();
			System.out.println("Tabelle " + tableName + " wurde erstellt.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void Traininganlegen(Connection c, String teamname, String platzname, String Termin) {
		
		String sel1="select liga,gruendungsjahr from team where teamname='"+teamname+"';";
		String sel2="select kapazitaet from platz where platzname='"+platzname+"';";
		String sel3="select teamname,platzname,datum from Training where platzname='"+platzname +"' and datum='"+Termin+"';";
		String insert="insert into Training(teamname,liga,gruendungsjahr,platzname,kapazitaet,datum)values(?,?,?,?,?,?);";
		//System.out.println(sel1);
		//System.out.println(sel2);	
		
		//ver...Vergleichsvariable
		String verTeamname="";
		String verPlatzname="";
		Date verDate = Date.valueOf("1900-01-01");
		String liga="";
		int gruendungsjahr=0;
		int kapazitaet=0;
		
		try {
			
			Statement stmt = c.createStatement();
			
			//Restlichen Daten vom Table Mannschaften selektiert
			ResultSet rs= stmt.executeQuery(sel1);
			while(rs.next()) {
				liga=rs.getString("liga");
				gruendungsjahr =rs.getInt("gruendungsjahr");
			}
			rs.close();
			
			//Restlichen Daten vom Table Platz selektiert
			ResultSet rs1= stmt.executeQuery(sel2);
			while(rs1.next()) {
				kapazitaet=rs1.getInt("kapazitaet");
			}
			rs1.close();
			
			//Datum & Platz wird vergliechen damit es keine doppel Einträge gibt 
			ResultSet rs2= stmt.executeQuery(sel3);
			while(rs2.next()) {
				verTeamname=rs2.getString("teamname");
				verPlatzname=rs2.getString("platzname");
				verDate=rs2.getDate("datum");
				
			}
			rs2.close();
			stmt.close();
			//System.out.println(verTeamname);
			//System.out.println(verPlatzname);
			//System.out.println(verDate);
			//System.out.println(Termin);
			if(verDate.equals(Date.valueOf(Termin)) && verPlatzname.equals(platzname)) { //TODO checken ob es keine Doppeleinträge mehr gibt 
				System.out.printf(""+verTeamname+" hat diesen Platz bereits am "+Termin+" gemietet\n");
			}else {
			PreparedStatement pstmt=c.prepareStatement(insert);
			pstmt.setString(1, teamname);
			pstmt.setString(2, liga);
			pstmt.setInt(3, gruendungsjahr);
			pstmt.setString(4, platzname);
			pstmt.setInt(5,kapazitaet);
			Date date1;
			date1 = Date.valueOf(Termin);
			pstmt.setDate(6,date1);
			pstmt.executeUpdate();
			pstmt.close();
			System.out.println("Sie haben so eben den "+platzname+"für den "+Termin+" erfolgreich reserviert.");
			}
		} catch (SQLException e) {
			System.out.println("Eingaben waren inkorrekt");
			//e.printStackTrace();
		}
	}

	static void TrainingJasonWrite(Connection c,String path) {
		try {
			FileWriter fw = new FileWriter(path);
		 
		JSONObject json = new JSONObject();
		String s = "";
		
		Statement stmt = c.createStatement();
		String sql = "select teamname, platzname, datum from Training;";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String teamname = rs.getString("teamname");
			String platzname = rs.getString("platzname");
			Date datum = rs.getDate("datum");
			
			json.put("Teamname", teamname);
			json.put("Platzname", platzname);
			json.put("Datum", datum);	
			s = s + json;
		
		}
		//System.out.println(s);
		fw.write(s);
		fw.flush();
		fw.close();
		rs.close();
		stmt.close();
		System.out.println("Datei wurde geschrieben");
		}
		catch (SQLException e) {
			System.out.println("Einagebn waren ingültig");
		}catch (IOException e1) {
			System.out.println("Etwas hat nicht funktioniert");
		}
		
	}
}
