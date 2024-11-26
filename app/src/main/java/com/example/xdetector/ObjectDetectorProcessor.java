package com.example.xdetector;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase;
import java.util.List;

/** A processor to run object detector. */
public class ObjectDetectorProcessor extends VisionProcessorBase<List<DetectedObject>> {

    private static final String TAG = "ObjectDetectorProcessor";

    private final ObjectDetector detector;

    public ObjectDetectorProcessor(Context context, ObjectDetectorOptionsBase options) {
        super(context);
        detector = ObjectDetection.getClient(options);
    }

    @Override
    public void stop() {
        super.stop();
        detector.close();
    }

    @Override
    protected Task<List<DetectedObject>> detectInImage(InputImage image) {
        return detector.process(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<DetectedObject> results, @NonNull GraphicOverlay graphicOverlay) {
        for (DetectedObject object : results) {
            graphicOverlay.add(new ObjectGraphic(graphicOverlay, object, rectF -> {
                //if (message != null && !message.isEmpty()) {
                    // Mostra il messaggio tramite Toast
                    //Toast.makeText(graphicOverlay.getContext(), message, Toast.LENGTH_SHORT).show();
                //}
                // Controlla il contenuto del messaggio e attiva un'azione
                //if (message.contains("Macchinetta caffè")) {
                    //showBlueAlert(graphicOverlay.getContext());
                //}
                showBlueAlert(graphicOverlay.getContext(), rectF);
            }, graphicOverlay.getContext()));
        }
    }

    private void showBlueAlert(Context context, RectF rectF) {
        // Crea un pulsante di avviso
        Button alertButton = new Button(context);
        //alertButton.setText("⚠️");
        //alertButton.setBackgroundColor(Color.TRANSPARENT);
        alertButton.setBackgroundResource(R.drawable.alert32);
        // alertButton.setTextColor(Color.WHITE); // Per impostare il colore del testo, se necessario
        alertButton.setTag("blue_alert_button");

        // Verifica che il contesto sia un'istanza di Activity
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            FrameLayout rootLayout = activity.findViewById(android.R.id.content);

            // Configura i parametri di layout per posizionare il pulsante
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );

            for (int i = 0; i < rootLayout.getChildCount(); i++) {
                View child = rootLayout.getChildAt(i);
                if (child.getTag() != null && child.getTag().equals("blue_alert_button")) {
                    Log.d("showBlueAlert", "Pulsante già presente.");
                    return;
                    //rootLayout.removeView(child);
                }
            }

            // Imposta i margini basati su rectF
            params.setMargins(
                    (int) rectF.left - 250,
                    (int) rectF.top,
                    (int) rectF.left - 150,
                    0
            );

            alertButton.setLayoutParams(params);
            rootLayout.addView(alertButton);

            // Imposta l'azione al clic sul pulsante
            alertButton.setOnClickListener(v -> {
                // Mostra un messaggio discreto con uno Snackbar posizionato in alto
                Snackbar snackbar = Snackbar.make(rootLayout, "Issue detected: filter to change", Snackbar.LENGTH_LONG)
                        .setAction("OK", null);

                // Posiziona lo Snackbar in alto
                View snackbarView = snackbar.getView();
                FrameLayout.LayoutParams snackbarParams = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                snackbarParams.gravity = Gravity.TOP;
                snackbarView.setLayoutParams(snackbarParams);

                snackbar.show();

                // Rimuove il pulsante dopo il clic
                rootLayout.removeView(alertButton);
            });
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Object detection failed!", e);
    }
}
