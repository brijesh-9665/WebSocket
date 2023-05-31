package websocket;

import javax.websocket.Encoder;
import javax.json.*;

public class MessageEncoder implements Encoder.Text<Product>
{

	@Override
	public String encode(Product message)
	{	  
		JsonObject jsonObject = Json.createObjectBuilder()
				.add("pid", message.getPid())
    	        .add("pname", message.getPname())
    	        .add("price", message.getPrice()).build();
    
    	return jsonObject.toString();
	}
	
    @Override
    public void destroy()
    {
        System.out.println("Destroying encoder...");
    }

	@Override
	public void init(javax.websocket.EndpointConfig config) 
	{	
		System.out.println("Initializing message encoder");
	}
    
}