package util;

import util.CheckOS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

/**
 * @author JavaDigest
 *
 */
public class EncryptionUtil {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     * String to hold the name of the private key file.
     */
    public File privateKeyFile;

    /**
     * String to hold name of the public key file.
     */
    public File publicKeyFile;

    public EncryptionUtil() {
        CheckOS os = new CheckOS();
        os.check();
        String saveDir = os.getSaveDir().getName() + os.getSlash();
        String slash = os.getSlash();
        privateKeyFile = new File(saveDir + "keys" + slash + "private.key");
        publicKeyFile = new File (saveDir + "keys" + slash + "public.key");
    }

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Private.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void generateKey() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // Saving the Private key in a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    public boolean areKeysPresent() {
        return privateKeyFile.exists() && publicKeyFile.exists();
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *          : original plain text
     * @param key
     *          :The public key
     * @return Encrypted text
     */
    public byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *          :encrypted text
     * @param key
     *          :The private key
     * @return plain text
     */
    public String decrypt(byte[] text, PrivateKey key) {
        byte[] decryptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            decryptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(decryptedText);
    }

    public File getPrivateKeyFile() {
        return privateKeyFile;
    }

    public File getPublicKeyFile() {
        return publicKeyFile;
    }

//    /**
//     * Test the EncryptionUtil
//     */
//    public static void main(String[] args) {
//
//        try {
//
//            // Check if the pair of keys are present else generate those.
//            if (!areKeysPresent()) {
//                // Method generates a pair of keys using the RSA algorithm and stores it
//                // in their respective files
//                generateKey();
//            }
//
//            final String originalText = "Text to be encrypted ";
//            ObjectInputStream inputStream = null;
//
//            // Encrypt the string using the public key
//            inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
//            final PublicKey publicKey = (PublicKey) inputStream.readObject();
//            final byte[] cipherText = encrypt(originalText, publicKey);
//
//            // Decrypt the cipher text using the private key.
//            inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
//            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
//            final String plainText = decrypt(cipherText, privateKey);
//
//            // Printing the Original, Encrypted and Decrypted Text
//            System.out.println("Original Text: " + originalText);
//            System.out.println("Encrypted Text: " +cipherText.toString());
//            System.out.println("Decrypted Text: " + plainText);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}