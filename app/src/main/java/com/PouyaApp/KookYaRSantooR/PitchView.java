package com.PouyaApp.KookYaRSantooR;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
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
        //     this.setZOrderOnTop(true);    // necessary
        surfaceHolder = getHolder();
        //   surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        isItOK = true;


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
//        Log.d("kookyar", "draw: " + centerPitch);
        if (centerPitch == 0) {
            paint.setColor(getContext().getResources().getColor(R.color.backgroundColor));
            canvas.drawRect(0, 0, width, height, paint);
        } else {
            paint.setColor(getContext().getResources().getColor(R.color.backgroundColor));
            canvas.drawRect(0, 0, width, height, paint);
            paint.setTextSize(width / 20);
            paint.setColor(Color.GRAY);
            canvas.drawText(laFrequnes + " Hz", width / 10, 25, paint);
            canvas.drawText(santurType, (width - width / 8), 30, paint);

            float halfWidth = width / 2;
            paint.setStrokeWidth(6.0f);
            paint.setColor(Color.WHITE);
            canvas.drawLine(halfWidth, 0, halfWidth, height, paint);


            double x1 = 1 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);
            x1 = -1 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);
            // ------------------------------------------
            x1 = 0.75 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);

            x1 = -0.75 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);
            // ------------------------------------------
            x1 = 0.5 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);

            x1 = -0.5 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);
            // ------------------------------------------
            x1 = 0.25 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);

            x1 = -0.25 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.90f,
                    height - (float) Math.cos(x1) * height * 0.90f, halfWidth
                            + (float) Math.sin(x1) * height * 1.00f, height
                            - (float) Math.cos(x1) * height * 1.00f, paint);


            float dx = (currentPitch - centerPitch) / 2;

            if (-1 < dx && dx < 1) {

                paint.setStrokeWidth(4.0f);
                paint.setColor(Color.GREEN);


            } else {
                paint.setColor(Color.RED);
                paint.setStrokeWidth(8.0f);

                if (dx < -1) {
                    if (currentPitch == 12) {
                        paint.setTextSize(width / 20);
                        paint.setColor(Color.YELLOW);
                        canvas.drawText("به سیم ضربه بزنید", width / 5, (height - height / 8), paint);
                    } else {
                        paint.setTextSize(width / 20);
                        paint.setColor(Color.YELLOW);
                        canvas.drawText("سیم را سفت کنید", width / 5, (height - height / 8), paint);

                    }
                } else {


                    paint.setTextSize(width / 20);
                    paint.setColor(Color.YELLOW);
                    canvas.drawText("سیم را شل کنید", width / 2 + width / 4 + width / 15, (height - height / 8), paint);


                }
                paint.setColor(Color.RED);
                dx = (dx < 0) ? -1 : 1;
            }

            dx = (float) Math.round(dx * 100) / 100;
            lastDX = (float) Math.round(lastDX * 100) / 100;

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

            double phi = lastDX * Math.PI / 4;
            canvas.drawLine(halfWidth, height - height / 20,
                    halfWidth + (float) Math.sin(phi) * height * 0.9f,
                    height - (float) Math.cos(phi) * height * 0.9f, paint);


            if (fixed && currentPitch != 12) {
                paint.setColor(Color.GREEN);
            } else {
                paint.setColor(Color.YELLOW);
            }
            canvas.drawCircle(width / 2, height - height / 20, height / 20, paint);
            paint.setColor(getResources().getColor(R.color.green));
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize(width / 20);
            if (alpha + alphaChangeSpeed <= 255) {
                alpha += alphaChangeSpeed;
            } else {
                alpha = 255;
            }
            paint.setAlpha(alpha);
            if (kookType != 1) {
                canvas.drawText(drawName2(), width / 2, height / 2, paint);
            } else
                canvas.drawText(drawName(), width / 2, height / 2, paint);

            canvas.drawText(midiToFer(centerPitch) + " Hz", width / 2, (height / 2 + 50), paint);


            paint.setColor(Color.CYAN);
            if (alphaChangeSpeedCent > 0) {
                if (alphaCent + alphaChangeSpeedCent <= 255) {
                    alphaCent += alphaChangeSpeedCent;
                } else {
                    alphaCent = 255;
                }
            }
            if (alphaChangeSpeedCent < 0) {
                if (alphaCent + alphaChangeSpeedCent >= 0) {
                    alphaCent += alphaChangeSpeedCent;
                } else {
                    alphaCent = 0;
                }
            }


            paint.setAlpha(alphaCent);

            float dif = (currentPitch - centerPitch) * 100;
            dif = (float) Math.round(dif * 10) / 10;
            String sign = (dif > 0) ? "+" : "";
            canvas.drawText(midiToFer(currentPitch) + " Hz/Cent: " + sign + dif, width / 2, height / 4, paint);


        }

    }

    private float midiToFer(float midi) {
        double frequens = 440 * Math.pow(2, ((midi - 69) / 12));
        frequens = (float) Math.round(frequens * 100) / 100;
        return (float) frequens;
    }

    private String drawName() {

        // TODO Auto-generated method stub
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
            if (tone == 0) drawName = noteName[noteIndex] + " اکتاو " + positionName[position];
            else {
                if (noteIndex == 0 || noteIndex == 2 || noteIndex == 4
                        || noteIndex == 5 || noteIndex == 7
                        || noteIndex == 9 || noteIndex == 11) {
                    drawName = noteName[(noteIndex)] + " سری " + " اکتاو " + positionName[position];
                } else {
                    drawName = noteName[(noteIndex + 1)] + " کرن " + " اکتاو " + positionName[position];
                }

            }
            return drawName;
        } else return "";

    }

    private String drawName2() {
        String drawName = "";
        // TODO Auto-generated method stub
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