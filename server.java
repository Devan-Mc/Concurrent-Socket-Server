import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.lang.Process;
import java.lang.Runtime;


class curDate extends Thread
{
	private Socket socket;
	private DataOutputStream outStream;
	private String msg = "";
	
	
	curDate(Socket a) throws IOException
	{
		this.socket = a;
		outStream = new DataOutputStream(socket.getOutputStream());
	}

	public void run()
	{
		
		try 
		{
			String date = new Date().toString();
			msg = msg.concat(date + "\n");
			outStream.writeUTF(msg);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}//sends string to client
	}
	
}

class upTime extends Thread
{
	private Socket socket;
	private DataOutputStream outStream;
	private String msg = "";
	String s;//used to read runtime processes
    Process p;
	upTime(Socket a) throws IOException
	{
		this.socket = a;
		outStream = new DataOutputStream(socket.getOutputStream());
	}

	public void run()
	{
		
		try 
		{
			p = Runtime.getRuntime().exec("uptime");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((s = stdInput.readLine()) != null)
			{
				msg = msg.concat(s + "\n");
			}
			outStream.writeUTF(msg);
			stdInput.close();
			p.destroy();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}

class Memory extends Thread
{
	private Socket socket;
	private DataOutputStream outStream;
	private String msg = "";
	String s;//used to read runtime processes
    Process p;
    
	Memory(Socket a) throws IOException
	{
		this.socket = a;
		outStream = new DataOutputStream(socket.getOutputStream());
	}
	
	public void run()
	{
		try
		{
			p = Runtime.getRuntime().exec("free -ht");
    		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		while((s = stdInput.readLine()) != null)
    		{
    			msg = msg.concat(s + "\n");
    		}
    		outStream.writeUTF(msg);
    		stdInput.close();
    		p.destroy();
    		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}

class Netstat extends Thread
{
	private Socket socket;
	private DataOutputStream outStream;
	private String msg = "";
	String s;//used to read runtime processes
    Process p;
    
	Netstat(Socket a) throws IOException
	{
		this.socket = a;
		outStream = new DataOutputStream(socket.getOutputStream());
	}

	public void run()
	{
		try
		{
			p = Runtime.getRuntime().exec("netstat -a");
    		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		while((s = stdInput.readLine()) != null)
    		{
    			
    			msg = msg.concat(s + "\n");
    			
    		}
    		outStream.writeUTF(msg);
    		stdInput.close();
    		p.destroy();
    		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}

class Users extends Thread
{
	private Socket socket;
	private DataOutputStream outStream;
	private String msg = "";
	String s;//used to read runtime processes
    Process p;
    
	Users(Socket a) throws IOException
	{
		this.socket = a;
		outStream = new DataOutputStream(socket.getOutputStream());
	}

	public void run()
	{
		try
		{
			p = Runtime.getRuntime().exec("users");
    		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		while((s = stdInput.readLine()) != null)
    		{
    			
    			msg = msg.concat(s + "\n");
    			
    		}
    		outStream.writeUTF(msg);
    		stdInput.close();
    		p.destroy();
    		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}

class Processes extends Thread
{
	private Socket socket;
	private DataOutputStream outStream;
	private String msg = "";
	String s;//used to read runtime processes
    Process p;
    
	Processes(Socket a) throws IOException
	{
		this.socket = a;
		outStream = new DataOutputStream(socket.getOutputStream());
	}

	public void run()
	{
		try
		{
			p = Runtime.getRuntime().exec("ps -s");
    		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		while((s = stdInput.readLine()) != null)
    		{
    			
    			msg = msg.concat(s + "\n");
    			
    		}
    		outStream.writeUTF(msg);
    		stdInput.close();
    		p.destroy();
    		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}

public class server 
{
	
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Requires port number");
			return;
		}
		int portNumber = Integer.parseInt(args[0]);
		
			try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			 
            System.out.println("Server is listening on port " + portNumber);
 
            Socket socket = serverSocket.accept();
            
            System.out.println("New client connected");
            
          
            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            String toClient = "";//initalizes the output string
            char choiceNumber = 'f';
            String s;//used to read runtime processes
            Process p;
            String text = "";
            while (true) 
            {
            	//this makes the server wait for number of clients being sent from client server
            	System.out.print("Waiting for client number\n");
                
                	//makes server wait for input
                	while(din.available() == 0)
                    {
                    	
                    }
                	
                	text = din.readUTF();//reads choice into a string
                	choiceNumber = text.charAt(0);//turns that choice into a char
        	    	
                	if (choiceNumber == '1')//send the current date and time
                    {
                		curDate obj = new curDate(socket);
                		obj.run();
                    }
                	
                	else if (choiceNumber == '2')//sends the up time in milliseconds
                    {
                		upTime obj = new upTime(socket);
                		obj.run();
                    }
        	    	
                	else if (choiceNumber == '3')//sends the memory usage
                    {
                		Memory obj = new Memory(socket);
                		obj.run();
                		
                    }
                	
                	else if (choiceNumber == '4')//sends Netstat
                    {
                		Netstat obj = new Netstat(socket);
                		obj.run();
                		
                    }
                	
                	else if (choiceNumber == '5')//sends a list of the current users on the server
                    {
                		Users obj = new Users(socket);
                		obj.run();
                		
                    }
                	
                	else if (choiceNumber == '6')//sends list of programs currently running on the server
                    {
                		Processes obj = new Processes(socket);
                		obj.run();
                		
                    }
                	
                	else if (choiceNumber == '7')//shuts down both servers
                    {
                      	break;
                    }
                	               
                
            }
            
            System.out.println("server shutting down");
            socket.close();
            serverSocket.close();
           
            return;
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
	}//end main
	
}
