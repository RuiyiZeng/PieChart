import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import zeng.ruiyi.com.apphtml.util.ConvertUtil;

/**
 * Created by RuiyiZeng on 2018/1/15.
 * 支持背景的更改
 * 支持动态的设置比例份额、颜色、标题
 * 注意：标题数据，颜色数据，比例数据的数组长度要统一
 */

public class PieChart extends View {
    private String TAG = "PieChart";
    private float width;
    private float height;
    private float radius;
    private String[] titles;
    private Float[] datas;
    private int[] pieColor;
    private int bgColor;
    private Paint piePaint;
    private Paint txtPaint;
    //折线长度
    int offset2;
    //折线后的横线长
    int offset3;
    //文字与横线之间的距离
    int offset4;

    public PieChart(Context context) {
        super(context);
        init(context);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        titles = new String[]{"Hua", "Mi", "iphone", "Oppo", "Vivo", "PRO"};
        datas = new Float[]{36.0f, 12.0f, 10.0f, 18.0f, 5.0f, 19.0f};
        bgColor = Color.parseColor("#506E79");
        pieColor = new int[6];
        pieColor[0] = Color.parseColor("#FF443D");
        pieColor[1] = Color.parseColor("#FFC236");
        pieColor[2] = Color.parseColor("#9C2EA9");
        pieColor[3] = Color.parseColor("#9B9EA5");
        pieColor[4] = Color.parseColor("#029688");
        pieColor[5] = Color.parseColor("#0095EF");

        piePaint = new Paint();
        piePaint.setAntiAlias(true);

        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setColor(Color.WHITE);
        txtPaint.setStrokeWidth(1);


       offset2 = ConvertUtil.dip2px(getContext(), 6);
       offset3=ConvertUtil.dip2px(getContext(),20);
       offset4=ConvertUtil.dip2px(getContext(),1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (wideMode != MeasureSpec.EXACTLY) {
            wideSize = ConvertUtil.dip2px(getContext(), 250);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = ConvertUtil.dip2px(getContext(), 250);
        }
        setMeasuredDimension(wideSize, heightSize);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        float radTemp = width < height ? width : height;
        radius = radTemp / 2 * (0.8f);
        txtPaint.setTextSize(radius / 18);
        float centerX = width / 2;
        float centerY = height / 2;
        float titleLen;
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        ////
        canvas.drawColor(bgColor);


        float degreeStart = 180.0f;
        for (int i = 0; i < datas.length; i++) {
            //绘制饼图
            piePaint.setColor(pieColor[i]);
            int offset = ConvertUtil.dip2px(getContext(), 4);

            if (i == 0) {
                RectF rectF1 = new RectF(centerX - radius-1, centerY - radius - offset,
                        centerX-1 + radius, centerY - offset + radius);
                canvas.drawArc(rectF1, degreeStart,
                        (float) (datas[i] / 100 * 360), true, piePaint);

            } else {
                canvas.drawArc(rectF, degreeStart, (float) (datas[i] / 100 * 360), true, piePaint);
            }
            /***************************************************折线和 绘制文字****************************************/
            //折线的起点：
            float startX = centerX +(float) Math.cos((degreeStart/180+datas[i] / 100 )* Math.PI) * radius ;
            float startY = centerY +(float) Math.sin((degreeStart/180+datas[i] / 100 )* Math.PI) * radius ;
            if(i==0){
                startX=startX-4;
                startY-=4;
            }
            txtPaint.setTextSize(10);
            titleLen = txtPaint.measureText(titles[i]);

            if (degreeStart +  (datas[i] / 100 * 180) <= 270 ) { //第二象限角
                Log.d(TAG,titles[i]+"：   "+i);
                Log.d(TAG, "第二象限角 "  );
                Log.d(TAG,"===========================================");
                canvas.drawLine(startX, startY, startX - offset2, startY - offset2, txtPaint);
                canvas.drawLine(startX - offset2, startY - offset2, startX - offset2 - offset3, startY - offset2, txtPaint);
                canvas.drawText(titles[i], startX - offset2 - offset3 - titleLen-offset4, startY - offset2 + 5, txtPaint);

            }else if( degreeStart +  (datas[i] / 100 * 180) > 450){    //第三象限角

                canvas.drawLine(startX, startY, startX - offset2, startY +offset2, txtPaint);
                canvas.drawLine(startX - offset2, startY +offset2, startX - offset2 - offset3, startY +offset2, txtPaint);
                txtPaint.setTextSize(10);
                titleLen = txtPaint.measureText(titles[i]);
                canvas.drawText(titles[i], startX - offset2 - offset3 - titleLen-offset4, startY + offset2 + 5, txtPaint);
            }else if(degreeStart +  (datas[i] / 100 * 180) > 270&&degreeStart +  (datas[i] / 100 * 180) <=360){ //第一象限
                Log.d(TAG,titles[i]+"：   "+i);
                Log.d(TAG, "第一象限角 "  );
                Log.d(TAG,"===========================================");

                canvas.drawLine(startX, startY, startX + offset2, startY -offset2, txtPaint);
                canvas.drawLine(startX+ offset2, startY -offset2, startX + offset2 + offset3, startY -offset2, txtPaint);
                txtPaint.setTextSize(10);
                canvas.drawText(titles[i], startX + offset2 + offset3+offset4 , startY - offset2 + 5, txtPaint);
            }
            else {
                Log.d(TAG,titles[i]+"：   "+i);
                Log.d(TAG, "第四象限角 "  );
                Log.d(TAG,"===========================================");

                canvas.drawLine(startX, startY, startX + offset2, startY +offset2, txtPaint);
                canvas.drawLine(startX+ offset2, startY +offset2, startX + offset2 + offset3, startY +offset2, txtPaint);
                txtPaint.setTextSize(10);
                canvas.drawText(titles[i], startX + offset2 + offset3+offset4 , startY + offset2 + 5, txtPaint);

            }

            degreeStart += (float) (datas[i] / 100 * 360);

        }

    }



    public void setTitles(String[] titles) {
        this.titles = titles;
        postInvalidate();
    }

    public void setDatas(Float[] datas) {
        this.datas = datas;
        postInvalidate();
    }

    public void setPieColor(int[] pieColor) {
        this.pieColor = pieColor;
        postInvalidate();
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        postInvalidate();
    }
}
