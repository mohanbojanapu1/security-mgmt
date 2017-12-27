package com.grit.secureid.CryptoService;

import org.apache.commons.codec.binary.Base64;

//import com.grit.gilog.GILog;
//import com.grit.sync.util.JSONUtils;

/**
 * Created by anuragrepaka on 08/05/17.
 * Modified by Sreevidya Ette on 28/08/17
 */

public class EncryptionDecryptionHelper {

    private static final String TAG = EncryptionDecryptionHelper.class.getSimpleName();
    static final String key = "Uakxx32wzX358WX920B67010j9TjXa35";
    
    // Sample Test Method
    public static void main( String[] args )
    {   
        String plainText = "Hello World!";
		
        //System.out.println("PlainText String - " + plainText);
        
        //String cipherText = "MGM2NDdiZmU5N2M2NTk0Y05WMk1aWjJIMTFrZC9TWklKTWpQUlE9PQo="; //encrypt(plainText);
        
        String cipherText = encrypt(plainText);
        		
        //System.out.println("CipherText String - " + cipherText);
        
        String decryptedPlainText = decrypt(cipherText);
        
        //System.out.println("PlainText String ( Encrypted and Then Decrypted ) - " + decryptedPlainText);
    }
    
    /**
     * method to encrypt plain text.
     * @param plainText reference to password entered by user.
     * @return reference to encrypted password text.
     */
    public static String encrypt(final String plainText) {
        try {
            CryptLib _crypt = new CryptLib();
            String iv = CryptLib.generateRandomIV(16); //16 bytes = 128 bit, random generated iv.
            //String iv = "2d8f6e860226a252";
            //System.out.println("encrypt - iv - " + iv);
            
            String encrypt = _crypt.encrypt(plainText, key, iv); //encryption, used above key & iv.
            
            //System.out.println("encrypt string ( with iv concat ) - " + encrypt);
            
            String ivAppendPswd = iv.concat(encrypt);   //iv appended to encryption, so that server can decrypt by using iv.
            byte[] ivAppendPswdBytes64 = ivAppendPswd.getBytes();   //base64 encode, to further randomize & 64bit text.
            String ivAppendPswdBytes64String = Base64.encodeBase64String(ivAppendPswdBytes64);            
            return ivAppendPswdBytes64String;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * method to decrypt the encrypted string.
     */
    public static String decrypt(final String encryptText) {
        try {
            CryptLib crypt = new CryptLib();
            byte[] base64EncryptByte = Base64.decodeBase64(encryptText);
            String base64EncryptString = new String (base64EncryptByte);
            //System.out.println("1 " + base64EncryptString + " " + base64EncryptString.length());
            String iv = base64EncryptString.substring(0, 16);
            
            //System.out.println("decrypt - iv - " + iv);
            
            String encryptedJsonString = base64EncryptString.substring(16);
            String decrypt = crypt.decrypt(encryptedJsonString, key, iv);
            return decrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}