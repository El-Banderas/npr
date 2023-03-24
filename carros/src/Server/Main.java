package Server;

import Common.Constants;
import Common.InfoNode;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Será que a cloud deve saber os ip's das torres? Porque podemos fazer de duas formas:
 * Quando recebe uma mensagem duma torre, passa a descobrir essa torre.
 * Pelo ficheiro, como um carro
 * Será que servidor comunica com torre? Para já não
 *
 * Arguments:
 * Windows
 * This port
 * The port must be the same as the file.
 * Example:
 * 9000
 *
 *
 * Linux, depois remover a posição
 * Example: ""
 */
public class Main {
	
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		InfoNode cloudInfo;
		InfoNode thisServer;
		
		cloudInfo = new InfoNode(Constants.CloudIP, Constants.cloudPort, false);
		thisServer = new InfoNode(null, Constants.serverPort, true);
		
		Server server = new Server(thisServer, cloudInfo);
		server.run();
	}
}
