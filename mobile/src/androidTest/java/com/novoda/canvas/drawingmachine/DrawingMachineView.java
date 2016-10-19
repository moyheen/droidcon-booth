package com.novoda.canvas.drawingmachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.TextureView;
import android.view.ViewGroup;

public class DrawingMachineView extends TextureView {

    private static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    private Canvas canvas;
    private Bitmap bitmap;

    public static DrawingMachineView createForDrawingOn(ViewGroup parent) {
        DrawingMachineView drawingMachineView = new DrawingMachineView(parent.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        parent.addView(drawingMachineView, layoutParams);
        return drawingMachineView;
    }

    private DrawingMachineView(Context context) {
        super(context);
    }

    public Canvas getCanvas() {
        checkForCanvas();
        return canvas;
    }

    private void checkForCanvas() {
        if (canvas == null) {
            createCanvas(getWidth(), getHeight(), Color.WHITE);
        }
    }

    private void createCanvas(int width, int height, @ColorInt int backgroundColour) {
        if (bitmap != null) {
            bitmap.recycle();
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(backgroundColour);
    }

    public Bitmap getBitmap() {
        checkForCanvas();
        return bitmap;
    }

}
