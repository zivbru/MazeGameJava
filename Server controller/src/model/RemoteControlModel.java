package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import presenter.RemoteControlProperties;


/**
 * The Class RemoteControlModel. This class is an implementation of the interface model
 *	to be used in a MVP architecture 
 */
public class RemoteControlModel extends Observable implements Model {


	/** The server properties. */
	RemoteControlProperties serverProperties;

	/** The socket. */
	DatagramSocket socket;

	/** The address. */
	InetAddress address;

	/** The client status. */
	ConcurrentHashMap<String,String> clientStatus = new ConcurrentHashMap<String, String>();

	/** The message data. */
	String [] messageData;

	/** The Remote server to server port. */
	int RemoteServerToServerPort;
	
	/** The Exec. */
	ExecutorService Exec=null;
	
	/** The shutdown. */
	volatile boolean shutdown = true;

	/**
	 * Instantiates a new remote control model.
	 *
	 * @param serverProperties the server properties
	 */
	public RemoteControlModel(RemoteControlProperties serverProperties){
		try {
			this.serverProperties = serverProperties; //intializing the properties data member
			socket = new DatagramSocket(serverProperties.getRemoteControlPortListener()); //getting the port from the user to which we will listen to
			address=InetAddress.getByName(serverProperties.getIP()); //address of use
			this.RemoteServerToServerPort=serverProperties.getPortOnWhichServerListens();
			messageData= new String [2];
		} catch (IOException e) {
			e.printStackTrace();

		}

	}
	
	/* (non-Javadoc)
	 * @see model.Model#getStatusClient(java.lang.String)
	 */
	@Override
	public void getStatusClient(String client) {
		String status = this.clientStatus.get(client.split(",")[0]+","+client.split(",")[1]);
		messageData[0] = Constants.CLIENT_STATUS;
		messageData[1] = status;
		setChanged();
		notifyObservers();

	}

	/* (non-Javadoc)
	 * @see model.Model#DisconnectClient(java.lang.String)
	 */
	@Override
	public void DisconnectClient(String client) {

		String message=client.split(",")[0]+","+ client.split(",")[1]+",disconnect";  
		byte[] data=message.getBytes(); 
		DatagramPacket sendPacket = new DatagramPacket(data,
				data.length, address, RemoteServerToServerPort);
		try {

			socket.send(sendPacket); 
		} catch (IOException e) {
			this.messageData[0]= Constants.CANNOT_REMOVE_CLIENT;
			this.messageData[1] = client + e.getMessage();
			setChanged();
			notifyObservers();
			return;
		}
		this.messageData[0] = Constants.CLIENT_REMOVED;
		this.messageData[1] = client;
		setChanged();
		notifyObservers(); 

	}

	/* (non-Javadoc)
	 * @see model.Model#StartServer()
	 */
	@Override
	public void StartServer() {

		String message="start server"; 
		byte[] data=message.getBytes(); 
		DatagramPacket sendPacket = new DatagramPacket(data,data.length, address, RemoteServerToServerPort);
		try { 
			socket.send(sendPacket); 
		} catch (IOException | NullPointerException e) {
			this.messageData[0]=Constants.CANNOT_START_SERVER;
			this.messageData[0] = e.getMessage();
			setChanged();
			notifyObservers();
			return;
		}

		message =this.serverProperties.getNumOfClients() + "," +(this.serverProperties.getPortServerClients());
		data = message.getBytes();
		sendPacket = new DatagramPacket(data,data.length, address, RemoteServerToServerPort);
		try {
			socket.send(sendPacket); //send Properties
		} catch (IOException e) {

			this.messageData[0]= Constants.CANNOT_START_SERVER;
			this.messageData[1] = e.getMessage();
			setChanged();
			notifyObservers();
			return;
		}

		Exec = Executors.newSingleThreadExecutor();
		Exec.execute((new Runnable(){

			@Override
			public void run() {
				while(true && shutdown){
					byte info[]=new byte[1000];
					DatagramPacket receivedPacket=new DatagramPacket(info,info.length);
					try {

						System.out.println("chunk");
						socket.receive(receivedPacket); //recieving data
						String line=new String(receivedPacket.getData(), 0,receivedPacket.getLength());
						String [] lines =line.split("\n"); 
						for(int i =0 ;i<lines.length;i++){//understanding the data and using it
							System.out.println(lines[i]);
							if(lines[i].split(",").length==3 ){  //if new user
								if(lines[i].split(",")[2].equals("connected")){
									clientStatus.put(lines[i].split(",")[0]+","+ lines[i].split(",")[1],"connected");
									setChanged();
									messageData[0] = Constants.CLIENT_ADDED;
									messageData[1] = lines[i];
									notifyObservers();
								}
								else
									if(lines[i].split(",")[2].equals("disconnected")) //if user disconnected
									{
										clientStatus.remove(lines[i]);
										messageData[0] = Constants.CLIENT_REMOVED;
										messageData[1] = "Client IP: " + lines[i].split(",")[0]+" Port: "+ lines[i].split(",")[1];
										setChanged();
										notifyObservers();
									}
									else
									{ //or just updating user status
										clientStatus.put(lines[i].split(",")[0]+","+ lines[i].split(",")[1],lines[i].split(",")[2]);
										messageData[0] = Constants.CLIENT_UPDATE;
										messageData[1] = lines[i].split(",")[0]+","+ lines[i].split(",")[1]+ "," +lines[i].split(",")[2];
										setChanged();
										notifyObservers();
									}
							}
							else if(lines[i].contains("Number")){
								messageData[0] =Constants.SERVER_START;
								messageData[1] = lines[i];
								setChanged(); 
								notifyObservers(); 
							}
							else if(lines[i].contains("stop")){
								messageData[0] = Constants.DISCONNECT_SERVER; 
								setChanged();
								notifyObservers();
							}
							else{
								messageData[0] =Constants.CLIENT_UPDATE;
								messageData[1] = lines[i];
								setChanged(); 
								notifyObservers(); 
							}

						}

					} catch (IOException e) {

					}
				}
			}
		}));



	}


	/* (non-Javadoc)
	 * @see model.Model#DisconnectServer()
	 */
	@Override
	public void DisconnectServer() {

		String message="stop server"; 
		byte[] data=message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(data,
				data.length, address, RemoteServerToServerPort);
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			this.messageData[0] = Constants.CANNOT_DISCONNECT_SERVER;
			this.messageData[1] = e.getMessage();
			setChanged();
			notifyObservers();
		}


	}

	/* (non-Javadoc)
	 * @see model.Model#getData()
	 */
	@Override
	public String[] getData() {

		return this.messageData;
	}

	/* (non-Javadoc)
	 * @see model.Model#exit()
	 */
	@Override
	public void exit() {

		shutdown =false;
		if(Exec!=null)
			Exec.shutdownNow();
		String message="exit"; 
		byte[] data=message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(data,
				data.length, address, RemoteServerToServerPort);
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
		this.messageData[0] = Constants.EXIT;
		setChanged();
		notifyObservers(); 

	}

}
