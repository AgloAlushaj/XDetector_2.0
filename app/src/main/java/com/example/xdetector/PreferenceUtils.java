package com.example.xdetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase.DetectorMode;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

/** Utility class to retrieve shared preferences. */
public class PreferenceUtils {

    public static ObjectDetectorOptions getObjectDetectorOptionsForLivePreview(Context context) {
        return getObjectDetectorOptions(
                context,
                R.string.pref_key_live_preview_object_detector_enable_multiple_objects,
                R.string.pref_key_live_preview_object_detector_enable_classification,
                ObjectDetectorOptions.STREAM_MODE);
    }

    private static ObjectDetectorOptions getObjectDetectorOptions(
            Context context,
            @StringRes int prefKeyForMultipleObjects,
            @StringRes int prefKeyForClassification,
            @DetectorMode int mode) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean enableMultipleObjects =
                sharedPreferences.getBoolean(context.getString(prefKeyForMultipleObjects), false);
        boolean enableClassification =
                sharedPreferences.getBoolean(context.getString(prefKeyForClassification), true);

        ObjectDetectorOptions.Builder builder =
                new ObjectDetectorOptions.Builder().setDetectorMode(mode);
        if (enableMultipleObjects) {
            builder.enableMultipleObjects();
        }
        if (enableClassification) {
            builder.enableClassification();
        }
        return builder.build();
    }

    @Nullable
    public static android.util.Size getCameraXTargetResolution(Context context) {
        String prefKey = context.getString(R.string.pref_key_camerax_rear_camera_target_resolution);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return android.util.Size.parseSize(sharedPreferences.getString(prefKey, null));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean shouldHideDetectionInfo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(R.string.pref_key_info_hide);
        return sharedPreferences.getBoolean(prefKey, false);
    }

    public static boolean isCameraLiveViewportEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String prefKey = context.getString(R.string.pref_key_camera_live_viewport);
        return sharedPreferences.getBoolean(prefKey, false);
    }

}

