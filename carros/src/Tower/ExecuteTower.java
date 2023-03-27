package Tower;

import Cloud.CloudConstants;
import Common.*;
import Server.ServerConstants;
import Server;
import java.io.IOException;
import java.net.*;


public class ExecuteTower {
    private Server miniCloud;

    public ExecuteTower(Server miniCloud){
        this.miniCloud=miniCloud;
    }

    public void receiveMessage(String message) {
        miniCloud.sendMessage(message);
    }

}
