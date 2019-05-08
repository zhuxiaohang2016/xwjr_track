package com.xwjr.track.attend.calendar;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

/**
 * 演示一个变态需求的周视图
 */

public class CustomWeekView extends WeekView {


    private int mRadius;

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();


    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();


    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();

    private float mSchemeBaseLine;

    public CustomWeekView(Context context) {
        super(context);
        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.STROKE);
        mCurrentDayPaint.setStrokeWidth(4f);
        mCurrentDayPaint.setColor(0xFFFFFFFF);


        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

//        //兼容硬件加速无效的代码
//        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
//        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));
    }


    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int height = mItemHeight - dipToPx(getContext(), 10f);
        int cx_start = cx - (mItemHeight - dipToPx(getContext(), 10f)) / 2;
        int cx_end = cx + (mItemHeight - dipToPx(getContext(), 10f)) / 2;
        canvas.drawRect(cx_start, 0, cx_end, height, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {

        //        boolean isSelected = isSelected(calendar);
        switch (calendar.getScheme()) {
            case "迟":
                mPointPaint.setColor(0xFFFF5050);
                break;
            case "退":
                mPointPaint.setColor(0xFFFF5050);
                break;
            case "忘":
                mPointPaint.setColor(0xFFFF5050);
                break;
            case "OK":
                mPointPaint.setColor(0xFFD3D3D3);
                break;
        }
//        if (isSelected) {
//            mPointPaint.setColor(Color.WHITE);
//        } else {
//            mPointPaint.setColor(Color.GRAY);
//        }

        canvas.drawCircle(x + mItemWidth / 2,  mItemHeight - dipToPx(getContext(), 10f) / 2 - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int offset = dipToPx(getContext(), 10f);
        int offsetHalf = dipToPx(getContext(), 10f) / 2;
        int height = mItemHeight - offset;
        int cx_start = cx - (mItemHeight - offset) / 2;
        int cx_end = cx + (mItemHeight - offset) / 2;

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawRect(cx_start, 0, cx_end, height, mCurrentDayPaint);
        }

//        if (hasScheme) {
//            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);
//
//            mTextPaint.setColor(calendar.getSchemeColor());
//
//            canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mCircleRadius, mPadding + mSchemeBaseLine, mTextPaint);
//        }

        //当然可以换成其它对应的画笔就不麻烦，
        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(0xFFffffff);
            mOtherMonthTextPaint.setColor(0xFFff0000);
        } else {
            mCurMonthTextPaint.setColor(0xffffffff);
            mOtherMonthTextPaint.setColor(0xFF74afff);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine - offsetHalf,
                    mSelectTextPaint);
//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        } else if (hasScheme) {

            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine - offsetHalf,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
//                    !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint : mSchemeLunarTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine - offsetHalf,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

//            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
//                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
//                            calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint :
//                                    mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
