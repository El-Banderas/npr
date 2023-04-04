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
		byte[] buf = new byte[MessageConstants.sizeBufferMessages];
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
	public static AWFullPacket parseMessageCar(DatagramSocket receiveSocket, InetAddress myIp) throws IOException, SelfCarMessage
	{
		byte[] buf = new byte[MessageConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		receiveSocket.receive(packet);
		
		AWFullPacket aw = new AWFullPacket(buf);

		String before = aw.toString();
		
		if (packet.getAddress().equals(myIp)) {
			if (debug) logger.info("\nPossible self message (me: " + myIp + ", other: " + packet.getAddress() + ", message: " + before + ")\n");
			throw new SelfCarMessage();
		}

		System.out.println("Recebeu de : " + aw.forwardInfo.getSenderID());
		
		return aw;
	}

	/**
	 * Checks if the packet should be forwarded, and says if we should resend in until confirmation received.
	 * @param aw Packet to resend with old information
	 * @param sendSocket Socket to send message
	 * @param carInfo Information necessary to update the packet
	 * @return If we should resend the message until receive confirmation.
	 */
	public static boolean maybeForwardMessage(AWFullPacket aw, DatagramSocket sendSocket, CarInfo carInfo){
		String before = aw.toString();

		// TODO: Check Distance and duplicate messages
			aw.forwardInfo.updateInfo(carInfo);
		System.out.println("Envia mensagem com id: " + aw.forwardInfo.getSenderID());
			SendMessages.sendMessage(sendSocket, Constants.MulticastGroup, Constants.portMulticast, aw);

			if (debug) logger.info("\nForwarding packet " + before + " ---> " + aw );

		// TODO: Depois verificar se mensagem é do tipo de esperar ou não.
		// Se calhar, se tiver destino é para reencaminhar.
		return false;
	}
}
