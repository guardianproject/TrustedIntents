
package info.guardianproject.trustedintents.test;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.test.AndroidTestCase;
import android.util.Log;

import com.android.AndroidIncludedAppsPin;
import com.android.AndroidSystemPin;

import info.guardianproject.trustedintents.TrustedIntents;

public class TrustedIntentsTests extends AndroidTestCase {
    private static final String TAG = "TrustedIntentsTests";

    PackageManager pm;
    final String[] packagesSignedBy61ed377e85d386a8dfee6b864bd85b0bfaa5af81 = new String[] {
            "com.android.browser", "com.android.calculator2", "com.android.calendar",
            "com.android.dreams.basic", "com.android.providers.calendar",
            "com.android.camera", "com.android.deskclock", "com.android.gesture.builder",
            "com.android.smoketest", "com.android.smoketest.tests",
            "com.android.emulator.connectivity.test", "com.android.development_settings",
            "com.android.email", "com.example.android.livecubes", "com.android.exchange"
    };
    final String[] packagesSignedBy27196e386b875e76adf700e7ea84e4c6eee33dfa = new String[] {
            "android", "com.android.certinstaller", "com.android.backupconfirm",
            "com.android.keyguard", "com.android.sdksetup", "com.android.sharedstoragebackup",
            "com.android.customlocale2", "com.android.development", "com.android.documentsui",
            "com.android.externalstorage", "com.android.location.fused", "com.android.inputdevices"
    };

    @Override
    public void setUp() {
        pm = getContext().getPackageManager();
    }

    private void checkAreSignaturesEqual(String[] packages) {
        Signature[] first = null;
        Signature[] second = null;
        for (int i = 0; i < packages.length; i++) {
            try {
                PackageInfo pkgInfo = pm.getPackageInfo(packages[i], PackageManager.GET_SIGNATURES);
                first = pkgInfo.signatures;
            } catch (NameNotFoundException e) {
                Log.w(TAG, "NameNotFoundException: " + e.getMessage());
                continue;
            }
            for (int j = 0; j < packages.length; j++) {
                if (i == j)
                    continue;
                try {
                    PackageInfo pkgInfo = pm.getPackageInfo(packages[j],
                            PackageManager.GET_SIGNATURES);
                    second = pkgInfo.signatures;
                } catch (NameNotFoundException e) {
                    Log.w(TAG, "NameNotFoundException: " + e.getMessage());
                    continue;
                }
                assertTrue(TrustedIntents.get().areSignaturesEqual(first, second));
            }
        }
    }

    public void testCheckAreSignaturesEqual() {
        checkAreSignaturesEqual(packagesSignedBy61ed377e85d386a8dfee6b864bd85b0bfaa5af81);
        checkAreSignaturesEqual(packagesSignedBy27196e386b875e76adf700e7ea84e4c6eee33dfa);
    }

    public void testCheckAreSignaturesNotEqual() {
        assertFalse(TrustedIntents.get().areSignaturesEqual(
                new AndroidIncludedAppsPin().getSignatures(),
                new AndroidSystemPin().getSignatures()));
        PackageInfo pkgInfo;
        Signature[] first = null;
        Signature[] second = null;
        int length = packagesSignedBy27196e386b875e76adf700e7ea84e4c6eee33dfa.length;
        if (length > packagesSignedBy61ed377e85d386a8dfee6b864bd85b0bfaa5af81.length)
            length = packagesSignedBy61ed377e85d386a8dfee6b864bd85b0bfaa5af81.length;
        for (int i = 0; i < length; i++) {
            try {
                pkgInfo = pm.getPackageInfo(
                        packagesSignedBy27196e386b875e76adf700e7ea84e4c6eee33dfa[i],
                        PackageManager.GET_SIGNATURES);
                first = pkgInfo.signatures;
                pkgInfo = pm.getPackageInfo(
                        packagesSignedBy61ed377e85d386a8dfee6b864bd85b0bfaa5af81[i],
                        PackageManager.GET_SIGNATURES);
                second = pkgInfo.signatures;
            } catch (NameNotFoundException e) {
                Log.w(TAG, "NameNotFoundException: " + e.getMessage());
                continue;
            }
            assertFalse(TrustedIntents.get().areSignaturesEqual(first, second));
        }

    }
}
