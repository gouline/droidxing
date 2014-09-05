/*
 * Copyright (C) 2012 ZXing authors
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

package net.gouline.droidxing.camera.open;

import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;

public final class OpenCameraInterface {

    private static final String TAG = OpenCameraInterface.class.getName();

    private OpenCameraInterface() {
    }


    /**
     * Opens the requested camera with {@link Camera#open(int)}, if one exists.
     *
     * @param cameraId       camera ID of the camera to use. A negative value means "no preference"
     * @param screenRotation Current screen rotation.
     * @return handle to {@link Camera} that was opened
     */
    public static Camera open(int cameraId, int screenRotation) {

        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            Log.w(TAG, "No cameras!");
            return null;
        }

        boolean explicitRequest = cameraId >= 0;

        if (!explicitRequest) {
            // Select a camera if no explicit camera requested
            int index = 0;
            while (index < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }

            cameraId = index;
        }

        Camera camera;
        if (cameraId < numCameras) {
            Log.i(TAG, "Opening camera #" + cameraId);
            camera = Camera.open(cameraId);

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, cameraInfo);
            setCameraOrientation(camera, cameraInfo, screenRotation);
        } else {
            if (explicitRequest) {
                Log.w(TAG, "Requested camera does not exist: " + cameraId);
                camera = null;
            } else {
                Log.i(TAG, "No camera facing back; returning camera #0");
                camera = Camera.open(0);
            }
        }

        return camera;
    }


    /**
     * Opens a rear-facing camera with {@link Camera#open(int)}, if one exists, or opens camera 0.
     *
     * @param screenRotation Current screen rotation.
     * @return handle to {@link Camera} that was opened
     */
    public static Camera open(int screenRotation) {
        return open(-1, screenRotation);
    }

    /**
     * Adjust camera orientation to the current rotation.
     * http://stackoverflow.com/a/10218309/818393
     *
     * @param camera         Camera instance.
     * @param info           Camera info.
     * @param screenRotation Current screen rotation.
     */
    private static void setCameraOrientation(Camera camera, Camera.CameraInfo info, int screenRotation) {
        int degrees = 0;
        switch (screenRotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        final int orientation;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            orientation = 360 - (info.orientation + degrees) % 360;  // compensate the mirror
        } else { // back-facing
            orientation = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(orientation);
    }
}
