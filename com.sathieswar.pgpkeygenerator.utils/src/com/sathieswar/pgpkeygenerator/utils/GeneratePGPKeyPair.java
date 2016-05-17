/*
 *  Copyright (C) 2016
 *
 *  The program(s) herein may be used and/or copied only with the
 *  written permission of the author or in accordance with the terms
 *  and conditions stipulated in the agreement/contract under which the
 *  program(s) have been supplied.
 *
 * Date        	 Author             Changes
 * May 17, 2016  Sathieswar         Created
 */
package com.sathieswar.pgpkeygenerator.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;

public class GeneratePGPKeyPair {
	public boolean createPGPKeyPair(String emailAddress, String passPhrase,
			String outputPath) {

		KeyPair keyPair = null;
		PGPKeyPair secretKey = null;
		PGPSecretKeyRing skr = null;

		try {

			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
					new BouncyCastleProvider());
			keyPairGen.initialize(4096);

			keyPair = keyPairGen.generateKeyPair();

			PGPSignatureSubpacketGenerator hashedGen = new PGPSignatureSubpacketGenerator();

			hashedGen.setKeyFlags(true, KeyFlags.CERTIFY_OTHER
					| KeyFlags.SIGN_DATA | KeyFlags.ENCRYPT_COMMS
					| KeyFlags.ENCRYPT_STORAGE);

			hashedGen.setPreferredCompressionAlgorithms(false,
					new int[] { CompressionAlgorithmTags.ZIP });

			hashedGen.setPreferredHashAlgorithms(false,
					new int[] { HashAlgorithmTags.SHA1 });

			hashedGen.setPreferredSymmetricAlgorithms(false,
					new int[] { SymmetricKeyAlgorithmTags.AES_256 });

			secretKey = new PGPKeyPair(PGPPublicKey.RSA_GENERAL, keyPair,
					new Date());

			PGPKeyRingGenerator keyRingGen = new PGPKeyRingGenerator(
					PGPSignature.POSITIVE_CERTIFICATION, secretKey,
					emailAddress + "<" + emailAddress + ">",
					PGPEncryptedData.AES_256, passPhrase.toCharArray(), true,
					hashedGen.generate(), null, new SecureRandom(),
					new BouncyCastleProvider());

			PGPPublicKeyRing pkr = keyRingGen.generatePublicKeyRing();
			ArmoredOutputStream pubout = new ArmoredOutputStream(
					new BufferedOutputStream(new FileOutputStream(outputPath
							+ "/public.asc")));
			pkr.encode(pubout);
			pubout.close();

			skr = keyRingGen.generateSecretKeyRing();
			ArmoredOutputStream secout = new ArmoredOutputStream(
					new BufferedOutputStream(new FileOutputStream(outputPath
							+ "/private.skr")));
			skr.encode(secout);
			secout.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
