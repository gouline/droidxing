/*
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

package net.gouline.droidxing.data;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;

import java.io.Serializable;

/**
 * Serializable container for raw results that can be passed as extras.
 *
 * @author Mike Gouline
 */
public class DroidXingResult implements Serializable {

    private final String text;
    private final byte[] rawBytes;
    private final float[][] resultPoints;
    private final String barcodeFormat;

    public DroidXingResult(Result rawResult) {
        this.text = rawResult.getText();
        this.rawBytes = rawResult.getRawBytes();
        this.resultPoints = serializeResultPoints(rawResult.getResultPoints());
        this.barcodeFormat = serializeBarcodeFormat(rawResult.getBarcodeFormat());
    }

    private static float[][] serializeResultPoints(ResultPoint[] points) {
        if (points == null) return null;

        final float[][] resultPoints = new float[points.length][2];
        for (int i = 0; i < points.length; ++i) {
            resultPoints[i][0] = points[i].getX();
            resultPoints[i][1] = points[i].getY();
        }
        return resultPoints;
    }

    private static ResultPoint[] deserializeResultPoints(float[][] points) {
        if (points == null) return null;

        final ResultPoint[] resultPoints = new ResultPoint[points.length];
        for (int i = 0; i < points.length; ++i) {
            resultPoints[i] = new ResultPoint(points[i][0], points[i][1]);
        }
        return resultPoints;
    }

    private static String serializeBarcodeFormat(BarcodeFormat format) {
        return format.toString();
    }

    private static BarcodeFormat deserializeBarcodeFormat(String format) {
        if (format != null) {
            for (BarcodeFormat currentFormat : BarcodeFormat.values()) {
                if (currentFormat.toString().equals(format)) {
                    return currentFormat;
                }
            }
        }
        return null;
    }

    public Result getRawResult() {
        return new Result(text, rawBytes,
                deserializeResultPoints(resultPoints),
                deserializeBarcodeFormat(barcodeFormat));
    }

    public ParsedResult getParsedResult() {
        final Result rawResult = getRawResult();
        if (rawResult != null) {
            return ResultParser.parseResult(rawResult);
        }
        return null;
    }

}
