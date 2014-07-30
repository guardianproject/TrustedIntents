
package info.guardianproject.trustedintents.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import info.guardianproject.trustedintents.TrustedIntents;

import java.security.cert.CertificateException;

public class ReceiverTestActivity extends Activity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Log.i(TAG, "intent package: " + intent.getPackage());
        ComponentName componentName = getCallingActivity();
        if (componentName != null)
            Log.i(TAG, "  componentName package: " + componentName.getPackageName()
                    + "  componentName class: " + componentName.getClassName());

        TrustedIntents ti = TrustedIntents.get(this);
        ti.addTrustedSigner(ThisPackagePin.class);
        try {
            ti.getIntentFromTrustedSender(this);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            assert false;
        } catch (CertificateException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
