package com.gkvk.patangasuchaka.bean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.util.Random;

@SuppressLint("AppCompatCustomView")
public class CaptchaImageView extends ImageView {
    private CaptchaImageView.CaptchaGenerator.Captcha generatedCaptcha;
    private int captchaLength = 6;
    private int captchaType = 2;
    private int width;
    private int height;
    private boolean isDot;
    private boolean isRedraw;

    public CaptchaImageView(Context context) {
        super(context);
    }

    public CaptchaImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void draw(int width, int height) {
        this.generatedCaptcha = CaptchaImageView.CaptchaGenerator.regenerate(width, height, this.captchaLength, this.captchaType, this.isDot);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.width = this.getMeasuredWidth();
        this.height = this.getMeasuredHeight();
    }

    public Bitmap getCaptchaBitmap() {
        return this.generatedCaptcha.getBitmap();
    }

    public String getCaptchaCode() {
        return this.generatedCaptcha.getCaptchaCode();
    }

    public void regenerate() {
        this.reDraw();
    }

    public void setCaptchaType(int type) {
        this.captchaType = type;
    }

    public void setCaptchaLength(int length) {
        this.captchaLength = length;
    }

    private void reDraw() {
        this.draw(this.width, this.height);
        this.setImageBitmap(this.generatedCaptcha.getBitmap());
    }

    public void setIsDotNeeded(boolean isNeeded) {
        this.isDot = isNeeded;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!this.isRedraw) {
            this.reDraw();
            this.isRedraw = true;
        }

    }

    public static class CaptchaGenerator {
        public static final int ALPHABETS = 1;
        public static final int NUMBERS = 2;
        public static final int BOTH = 3;

        public CaptchaGenerator() {
        }

        private static CaptchaImageView.CaptchaGenerator.Captcha regenerate(int width, int height, int length, int type, boolean isDot) {
            Paint border = new Paint();
            border.setStyle(Style.STROKE);
            border.setColor(Color.parseColor("#FFFFFF"));
            Paint paint = new Paint();
            //paint.setColor(-16777216);
            paint.setColor(Color.parseColor("#FFFFFF"));
            paint.setStyle(Style.FILL_AND_STROKE);
            if (isDot) {
                paint.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                paint.setTypeface(Typeface.MONOSPACE);
            }

            Bitmap bitMap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitMap);
            canvas.drawColor(Color.parseColor("#3C3837"));
            int textX = generateRandomInt(width - width / 5 * 4, width / 2);
            int textY = generateRandomInt(height - height / 3, height - height / 4);
            String generatedText = drawRandomText(canvas, paint, textX, textY, length, type, isDot);
            if (isDot) {
                canvas.drawLine((float)textX, (float)(textY - generateRandomInt(7, 10)), (float)(textX + length * 33), (float)(textY - generateRandomInt(5, 10)), paint);
                canvas.drawLine((float)textX, (float)(textY - generateRandomInt(7, 10)), (float)(textX + length * 33), (float)(textY - generateRandomInt(5, 10)), paint);
            } else {
                canvas.drawLine((float)textX, (float)(textY - generateRandomInt(7, 10)), (float)(textX + length * 23), (float)(textY - generateRandomInt(5, 10)), paint);
                canvas.drawLine((float)textX, (float)(textY - generateRandomInt(7, 10)), (float)(textX + length * 23), (float)(textY - generateRandomInt(5, 10)), paint);
            }

            canvas.drawRect(0.0F, 0.0F, (float)(width - 1), (float)(height - 1), border);
            if (isDot) {
                makeDots(bitMap, width, height, textX, textY);
            }

            return new CaptchaImageView.CaptchaGenerator.Captcha(generatedText, bitMap);
        }

        private static void makeDots(Bitmap bitMap, int width, int height, int textX, int textY) {
            int white = -526337;
            int black = -16777216;
            int grey = -3355444;
            Random random = new Random();

            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    int pixel = bitMap.getPixel(x, y);
                    if (pixel == white) {
                        pixel = random.nextBoolean() ? black : white;
                    }

                    bitMap.setPixel(x, y, pixel);
                }
            }

        }

        private static String drawRandomText(Canvas canvas, Paint paint, int textX, int textY, int length, int type, boolean isDot) {
            String generatedCaptcha = "";
            int[] scewRange = new int[]{-1, 1};
            int[] textSizeRange = new int[]{70, 72, 74, 75};
            Random random = new Random();
            paint.setTextSkewX((float)scewRange[random.nextInt(scewRange.length)]);

            for(int index = 0; index < length; ++index) {
                String temp = generateRandomText(type);
                generatedCaptcha = generatedCaptcha + temp;
                paint.setTextSize((float)textSizeRange[random.nextInt(textSizeRange.length)]);
                if (isDot) {
                    canvas.drawText(temp, (float)(textX + index * 45), (float)textY, paint);
                } else {
                    canvas.drawText(temp, (float)(textX + index * 40), (float)textY, paint);
                }
            }

            return generatedCaptcha;
        }

        private static String generateRandomText(int type) {
            String[] numbers = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            String[] alphabets = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
            Random random = new Random();
            Random mixedRandom = new Random();
            String temp;
            if (type == 1) {
                temp = alphabets[random.nextInt(alphabets.length)];
            } else if (type == 2) {
                temp = numbers[random.nextInt(numbers.length)];
            } else {
                temp = mixedRandom.nextBoolean() ? alphabets[random.nextInt(alphabets.length)] : numbers[random.nextInt(numbers.length)];
            }

            return temp;
        }

        private static int generateRandomInt(int length) {
            Random random = new Random();
            int ran = random.nextInt(length);
            return ran == 0 ? random.nextInt(length) : ran;
        }

        private static int generateRandomInt(int min, int max) {
            Random rand = new Random();
            return rand.nextInt(max - min + 1) + min;
        }

        private static class Captcha {
            private String captchaCode;
            private Bitmap bitmap;

            Captcha(String captchaCode, Bitmap bitmap) {
                this.captchaCode = captchaCode;
                this.bitmap = bitmap;
            }

            String getCaptchaCode() {
                return this.captchaCode;
            }

            public void setCaptchaCode(String captchaCode) {
                this.captchaCode = captchaCode;
            }

            Bitmap getBitmap() {
                return this.bitmap;
            }

            public void setBitmap(Bitmap bitmap) {
                this.bitmap = bitmap;
            }
        }
    }
}
