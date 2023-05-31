package websocket;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class MyFrame1 extends JFrame 
{
	
	private static final long serialVersionUID = 1L;
	private JTextField t1,t2,t3;
	private JButton btn1;
	private JTable table;
	public DefaultTableModel  dtm;
	private JScrollPane jsp;
	public ClientManager client ;
    private Session session;
    private URI uri;
 
    public MyFrame1()
	{
		initializeComponent();
		connectToWebSocket();
	}
	
	private void initializeComponent()
	{
		setLayout(null);	
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		btn1=new JButton("Add");
		btn1.setBounds(100,250,100,30);
			
		t1=new JTextField();
		t1.setBounds(100,50,100,30);

		t2=new JTextField();
		t2.setBounds(100,100,100,30);

		t3=new JTextField();
		t3.setBounds(100,150,100,30);

		dtm=new DefaultTableModel();
		dtm.addColumn("pid");
		dtm.addColumn("pname");
		dtm.addColumn("price");
		
		
		btn1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Product p;
				p=new Product();
				p.setPid(Integer.parseInt(t1.getText()));
				p.setPname(t2.getText());
				p.setPrice(Double.parseDouble(t3.getText()));	
				
				try 
			    {
			    	session.getBasicRemote().sendObject(p);		        
			    	
			    } catch (EncodeException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		table=new JTable(dtm);
		jsp=new JScrollPane(table);
		jsp.setBounds(200,100,250,250);

		add(jsp);
		
		add(t1);
		add(t2);
		add(t3);
		
		add(btn1);
		
		setVisible(true);
	}
	
	private void connectToWebSocket()
    {
    	try
		{
    		ChatClientEndpoint chatClientEndPoint=new ChatClientEndpoint();
    		chatClientEndPoint.addMessageListener(new MessageListener() {
    			public void actionPerformed(Product p)
    			{
    				dtm.addRow(new Object[] { p.getPid(),p.getPname(),p.getPrice() });
    			}
    		});
    		
			uri = new URI("ws://192.168.0.106:8026/folder/app");
			client = ClientManager.createClient();
			session =client.connectToServer(chatClientEndPoint, uri);
		}
		catch(Exception ex)
		{
			System.out.println(ex);
		}
    }
	
}

interface MessageListener
{
	void actionPerformed(Product p);
}


@ClientEndpoint(
encoders = MessageEncoder.class,
decoders = MessageDecoder.class)
public class ChatClientEndpoint 
{
    private static CountDownLatch latch;
    private MessageListener listener;
    
    @OnOpen
    public void onOpen(Session session) throws EncodeException 
    {
        System.out.println ("--- Connected " + session.getId());
        try 
        {
            session.getBasicRemote().sendText("start");    
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @OnMessage
    public void onMessage(Product message, Session session) throws IOException, EncodeException
    {
        if(this.listener!=null)
        {
        	this.listener.actionPerformed(message);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason)
    {
        System.out.println("Session " + session.getId() + " closed because " + closeReason);
        latch.countDown();
    }
    
    
    public void addMessageListener(MessageListener listener)
    {
    	this.listener=listener;
    }
    
    public static void main(String[] args) throws EncodeException 
    {
		new MyFrame1();
    }
}
