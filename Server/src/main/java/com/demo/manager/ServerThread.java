package com.demo.manager;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerThread extends Thread {
	// The Server that spawned us
	private Server server;
	// The Socket connected to our client
	private Socket socket;
	
	private final int LOGIN=-1;
	private final int GET_STATUS_USER=0;
	private final int CREATE_NEW_ROOM=1;
	private final int JOIN_NEW_ROOM=2;
	private final int SEND_MES_IN_ROOM=3;
	private Integer currentUser=0;
	private List<String> roomList=new ArrayList<String>();

	// Constructor.
	public ServerThread(Server server, Socket socket) {
		// Save the parameters
		this.server = server;
		this.socket = socket;
		// Start up the thread
		start();
	}

	// This runs in a separate thread when start() is called in the
	// constructor.
	public void run() {
		try {
			// Create a DataInputStream for communication; the client
			// is using a DataOutputStream to write to us
			DataInputStream din = new DataInputStream(socket.getInputStream());
			// Over and over, forever ...
			while (true) {
				// ... read the next message ...
				String message = din.readUTF();
				try {
					JSONObject json=new JSONObject(message);
					if(!json.isNull("type")){
						
						if(json.getInt("type")==SEND_MES_IN_ROOM){
							String roomName=json.getString("roomName");
							String mes=json.getString("message");
							
							server.sendToRoom(roomName, mes);
						}
						

//						if(json.getInt("type")==JOIN_NEW_ROOM){
//							String mes=json.getString("message");
//							
//						}
						
						//{"type":1,"msisdn":978712200,"message":"Xin chao"}
						if(json.getInt("type")==CREATE_NEW_ROOM){
							List<Socket> lst=new ArrayList<Socket>();
							lst.add(socket);
							
							String phone=json.getString("msisdn");
							Socket friend=server.chkUsersOnline.get(Integer.parseInt(phone));
							lst.add(friend);
							
							String roomName=UUID.randomUUID().toString();
							server.getRooms().put(roomName, lst);
							server.responseRoomName(socket, roomName);
							
							server.sendMesToFriend(friend, "Ban duoc moi vao room");
						}
						
						if(json.getInt("type")==LOGIN){
							String phone=json.getString("msisdn");
							currentUser=Integer.parseInt(phone);
							server.usersOnline.put(currentUser, true);
							server.chkUsersOnline.put(currentUser, socket);
						}
						
						if(json.getInt("type")==GET_STATUS_USER){
							String phone=json.getString("msisdn");
							int msisdn=Integer.parseInt(phone);
							server.responseStatus(socket, msisdn);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// ... tell the world ...
				System.out.println("Sending " + message);
				// ... and have the server send it to all clients
//				server.sendToAll(message);
			}
		} catch (EOFException ie) {
			// This doesn't need an error message
		} catch (IOException ie) {
			// This does; tell the world!
			ie.printStackTrace();
		} finally {
			// The connection is closed for one reason or another,
			// so have the server dealing with it
			server.removeConnection(socket);
			
			//
			server.usersOnline.remove(currentUser);
			
			//
			for (String room : roomList) {
				roomList.remove(room);
				server.getRooms().remove(room);
			}
		}
	}
}