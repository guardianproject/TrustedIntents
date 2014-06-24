
package info.guardianproject.trustedintents.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.android.AndroidIncludedAppsPin;
import com.android.AndroidSystemPin;

import info.guardianproject.trustedintents.ApkSignaturePin;

public class ApkSignaturePinTests extends AndroidTestCase {
    private static final String TAG = "ApkSignatureTests";

    private String androidSystemFingerprint = "27196e386b875e76adf700e7ea84e4c6eee33dfa";
    private String androidIncludedAppsFingerprint = "61ed377e85d386a8dfee6b864bd85b0bfaa5af81";

    public void testFingerprints() {
        ApkSignaturePin android = new AndroidSystemPin();
        assertTrue(android.doFingerprintsMatchCertificates());
        Log.i(TAG, androidSystemFingerprint + " == " + android.getSHA1Fingerprint());
        assertEquals(androidSystemFingerprint, android.getSHA1Fingerprint());

        ApkSignaturePin comAndroid = new AndroidIncludedAppsPin();
        assertTrue(comAndroid.doFingerprintsMatchCertificates());
        Log.i(TAG, androidIncludedAppsFingerprint + " == " + comAndroid.getSHA1Fingerprint());
        assertEquals(androidIncludedAppsFingerprint, comAndroid.getSHA1Fingerprint());
    }

}
