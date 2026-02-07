package com.labsisouleimanedev.touchflip3d;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient; // لعمل اللمعان
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class RotateView2 extends FrameLayout {
    private final Matrix matrix = new Matrix();
    private final Camera camera = new Camera();
    private final Paint shadowPaint = new Paint();
    private final Paint glossyPaint = new Paint(); // Paint جديد لطبقة اللمعان

    private float rotationX = 0;
    private float rotationY = 0;
    private float lastTouchX;
    private float lastTouchY;
    private float startTouchX, startTouchY;

    private float touchDirectionY = 1.0f;
    private float touchDirectionX = 1.0f;

    private boolean isXEnabled, isYEnabled, autoRotate, showShadows, showGlossyEffect; // إضافة showGlossyEffect
    private int flipDirection;
    private boolean isAnimating = false;
    private ValueAnimator animator;

    public RotateView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateView2);
        try {
            isXEnabled = a.getBoolean(R.styleable.RotateView2_enableRotateX, true);
            isYEnabled = a.getBoolean(R.styleable.RotateView2_enableRotateY, true);
            flipDirection = a.getInt(R.styleable.RotateView2_flipDirection, 1);
            autoRotate = a.getBoolean(R.styleable.RotateView2_autoRotate, false);
            showShadows = a.getBoolean(R.styleable.RotateView2_showShadows, true);
            showGlossyEffect = a.getBoolean(R.styleable.RotateView2_showGlossyEffect, false); // القيمة الافتراضية "false"
        } finally {
            a.recycle();
        }

        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setClipChildren(false);
        setClipToPadding(false);

        // إعدادات الـ Paint الخاص باللمعان
        glossyPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN)); // يجعل الألوان تندمج بشكل إضاءة
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetRotation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) animator.cancel();
    }

    public void setAutoRotate(boolean autoRotate) {
        this.autoRotate = autoRotate;
    }

    public void setFlipDirection(int direction) {
        this.flipDirection = direction;
        resetRotation();
    }

    public void resetRotation() {
        rotationX = 0;
        rotationY = 0;
        isAnimating = false;
        invalidate();
    }
    // دالة لتفعيل أو تعطيل الدوران حول المحور الأفقي (X)
    public void setEnableRotateX(boolean enabled) {
        this.isXEnabled = enabled;
        invalidate(); // تحديث العرض
    }

    // دالة لتفعيل أو تعطيل الدوران حول المحور العمودي (Y)
    public void setEnableRotateY(boolean enabled) {
        this.isYEnabled = enabled;
        invalidate(); // تحديث العرض
    }

    // --- هذا الجزء لم يتغير في منطق الدوران ---
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildCount() < 2) {
            super.dispatchDraw(canvas);
            return;
        }

        View back = getChildAt(0);
        View front = getChildAt(1);

        float degY = ((rotationY % 360) + 360) % 360;
        float degX = ((rotationX % 360) + 360) % 360;

        boolean isBack = (flipDirection == 0) ? (degX > 90 && degX < 270) : (degY > 90 && degY < 270);

        back.setVisibility(isBack ? VISIBLE : GONE);
        front.setVisibility(isBack ? GONE : VISIBLE);

        canvas.save();
        applyTransformation(canvas);

        if (isBack) {
            canvas.save();
            if (flipDirection == 0) {
                canvas.scale(1, -1, getWidth() / 2f, getHeight() / 2f);
            } else {
                canvas.scale(-1, 1, getWidth() / 2f, getHeight() / 2f);
            }
            super.dispatchDraw(canvas);
            if (showShadows) drawShadowOverlay(canvas);
            // رسم اللمعان فوق الظل
            if (showGlossyEffect) drawGlossyEffect(canvas);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
            if (showShadows) drawShadowOverlay(canvas);
            // رسم اللمعان فوق الظل
            if (showGlossyEffect) drawGlossyEffect(canvas);
        }

        canvas.restore();
    }
    // --- نهاية الجزء الذي لم يتغير ---

    private void drawShadowOverlay(Canvas canvas) {
        float intensity = Math.abs((float)(Math.cos(Math.toRadians(rotationX)) * Math.cos(Math.toRadians(rotationY))));
        intensity = 0.35f + (intensity * 0.65f);
        int alpha = (int) (255 * (1.0f - intensity));

        shadowPaint.setColor(Color.BLACK);
        shadowPaint.setAlpha(alpha);
        canvas.drawRect(0, 0, getWidth(), getHeight(), shadowPaint);
    }

    private void drawGlossyEffect(Canvas canvas) {
        // حساب زاوية ميلان اللمعان بناءً على دوران Y
        float angle = (rotationY % 360 + 360) % 360; // بين 0 و 360
        float offset = getWidth() * (angle / 360f); // تحريك اللمعان من اليسار لليمين

        // تحديد بداية ونهاية التدرج اللوني (اللمعان)
        // يبدأ شفاف، ثم أبيض، ثم شفاف مرة أخرى
        LinearGradient gradient = new LinearGradient(
                0 + offset * 1.5f - getWidth() / 2f, // نقطة بداية X
                0,
                getWidth() + offset * 1.5f - getWidth() / 2f, // نقطة نهاية X
                getHeight(),
                new int[]{Color.TRANSPARENT, Color.argb(120, 255, 255, 255), Color.TRANSPARENT}, // أبيض شبه شفاف في المنتصف
                new float[]{0.0f, 0.5f, 1.0f}, // توزيع الألوان
                Shader.TileMode.CLAMP
        );
        glossyPaint.setShader(gradient);

        canvas.save();
        // نقل اللمعان لمركز البطاقة
        canvas.translate(0, 0);
        // تدوير اللمعان قليلاً ليعطي إحساساً بالميلان
        canvas.rotate(25, getWidth() / 2f, getHeight() / 2f);
        canvas.drawRect(0, 0, getWidth(), getHeight(), glossyPaint);
        canvas.restore();
    }


    private void applyTransformation(Canvas canvas) {
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float density = getResources().getDisplayMetrics().density;

        matrix.reset();
        camera.save();
        camera.setLocation(0, 0, -12 * density);
        camera.rotateX(rotationX);
        camera.rotateY(rotationY);
        camera.getMatrix(matrix);
        camera.restore();

        float[] mValues = new float[9];
        matrix.getValues(mValues);
        mValues[6] /= density;
        mValues[7] /= density;
        matrix.setValues(mValues);

        matrix.preTranslate(-centerX, -centerY);
        matrix.postScale(0.8f, 0.8f);
        matrix.postTranslate(centerX, centerY);

        canvas.concat(matrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (autoRotate) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startTouchX = event.getX();
                    startTouchY = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    float dist = (float) Math.hypot(event.getX() - startTouchX, event.getY() - startTouchY);
                    if (dist < 10 && !isAnimating) performAutoFlip();
                    return true;
            }
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                touchDirectionX = (((rotationX % 360) + 360) % 360 > 90 && ((rotationX % 360) + 360) % 360 < 270) ? -1.0f : 1.0f;
                touchDirectionY = (((rotationY % 360) + 360) % 360 > 90 && ((rotationY % 360) + 360) % 360 < 270) ? -1.0f : 1.0f;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - lastTouchX;
                float dy = event.getY() - lastTouchY;
                if (flipDirection == 1) {
                    if (isYEnabled) rotationY += dx * 0.5f;
                    if (isXEnabled) rotationX -= dy * 0.5f;
                } else {
                    if (isYEnabled) rotationY += dx * 0.5f * touchDirectionX;
                    if (isXEnabled) rotationX -= dy * 0.5f;
                }
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                invalidate();
                break;
        }
        return true;
    }

    private void performAutoFlip() {
        isAnimating = true;
        float start = (flipDirection == 1) ? rotationY : rotationX;
        float end = (Math.round(start / 180f) % 2 == 0) ? start + 180 : start - 180;

        animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            if (flipDirection == 1) rotationY = (float) animation.getAnimatedValue();
            else rotationX = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        animator.start();
    }
}