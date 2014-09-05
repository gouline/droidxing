/*
 * Copyright (C) 2010 ZXing authors
 * Copyright (C) 2014 Mike Gouline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gouline.droidxing.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.google.zxing.client.android.camera.CameraConfigurationUtils;

import net.gouline.droidxing.CapturePreferences;

/**
 * A class which deals with reading, parsing, and setting the camera parameters which are used to
 * configure the camera hardware.
 */
final class CameraConfigurationManager {

    private static final String TAG = "CameraConfiguration";

    private final Context context;
    private Point screenResolution;
    private Point cameraResolution;
    private int screenRotation;

    CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * Reads, one time, values from the camera that are needed by the app.
     */
    void initFromCameraParameters(Camera camera, int screenRotation) {
        Camera.Parameters parameters = camera.getParameters();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Display display = manager.getDefaultDisplay();
        Point theScreenResolution = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            display.getSize(theScreenResolution);
        } else {
            theScreenResolution.set(display.getWidth(), display.getHeight());
        }
        screenResolution = theScreenResolution;
        Log.i(TAG, "Screen resolution: " + screenResolution);
        cameraResolution = CameraConfigurationUtils
                .findBestPreviewSizeValue(parameters, screenResolution);
        Log.i(TAG, "Camera resolution: " + cameraResolution);

        this.screenRotation = screenRotation;
    }

    void setDesiredCameraParameters(Camera camera, boolean safeMode) {
        Camera.Parameters parameters = camera.getParameters();
        Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

        if (safeMode) {
            Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
        }

        initializeTorch(parameters, safeMode);

        CameraConfigurationUtils.setFocus(
                parameters,
                CapturePreferences.getBoolean(CapturePreferences.KEY_AUTO_FOCUS),
                CapturePreferences.getBoolean(CapturePreferences.KEY_DISABLE_CONTINUOUS_FOCUS),
                safeMode);

        if (!safeMode) {
            if (CapturePreferences.getBoolean(CapturePreferences.KEY_INVERT_SCAN)) {
                CameraConfigurationUtils.setInvertColor(parameters);
            }

            if (!CapturePreferences.getBoolean(CapturePreferences.KEY_DISABLE_BARCODE_SCENE_MODE)) {
                CameraConfigurationUtils.setBarcodeSceneMode(parameters);
            }

            if (!CapturePreferences.getBoolean(CapturePreferences.KEY_DISABLE_METERING)) {
                CameraConfigurationUtils.setVideoStabilization(parameters);
                CameraConfigurationUtils.setFocusArea(parameters);
                CameraConfigurationUtils.setMetering(parameters);
            }
        }

        parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
        camera.setParameters(parameters);

        Camera.Parameters afterParameters = camera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
            Log.w(TAG,
                    "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +
                            ", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height
            );
            cameraResolution.x = afterSize.width;
            cameraResolution.y = afterSize.height;
        }
    }

    Point getCameraResolution() {
        return cameraResolution;
    }

    Point getScreenResolution() {
        return screenResolution;
    }

    int getScreenRotation() {
        return screenRotation;
    }

    boolean getTorchState(Camera camera) {
        if (camera != null) {
            String flashMode = camera.getParameters().getFlashMode();
            return flashMode != null &&
                    (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
                            Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
        }
        return false;
    }

    void setTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        doSetTorch(parameters, newSetting, false);
        camera.setParameters(parameters);
    }

    private void initializeTorch(Camera.Parameters parameters, boolean safeMode) {
        boolean currentSetting = FrontLightMode.readPref() == FrontLightMode.ON;
        doSetTorch(parameters, currentSetting, safeMode);
    }

    private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
        CameraConfigurationUtils.setTorch(parameters, newSetting);
        if (!safeMode && !CapturePreferences.getBoolean(CapturePreferences.KEY_DISABLE_EXPOSURE)) {
            CameraConfigurationUtils.setBestExposure(parameters, newSetting);
        }
    }
}
