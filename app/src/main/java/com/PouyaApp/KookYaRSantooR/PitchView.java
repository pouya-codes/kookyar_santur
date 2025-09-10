package com.PouyaApp.KookYaRSantooR;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("DrawAllocation")
public class PitchView extends SurfaceView implements Runnable {
    private int kookPitch;
    private int kookType = 1;
    private String santurType;
    private int alpha = 0;
    private final int alphaChangeSpeed = 10;
    private int alphaCent = 0;
    private int alphaChangeSpeedCent = 0;
    private final int alphaChangeSpeedCentUnit = 10;
    private boolean firstCenter = true;
    private boolean draw = false;
    private boolean runned = false;
    private boolean fixed;
    private final float speed = 0.04f;
    private float lastDX = -1;
    private float centerPitch = 0, currentPitch, midiRef;
    private int width, height;
    private int laFrequnes;
    private final Paint paint = new Paint();
    private final Paint backgroundPaint = new Paint();
    private final Paint needlePaint = new Paint();
    private final Paint textPaint = new Paint();
    private final Paint arcPaint = new Paint();
    Thread thread;
    SurfaceHolder surfaceHolder;
    boolean isItOK = false;

    public void pause() {
        isItOK = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }

    }

    public void resume() {
        isItOK = true;
        thread = new Thread(this);
        thread.start();
    }

    public void setKookType(int kookType) {
        this.kookType = kookType;

    }

    public float getCurrentPitch() {
        return currentPitch;
    }

    @Override
    public void run() {
        try {

            while (isItOK == true) {
                if (!surfaceHolder.getSurface().isValid()) {
                    continue;

                }

                Canvas canvas = surfaceHolder.lockCanvas();
                draw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
                try {
                    if (centerPitch == 0) thread.sleep(100);
                    else thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
//            e.print

        }



    }

    private void init() {
        surfaceHolder = getHolder();
        isItOK = true;
        
        // Initialize paint objects with anti-aliasing
        paint.setAntiAlias(true);
        backgroundPaint.setAntiAlias(true);
        needlePaint.setAntiAlias(true);
        textPaint.setAntiAlias(true);
        arcPaint.setAntiAlias(true);
        
        // Set text paint properties
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setColor(Color.WHITE);
        
        // Set needle paint properties
        needlePaint.setStrokeWidth(8.0f);
        needlePaint.setStrokeCap(Paint.Cap.ROUND);
        
        // Set arc paint properties
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(6.0f);
    }

    public boolean getRunned() {
        return runned;
    }

    public void start() {
        thread = new Thread(this);
        thread.start();


    }

    public PitchView(Context context) {
        super(context);
        init();
        start();
    }

    public PitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        start();
    }


    public PitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        start();
    }

    public void setCenterPitch(float centerPitch) {

        alpha = 0;

        if (!firstCenter) {
            draw = true;
            this.centerPitch = centerPitch;

        }
        firstCenter = false;


    }

    public void setMidiRef(float midiRef) {
        this.midiRef = midiRef;
    }

    public float getCenterPitch() {
        return centerPitch;

    }

    public void setCurrentPitch(float currentPitch) {

        if (currentPitch <= 12) {
            alphaChangeSpeedCent = -alphaChangeSpeedCentUnit;

        } else {
            alphaChangeSpeedCent = alphaChangeSpeedCentUnit;
        }
        this.currentPitch = currentPitch;


    }

    public void setAFrequnse(int Frequnes) {
        this.laFrequnes = Frequnes;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h / 2, oldw, oldh);
        width = w;
        height = h;
    }


    public void draw(Canvas canvas) {
        super.draw(canvas);
        
        if (centerPitch == 0) {
            // Draw background
            backgroundPaint.setColor(getContext().getResources().getColor(R.color.backgroundColor));
            canvas.drawRect(0, 0, width, height, backgroundPaint);
            return;
        }

        // Draw gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 0, height,
                getContext().getResources().getColor(R.color.backgroundColor),
                Color.argb(255, 20, 20, 30), Shader.TileMode.CLAMP);
        backgroundPaint.setShader(gradient);
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        backgroundPaint.setShader(null);

        float halfWidth = width / 2f;
        float centerY = height * 0.75f;
        float radius = Math.min(width, height) * 0.3f;

        // Draw frequency and type info with better styling
        textPaint.setTextSize(width / 25f);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTextAlign(Align.LEFT);
        canvas.drawText(laFrequnes + " Hz", width / 20f, height / 15f, textPaint);
        textPaint.setTextAlign(Align.RIGHT);
        canvas.drawText(santurType != null ? santurType : "", width - width / 20f, height / 15f, textPaint);

        // Draw main tuning arc
        RectF arcRect = new RectF(halfWidth - radius, centerY - radius, halfWidth + radius, centerY + radius);
        
        // Draw background arc
        arcPaint.setColor(Color.argb(80, 255, 255, 255));
        canvas.drawArc(arcRect, 135, 270, false, arcPaint);

        // Draw tick marks for better precision
        drawTickMarks(canvas, halfWidth, centerY, radius);

        // Calculate needle position
        float dx = (currentPitch - centerPitch) / 2f;
        boolean inTune = (-1 < dx && dx < 1);
        
        // Draw status indicator
        drawStatusIndicator(canvas, dx, inTune);

        // Smooth needle movement
        if (dx > lastDX && (lastDX + speed) <= dx) {
            lastDX += speed;
            fixed = false;
        } else if (dx < lastDX && (lastDX - speed) >= dx) {
            lastDX -= speed;
            fixed = false;
        } else {
            lastDX = dx;
            fixed = true;
        }

        // Draw needle with improved design
        drawNeedle(canvas, halfWidth, centerY, radius, lastDX, inTune);

        // Draw center hub
        drawCenterHub(canvas, halfWidth, centerY, inTune && fixed);

        // Draw note information with fade-in effect
        drawNoteInfo(canvas, halfWidth, centerY - radius - 50);

        // Draw frequency and cent information
        drawFrequencyInfo(canvas, halfWidth, height / 6f);
    }

    private void drawTickMarks(Canvas canvas, float centerX, float centerY, float radius) {
        Paint tickPaint = new Paint();
        tickPaint.setAntiAlias(true);
        tickPaint.setColor(Color.WHITE);
        tickPaint.setStrokeWidth(2.0f);

        for (int i = -4; i <= 4; i++) {
            double angle = i * Math.PI / 8;
            float startRadius = radius * 0.85f;
            float endRadius = radius * (i % 2 == 0 ? 0.95f : 0.90f);
            
            float startX = centerX + (float) Math.sin(angle) * startRadius;
            float startY = centerY - (float) Math.cos(angle) * startRadius;
            float endX = centerX + (float) Math.sin(angle) * endRadius;
            float endY = centerY - (float) Math.cos(angle) * endRadius;
            
            tickPaint.setStrokeWidth(i == 0 ? 4.0f : 2.0f);
            tickPaint.setColor(i == 0 ? Color.GREEN : Color.WHITE);
            canvas.drawLine(startX, startY, endX, endY, tickPaint);
        }
    }

    private void drawStatusIndicator(Canvas canvas, float dx, boolean inTune) {
        textPaint.setTextSize(width / 22f);
        textPaint.setTextAlign(Align.CENTER);
        
        if (inTune) {
            textPaint.setColor(Color.GREEN);
            canvas.drawText("کوک شده", width / 2f, height * 0.9f, textPaint);
        } else {
            textPaint.setColor(Color.YELLOW);
            if (dx < -1) {
                if (currentPitch == 12) {
                    canvas.drawText("به سیم ضربه بزنید", width / 2f, height * 0.9f, textPaint);
                } else {
                    canvas.drawText("سیم را سفت کنید", width / 2f, height * 0.9f, textPaint);
                }
            } else {
                canvas.drawText("سیم را شل کنید", width / 2f, height * 0.9f, textPaint);
            }
        }
    }

    private void drawNeedle(Canvas canvas, float centerX, float centerY, float radius, float dx, boolean inTune) {
        dx = Math.max(-1, Math.min(1, dx)); // Clamp dx to [-1, 1]
        double phi = dx * Math.PI / 4;
        
        float needleLength = radius * 0.8f;
        float needleX = centerX + (float) Math.sin(phi) * needleLength;
        float needleY = centerY - (float) Math.cos(phi) * needleLength;
        
        // Draw needle shadow
        needlePaint.setColor(Color.argb(60, 0, 0, 0));
        canvas.drawLine(centerX + 2, centerY + 2, needleX + 2, needleY + 2, needlePaint);
        
        // Draw needle
        needlePaint.setColor(inTune ? Color.GREEN : Color.RED);
        canvas.drawLine(centerX, centerY, needleX, needleY, needlePaint);
    }

    private void drawCenterHub(Canvas canvas, float centerX, float centerY, boolean tuned) {
        float hubRadius = width / 40f;
        
        // Draw outer ring
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4.0f);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, hubRadius + 4, paint);
        
        // Draw center circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(tuned ? Color.GREEN : Color.YELLOW);
        canvas.drawCircle(centerX, centerY, hubRadius, paint);
    }

    private void drawNoteInfo(Canvas canvas, float centerX, float centerY) {
        if (alpha + alphaChangeSpeed <= 255) {
            alpha += alphaChangeSpeed;
        } else {
            alpha = 255;
        }
        
        textPaint.setAlpha(alpha);
        textPaint.setColor(getResources().getColor(R.color.green));
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTextSize(width / 18f);
        
        String noteName = (kookType != 1) ? drawName2() : drawName();
        canvas.drawText(noteName, centerX, centerY, textPaint);
        
        textPaint.setTextSize(width / 25f);
        textPaint.setColor(Color.CYAN);
        canvas.drawText(String.format("%.1f Hz", midiToFer(centerPitch)), centerX, centerY + 35, textPaint);
    }

    private void drawFrequencyInfo(Canvas canvas, float centerX, float centerY) {
        // Update cent alpha
        if (alphaChangeSpeedCent > 0) {
            alphaCent = Math.min(255, alphaCent + alphaChangeSpeedCent);
        } else if (alphaChangeSpeedCent < 0) {
            alphaCent = Math.max(0, alphaCent + alphaChangeSpeedCent);
        }

        textPaint.setAlpha(alphaCent);
        textPaint.setColor(Color.CYAN);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTextSize(width / 28f);
        
        float dif = (currentPitch - centerPitch) * 100;
        dif = Math.round(dif * 10) / 10f;
        String sign = (dif > 0) ? "+" : "";
        
        canvas.drawText(String.format("%.1f Hz / Cent: %s%.1f", 
            midiToFer(currentPitch), sign, dif), centerX, centerY, textPaint);
    }

    private float midiToFer(float midi) {
        double frequens = 440 * Math.pow(2, ((midi - 69) / 12));
        frequens = Math.round(frequens * 100) / 100.0;
        return (float) frequens;
    }

    private String drawName() {
        if (midiRef > 12) {
            String[] noteName = {"دو", "ر بمل", "ر", "می بمل", "می", "فا",
                    "سل بمل", "سل", "لا بمل", "لا", "سی بمل", "سی"};
            String[] positionName = {"اول", "دوم", "سوم", "چهارم", "پنجم",
                    "ششم", "هفتم"};
            float tone = midiRef % 1;
            float midi = (int) midiRef;
            int noteIndex = (int) (midi % 12);
            int position = (int) (midi / 12) - 2;
            String drawName;
            if (tone == 0) {
                drawName = noteName[noteIndex] + " اکتاو " + positionName[position];
            } else {
                if (noteIndex == 0 || noteIndex == 2 || noteIndex == 4
                        || noteIndex == 5 || noteIndex == 7
                        || noteIndex == 9 || noteIndex == 11) {
                    drawName = noteName[(noteIndex)] + " سری " + " اکتاو " + positionName[position];
                } else {
                    drawName = noteName[(noteIndex + 1)] + " کرن " + " اکتاو " + positionName[position];
                }
            }
            return drawName;
        } else {
            return "";
        }
    }

    private String drawName2() {
        String drawName = "";
        if (kookPitch == 1 || kookPitch == 10 || kookPitch == 19) drawName = "می کرن";
        else if (kookPitch == 2 || kookPitch == 11 || kookPitch == 20) drawName = "فا";
        else if (kookPitch == 3 || kookPitch == 12 || kookPitch == 21) drawName = "سل";
        else if (kookPitch == 4 || kookPitch == 13) drawName = "لا کرن";
        else if (kookPitch == 22) drawName = "لا بمل";
        else if (kookPitch == 5 || kookPitch == 14 || kookPitch == 23) drawName = "سی بمل";
        else if (kookPitch == 6 || kookPitch == 15 || kookPitch == 24) drawName = "دو";
        else if (kookPitch == 16 || kookPitch == 25) drawName = "ر";
        else if (kookPitch == 7) drawName = "ر کرن";
        else if (kookPitch == 8 || kookPitch == 17 || kookPitch == 26) drawName = "می بمل";
        else if (kookPitch == 9 || kookPitch == 18 || kookPitch == 27) drawName = "فا";
        else if (kookPitch == 0) drawName = "دو";
        
        String[] positionName = {"اول", "دوم", "سوم", "چهارم", "پنجم",
                "ششم", "هفتم"};
        float midi = (int) midiRef;
        int position = (int) (midi / 12) - 2;
        drawName += " اکتاو " + positionName[position];
        return drawName;
    }

    public void setKookPitch(int kookPitch) {
        this.kookPitch = kookPitch;
    }

    public void setSanturType(String santurType) {
        this.santurType = santurType;
    }
}