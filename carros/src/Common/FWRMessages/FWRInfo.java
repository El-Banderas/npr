package Common.FWRMessages;

import Common.Messages.MessagesConstants;
import Common.Position;
import org.w3c.dom.ls.LSOutput;

import java.nio.ByteBuffer;

public class FWRInfo {
    public boolean destiny;
    public int TTL;
    public Position dest;

    public int distDest;
    public byte[] idSender;
    public int seqNumber;
    public byte[] content;

    // Seq Number == -1 => Hellos
    public FWRInfo(int TTL, byte[] idSender, int seqNumber) {
        this.TTL = TTL;
        this.destiny = false;
        this.idSender = idSender;
        this.seqNumber = seqNumber;
        this.content = new byte[MessagesConstants.sizeBufferMessages];
    }

    public FWRInfo(int TTL, Position dest, int distDest, byte[] idSender, int seqNumber) {
        this.TTL = TTL;
        this.dest = dest;
        this.distDest = distDest;
        this.idSender = idSender;
        this.seqNumber = seqNumber;
        this.content = new byte[MessagesConstants.sizeBufferMessages];
    }

    public byte[] toByteArray(int lengthContent){
        //  TTL | Pos Dest (x e y) | Dist Dest | Seq N | sizeContent | ID length | ID Sender |  Message
        if (destiny){
            return ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
                    .putInt(TTL) // TTL
                    .putInt(dest.x) //  Dest X
                    .putInt(dest.y) // Dest Y
                    .putInt(distDest) // Dist Dest
                    .putInt(seqNumber)  // Seq Num
                    .putInt(lengthContent)  // Length content message
                    .putInt(idSender.length)  // ID sender
                    .put(idSender)  // ID sender
                    .array();
        }
        else {
            return ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
                    .putInt(TTL) // TTL
                    .putInt(-1) //  Dest X
                    .putInt(seqNumber)  // Seq Num
                    .putInt(lengthContent)  // Length content message
                    .putInt(idSender.length)  // ID sender
                    .put(idSender)  // ID sender
                    .array();
        }
    }
    // TTL | Pos Dest (x e y) | -1 | ID Sender | Seq N | Message


    public FWRInfo(byte[] message) {
        int sizeInt = 4;
        this.content = new byte[MessagesConstants.sizeBufferMessages];

        ByteBuffer bbuf = ByteBuffer.wrap(message);
        int TTL = bbuf.getInt();
        int maybeDestX = bbuf.getInt();
        // If destiny is defined
        if (maybeDestX == -1){
            boolean debug = maybeDestX == -1;
            System.out.println("Tem destino? " + maybeDestX + "== " + -1 + " = " + debug);
            int destY = bbuf.getInt();
            int distance = bbuf.getInt();
            int seqNumber = bbuf.getInt();
            int lengthContent = bbuf.getInt();
            int idLength = bbuf.getInt();
            System.out.println("Coisas lidas: ");
            System.out.println("TTL: " + TTL + " | pos: " + maybeDestX + ", " + destY + " |dist: " + distance);
            System.out.println("seq num: " + seqNumber + " | len cont: " + lengthContent + " |id len: " + idLength);

            byte[] idArray = new byte[idLength];
            // Cuidado com este 8, é o tamanho de 2 ints
            System.out.println("Info mensagem: " + TTL + " | " + maybeDestX + ", " + destY + " | " + idLength);

            System.arraycopy(message, sizeInt*7, idArray, 0, idLength);
            System.arraycopy(message, sizeInt* 7 + idLength, content, 0 , lengthContent);
        }
        else {
            System.out.println("Mensagem sem destino");
            int seqNumber = bbuf.getInt();
            int lengthContent = bbuf.getInt();
            int idLength = bbuf.getInt();
            System.out.println("Coisas lidas: ");
            System.out.println("TTL: " + TTL + " | seq num: "+seqNumber + " | len cont: " + lengthContent + " |id len: " + idLength);
            byte[] idArray = new byte[idLength];
            // Cuidado com este 8, é o tamanho de 2 ints
            System.out.println("Info mensagem: " + TTL + " | " + seqNumber + " | " + idLength);
            System.arraycopy(message, sizeInt*5, idArray, 0, idLength);
            System.arraycopy(message, sizeInt* 5 + idLength, content, 0 , lengthContent);
        }
    }

    @Override
    public String toString() {
        return "FWRInfo{" +
                "TTL=" + TTL +
                ", seqNumber=" + seqNumber +
                '}';
    }
}
