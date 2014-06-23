
package info.guardianproject.trustedintents;

import android.content.pm.Signature;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class CertificatePin {

    protected byte[] certificate = null;
    private Signature[] signatures;

    public byte[] getEncoded() {
        return certificate; // DER-encoded X.509 Certificate
    }

    public Signature[] getSignatures() {
        // TODO this needs to handle multiple Signature instances
        if (signatures == null) {
            signatures = new Signature[1];
            signatures[0] = new Signature(certificate);
        }
        return signatures;
    }

    public String getFingerprint(String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(certificate);
            BigInteger bi = new BigInteger(1, hashBytes);
            md.reset();
            return String.format("%0" + (hashBytes.length << 1) + "x", bi);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMD5Fingerprint() {
        return getFingerprint("MD5");
    }

    public String getSHA1Fingerprint() {
        return getFingerprint("SHA1");
    }

    public String getSHA256Fingerprint(byte[] input) {
        return getFingerprint("SHA-256");
    }
}
