package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServeurNommage {

	
	  static String nomServeur ="-- Serveur TCP Java --";

	    int port = 8888;  // port par défaut
		static long time_spent = 0;
	    static int nb_requetes = 0;
	    static ServerSocket socket;
	    static Socket client;
	    static int nbClient = 0;
	    String adressIP = "localhost";
	    

	    private HashMap<String,ServeurTCP> listServeur;
	    public HashMap<String, ServeurTCP> getListServeur() {
			return listServeur;
		}

		public void setListServeur(HashMap<String, ServeurTCP> listServeur) {
			this.listServeur = listServeur;
		}

		private HashMap<String,Socket> listSock;
	    
	   
	    public void start()
	    {
	    	
			// Création de la socket
			try {socket = new ServerSocket(port);} catch (IOException e){
			erreur("Impossible d'ouvrir le port "+port+":"+e);}	
		    System.out.println("Démarrage Serveur de nommage sur le port "+port);
		    Thread thr;
		    
		    while( true ) { // Attente de connexion
				
				try {
					client = socket.accept();
					System.out.println("Un nouveau client est arrivé.");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ServerNommageThread th = new ServerNommageThread(nbClient+"",this,client);
				listSock.put(nbClient+"", client);
				th.start();
				nbClient++;
				
			}
		    
	    }
	    
	    static void message(String msg){
			System.err.println(msg);
		 }
	    
	    static void erreur(String msg){
	    	message("Erreur: "+msg);
	    }
	    
	    public ServeurNommage(String ip, int port)
	    {
	    	this.adressIP = ip;
	    	this.port = port;
	    	this.listSock = new HashMap<String,Socket>();
	    	this.listServeur = new HashMap<String,ServeurTCP>();
	    	
	    }
	    
	    public String getServName(String requete)
	    {
	    	String[] tabReq = requete.split(" ");
	    	System.out.println(tabReq);
	    	for(String servName : listServeur.keySet())
	    	{
	    		if(servName.equals(tabReq[1]))
	    		{
	    			ServeurTCP s = listServeur.get(servName);
	    			return "KOOL "+s.getIP()+" "+s.getPort()+"\n";
	    		}	    			
	    	}
	    	return "NOSV \n";
	    }
	    
	    public void addServeur(String name, ServeurTCP serv)
	    {
	    	//System.out.println("test add serveur"+serv);
	    	this.listServeur.put(name, serv);
	    }
	    
	    public static void main (String argv[]) {
	    	
	    	final ServeurTCP serv = new ServeurTCP("localhost",8888);
	    	final ServeurNommage servN = new ServeurNommage("localhost",8889);
	    	
	    	Thread th = new Thread(){
	    		public void run(){
	    			serv.start();
	    		}
	    	};
	    	Thread th2 = new Thread(){
	    		public void run(){
	    			servN.start();
	    		}
	    	};
	    	th.start();
	    	th2.start();
	    	
	    	servN.addServeur("www.Thomasde.fr",serv);
			servN.afficherList();
			
	    	
	    	
	    	
	  
	    }
	    
	    public void afficherList(){
	    	System.out.println("--------- liste serveur ---------");
	    	for(ServeurTCP s : listServeur.values()){
	    		System.out.println("Serveur : "+s.toString());
	    	}
	    }
}
