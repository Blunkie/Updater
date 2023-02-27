package org.example;

import java.util.ArrayList;

public class ClientPacket
{
	String name;
	String obfuscatedName;
	ArrayList<String[]> writes = new ArrayList<>();

	@Override
	public String toString()
	{
		StringBuilder returnVal =
				new StringBuilder("public static final String "+name+"_OBFUSCATEDNAME = \""+ obfuscatedName+"\";\n");
		int x=1;
		for (String[] write : writes)
		{
			returnVal.append("public static final String "+name+"_"+"WRITE"+x+" = \"").append(write[1]+"\";\n");
			returnVal.append("public static final String "+name+"_"+"METHOD_NAME"+x+" = \"").append(write[0]+"\";\n");
			x++;
		}
		return returnVal.toString();
	}
}
