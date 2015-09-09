package com.demo.manager;
import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Server {
	// The ServerSocket we'll use for accepting new connections
	private ServerSocket ss;
	// A mapping from sockets to DataOutputStreams. This will
	// help us avoid having to create a DataOutputStream each time
	// we want to write to a stream.
	private Hashtable outputStreams = new Hashtable();

	public Map<Integer, Boolean> usersOnline = new HashMap<Integer, Boolean>();

	public Map<Integer, Socket> chkUsersOnline = new HashMap<Integer, Socket>();

	// chat room
	private Map<String, List<Socket>> rooms = new HashMap<String, List<Socket>>();

	// Constructor and while-accept loop all in one.
	public Server(int port) throws IOException {
		// All we have to do is listen
		listen(port);

	}

	private void listen(int port) throws IOException {
		// Create the ServerSocket
		ss = new ServerSocket(port);
		// Tell the world we're ready to go
		System.out.println("Listening on " + ss);
		// Keep accepting connections forever
		while (true) {
			// Grab the next incoming connection
			Socket s = ss.accept();
			// Tell the world we've got it
			System.out.println("Connection from " + s);
			// Create a DataOutputStream for writing data to the
			// other side
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			// Save this stream so we don't need to make it again
			outputStreams.put(s, dout);
			// Create a new thread for this connection, and then forget
			// about it
			new ServerThread(this, s);
		}
	}

	// Get an enumeration of all the OutputStreams, one for each client
	// connected to us
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	// Send a message to all clients (utility routine)
	void sendToAll(String message) {
		// We synchronize on this because another thread might be
		// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		synchronized (outputStreams) {
			// For each client ...
			for (Enumeration e = getOutputStreams(); e.hasMoreElements();) {
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream) e.nextElement();
				// ... and send the message
				try {
					dout.writeUTF(message);
				} catch (IOException ie) {
					System.out.println(ie);
				}
			}
		}
	}

	void sendToRoom(String roomName, String message) {
		// We synchronize on this because another thread might be
		// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		// For each client ...
		List<Socket> lst = rooms.get(roomName);
		for (Socket s : lst) {
			DataOutputStream dout;
			try {
				dout = new DataOutputStream(s.getOutputStream());
				dout.writeUTF(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// response status user
	void sendMesToFriend(Socket s, String content) {
		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			JSONObject json = new JSONObject();
			json.put("message", content);
			dout.writeUTF(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// response status user
	void responseStatus(Socket s, Integer msisdn) {
		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());

			JSONObject json = new JSONObject();
			json.put("msisdn", msisdn);

			Boolean isOnline = usersOnline.get(msisdn);
			if (isOnline != null) {
				json.put("online", true);
			} else {
				json.put("online", false);
			}

			dout.writeUTF(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// response room name
	void responseRoomName(Socket s, String roomName) {
		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());

			JSONObject json = new JSONObject();
			json.put("roomName", roomName);
			dout.writeUTF(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Remove a socket, and it's corresponding output stream, from our
	// list. This is usually called by a connection thread that has
	// discovered that the connectin to the client is dead.
	void removeConnection(Socket s) {
		// Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streamsa
		synchronized (outputStreams) {
			// Tell the world
			System.out.println("Removing connection to " + s);
			// Remove it from our hashtable/list
			outputStreams.remove(s);
			// Make sure it's closed
			try {
				s.close();
			} catch (IOException ie) {
				System.out.println("Error closing " + s);
				ie.printStackTrace();
			}
		}
	}

	// Main routine
	// Usage: java Server <port>
	static public void main(String args[]) throws Exception {
		// Get the port # from the command line

		// int port = Integer.parseInt(args[0]);
		int port = Integer.parseInt("9000");
		// Create a Server object, which will automatically begin
		// accepting connections.
		new Server(port);
	}

	public Map<String, List<Socket>> getRooms() {
		return rooms;
	}

	public void setRooms(Map<String, List<Socket>> rooms) {
		this.rooms = rooms;
	}
}
