package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import AWFullP.FwdLayer.SelfCarMessage;
import Common.CarInfo;
import Common.Constants;


public class ReceiveMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.received");
	private static boolean debug = false;


	public static AWFullPacket receiveData(DatagramSocket socket) throws IOException
	{
		byte[] buf = new byte[MessageConstants.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		AWFullPacket received = new AWFullPacket(packet);

		if (debug) logger.info("\nReceived packet " + received.toString() + " from " + packet.getAddress().toString() + ":" + packet.getPort());

		return received;
	}
	
	/**
	 * Receives and forwards, when in case, received messages.
	 *
	 * @param receiveSocket - To receive messages.
	 * @param myIp          - To filter messages send from the same car.
	 * @return
	 * @throws IOException
	 */
	public static AWFullPacket parseMessageCar(DatagramSocket receiveSocket, InetAddress myIp, String myID) throws IOException, SelfCarMessage
	{
		byte[] buf = new byte[MessageConstants.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		receiveSocket.receive(packet);
		
		AWFullPacket aw = new AWFullPacket(buf);

		String before = aw.toString();
		//System.out.println("Recebeu mensagem de próprio: "+packet.getAddress().equals(myIp) + " Pacote: " + packet.getAddress() + " EU:  " + myIp.toString());
		if (packet.getAddress().equals(myIp) || aw.forwardInfo.getSenderID().equals(myID)) {
			if (debug) logger.info("\nPossible self message (me: " + myIp + ", other: " + packet.getAddress() + ", message: " + before + ")\n");
			throw new SelfCarMessage();
		}


		return aw;
	}


	public static void maybeForwardMessage(AWFullPacket aw, DatagramSocket sendSocket, CarInfo carInfo){
		String before = aw.toString();

		// TODO: Check Distance and duplicate messages
				aw.forwardInfo.updateInfo(carInfo);

			SendMessages.sendMessage(sendSocket, Constants.MulticastGroup, Constants.portMulticast, aw);
			if (debug) logger.info("\nForwarding packet " + before + " ---> " + aw );

	}
}
