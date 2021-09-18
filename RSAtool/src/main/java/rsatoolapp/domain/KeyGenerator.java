package rsatoolapp.domain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyGenerator {
    KeyPairGenerator kpg;
    Base64.Encoder encoder;
    PrivateKey pvtKey;
    PublicKey pubKey;
    KeyFactory kf;

    public KeyGenerator() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        kf = KeyFactory.getInstance("RSA");
        encoder = Base64.getEncoder();

/*        // Write the private key in Base64 to a file
        Writer writer = new FileWriter("private.key");
        writer.write("-----BEGIN RSA PRIVATE KEY-----\n" + encoder.encodeToString(pvtKey.getEncoded()) + "\n-----END RSA PRIVATE KEY-----\n");
        writer.close();

        // Write the public key in Base64 to a file
        writer = new FileWriter("public.pub");
        writer.write("-----BEGIN RSA PUBLIC KEY-----\n" + encoder.encodeToString(pubKey.getEncoded()) + "\n-----END RSA PUBLIC KEY-----\n");
        writer.close();*/
    }

    public void generateKeys() throws IOException, InvalidKeySpecException {
        // Generate they key pair
        KeyPair kp = kpg.generateKeyPair();
        Key pb = kp.getPublic();
        Key pt = kp.getPrivate();

        // Write the encoded private key in to a file
        FileOutputStream out = new FileOutputStream("rsa.key");
        out.write(pt.getEncoded());
        out.close();

        // Write the encoded public key in to a file
        out = new FileOutputStream("rsa.pub");
        out.write(pb.getEncoded());
        out.close();

        // Read all bytes from the private key file
        Path path = Paths.get("rsa.key");
        byte[] bytes = Files.readAllBytes(path);

        // Generate private key
        PKCS8EncodedKeySpec ksPrivate = new PKCS8EncodedKeySpec(bytes);
        pvtKey = kf.generatePrivate(ksPrivate);

        // Read all the public key bytes
        path = Paths.get("rsa.pub");
        bytes = Files.readAllBytes(path);

        // Generate public key
        X509EncodedKeySpec ksPublic = new X509EncodedKeySpec(bytes);
        pubKey = kf.generatePublic(ksPublic);
    }

    public String getPublicKey() {
        return "-----BEGIN RSA PUBLIC KEY-----\n"
                + encoder.encodeToString(pubKey.getEncoded())
                + "\n-----END RSA PUBLIC KEY-----\n";
    }

    public String getPrivateKey() {
        return "-----BEGIN RSA PRIVATE KEY-----\n"
                + encoder.encodeToString(pvtKey.getEncoded())
                + "\n-----END RSA PRIVATE KEY-----\n";
    }
}