package AWFullP.AppLayer;

import AWFullP.MessageConstants;
import Car.AmbulanceInfo;
import Common.CarInfo;
import Common.Position;
import Common.Vector;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AWFullPAmbPath extends AWFullPAppLayer
{
	private String carID;
	private AmbulanceInfo ambulanceInfo;



	public AWFullPAmbPath(CarInfo info, AmbulanceInfo ambulanceInfo)
	{
		super(MessageConstants.AMBULANCE_PATH);

		this.carID = info.getID();
		this.ambulanceInfo = ambulanceInfo;
	}
	public AWFullPAmbPath(byte[] arr)
	{
		super(arr);

		ByteBuffer buf = ByteBuffer.wrap(arr);
		buf.position(
				MessageConstants.AWFULLP_HEADER_SIZE +
				MessageConstants.GEO_HEADER_SIZE +
				MessageConstants.APP_HEADER_SIZE
				);


		byte[] carID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(carID_bytes, 0, MessageConstants.ID_SIZE);
		this.carID = new String(carID_bytes).trim();

		int number_stops = buf.getInt();

		Map <Integer, Position> ambulanceInfo = new HashMap<>();
		for (int i = 0; i < number_stops; i++){
			int time = buf.getInt();
			float posX = buf.getFloat();
			float posY = buf.getFloat();
			System.out.println("Paragem: " + time + " ("+posX+" , " + posY + ")");
			ambulanceInfo.put(time, new Position(posX, posY));

		}
		this.ambulanceInfo = new AmbulanceInfo(ambulanceInfo);
	}

	public AWFullPAmbPath(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	

	@Override
	public byte[] toBytes()
	{
		try {

			byte[] carID_bytes = Arrays.copyOf(this.carID.getBytes(), MessageConstants.ID_SIZE);
			ByteArrayOutputStream toWrite = new ByteArrayOutputStream();
			DataOutputStream writeArray = new DataOutputStream(toWrite);
			writeArray.writeInt(MessageConstants.AMBULANCE_PATH);
			writeArray.write(carID_bytes, 0, MessageConstants.ID_SIZE);
			writeArray.writeInt(ambulanceInfo.path.size());

			for (Map.Entry<Integer, Position> entry : ambulanceInfo.path.entrySet()) {
				writeArray.writeInt(entry.getKey());
				writeArray.writeFloat(entry.getValue().x);
				writeArray.writeFloat(entry.getValue().y);
			}
			byte[] allBytes = toWrite.toByteArray();
			return allBytes;
	} catch (IOException e) {
	throw new RuntimeException(e);
}

}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; super: " + super.toString()
			+	" ; car: " + this.carID
			+	"}"
			);
	}
}
