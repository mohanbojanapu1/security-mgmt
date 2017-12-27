package com.grit.secureid.CryptoService;

import java.util.Random;

/**
 * Created by Sreevidya Ette on 30/10/17
 */

public class OTPRandomGeneration {

	/**
	 * Java code to explain how to generate OTP Here we are using random() method of
	 * util class in Java
	 * 
	 * @author Sreevidya on 16/10/2017
	 */
	public static char[] generateOTP(int len) {

		System.out.println("Generating OTP using random() : ");
		System.out.print("You OTP is : ");

		// Using numeric values
		String numbers = "0123456789";
		
		String firstIndexNum = "123456789"; 

		// Using random method
		Random rndm_method = new Random();

		char[] otp = new char[len];

		for (int i = 0; i < len; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			if (i == 0) {
				otp[i] = firstIndexNum.charAt(rndm_method.nextInt(firstIndexNum.length()));
			}else {
				otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
			}
			
			System.out.print("You Final OTP is +++: " + otp[i]);
		}
		return otp;
	}

}