package com.grit.secureid.CryptoService;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import org.apache.tomcat.util.codec.binary.Base64;

import com.google.common.io.BaseEncoding;
import javax.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*
 * This class purpose is to provide required functionality to work with Deffie-Hellman Key Exchange algorithm
 */
public class DeffieHellman {
    private DeffieHellman() {}
    
    /* This method takes the client public key and derives the server public key and shared secret key and returns them
     * @param targetPublicKey which is the public key of client
     * @return Map which contains the shared secret key and the public key of server with keys
     * sharedSecretKey and publicKey respectively to retrieve from the map
     */
    
    
    //hardcoded p and q values--
    
    private static BigInteger g512 = new BigInteger(
            "153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7"
          + "749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b"
          + "410b7a0f12ca1cb9a428cc", 16);
    private static BigInteger p512 = new BigInteger(
            "9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd387"
          + "44d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94b"
          + "f0573bf047a3aca98cdf3b", 16);
    
    
    public static Map<String,Object> getSSKeyAndPKey(byte[] clientPublicKey) throws Exception {
		
    	Map<String,Object> result = new HashMap<String,Object>();
	     /*
	      * Let's turn over to Server. Server has received Cleint's public key
	      * in encoded format.
	      * It instantiates a DH public key from the encoded key material.
	      */
    	KeyFactory serverKeyFac = KeyFactory.getInstance("DH");
    	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(clientPublicKey);

    	PublicKey alicePubKey = serverKeyFac.generatePublic(x509KeySpec);

    	/*
    	 * Server gets the DH parameters associated with Clent's public key.
    	 * It must use the same parameters when it generates his own key
    	 * pair.
    	 */
    	DHParameterSpec dhParamFromAlicePubKey = ((DHPublicKey)alicePubKey).getParams();

    	// Server creates its own DH key pair
    	System.out.println("Server: Generate DH keypair ...");
    	KeyPairGenerator serverKpairGen = KeyPairGenerator.getInstance("DH");
    	serverKpairGen.initialize(dhParamFromAlicePubKey);
    	KeyPair serverKpair = serverKpairGen.generateKeyPair();

    	// Server creates and initializes its DH KeyAgreement object
    	System.out.println("Server: Initialization ...");
    	KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DH");
    	serverKeyAgree.init(serverKpair.getPrivate());

    	// Server encodes its public key, and sends it over to Client.
    	byte[] bobPubKeyEnc = serverKpair.getPublic().getEncoded();
   
    	result.put("publicKey", bobPubKeyEnc);
    	/*
    	 * Server uses Client's public key for the first (and only) phase
    	 * of his version of the DH
    	 * protocol.
    	 */
    	System.out.println("Server: Execute PHASE1 ...");
    	serverKeyAgree.doPhase(alicePubKey, true);
    	
    	byte[] serverSharedSecret = serverKeyAgree.generateSecret();
   
    	result.put("sharedSecretKey", serverSharedSecret);

    	System.out.println("Server secret: " + toHexString(serverSharedSecret));
   
    	return result;
    }
    

     
	  public static Map<String,Object> getKeysForEndToEndEncryption(String byteString) throws Exception 
	  {
		
	 
		
		 	byte[] clientPublicKey=getByteValue(byteString);
		 
	    	Map<String,Object> result = new HashMap<String,Object>();
		     /*
		      * Let's turn over to Server. Server has received Cleint's public key
		      * in encoded format.
		      * It instantiates a DH public key from the encoded key material.
		      */
	    	KeyFactory serverKeyFac = KeyFactory.getInstance("DH");
	    	
	    	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(clientPublicKey);
	
	    	PublicKey clientPubKey = serverKeyFac.generatePublic(x509KeySpec);
	
	    	/*
	    	 * Server gets the DH parameters associated with Clent's public key.
	    	 * It must use the same parameters when it generates his own key
	    	 * pair.
	    	 */
	    	//DHParameterSpec dhParamFromAlicePubKey = ((DHPublicKey)alicePubKey).getParams();
	    	
	    	DHParameterSpec dhParamFromAlicePubKey = new DHParameterSpec(p512, g512);
	
	    	// Server creates its own DH key pair
	    	System.out.println("Server: Generate DH keypair ...");
	    	KeyPairGenerator serverKpairGen = KeyPairGenerator.getInstance("DH");
	    	serverKpairGen.initialize(dhParamFromAlicePubKey);
	    	KeyPair serverKpair = serverKpairGen.generateKeyPair();
	    	
	    	// Server creates and initializes its DH KeyAgreement object
	    	System.out.println("Server: Initialization ...");
	    	KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DH");
	    	serverKeyAgree.init(serverKpair.getPrivate());
	
	    	// Server encodes its public key, and sends it over to Client.
	    	byte[] serverPubKeyEnc = serverKpair.getPublic().getEncoded();
	    	
	    	
	    	/*
	    	 * Server uses Client's public key for the first (and only) phase
	    	 * of his version of the DH
	    	 * protocol.
	    	 */
	    	System.out.println("Server: Execute PHASE1 ...");
	    	serverKeyAgree.doPhase(clientPubKey, true);
	    	
	    	byte[] serverSharedSecret = serverKeyAgree.generateSecret();
	  
	    	result.put("sharedSecretKey", makeSHA1Hash(getByteArrayValue(serverSharedSecret)));
	    	result.put("publicKey", getByteArrayValue(serverPubKeyEnc));
	    	
	    	return result;
    }
    
	  	
	  	public static byte[] getByteValue(String response)
		 {
		
			   String[] byteValues = response.substring(1, response.length() - 1).split(",");
			   byte[] bytes = new byte[byteValues.length];
			
			   for (int i=0, len=bytes.length; i<len; i++) {
			      bytes[i] = Byte.parseByte(byteValues[i].trim());     
			   }
			   return bytes;
		 }
	  
		 public static String getByteArrayValue(byte[] keys)
		 {
			 StringBuffer serverShareKeyString=new StringBuffer();
		 	for(int i=0;i<keys.length;i++)
		 	{
		 		if(i==keys.length-1)
		 		serverShareKeyString.append(keys[i]);
		 		else
		 		serverShareKeyString.append(keys[i]).append(",");
		 		
		 		
		 	}
		 //	serverShareKeyString.append("]");
		 	return serverShareKeyString.toString();
		 }
		
		
		 public static String makeSHA1Hash(String input)
		          throws NoSuchAlgorithmException, UnsupportedEncodingException
		 {
		          MessageDigest md = MessageDigest.getInstance("SHA1");
		          md.reset();
		          byte[] buffer = input.getBytes();
		          md.update(buffer);
		          byte[] digest = md.digest();
		
		          String hexStr = "";
		          for (int i = 0; i < digest.length; i++) {
		              hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
		          }
		          return hexStr;
		 }
		
		
		
		 
 
    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    private static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len-1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }
    
}
