package websocket;
import java.io.IOException;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.EncodeException;
import javax.websocket.server.ServerEndpoint;

//https://www.byteslounge.com/tutorials/java-ee-html5-websockets-with-multiple-clients-example
//https://github.com/TooTallNate/Java-WebSocket

@ServerEndpoint(value = "/app",
encoders = MessageEncoder.class,
decoders = MessageDecoder.class)

public class ChatServerEndpoint 
{
	
	 @OnOpen
	 public void onOpen(Session session)
	 {
		 System.out.println ("Connected, sessionID = " + session.getId());
	 }
	
	 @OnMessage
	 public void onMessage(Product message, Session session) throws EncodeException
	 {
		 for (Session clientSession : session.getOpenSessions())
		 {
		
			 if(clientSession.isOpen())
			 {
				 System.out.println(clientSession.getId());
				 try 
				 {
					 clientSession.getBasicRemote().sendObject(message);
					 
				 } catch (IOException e)
				 {
					 System.out.println(e);
				 }
			 }    
		 }
	 }
	
	 @OnClose
	 public void onClose(Session session, CloseReason closeReason) 
	 {
		 System.out.println("Session " + session.getId() +" closed because " + closeReason);
	 }
}