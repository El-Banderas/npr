package AWFullP.AppLayer;

//import java.io.IOException;
//import java.net.DatagramSocket;
//import java.net.InetAddress;

public interface IAWFullPAppLayer
{
	public int getType();
	public byte[] toBytes();
	//public void sendTo(DatagramSocket from, InetAddress to, int port) throws IOException;
}
