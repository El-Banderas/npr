package Server;

import Cloud.CloudConstants;
import Common.Constants;
import Common.InfoNode;
import Common.InfoNodeWindows;

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
	public static void main(String[] args) throws UnknownHostException, SocketException {

		System.out.println("Is linux?: " + Constants.core);

		InfoNode cloudInfo;
		InfoNode thisServer = null;
		if (Constants.core) {
			cloudInfo = new InfoNode(null, ServerConstants.port, true);

		} else {
			thisServer = new InfoNodeWindows(Integer.parseInt(args[0]), true);
			cloudInfo = new InfoNodeWindows(CloudConstants.port, false);
		}
		ExecuteServer server = new ExecuteServer(thisServer, cloudInfo);
		server.run();
	}
	}
