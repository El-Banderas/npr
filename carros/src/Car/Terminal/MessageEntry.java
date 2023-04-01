package Car.Terminal;


public class MessageEntry
{
	public String text;
	public int numberEntryes;


	public MessageEntry(String text)
	{
		this.text = text;
		numberEntryes = 1;
	}


	public void addEntry()
	{
		numberEntryes++;
	}

	@Override
	public String toString()
	{
		return "Received "+ numberEntryes + " from type " + text;
	}
}