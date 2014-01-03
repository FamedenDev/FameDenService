package com.fameden.util.encryptdecrypt;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RavBase64.Base64Decoder;
import RavBase64.Base64Encoder;

import com.fameden.common.constants.CommonConstants;
import com.fameden.common.exception.BreachingSingletonException;

public class CustomEncryptionImpl {

	private static Logger logger = LoggerFactory
			.getLogger(CustomEncryptionImpl.class);

	private static CustomEncryptionImpl SINGLETON;

	private CustomEncryptionImpl() throws BreachingSingletonException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		if (SINGLETON != null) {
			throw new BreachingSingletonException(
					CustomEncryptionImpl.class.getName());
		}
	}

	public static CustomEncryptionImpl getInstance()
			throws BreachingSingletonException, NoSuchAlgorithmException,
			NoSuchPaddingException {
		if (SINGLETON == null)

			SINGLETON = new CustomEncryptionImpl();

		return SINGLETON;
	}

	public String encryptNum(int number) {
		int finalNumber = 0;
		while (true) {
			finalNumber = finalNumber * 10 + number % 10;
			number = (number - number % 10) / 10;
			if (number == 0)
				break;
		}
		logger.info("For Encryption for Number the parsed number is "
				+ finalNumber);
		return encryptText(new StringBuffer("" + finalNumber));
	}

	public int decryptNum(String text) {
		String decryptText = decryptText(new StringBuffer(text));
		int number = Integer.parseInt(decryptText);
		int finalNumber = 0;
		while (true) {
			finalNumber = finalNumber * 10 + number % 10;
			number = (number - number % 10) / 10;
			if (number == 0)
				break;
		}
		logger.info("Decrypted Number is " + finalNumber);
		return finalNumber;
	}

	public String encryptText(StringBuffer text) {
		text = addPad(text);
		Base64Encoder encoder = new Base64Encoder(text.toString());
		return encoder.get();
	}

	public String decryptText(StringBuffer text) {
		Base64Decoder decoder = new Base64Decoder(text.toString());

		text = removePad(new StringBuffer(decoder.get()));
		return text.toString();
	}

	private StringBuffer addPad(StringBuffer input) {
		input.insert(0, CommonConstants.PADDING);

		input.append(CommonConstants.PADDING);

		return input;

	}

	private StringBuffer removePad(StringBuffer input) {
		input.delete(0, CommonConstants.PADDING.length());
		input.delete(input.indexOf(CommonConstants.PADDING), input.length());
		return input;
	}
}