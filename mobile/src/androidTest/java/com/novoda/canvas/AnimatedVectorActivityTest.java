package com.novoda.canvas;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageView;

import com.novoda.canvas.base.NovodaActivityTest;

import tyrantgit.explosionfield.ExplosionField;

import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

public class AnimatedVectorActivityTest extends NovodaActivityTest {

    enum Theme {
        BLUE(R.drawable.vector_animated_novoda_blue_logo, R.color.vector_white),
        WHITE(R.drawable.vector_animated_novoda_white_logo, R.color.vector_blue);

        private final int vectorDrawableRes;
        private final int backgroundColorRes;

        Theme(@DrawableRes int vectorDrawableRes, @ColorRes int backgroundColorRes) {
            this.vectorDrawableRes = vectorDrawableRes;
            this.backgroundColorRes = backgroundColorRes;
        }
    }

    public static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());
    public static final int INITIAL_DELAY_MILLIS = 2000;
    public static final int EXPLOSION_DELAY_MILLIS = 8500;

    private ExplosionField explosionField;
    private ImageView imageView;

    @Override
    public void startTestFor(Activity activity) {
        Theme theme = NovodaActivity.RANDOM.nextBoolean() ? Theme.BLUE : Theme.WHITE;

        setBackground(activity, theme);

        imageView = createImageView(activity, theme);
        getParent().addView(imageView);

        explosionField = ExplosionField.attach2Window(activity);

        delayInitialAnimation();
        delayExplosion();
    }

    private void setBackground(Activity activity, Theme theme) {
        @ColorInt int color = activity.getResources().getColor(theme.backgroundColorRes);
        ColorDrawable background = new ColorDrawable(color);
        getParent().setBackground(background);
    }

    private ImageView createImageView(Context context, Theme theme) {
        ImageView imageView = new ImageView(context);

        int padding = context.getResources().getDimensionPixelSize(R.dimen.image_padding);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(CENTER_INSIDE);

        AnimatedVectorDrawableCompat animatedVector = AnimatedVectorDrawableCompat.create(context, theme.vectorDrawableRes);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        imageView.setImageDrawable(animatedVector);
        animatedVector.start();
        return imageView;
    }

    private void delayInitialAnimation() {
        MAIN_THREAD.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        imageView.animate().start();
                    }
                }, INITIAL_DELAY_MILLIS
        );
    }

    private void delayExplosion() {
        MAIN_THREAD.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        explosionField.explode(imageView);
                    }
                }, EXPLOSION_DELAY_MILLIS
        );
    }

}
