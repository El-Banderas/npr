package Cloud;

import Common.*;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Será que a cloud deve saber os ip's das torres? Porque podemos fazer de duas formas:
 * Quando recebe uma mensagem duma torre, passa a descobrir essa torre.
 * Pelo ficheiro, como um carro
 *
 * Arguments:
 * Windows
 * Para já não recebe nada. Depois posso meter a cena de ler as torres dum ficheiro, como no carro
 * The port must be the same as the file.
 * Example:
 *
 *
 * Linux, depois remover a posição
 * Example: ""
 */
public class Main {
	public static void main(String[] args) throws UnknownHostException, SocketException {

		System.out.println("Is linux?: " + Constants.linux);

		InfoNode cloudInfo;
		if (Constants.linux) {
			cloudInfo = new InfoNode(null, CloudConstants.port, true);

		} else {
			cloudInfo = new InfoNodeWindows(CloudConstants.port, true);
		}
		ExecuteCloud cloud = new ExecuteCloud(cloudInfo);
		cloud.run();
	}
	}
