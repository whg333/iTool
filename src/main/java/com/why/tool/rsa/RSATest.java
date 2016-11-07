package com.why.tool.rsa;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSATest {
	
	public static void main(String[] args) throws Exception {
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream publicInput = loader.getResourceAsStream("java_rsa_public_key.pem");
		InputStream privateInput = loader.getResourceAsStream("java_pkcs8_rsa_private_key.pem");
		
		//InputStream publicInput = new FileInputStream("C:\\java_rsa_public_key.pem");
        //InputStream privateInput = new FileInputStream("C:\\java_pkcs8_rsa_private_key.pem");
		//InputStream privateInput = new FileInputStream("C:\\java_rsa_private_key.pem");
		
		PublicKey publicKey = RSAUtils.loadPublicKey(publicInput);
		PrivateKey privateKey = RSAUtils.loadPrivateKey(privateInput);
		
		//String publicKey = RSAUtils.PUCLIC_KEY;
		//String privateKey = RSAUtils.PRIVATE_KEY;

		String plainText = "明文123456";
		String cipherText = RSAUtils.encrypt(plainText, publicKey);
		System.out.println(cipherText);
		String decryptPlainText = RSAUtils.decrypt(cipherText, privateKey);
		System.out.println(decryptPlainText);
		if (!decryptPlainText.equals(plainText)) {
			System.out.println("RSA程序出错1");
		} else {
			System.out.println("RSA程序正确1");
		}
		
		//plainText = "654321文明";
		cipherText = RSAUtils.encrypt(plainText, privateKey);
		System.out.println(cipherText);
		decryptPlainText = RSAUtils.decrypt(cipherText, publicKey);
		System.out.println(decryptPlainText);
		if (!decryptPlainText.equals(plainText)) {
			System.out.println("RSA程序出错2");
		} else {
			System.out.println("RSA程序正确2");
		}
	}
}
