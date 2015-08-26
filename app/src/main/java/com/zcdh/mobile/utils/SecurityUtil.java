package com.zcdh.mobile.utils;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/** 
* This program generates a AES key, retrieves its raw bytes, and then 
* reinstantiates a AES key from the key bytes. The reinstantiated key is used 
* to initialize a AES cipher for encryption and decryption.  SecurityUtil.encrypt(
*/ 

public class SecurityUtil {   
	
	private final static String SQLCIPHER_PWD = "abcabc";
       
	 public static String encrypt(String cleartext) {      
         byte[] rawKey;
         byte[] result = null;
		try {
			rawKey = getRawKey(SQLCIPHER_PWD.getBytes());
			result = encrypt(rawKey, cleartext.getBytes("utf-8"));      
		} catch (Exception e) {
			e.printStackTrace();
		}      
         return toHex(result);      
     }      
           
     public static String decrypt(String encrypted) {      
         byte[] rawKey; 
         byte[] result = null;
         String strResult = null;
           
         try {
        	 if(encrypted.length()!=32 && encrypted.length()!=64){
        		 return encrypted;
        	 }
        	 rawKey= getRawKey(SQLCIPHER_PWD.getBytes());      
        	 byte[] enc = toByte(encrypted);      
        	 result = decrypt(rawKey, enc); 
        	 strResult=new String(result,"utf-8");   
         } catch (Exception e) {
        	 e.printStackTrace();
			return encrypted;
         } 
         
         return  strResult;   
     }  
      
     /*
     private static byte[] getRawKey(byte[] seed) throws Exception {      
         KeyGenerator kgen = KeyGenerator.getInstance("AES");      
         SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");      
         sr.setSeed(seed);      
         kgen.init(128, sr); // 192 and 256 bits may not be available      
         SecretKey skey = kgen.generateKey();      
         byte[] raw = skey.getEncoded();      
         return raw;      
     }      
      */
     
     private static byte[] getRawKey(byte[] seed) throws Exception {
 		KeyGenerator kgen = KeyGenerator.getInstance("AES");
 		SecureRandom sr = null;
 		if (android.os.Build.VERSION.SDK_INT >= 17) {
 			sr = SecureRandom.getInstance("SHA1PRNG","Crypto");
 		}
 		else{
 			sr = SecureRandom.getInstance("SHA1PRNG");
 		}
 		sr.setSeed(seed);	
 		kgen.init(128, sr); // 192 and 256 bits may not be available	
 		SecretKey skey = kgen.generateKey();
 		byte[] raw = skey.getEncoded();
 		return raw;
 	}
           
     private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {      
         SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");      
         Cipher cipher = Cipher.getInstance("AES");      
         cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
        
         byte[] encrypted = cipher.doFinal(clear);      
         return encrypted;      
     }      
      
     private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {      
         SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");      
         Cipher cipher = Cipher.getInstance("AES");      
         cipher.init(Cipher.DECRYPT_MODE, skeySpec);      
         byte[] decrypted = cipher.doFinal(encrypted);      
         return decrypted;      
     }      
      
     public static String toHex(String txt) {      
         return toHex(txt.getBytes());      
     }      
     public static String fromHex(String hex) {      
         return new String(toByte(hex));      
     }      
           
     public static byte[] toByte(String hexString) {      
         int len = hexString.length()/2;      
         byte[] result = new byte[len];      
         for (int i = 0; i < len; i++)      
             result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();      
         return result;      
     }      
      
     public static String toHex(byte[] buf) {      
         if (buf == null)      
             return "";      
         StringBuffer result = new StringBuffer(2*buf.length);      
         for (int i = 0; i < buf.length; i++) {      
             appendHex(result, buf[i]);      
         }      
         return result.toString();      
     }      
     private final static String HEX = "0123456789ABCDEF";      
     private static void appendHex(StringBuffer sb, byte b) {      
         sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));      
     }   
     
     /**
 	 * 密码复杂度
 	 * @return
 	 * @author YJN, 2013-11-29 下午5:15:56
 	 */
 	public static int pwdComplexity(String pwd){
 		int cmpexity = 0;
 		
 		//最强
 		String reg1 = "^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9])).{6,}";
 		//最弱
 		String reg3 = "([a-z]){6,}|([0-9]){6,}|([A-Z]){6,}";
 		 if(pwd.matches(reg1)){
 			 cmpexity += 60;
 		 }else if(pwd.matches(reg3)){
 			 cmpexity += 40;
 		 }else{
 			 //次
 			 cmpexity += 20;
 		 }
 		
 		return cmpexity;
 	}
    
 	
 	
}