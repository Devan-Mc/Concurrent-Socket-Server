import java.net.*;
import java.io.*;

class Instance extends Thread
{
	private static Socket socket;
	private static DataInputStream inStream;
	private static DataOutputStream outStream;
	private String msg = "";
	
	Instance(Socket a) throws IOException
	{
		
		this.socket = a;
		this.inStream = new DataInputStream(socket.getInputStream());
		this.outStream = new DataOutputStream(socket.getOutputStream());
	}
	
	
	public void run(int position, String option) throws IOException
	{
		long start = System.currentTimeMillis();
		write(option);
		while(inStream.available() == 0)
		{
			
		}
		msg = read();
		int clNum = position + 1;
    	Client.time[position] = System.currentTimeMillis() - start;
    	System.out.print("client "+clNum+": "+ msg + "\n");// get server message
    	
		
	}
	
	public static synchronized void write(String a)
	{
		try
		{
			outStream.writeUTF(a);
			outStream.flush();
		}
		catch (IOException e) 
		{
			System.out.println("2)this is where the error is happening");
			e.printStackTrace();
		}
	}
	public static synchronized String read()
	{
		String message = " ";
		try
		{
			message =inStream.readUTF();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return message;
	}
}



public class Client 
{

	public static long[] time = new long[100];
	public static void main(String[] args) 
	{
		if (args.length < 2) 
        {
        	System.out.println("Requires ip adress and port number.");
        	return;
        }
        
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        try (Socket socket = new Socket(hostname, port)) 
        {
        	
            DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            String clientMessage="",clientNumber="";
            long totalTime = 0;
            float averageTime = 0;
            
            
            do 
            {
            	totalTime = 0;
            	averageTime = 0;
            	System.out.println("Pick a menu item");
        		System.out.println("1. Host current Date and Time\r\n" + 
        		"2. Host uptime\r\n" + 
        		"3. Host memory use\r\n" + 
        		"4. Host Netstat\r\n" + 
        		"5. Host current users\r\n" + 
        		"6. Host running processes\r\n" + 
        		"7. Quit");	
        		clientMessage=br.readLine();// get clients message
        		clientMessage = clientMessage.replaceAll(" ", "");
        		if (clientMessage.equals("7"))// exit program
        	      {
        			
        	    	 System.out.println("Good bye ending program."); 
        	    	 break;
        	      }
        		
        		// make sure an available choice is made by the user
        	    if (Integer.parseInt(clientMessage)< 1 || Integer.parseInt(clientMessage) > 7 )
        	    {
        	    	System.out.println("Please enter  correct option for client message.");
        	    	continue;
        	    }
        	   
        	    // get a number of clients to be run to the server with the given command
        	    System.out.print("Please enter a number of clients ");
        	    clientNumber=br.readLine();
        	    
                // make sure client number is inbetween 1 and 100
        	    if (Integer.parseInt(clientNumber)< 0 || (Integer.parseInt(clientNumber)>100))
        	    {
        	    	System.out.println("Please enter a number between 1-100");
        	    	continue;
        	    }
        	    
        	    
        	    for(int i = 0; i < Integer.parseInt(clientNumber); i++)
        	    {
        	    	Instance obj = new Instance(socket);
        	    	obj.run(i, clientMessage);
        	    }
        	   // might need a way to slow time down here
        	    for (int i=0; i < Integer.parseInt(clientNumber);i++)
        	    {
        	    	System.out.println("Time of opperation for client" + (i + 1) + ": " + time[i] + "ms");
        	    	totalTime += time[i];
        	    }
        	    System.out.println("Total execution time: " + totalTime +"ms");
        	    averageTime = (float) totalTime / (float) Integer.parseInt(clientNumber);
        	    System.out.println("Average execution time: " + averageTime+"ms");
        	    
            } while (!clientMessage.equals("7"));// end of do while loop
        	    
            outStream.writeUTF("7");
            outStream.close();
            socket.close();
		
        }
        catch (UnknownHostException ex) 
        {
        	System.out.println("Server not found: " + ex.getMessage());
    	} 
        catch (IOException ex) 
        {
        	System.out.println("I/O error: " + ex.getMessage());
    	}

	}
	
}
