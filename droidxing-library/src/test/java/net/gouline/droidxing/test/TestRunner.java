package net.gouline.droidxing.test;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

public class TestRunner extends RobolectricTestRunner {

    public TestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        return new AndroidManifest(
                Fs.fileFromPath("src/main/AndroidManifest.xml"),
                Fs.fileFromPath("src/main/res"),
                Fs.fileFromPath("src/main/assets")) {
            @Override
            public int getTargetSdkVersion() {
                return 18;
            }
        };
    }

}
