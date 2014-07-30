
package info.guardianproject.trustedintents.test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    }
}
