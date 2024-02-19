package com.daviddev16.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Manager {

	private static final SecureRandom RANDOM = new SecureRandom();
	private static SecretKeyFactory SECURITY_KEY_FACTORY;
	static {
		try {
			SECURITY_KEY_FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private static final int KEY_LENGTH = 256;
	private static final int ITERATION_COUNT = 65536;

	private static final String SALT = "b52ebcbc7782de35da";
	private static final String SECRET_KEY = "6fe8ecbc1deafa51c2ecf088cf364eba1ceba9032ffbe2621e771b90ea93153d";

	public static String encrypt(String strToEncrypt) throws Exception {
		byte[] iv = new byte[16];
		RANDOM.nextBytes(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
		SecretKey tmp = SECURITY_KEY_FACTORY.generateSecret(spec);
		SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
		byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
		byte[] encryptedData = new byte[iv.length + cipherText.length];
		System.arraycopy(iv, 0, encryptedData, 0, iv.length);
		System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	public static String decrypt(String strToDecrypt) throws Exception {
		byte[] encryptedData = Base64.getDecoder().decode(strToDecrypt);
		byte[] iv = new byte[16];
		System.arraycopy(encryptedData, 0, iv, 0, iv.length);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
		SecretKey tmp = SECURITY_KEY_FACTORY.generateSecret(spec);
		SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
		byte[] cipherText = new byte[encryptedData.length - 16];
		System.arraycopy(encryptedData, 16, cipherText, 0, cipherText.length);
		byte[] decryptedText = cipher.doFinal(cipherText);
		return new String(decryptedText, "UTF-8");
	}
}
