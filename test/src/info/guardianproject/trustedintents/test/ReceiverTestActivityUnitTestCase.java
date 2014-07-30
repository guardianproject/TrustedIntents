
package info.guardianproject.trustedintents.test;

import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.text.TextUtils;

public class ReceiverTestActivityUnitTestCase extends ActivityUnitTestCase<ReceiverTestActivity> {
    private static final String TAG = "ReceiverActivityTestCase";

    private Instrumentation instrumentation;

    public ReceiverTestActivityUnitTestCase() {
        super(ReceiverTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        instrumentation = getInstrumentation();
    }

    public void testSettingComponent() {
        Intent sentIntent = new Intent(instrumentation.getTargetContext(),
                ReceiverTestActivity.class);
        startActivity(sentIntent, null, null);
        Intent receivedIntent = getActivity().getIntent();
        assertTrue(receivedIntent != null);
        assertTrue(receivedIntent.getPackage() == null);
        ComponentName receivedComponentName = receivedIntent.getComponent();
        assertTrue(receivedComponentName != null);
        String packageName = receivedComponentName.getPackageName();
        assertEquals(packageName, getActivity().getPackageName());
    }

    public void testSettingPackage() {
        Intent sentIntent = new Intent(Intent.ACTION_MAIN);
        String packageName = this.getClass().getPackage().getName();
        sentIntent.setPackage(packageName);
        startActivity(sentIntent, null, null);
        Intent receivedIntent = getActivity().getIntent();
        assertTrue(receivedIntent != null);
        assertTrue(receivedIntent.getPackage() != null);
        ComponentName receivedComponentName = receivedIntent.getComponent();
        assertTrue(receivedComponentName != null);
        assertFalse(TextUtils.isEmpty(receivedComponentName.getPackageName()));
        assertEquals(packageName, receivedComponentName.getPackageName());
    }
}
