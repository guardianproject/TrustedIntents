
package info.guardianproject.trustedintents.test;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

import info.guardianproject.trustedintents.ApkSignaturePin;

public class ThisPackagePin extends ApkSignaturePin {

    public ThisPackagePin(PackageManager pm) {
        PackageInfo pkgInfo;
        try {
            pkgInfo = pm.getPackageInfo(
                    ReceiverTestActivityUnitTestCase.class.getPackage().getName(),
                    PackageManager.GET_SIGNATURES);
            Signature[] signatures = pkgInfo.signatures;
            certificates = new byte[signatures.length][];
            for (int i = 0; i < certificates.length; i++)
                certificates[i] = signatures[i].toByteArray();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
