package stringgenerator;

import java.security.SecureRandom;


public class RandomStringGenerator
{
	static final String AB = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	static SecureRandom rnd = new SecureRandom();
	
	public String RandomString()
	{
		return RandomString(rnd.nextInt(9)+1);
	}
	
	public String RandomString(int len)
	{
		StringBuilder sb = new StringBuilder(len);
		for (int i=0; i<len; i++)
		{
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return sb.toString();
	}
}
