package Cloud;

import Common.Constants;
import Common.InfoNode;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;

import java.io.IOException;
import java.net.SocketException;


public class Cloud implements Runnable {

	private Common.InfoNode cloud;


	public Cloud(InfoNode cloud)
	{
		this.cloud = cloud;
	}
	
	
	@Override
	public void run()
	{
		try {
			cloud.socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true) {
			try {
				MessageAndType message = ReceiveMessages.receiveData(cloud.socket);
				handleMessage(message);
			} catch (IOException e) {
				System.out.println("[Cloud] Timeout passed. Nothing received.");
				//System.out.println("Receiving in: "+cloud.socket.getLocalAddress() +" | "+  cloud.socket.getLocalPort() );
			}
		}
	}
	
	private void handleMessage(MessageAndType message)
	{
		switch(message.type) {
			case MessagesConstants.ServerHelloMessage:
				System.out.println("Received Hello form server");
				break;
			default:
				System.out.println("Received message, type unkown: " + message.type);
		}
	}
}
