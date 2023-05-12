package websocket;
import java.io.StringReader;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.json.*;

public class MessageDecoder implements Decoder.Text<product> 
{

	@Override
  	public product decode(String jsonMessage)
	{
		product message = new product();
	
		try
		{
			JsonObject jsonObject = Json.createReader(new StringReader(jsonMessage)).readObject();
	    
			message.setPid(jsonObject.getInt("pid"));
			message.setPname(jsonObject.getString("pname"));
			message.setPrice(jsonObject.getInt("price"));
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return message;
	}
	
	@Override
	public boolean willDecode(String jsonMessage) 
	{
		try 
		{
			Json.createReader(new StringReader(jsonMessage)).readObject();
			return true;
      
		} catch (Exception e) {
			return false;
		}
	}

	public void init(EndpointConfig ec) 
	{
		System.out.println("Initializing message decoder");
	}

	@Override
	public void destroy()
	{
		System.out.println("Destroyed message decoder");
	}
}