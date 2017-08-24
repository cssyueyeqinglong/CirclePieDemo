package com.cy.circlepie.view;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.cy.circlepie.R;

/**
 * Created by Administrator on 2017/8/24.
 */

public class ExpandLongTextView extends AppCompatTextView {
    private String originText;
    private int initWidth = 0;
    private int mMaxLines = 6;

    public ExpandLongTextView(Context context) {
        super(context);
    }

    public ExpandLongTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandLongTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private SpannableString ELLIPSIS = null;
    private SpannableString EXPENDE = null;

    private void init() {
        String content = "显示全部";
        ELLIPSIS = new SpannableString(content);
        ButtonSpan span = new ButtonSpan(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMaxLines(Integer.MAX_VALUE);
//                setText(originText);
                setEclipsText(originText);
            }
        }, R.color.colorPrimary);
        ELLIPSIS.setSpan(span, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        String contentPart = "收起";
        EXPENDE = new SpannableString(contentPart);
        ButtonSpan spanPart = new ButtonSpan(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMaxLines(mMaxLines);
                setText(originText);
            }
        }, R.color.colorPrimary);
        EXPENDE.setSpan(spanPart, 0, contentPart.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.mMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    /**
     * 初始化TextView的宽度 * @param width
     */
    public void initWidth(int width) {
        initWidth = width;
    }

    public void setExpandText(CharSequence text) {
        if (null == ELLIPSIS) {
            init();
        }
        originText = text.toString();
        boolean appendShowAll = false;
        int maxLines = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            maxLines = getMaxLines();
        } else {
            maxLines = mMaxLines;
        }
        String workingText = new StringBuilder(originText).toString();
        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workingText);
            if (layout.getLineCount() > maxLines) {
                //获取一行显示字符个数，然后截取字符串数
                int i = maxLines - 1;
                int lineEnd = layout.getLineEnd(maxLines - 1);
                workingText = originText.substring(0, layout.getLineEnd(maxLines - 1)).trim() + "..." + ELLIPSIS;
                Layout layout2 = createWorkingLayout(workingText);
                while (layout2.getLineCount() > maxLines) {
                    int lastSpace = workingText.length() - 1;
                    if (lastSpace == -1) {
                        break;
                    }
                    workingText = workingText.substring(0, lastSpace);
                    layout2 = createWorkingLayout(workingText + "..." + ELLIPSIS);
                }
                appendShowAll = true;
                workingText = workingText + "...";
            }
        }
        setText(workingText);
        if (appendShowAll) {
            // 必须使用append，不能在上面使用+连接，否则spannable会无效
            append(ELLIPSIS);
            setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void setEclipsText(CharSequence text) {
        if (null == EXPENDE) {
            init();
        }
        String workingText = new StringBuilder(originText).toString();
        Layout layout = createWorkingLayout(workingText);
        int lineCount = layout.getLineCount();//获取lineNumber
        workingText = workingText + EXPENDE;
        Layout layout2 = createWorkingLayout(workingText);
        int i = -1;
        if (layout2.getLineCount() == lineCount) {
            while (layout2.getLineCount() == lineCount) {
                workingText += "说";
                layout2 = createWorkingLayout(workingText);
                i++;
            }
            setText(originText);
            String tab = null;
            for (int j = 0; j < i; j++) {
                tab += "说";
            }
            String s = tab.replaceAll(tab, " ");
            append(s);
        } else {
            int count = layout2.getLineCount();
            int lineEnd = layout2.getLineEnd(count - 1);
            char c = workingText.charAt(lineEnd - 1);
            if ('起' == c) {
                workingText = originText.trim();
                setText(workingText);
                append("\n");
            } else {
                originText = text.toString();
                workingText = originText.trim();
                setText(workingText);
            }
        }
        // 必须使用append，不能在上面使用+连接，否则spannable会无效
        append(EXPENDE);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    //返回textview的显示区域的layout，该textview的layout并不会显示出来，只是用其宽度来比较要显示的文字是否过长
    private Layout createWorkingLayout(String workingText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return new StaticLayout(workingText, getPaint(), initWidth - getPaddingLeft() - getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, getLineSpacingMultiplier(), getLineSpacingExtra(), false);
        } else {
            return new StaticLayout(workingText, getPaint(), initWidth - getPaddingLeft() - getPaddingRight(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }
    }

    class ButtonSpan extends ClickableSpan {

        View.OnClickListener onClickListener;
        private Context context;
        private int colorId;

        public ButtonSpan(Context context, View.OnClickListener onClickListener) {
            this(context, onClickListener, R.color.colorPrimaryDark);
        }

        public ButtonSpan(Context context, View.OnClickListener onClickListener, int colorId) {
            this.onClickListener = onClickListener;
            this.context = context;
            this.colorId = colorId;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(colorId));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (onClickListener != null) {
                onClickListener.onClick(widget);
            }
        }
    }
}
