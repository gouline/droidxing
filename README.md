DroidXing [![Build Status](https://travis-ci.org/mgouline/droidxing.svg?branch=master)](https://travis-ci.org/mgouline/droidxing)
===========

Simple Android wrapper for [ZXing](https://github.com/zxing/zxing).

Summary
-------
DroidXing is a fork of the [ZXing Android app](https://github.com/zxing/zxing/tree/master/android) intended for anyone, who only wants to allow users to input data in their app via a barcode (1D or 2D, including QR codes).

The flow revolves around the capture activity that gets started, provides the viewfinder UI to allow the user to scan the code and closes, handing the control back to your app.

Usage
-----
The easiest way to import the library into your project is by grabbing the AAR from Maven Central. Alternatively, you can check out the the source and manually import it into your IDE.

```gradle
depedencies {
  compile 'net.gouline.droidxing:droidxing:0.1.1@aar'
}
```

Next, declare the `CaptureActivity` in your AndroidManifest.xml.

```xml
<activity android:name="net.gouline.droidxing.CaptureActivity" />
```

Now you can just start `CaptureActivity` for result and let it handle the scanning.

```java
activity.startActivityForResult(new Intent(activity, CaptureActivity.class), 0);
```

Once the code has been scanned, you can retrieve the data from the result by overriding `onActivityResult()` in the activity that started the `CaptureActivity`.

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if (resultCode == RESULT_OK && data != null) {
    Serializable codeResult = data.getSerializableExtra(CaptureActivity.EXTRA_CODE_RESULT);
    if (codeResult != null && codeResult instanceof CaptureResult) {
      CaptureResult codeResultBlock = (CaptureResult) codeResult;
      Result rawResult = codeResultBlock.getRawResult(); // Raw scan data
      ParsedResult parsedResult = codeResultBlock.getParsedResult(); // Parsed data
    }
  }
}
```

Once you have the `ParsedResult` object, you can test for the subtype you are expecting (look at the classes in the `com.google.zxing.client.result` package that extend `ParsedResult`) and retrieve the data. Below is a URI code example.

```java
public String getCodeURI(ParsedResult parsedResult) {
  if (parsedResult != null && parsedResult instanceof URIParsedResult) {
    return ((URIParsedResult) parsedResult).getURI();
  }
  return null;
}
```

Configuration
-------------
Default configuration will suffice for most users but the following advanced features can be provided:

| Key                              | Description                           | Default | Type           |
|:-------------------------------- |:------------------------------------- |:------- |:-------------- |
| `KEY_DECODE_1D_PRODUCT`          | enable 1D product barcodes            | true    | boolean        |
| `KEY_DECODE_1D_INDUSTRIAL`       | enable 1D industrial barcodes         | true    | boolean        |
| `KEY_DECODE_QR`                  | enable [QR codes][1]                  | true    | boolean        |
| `KEY_DECODE_DATA_MATRIX`         | enable decoding of [data matrices][2] | true    | boolean        |
| `KEY_DECODE_AZTEC`               | enable [Aztec codes][3]               | false   | boolean        |
| `KEY_DECODE_PDF417`              | enable decoding [PDF417 codes][4]     | false   | boolean        |
| `KEY_FRONT_LIGHT_MODE`           | front light mode: ON, OFF or AUTO     | OFF     | FrontLightMode |
| `KEY_AUTO_FOCUS`                 | enable auto-focus                     | true    | boolean        |
| `KEY_INVERT_SCAN`                | enable inversion of the scan          | false   | boolean        |
| `KEY_DISABLE_CONTINUOUS_FOCUS`   | disable continuous focus              | true    | boolean        |
| `KEY_DISABLE_EXPOSURE`           | disable exposure                      | true    | boolean        | 
| `KEY_DISABLE_METERING`           | disable metering                      | true    | boolean        | 
| `KEY_DISABLE_BARCODE_SCENE_MODE` | disable barcode scene mode            | true    | boolean        | 

[1]: http://en.wikipedia.org/wiki/QR_code
[2]: http://en.wikipedia.org/wiki/Data_matrix_(computer)
[3]: http://en.wikipedia.org/wiki/Aztec_Code
[4]: http://en.wikipedia.org/wiki/PDF417

To override defaults, you can set a custom properties provider.

```java
CapturePreferences.setProvider(new Provider() {
  @Override
  public Object getValue(String key) {
    if (CapturePreferences.KEY_DECODE_QR.equals(key)) {
      return false;
    } else if (CapturePreferences.KEY_DECODE_AZTEC.equals(key)) {
      return true;
    }
    return null;
  }
});
```

This is a simplistic example, in the real world you can either plug in a hash map with your values or implement any other retrieval flow.

If your custom provider is not set or does not return a value for any key, configuration will fall back to the defaults. As a result, only values differing to the defaults should be supplied by the custom provider.
