package com.grit.secureid.CryptoService;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.lang.RandomStringUtils;

/**
 * @author mohan created date 29/11/2017
 * 
 * PasswordHelper class - Security Utils for the password strings etc.
 */

public class PasswordHelper {
	
	/**
	 * Generates the hash for the given plain text string, salt key & hash type
	 * 
	 * @param 
	 * 	txt - plain text 
	 * 	saltKey - salt key
	 * 	hashType - algorithm
	 * @return hash 
	 */
	
	public String generateHash(String txt, String saltKey, String hashType) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		// Default hashType is null
		if(hashType == null) {
			hashType = "SHA1";
		}
		saltKey += txt;
		MessageDigest md = MessageDigest.getInstance(hashType);
        md.reset();
        byte[] buffer = saltKey.getBytes();
        md.update(buffer);
        byte[] digest = md.digest();
        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        
        return hexStr;

	}
	
	/**
	 * Generate the random salt key for the password
	 * @return hash 
	 */
	
	public String generateRandomSaltKey() throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		String randomstring = RandomStringUtils.random(10, 0, 0, true, true, null, new SecureRandom());
		String strHash = DeffieHellman.makeSHA1Hash(randomstring);
		return strHash;
	}
	
}
