package com.wayki.wallet.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ImageEditText extends AppCompatEditText {
    private Context mContext;
    private String TypefaceName = "";

    public String getTypefaceName() {
        return TypefaceName;
    }

    public void setTypefaceName(String typefaceName) {
        TypefaceName = typefaceName;
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + TypefaceName + ".TTF");
        this.setTypeface(typeface);
        System.gc();
    }

    /**
     * EditText右侧的图标
     */
    protected Drawable mRightDrawable;

    private RightPicOnclickListener rightPicOnclickListener;

    public ImageEditText(Context context) {
        super(context);
        init(context);
        this.mContext = context;
        // TypefaceName = attrs.getAttributeValue(null, "TypefaceName");
        if (TypefaceName != null && !"".equals(TypefaceName)) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + TypefaceName + ".TTF");
            this.setTypeface(typeface);
        }
    }

    public ImageEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.mContext = context;
        // 先判断是否配置的资源文件
        int resouceId = attrs.getAttributeResourceValue(null, "typefaceName", 0);
        if (resouceId != 0) {
            TypefaceName = context.getResources().getString(resouceId);
        } else {
            TypefaceName = attrs.getAttributeValue(null, "typefaceName");
        }
        if (TypefaceName != null && !"".equals(TypefaceName)) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + TypefaceName + ".TTF");
            this.setTypeface(typeface);
        }
    }

    public ImageEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        this.mContext = context;
        int resouceId = attrs.getAttributeResourceValue(null, "typefaceName", 0);
        if (resouceId != 0) {
            TypefaceName = context.getResources().getString(resouceId);
        } else {
            TypefaceName = attrs.getAttributeValue(null, "typefaceName");
        }
        if (TypefaceName != null && !"".equals(TypefaceName)) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + TypefaceName + ".TTF");
            this.setTypeface(typeface);
        }
    }

    private void init(final Context context) {
        mRightDrawable = getCompoundDrawables()[2];

        if (mRightDrawable == null) {
            return;
        }

        mRightDrawable.setBounds(0, 0, mRightDrawable.getIntrinsicWidth(), mRightDrawable.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {

                    //设置点击EditText右侧图标EditText失去焦点，
                    // 防止点击EditText右侧图标EditText获得焦点，软键盘弹出
                    setFocusableInTouchMode(false);
                    setFocusable(false);

                    //点击EditText右侧图标事件接口回调
                    if (rightPicOnclickListener != null) {
                        rightPicOnclickListener.rightPicClick();
                    }
                } else {
                    //设置点击EditText输入区域，EditText请求焦点，软键盘弹出，EditText可编辑
                    setFocusableInTouchMode(true);
                    setFocusable(true);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setRightPicOnclickListener(RightPicOnclickListener rightPicOnclickListener) {
        this.rightPicOnclickListener = rightPicOnclickListener;
    }

    public interface RightPicOnclickListener {
        void rightPicClick();
    }

}

