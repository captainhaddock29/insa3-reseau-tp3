package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	
	String nbC;
	ServeurTCP serv;
	Socket client;
	
	public ServerThread(String nbClient, ServeurTCP serv, Socket client)
	{
		this.nbC = nbClient;
		this.serv = serv;
		this.client = client;
	}
	
	static void message(String msg){
		System.err.println(msg);
	    }

	    static void erreur(String msg){
	    	message("Erreur: "+msg);
	    }
	    
	public void run(){
		Socket s = client;
		String requete = null;
		BufferedReader in = null;
		PrintWriter out = null;
		
		System.out.println("Serveur ok");
		while(true){
			try {
				in = new BufferedReader(new 
						InputStreamReader(s.getInputStream()));  
				
			} catch (IOException e){
				erreur("Lecture socket "+e);
			}
			try { requete = in.readLine();} catch (IOException e){
				erreur("lecture "+e);
			}
			String response = serv.callMethod(requete, this);
			try {
				out = new PrintWriter(s.getOutputStream(),true);
				out.println("Server to "+nbC+" : "+response);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Client "+nbC+" : "+requete);
			/*for(Socket s1 : listSock.values())
			{
				try {
					out = new PrintWriter(s1.getOutputStream(),true);
					out.println("Client "+nbC+" : "+requete);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			
			if(requete.equals("QUIT"))
			{	
				try {
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Thread.currentThread().interrupt(); // Très important de réinterrompre
				System.out.println("Le client s'est fait tejjjjj");
				
                break;
			}
		}
	}
	
	public void setNbC(String nbC)
	{
		this.nbC = nbC;
	}
	
	public String getNbc()
	{
		return this.nbC;
	}
	
	
}
