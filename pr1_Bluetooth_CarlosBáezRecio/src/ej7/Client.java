package ej7;

import java.io.*;
import java.util.Scanner;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;



public class Client {
	
	private String url = null;
	private static String text = ""; //mensajes recibidos
	
	public Client (String url) {
		this.url = url;
	}
	
	public void initChat () {
		try {
			//Abrimos la conexi�n
			StreamConnection con = (StreamConnection) Connector.open(url);
			
			//Creaci�n de los elementos para el envio y recepci�n de mensajes
			OutputStream os = con.openOutputStream();
			InputStream is = con.openInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		
			Scanner sc = new Scanner(System.in);
			String me = ""; //mensajes enviados
			
			System.out.println("Connected");
			while (!text.equals("FIN.")) {
				
				System.out.print("YOU>> ");
				me = sc.nextLine();
				bw.write(me);
				bw.newLine();
				bw.flush();
				
				System.out.println("Waiting for server response...");
				text = br.readLine(); //hasta que no recibo respuesta, espero.
				System.out.println("SERVER>> "+text);
				
				
				
			}
			
			System.out.println("Closing chat...");
			br.close();
			bw.close();
			sc.close();
			con.close();
			text = "";
			System.out.println("Chat is now closed");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
