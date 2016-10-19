package com.novoda.canvas;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.novoda.canvas.base.ColorEditor;
import com.novoda.canvas.base.NovodaActivityTest;
import com.novoda.canvas.base.RandomColorFactory;

import static com.novoda.canvas.NovodaActivity.RANDOM;

public class TilesSpiralActivityTest extends NovodaActivityTest {

    private static final int TILES_COUNT = 30;
    private static final int MIN_SIZE = 100;
    private static final int FADE_DURATION = 300;
    private static final int TILES_LEVEL_LIGHTEN_DELTA = 10;
    private static final int STROKE_COLOR_DARKEN_DELTA = 25;
    private static final int DARKER_BASE_COLOR_DARKEN_DELTA = 40;

    private final RandomColorFactory randomColorFactory = new RandomColorFactory(RANDOM);
    private final ColorEditor colorEditor = new ColorEditor();
    private Rect availableRect;

    @Override
    public void startTestFor(Activity activity) {
        ViewGroup parent = getParent();
        availableRect = new Rect(parent.getLeft(), parent.getTop(), parent.getRight(), parent.getBottom());
        int baseColor = colorEditor.darken(randomColorFactory.getColor(), DARKER_BASE_COLOR_DARKEN_DELTA);
        int strokeColor = colorEditor.darken(baseColor, STROKE_COLOR_DARKEN_DELTA);
        TileType type = TileType.BOTTOM;

        for (int i = 0; i < TILES_COUNT && screenNotFullAlready(); i++) {
            int tileColor = colorEditor.lighten(baseColor, TILES_LEVEL_LIGHTEN_DELTA * i);
            View tile = createTile(type, tileColor, strokeColor, activity);
            parent.addView(tile);
            revealTile(tile, FADE_DURATION * i);
            type = type.getNext();
        }
    }

    private boolean screenNotFullAlready() {
        return availableRect.width() > 0 && availableRect.height() > 0;
    }

    private View createTile(TileType type, @ColorInt int tileColor, @ColorInt int strokeColor, Context context) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, strokeColor);
        drawable.setColor(tileColor);

        int tileTop = type.getTop(availableRect);
        int tileLeft = type.getLeft(availableRect);
        int tileRight = type.getRight(availableRect);
        int tileBottom = type.getBottom(availableRect);

        View square = new View(context);
        square.setBackground(drawable);
        square.setLayoutParams(
                new LinearLayout.LayoutParams(
                        tileRight - tileLeft,
                        tileBottom - tileTop
                )
        );

        square.setY(tileTop);
        square.setX(tileLeft);
        square.setAlpha(0f);

        type.updateAvailableSpace(availableRect, tileLeft, tileTop, tileRight, tileBottom);

        return square;
    }

    private void revealTile(View square, int delay) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(square, "alpha", 0f, 1f);
        alphaAnimator.setDuration(FADE_DURATION);
        alphaAnimator.setStartDelay(delay);
        alphaAnimator.start();
    }

    private enum TileType {
        BOTTOM {
            @Override
            public int getTop(Rect availableRect) {
                return availableRect.bottom - getRandomHeight(availableRect);
            }

            @Override
            public void updateAvailableSpace(Rect availableRect, int tileLeft, int tileTop, int tileRight, int tileBottom) {
                availableRect.set(availableRect.left, availableRect.top, availableRect.right, tileTop);
            }
        },
        RIGHT {
            @Override
            public int getLeft(Rect availableRect) {
                return availableRect.right - getRandomWidth(availableRect);
            }

            @Override
            public void updateAvailableSpace(Rect availableRect, int tileLeft, int tileTop, int tileRight, int tileBottom) {
                availableRect.set(availableRect.left, availableRect.top, tileLeft, availableRect.bottom);
            }
        },
        TOP {
            @Override
            public int getBottom(Rect availableRect) {
                return availableRect.top + getRandomHeight(availableRect);
            }

            @Override
            public void updateAvailableSpace(Rect availableRect, int tileLeft, int tileTop, int tileRight, int tileBottom) {
                availableRect.set(availableRect.left, tileBottom, availableRect.right, availableRect.bottom);
            }
        },
        LEFT {
            @Override
            public int getRight(Rect availableRect) {
                return availableRect.left + getRandomWidth(availableRect);
            }

            @Override
            public void updateAvailableSpace(Rect availableRect, int tileLeft, int tileTop, int tileRight, int tileBottom) {
                availableRect.set(tileRight, availableRect.top, availableRect.right, availableRect.bottom);
            }
        };

        TileType getNext() {
            int nextIndex = (this.ordinal() + 1) % TileType.values().length;
            return TileType.values()[(nextIndex)];
        }

        public int getTop(Rect availableRect) {
            return availableRect.top;
        }

        public int getLeft(Rect availableRect) {
            return availableRect.left;
        }

        public int getRight(Rect availableRect) {
            return availableRect.right;
        }

        public int getBottom(Rect availableRect) {
            return availableRect.bottom;
        }

        private static int getRandomWidth(Rect availableRect) {
            return Math.min(
                    availableRect.width(),
                    Math.max(
                            MIN_SIZE,
                            RANDOM.nextInt(availableRect.width() / 3)
                    )
            );
        }

        private static int getRandomHeight(Rect availableRect) {
            return Math.min(
                    availableRect.height(),
                    Math.max(
                            MIN_SIZE,
                            RANDOM.nextInt(availableRect.height() / 3)
                    )
            );
        }

        public abstract void updateAvailableSpace(Rect availableRect, int tileLeft, int tileTop, int tileRight, int tileBottom);
    }
}
