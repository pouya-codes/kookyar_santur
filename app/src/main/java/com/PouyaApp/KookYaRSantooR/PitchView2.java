package com.PouyaApp.KookYaRSantooR;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class PitchView2 extends View {
    double draw = -1;
    private float centerPitch, currentPitch, midiRef;
    private int width, height;
    private double AFrequnes;
    private String santurType;
    private final Paint paint = new Paint();
    private int kookType = 1;
    private int kookPitch;

    public PitchView2(Context context) {
        super(context);
    }

    public PitchView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PitchView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCenterPitch(float centerPitch) {
        this.centerPitch = centerPitch;
        invalidate();
    }

    public float getCenterPitch() {
        return centerPitch;

    }

    public void setCurrentPitch(float currentPitch) {
        this.currentPitch = currentPitch;
        draw = -1;
        invalidate();
    }

    public void setKookType(int kookType) {
        this.kookType = kookType;

    }

    public void setKookPitch(int kookPitch) {
        this.kookPitch = kookPitch;
    }

    public void setAFrequnse(double Frequnes) {
        this.AFrequnes = Frequnes;
        invalidate();
    }

    public void setMidiRef(float midiRef) {
        this.midiRef = midiRef;
        invalidate();
    }

    public void setSanturType(String santurType) {
        this.santurType = santurType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h / 2, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (centerPitch >= 12) {
            paint.setTextSize(width / 20);
            paint.setColor(getResources().getColor(R.color.gray));
            canvas.drawText(AFrequnes + " Hz", width / 10, 30, paint);
            canvas.drawText(santurType, (width - width / 8), 30, paint);

            float halfWidth = width / 2;
            paint.setStrokeWidth(6.0f);
            paint.setColor(Color.WHITE);
            canvas.drawLine(halfWidth, 0, halfWidth, height, paint);


            float dx = (currentPitch - centerPitch);
            dx = (float) Math.round(dx * 1000) / 1000;
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
                        canvas.drawText("به سیم ضربه بزنید", width / 4 - width / 16, (height - height / 8), paint);
                    } else {
                        paint.setTextSize(width / 20);
                        paint.setColor(Color.YELLOW);
                        canvas.drawText("سیم را سفت کنید", width / 4 - width / 16, (height - height / 8), paint);
//          		 icon = BitmapFactory.decodeResource(getResources(),R.drawable.seft2);
//           		 canvas.drawBitmap(icon, 0,0, paint);
                    }
                } else {


                    paint.setTextSize(width / 20);
                    paint.setColor(Color.YELLOW);

                    canvas.drawText("سیم را شل کنید", (width - width / 4), (height - height / 8), paint);
//          		 icon = BitmapFactory.decodeResource(getResources(),R.drawable.shol2);
//           		 canvas.drawBitmap(icon,width-icon.getWidth(),0, paint);

                }
                paint.setColor(Color.RED);
                dx = (dx < 0) ? -1 : 1;
            }
            double phi = dx * Math.PI / 4;
            canvas.drawLine(halfWidth, height - height / 20,
                    halfWidth + (float) Math.sin(phi) * height * 0.9f,
                    height - (float) Math.cos(phi) * height * 0.9f, paint);


            paint.setStrokeWidth(6.0f);
            paint.setColor(Color.WHITE);
            double x1 = 1 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.05f,
                    height - (float) Math.cos(x1) * height * 1.05f, paint);
            x1 = -1 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.05f,
                    height - (float) Math.cos(x1) * height * 1.05f, paint);
            //------------------------------------------
            x1 = 0.75 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.04f,
                    height - (float) Math.cos(x1) * height * 1.04f, paint);

            x1 = -0.75 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.04f,
                    height - (float) Math.cos(x1) * height * 1.04f, paint);
            //------------------------------------------
            x1 = 0.5 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.03f,
                    height - (float) Math.cos(x1) * height * 1.03f, paint);

            x1 = -0.5 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.03f,
                    height - (float) Math.cos(x1) * height * 1.03f, paint);
            //------------------------------------------
            x1 = 0.25 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.1f,
                    height - (float) Math.cos(x1) * height * 1.1f, paint);

            x1 = -0.25 * Math.PI / 4;
            canvas.drawLine(halfWidth + (float) Math.sin(x1) * height * 0.95f, height - (float) Math.cos(x1) * height * 0.95f,
                    halfWidth + (float) Math.sin(x1) * height * 1.1f,
                    height - (float) Math.cos(x1) * height * 1.1f, paint);


            paint.setColor(Color.YELLOW);
            canvas.drawCircle(width / 2, height - height / 20, height / 20, paint);


            paint.setColor(getResources().getColor(R.color.gray));
            paint.setTextAlign(Align.CENTER);
            paint.setTextSize(width / 20);
            if (kookType != 1) {
                canvas.drawText(drawName2(), width / 2, height / 2, paint);
            } else
                canvas.drawText(drawName(), width / 2, height / 2, paint);

        }

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

    private String drawName() {
        // TODO Auto-generated method stub
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
    }


}