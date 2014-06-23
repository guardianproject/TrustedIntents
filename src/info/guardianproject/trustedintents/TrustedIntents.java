
package info.guardianproject.trustedintents;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.security.cert.CertificateException;
import java.util.LinkedHashMap;

public class TrustedIntents {

    private static TrustedIntents instance;

    private LinkedHashMap<String, Signature[]> map = new LinkedHashMap<String, Signature[]>();

    private TrustedIntents() {
    }

    public static TrustedIntents get() {
        if (instance == null)
            instance = new TrustedIntents();
        return instance;
    }

    public void addPin(String packageName, Signature[] pin) {
        map.put(packageName, pin);
    }

    public void checkPin(String packageName, Signature[] signatures)
            throws NameNotFoundException, CertificateException {
        if (TextUtils.isEmpty(packageName))
            throw new NameNotFoundException("packageName cannot be null or empty!");
        if (signatures == null || signatures.length == 0)
            throw new CertificateException("signatures cannot be null or empty!");
        for (int i = 0; i < signatures.length; i++)
            if (signatures[i] == null || signatures[i].toByteArray().length == 0)
                throw new CertificateException("Certificates cannot be null or empty!");
        if (!map.containsKey(packageName))
            throw new NameNotFoundException(packageName);
        if (!areSignaturesEqual(signatures, map.get(packageName)))
            throw new CertificateException("Signature not equal to pin for " + packageName);
    }

    public boolean areSignaturesEqual(Signature[] sigs0, Signature[] sigs1) {
        // TODO where is Android's implementation of this that I can just call?
        if (sigs0 == null || sigs1 == null)
            return false;
        if (sigs0.length == 0 || sigs1.length == 0)
            return false;
        if (sigs0.length != sigs1.length)
            return false;
        for (int i = 0; i < sigs0.length; i++)
            if (!sigs0[i].equals(sigs1[i]))
                return false;
        return true;
    }
}
