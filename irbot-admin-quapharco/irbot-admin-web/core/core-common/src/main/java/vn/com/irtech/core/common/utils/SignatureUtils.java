package vn.com.irtech.core.common.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class SignatureUtils {

	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024, new SecureRandom());
			KeyPair pair = generator.generateKeyPair();

			return pair;
		} catch (Exception e) {
			return null;
		}
	}

	public static String encodeToString(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	public static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			byte[] decoded = Base64.getDecoder().decode(base64PublicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (Exception e) {
			return null;
		}
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
					Base64.getDecoder().decode(base64PrivateKey.getBytes()));
			KeyFactory keyFactory = null;
			keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			return null;
		}
	}

	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), "UTF-8");
	}

	public static String sign(String plainText, PrivateKey privateKey) {
		try {
			Signature privateSignature = Signature.getInstance("SHA256withRSA");
			privateSignature.initSign(privateKey);
			privateSignature.update(plainText.getBytes("UTF-8"));

			byte[] signature = privateSignature.sign();

			return Base64.getEncoder().encodeToString(signature);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) {
		try {
			Signature publicSignature = Signature.getInstance("SHA256withRSA");
			publicSignature.initVerify(publicKey);
			publicSignature.update(plainText.getBytes("UTF-8"));

			byte[] signatureBytes = Base64.getDecoder().decode(signature);

			return publicSignature.verify(signatureBytes);
		} catch (Exception e) {
			return false;
		}
	}
}
