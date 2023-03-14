package Car.Terminal;

import Common.Messages.SendMessages;

import java.net.DatagramSocket;

public class RefreshOption extends Option{
    public RefreshOption() {
        this.option = TerminalConstants.BreakOption;
        this.text = TerminalConstants.RefreshText;
    }

    @Override
    public void action(DatagramSocket send) {
    }
}
