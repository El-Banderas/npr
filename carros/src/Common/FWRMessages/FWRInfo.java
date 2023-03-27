package Common.FWRMessages;

import Common.CarInfo;
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

    public byte[] toByteArray(){
        //  TTL | Pos Dest (x e y) | Dist Dest | Seq N | sizeContent | ID length | ID Sender |  Message
        int sizeInt = 4;
        int sizeID = idSender.length;
        if (destiny){
            return ByteBuffer.allocate(sizeInt * 6 + sizeID )
                    .putInt(TTL) // TTL
                    .putInt(dest.x) //  Dest X
                    .putInt(dest.y) // Dest Y
                    .putInt(distDest) // Dist Dest
                    .putInt(seqNumber)  // Seq Num
                    .putInt(idSender.length)  // ID sender
                    .put(idSender)  // ID sender
                    .array();
        }
        else {
            return ByteBuffer.allocate(sizeInt * 4 + sizeID)
                    .putInt(TTL) // TTL
                    .putInt(-1) //  Dest X
                    .putInt(seqNumber)  // Seq Num
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
        this.TTL = bbuf.getInt();
        int maybeDestX = bbuf.getInt();
        // If destiny is defined
        if (maybeDestX != -1){
            int destY = bbuf.getInt();
            this.dest = new Position(maybeDestX, destY);
            this.distDest = bbuf.getInt();
            this.seqNumber = bbuf.getInt();
            int idLength = bbuf.getInt();
            //System.out.println("Coisas lidas: ");
            //System.out.println("TTL: " + TTL + " | pos: " + maybeDestX + ", " + destY + " |dist: " + distance);
            //System.out.println("seq num: " + seqNumber +  " |id len: " + idLength);

            idSender = new byte[idLength];
            // Cuidado com este 8, é o tamanho de 2 ints

            System.arraycopy(message, sizeInt*6, idSender, 0, idLength);
            System.arraycopy(message, sizeInt* 6 + idLength, content, 0 , message.length-(sizeInt* 6 + idLength));
        }
        else {
            this.seqNumber = bbuf.getInt();
            int idLength = bbuf.getInt();
            //System.out.println("Coisas lidas: ");
            //System.out.println("TTL: " + TTL + " | seq num: "+seqNumber + " |id len: " + idLength);
            idSender = new byte[idLength];
            // Cuidado com este 8, é o tamanho de 2 ints
           // System.out.println("Info mensagem: " + TTL + " | " + seqNumber + " | " + idLength);
            System.arraycopy(message, sizeInt*4, idSender, 0, idLength);
           // System.out.println("ID: " + new String(idArray));

            System.arraycopy(message, sizeInt* 4 + idLength, content, 0 , message.length-(sizeInt* 4 + idLength));
        }
    }

    @Override
    public String toString() {
        return "FWRInfo{" +
                "TTL=" + TTL +
                ", seqNumber=" + seqNumber +
                '}';
    }
    // We don't change seq number because the message is relative to the original car, not the cars that resend.
    public void updateInfo(CarInfo carInfo) {
        TTL = TTL-1;
        distDest = (int) Position.distance(dest, carInfo.pos);
        idSender = carInfo.id.getBytes();
    }
}
