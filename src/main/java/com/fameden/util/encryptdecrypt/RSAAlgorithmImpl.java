package com.fameden.util.encryptdecrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.fameden.common.exception.BreachingSingletonException;
import com.fameden.housekeeping.constants.HouseKeepingConstants;

public class RSAAlgorithmImpl {

	private static Logger logger = LoggerFactory
			.getLogger(RSAAlgorithmImpl.class);

	private static RSAAlgorithmImpl SINGLETON;

	private static final String ALGORITHM = "RSA";

	private static final String PRIVATE_KEY_FILE = System
			.getProperty(HouseKeepingConstants.PRIVATE_RSA_KEY);

	private static Cipher cipher = null;

	private static final String PUBLIC_KEY_FILE = System
			.getProperty(HouseKeepingConstants.PUBLIC_RSA_KEY);

	private RSAAlgorithmImpl() throws BreachingSingletonException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(RSAAlgorithmImpl.class.getName());
		} else {
			cipher = Cipher.getInstance(ALGORITHM);
		}
	}

	public static RSAAlgorithmImpl getInstance()
			throws BreachingSingletonException, NoSuchAlgorithmException,
			NoSuchPaddingException {
		if (SINGLETON == null)

			SINGLETON = new RSAAlgorithmImpl();

		return SINGLETON;
	}

	@SuppressWarnings("unused")
	private void generateKey() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance(ALGORITHM);
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();

			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_FILE);

			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			ObjectOutputStream publicKeyOS = new ObjectOutputStream(
					new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			ObjectOutputStream privateKeyOS = new ObjectOutputStream(
					new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private boolean areKeysPresent() {

		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(PUBLIC_KEY_FILE);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}

	private byte[] encrypt(String text) {

		ObjectInputStream inputStream = null;
		byte[] cipherText = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(
					PUBLIC_KEY_FILE));
			PublicKey key = (PublicKey) inputStream.readObject();

			cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = cipher.doFinal(text.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cipherText;
	}

	public String encryptText(String text) {
		return new BASE64Encoder().encode(encrypt(text));
	}

	private String decrypt(byte[] text) {

		ObjectInputStream inputStream = null;
		byte[] dectyptedText = null;
		try {
			logger.info("The path is " + System.getProperty(HouseKeepingConstants.PRIVATE_RSA_KEY));
			inputStream = new ObjectInputStream(new FileInputStream(
					PRIVATE_KEY_FILE));
			PrivateKey key = (PrivateKey) inputStream.readObject();
			cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new String(dectyptedText);
	}

	public String decryptText(String input) {
		String result = null;
		try {
			result = decrypt(new BASE64Decoder().decodeBuffer(input));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) {
		/*
		 * try { if (!areKeysPresent()) { generateKey(); }
		 * 
		 * final String originalText = "ravjot28   ";
		 * 
		 * String source = encryptText(originalText);
		 * 
		 * String dest = decryptText(source);
		 * 
		 * System.out.println(source + "\n" + dest);
		 * 
		 * System.out.println(originalText.length() + " " + source.length() +
		 * " " + dest.length());
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}
}