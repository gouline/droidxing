DroidXing
===========

Simple Android wrapper for [ZXing](https://github.com/zxing/zxing).

Minimum Requirements
--------------------
* [Android SDK](https://developer.android.com/sdk/index.html)
* Android API 19 (4.4)
* Android Build Tools 19.1.0
* IDE: [IntelliJ 13 CE](http://www.jetbrains.com/idea/download/) or [Android Studio 0.6.0](http://developer.android.com/sdk/installing/studio.html)

Usage
-----
Include the library as a module in your IDE. (NOTE: currently working on submitting it into Maven Central, which should make this step easier)

Declare the `CaptureActivity` in your AndroidManifest.xml.

```xml
<activity
    android:name="net.gouline.droidxing.CaptureActivity"
    android:configChanges="orientation"
    android:screenOrientation="nosensor"/>
```

Now you can just start `CaptureActivity` for result and once the code is scanned, you can retrieve it from `EXTRA_CODE_RESULT` extra.
