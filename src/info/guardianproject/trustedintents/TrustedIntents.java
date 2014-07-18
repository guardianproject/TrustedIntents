
package info.guardianproject.trustedintents;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.security.cert.CertificateException;
import java.util.LinkedHashSet;

public class TrustedIntents {

    private static TrustedIntents instance;

    private final Context context;
    private PackageManager pm;

    private final LinkedHashSet<ApkSignaturePin> pinList;

    private TrustedIntents(Context context) {
        this.context = context;
        pinList = new LinkedHashSet<ApkSignaturePin>();
    }

    public static TrustedIntents get(Context context) {
        if (instance == null)
            instance = new TrustedIntents(context.getApplicationContext());
        return instance;
    }

    public boolean isReceiverTrusted(Intent intent) {
        if (!isIntentSane(intent))
            return false;
        String packageName = intent.getPackage();
        if (TextUtils.isEmpty(packageName)) {
            packageName = intent.getComponent().getPackageName();
        }
        try {
            checkTrustedSigner(packageName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (CertificateException e) {
            return false;
        }
        return true;
    }

    private boolean isIntentSane(Intent intent) {
        if (intent == null)
            return false;
        if (TextUtils.isEmpty(intent.getPackage())) {
            ComponentName componentName = intent.getComponent();
            if (componentName == null || TextUtils.isEmpty(componentName.getPackageName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add an APK signature that is always trusted for any packageName.
     *
     * @param pin {@link Class} of the {@link ApkSignaturePin} to trust
     * @return boolean
     * @throws {@link IllegalArgumentException} the class cannot be instantiated
     */
    public boolean addTrustedSigner(Class<? extends ApkSignaturePin> cls) {
        try {
            Constructor<? extends ApkSignaturePin> constructor = cls.getConstructor();
            return pinList.add((ApkSignaturePin) constructor.newInstance((Object[]) null));
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }
    }

    public void checkTrustedSigner(String packageName)
            throws NameNotFoundException, CertificateException {
        if (pm == null)
            pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        checkTrustedSigner(packageInfo.signatures);
    }

    public void checkTrustedSigner(PackageInfo packageInfo)
            throws NameNotFoundException, CertificateException {
        checkTrustedSigner(packageInfo.signatures);
    }

    public void checkTrustedSigner(Signature[] signatures)
            throws NameNotFoundException, CertificateException {
        if (signatures == null || signatures.length == 0)
            throw new CertificateException("signatures cannot be null or empty!");
        for (int i = 0; i < signatures.length; i++)
            if (signatures[i] == null || signatures[i].toByteArray().length == 0)
                throw new CertificateException("Certificates cannot be null or empty!");

        // check whether the APK signer is trusted for all apps
        for (ApkSignaturePin pin : pinList)
            if (areSignaturesEqual(signatures, pin.getSignatures()))
                return; // found a matching trusted APK signer

        throw new CertificateException("APK signatures did not match!");
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

    public void startActivity(Context context, Intent intent) throws CertificateException {
        if (!isIntentSane(intent))
            throw new ActivityNotFoundException("The intent was null or empty!");
        String packageName = intent.getPackage();
        if (TextUtils.isEmpty(packageName)) {
            packageName = intent.getComponent().getPackageName();
            intent.setPackage(packageName);
        }
        try {
            checkTrustedSigner(packageName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            throw new ActivityNotFoundException(e.getLocalizedMessage());
        }
        context.startActivity(intent);
    }
}
