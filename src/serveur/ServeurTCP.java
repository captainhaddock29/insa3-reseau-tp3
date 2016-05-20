package serveur;

import java.net.*;
import java.sql.Time;
import java.io.*;
import java.util.*;

public class ServeurTCP
{
    static String nomServeur ="-- Serveur TCP Java --";

    int port = 8888;  // port par défaut
	static long time_spent = 0;
    static int nb_requetes = 0;
    static ServerSocket socket;
    static Socket client;
    static int nbClient = 0;
    String adressIP = "localhost";
    
    
    protected static HashMap<String,Socket> listSock;

    
    static void message(String msg){
		System.err.println(msg);
	 }
    
    static void erreur(String msg){
    	message("Erreur: "+msg);
    }

    static String date(){
    	// ou System.currentTimeMillis()
    	Date d= new Date();
	
    	return d.toString();
    }

    
    public void start()
    {
    	listSock = new HashMap<String,Socket>();
		// Création de la socket
    	System.out.println("Démarrage Serveur sur le port "+port);
		try {socket = new ServerSocket(port);} catch (IOException e){
		erreur("Impossible d'ouvrir le port "+port+":"+e);}	
	    System.out.println("Démarrage Serveur sur le port "+port);
	    Thread thr;
	    
	    while( true ) { // Attente de connexion
			
			try {
				client = socket.accept();
				System.out.println("Un nouveau client est arrivé.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Thread th = new ServerThread(nbClient+"",this,client);
			listSock.put(nbClient+"", client);
			th.start();
			nbClient++;
			
		}
	    
    }
    
    public String callMethod(String requete, ServerThread t)
    {
    	
    	String[] tabReq = requete.split(" ");
    	String result = "";
    	if(tabReq[0].equals("CALL"))
    	{
    		switch (tabReq[1])
    		{
    			case "ADD" :
    				if(tabReq[2]!=null && tabReq[3]!=null)
    				{
    					
    					result="RPLY ADD "+add(Integer.valueOf(tabReq[2]), Integer.valueOf(tabReq[3]))+" \n"; 
    				}
    				else
    				{
    					result = "";
    				}
    				break;
    			case "MULT" :
    				if(tabReq[2]!=null && tabReq[3]!=null)
    				{
    					
    					result="RPLY MULT "+mult(Integer.valueOf(tabReq[2]), Integer.valueOf(tabReq[3]))+" \n"; 
    				}
    				else
    				{
    					result = "";
    				}
    				break;
    				
    			case "REGISTER" :
    				if(tabReq[2]!=null )
    				{
    					String nbC = tabReq[2];
    					Socket skt = listSock.remove(nbC);
    					listSock.put(tabReq[2], skt);
    					result="RPLY REGISTER "+tabReq[2]+" \n"; 
    					t.setNbC(tabReq[2]);
    				}
    				else
    				{
    					result = "";
    				}
    				break;
    			default :
    				result = "";
    				break;
    		}
    	}
    	else
    	{
    		result = "NONE";
    	}
    	return result;
    }
    
    public static int add(int a, int b){
    	return a+b;
    }
    public static int mult(int a, int b){
    	return a*b;
    }

    public ServeurTCP(String ip, int port)
    {
    	this.adressIP = ip;
    	this.port = port;
    }
    

    
    public String getIP()
    {
    	return this.adressIP;
    }
    
    public int getPort()
    {
    	return this.port;
    }
    
    
    
}