package Fussballverwaltung;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Verwaltung {
	//TODO Config Datei einbinden
	static String url = "jdbc:mysql://localhost:3306/Fussballverwaltung";
	static String user = "root"; // user anlegen
	static String pass = "";  //PW
	
	static boolean loopMain=true;
	static boolean loopTeam=true;
	static boolean loopPlatz=true;
	static boolean loopTraining=true;
	static boolean loopAusgabe=true;
	static String auswahlMain="0";
	static String auswahl="0";
	
	static String pathJson;
	static String pathCSV;
	static String Termin;
	
	static String teamname="";
	static String liga="";
	static int gruendungsjahr=0;
	
	static String platzname="";
	static int kabinen=0;
    
	static Connection getConnection(String tableName) throws ClassNotFoundException {
		try {
			return DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		//TODO Loop erstellen
		Connection c= getConnection("fussballverwaltung.db");
		Team.createTable(c, "team");
		Platz.createTable(c, "platz");
		Scanner sc = new Scanner(System.in);
		//Loop
		while(loopMain) {
		System.out.println("Platzverwaltung:\nTeam in Sytem aufnehmen(1)\nPlatz zum vermieten anbieten(2)\nPlatz f�r ein Training buchen(3)\nAsuagbe der Trainings(4)\nBeenden(5)");
		auswahlMain=sc.nextLine();
		if(auswahlMain.equals("1")) {
		//Loop
			while(loopTeam) {
		System.out.println("Ein Team manuell eingeben(1)");
		System.out.println("Mehrere Teams durch eine CSV-Datei einlesen(2)");
		auswahl=sc.nextLine();
		if(auswahl.equals("1")) {
			System.out.println("Bitte den vollst�ndigen Teamname eingeben");
			teamname=sc.nextLine();
			System.out.println("Bitte Ihre aktuelle Liga/Klasse eingebn");
			liga=sc.nextLine();
			System.out.println("Bitte das Gr�ndungsjahr eingeben");
			gruendungsjahr=sc.nextInt();
			System.out.println(teamname);
			System.out.println(liga);
			System.out.println(gruendungsjahr);
			Team.insertTeam(c, teamname, liga, gruendungsjahr);
			loopTeam=false;
		}else if(auswahl.equals("2")) {
			System.out.println("Bitte geben sie den Pfad zu ihrer CSV-Datei ein");
			pathCSV=sc.nextLine();
			System.out.println(pathCSV);
			Team.CSVInsert(c, pathCSV);
			loopTeam=false;
		}
		if(!auswahl.equals("1")&&!auswahl.equals("2")){
			System.out.println("Ung�ltige Einagbe");
		}
		}
		}
		if(auswahlMain.equals("2")) {
			//Loop
			while(loopPlatz) {
			System.out.println("Eine Platz manuell eingeben(1)");
			System.out.println("Mehrere Pl�tze durch eine CSV-Datei einlesen(2)");
			auswahl=sc.nextLine();
			if(auswahl.equals("1")) {
				System.out.println("Bitte den vollst�ndigen Platzname eingeben");
				platzname=sc.nextLine();
				System.out.println("Bitte die Anzahl der Kabinen angeben");
				kabinen=sc.nextInt();
				System.out.println(platzname);
				System.out.println(kabinen);
				Platz.insertPlatz(c, platzname, kabinen);
				loopPlatz=false;
			}else if(auswahl.equals("2")) {
				System.out.println("Bitte geben sie den Pfad zu ihrer CSV-Datei ein");
				pathCSV=sc.nextLine();
				System.out.println(pathCSV);
				Platz.CSVInsert(c, pathCSV);
				loopPlatz=false;
			}
			if(!auswahl.equals("1")&&!auswahl.equals("2")){
				System.out.println("Ung�ltige Einagbe");
			}
			}
		}
		if(auswahlMain.equals("3")) {
			System.out.println("Teamname eingeben");
			teamname=sc.nextLine();
			System.out.println("Platz ausw�hlen");
			platzname=sc.nextLine();
			System.out.println("Bitte den gew�nschten Termin eingeben\nBsp:2000-01-01");
			Termin=sc.nextLine();
			Training.Traininganlegen(c, teamname, platzname, Termin);
		}
		if(auswahlMain.equals("4")) {
			System.out.println("Speicherort angeben");
			pathJson=sc.nextLine();
			Training.TrainingJasonWrite(c, pathJson);
		}
		if(auswahlMain.equals("5")) {
			System.out.println("Programm wird beendet");
			loopMain=false;
		}
		//System.out.println(auswahl);
		if(!auswahlMain.equals("1")&&!auswahlMain.equals("2")&&!auswahlMain.equals("3")&&!auswahlMain.equals("4")&&!auswahlMain.equals("5")){
			System.out.println("Keine g�ltige eingabe\nBitte erneut ausw�hlen");
		}
	}
	sc.close();
	}

}
