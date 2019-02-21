package pk.horizontaldayslider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Pradeep Kumar on 27-Feb-17.
 */

public class HorizontalDaySlider extends HorizontalScrollView {

    Context context;
    private int dayLimits = 7;
    private int currentSelectedDayPosition = 0;

    private int SelectedBackgroundColor = Color.parseColor("#3F51B5");
    private int PressedBackgroundColor = Color.parseColor("#3F51B5");
    private int NormalBackgroundColor = Color.parseColor("#acacac");


    private int NormalTextColor = Color.parseColor("#FFFFFF");
    private int SelectedTextColor = Color.parseColor("#767676");


    public HorizontalDaySlider(Context context) {
        super(context);
        this.context = context;
        hideScrollbar();
        addHViews(currentSelectedDayPosition);
    }

    public HorizontalDaySlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        hideScrollbar();
        addHViews(0);

    }



    private void addHViews(int position) {

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearLayout.removeAllViews();

        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
        for (int i = 0; i < dayLimits; i++) {

            TextView dayText = new TextView(context);
            dayText.setLayoutParams(new LayoutParams((int) dp2px(context, 55), ViewGroup.LayoutParams.WRAP_CONTENT));
            dayText.setPadding(0, 5, 0, 5);
            dayText.setTextSize(20);
            dayText.setGravity(Gravity.CENTER);
            dayText.setBackgroundDrawable(btnSelector());

            dayText.setTextColor(setTextColorList());
            dayText.setClickable(true);

            dayText.setSelected(i == position);


            if (currentSelectedDayPosition != 0)
                calendar.add(Calendar.DATE, 1);

            Date date = calendar.getTime();
            String finalDaysString = sdf.format(date) + "\n" + calendar.get(Calendar.DAY_OF_MONTH)+ "\n" + calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            dayText.setText(finalDaysString);


            dayText.setTag(new String[]{String.valueOf(i), date.toString()});

            final int finalI = i;
            dayText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String string[] = (String[]) v.getTag();
                    addHViews(Integer.parseInt(string[0]));
                    currentSelectedDayPosition  = finalI;
                    dayListener.onDayClickListener(finalI, new Date(string[1]));
                }
            });

            linearLayout.addView(dayText);

        }

        this.removeAllViews();
        this.addView(linearLayout);
    }


    /**
     * hide scroll bars
     */
    private void hideScrollbar() {
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
    }


    /**
     * set number of days to be display
     *
     * @param dayLimits
     */
    public void setDayLimits(int dayLimits) {
        this.dayLimits = dayLimits;
        addHViews(currentSelectedDayPosition);
    }


    /**
     * set day listener
     *
     * @param dayListener
     */
    public void setDayListener(DayListener dayListener) {
        this.dayListener = dayListener;
        addHViews(currentSelectedDayPosition);
    }

    /**
     * set selected day
     *
     * @param currentSelectedDayPosition
     */
    public void setCurrentSelectedDayPosition(int currentSelectedDayPosition) {
        this.currentSelectedDayPosition = currentSelectedDayPosition;
        if (currentSelectedDayPosition > dayLimits)
            this.currentSelectedDayPosition = 0;
        addHViews(this.currentSelectedDayPosition);
    }

    /**
     * Showing in normal position of background color
     *
     * @param normalBackgroundColor
     */
    public void setNormalBackgroundColor(int normalBackgroundColor) {
        this.NormalBackgroundColor = normalBackgroundColor;
        addHViews(currentSelectedDayPosition);
    }

    /**
     * showing in normal poistion of textcolor
     *
     * @param normalTextColor
     */
    public void setNormalTextColor(int normalTextColor) {
        this.NormalTextColor = normalTextColor;
        addHViews(currentSelectedDayPosition);
    }

    /**
     * showing when pressed
     *
     * @param pressedBackgroundColor
     */
    public void setPressedBackgroundColor(int pressedBackgroundColor) {
        this.PressedBackgroundColor = pressedBackgroundColor;
        addHViews(currentSelectedDayPosition);
    }

    /**
     * showing when selected
     *
     * @param selectedBackgroundColor
     */
    public void setSelectedBackgroundColor(int selectedBackgroundColor) {
        this.SelectedBackgroundColor = selectedBackgroundColor;
        addHViews(currentSelectedDayPosition);
    }

    /**
     * showing when day is selected
     *
     * @param selectedTextColor
     */

    public void setSelectedTextColor(int selectedTextColor) {
        this.SelectedTextColor = selectedTextColor;
        addHViews(currentSelectedDayPosition);

    }


    public interface DayListener {
        void onDayClickListener(int position, Date date);
    }


    private DayListener dayListener = new DayListener() {
        @Override
        public void onDayClickListener(int position, Date date) {

        }
    };


    private StateListDrawable btnSelector() {
        StateListDrawable bg = new StateListDrawable();

        Drawable normal = cornerDrawable(NormalBackgroundColor);
        Drawable pressed = cornerDrawable(PressedBackgroundColor);
        Drawable selected = cornerDrawable(SelectedBackgroundColor);

        bg.addState(new int[]{android.R.attr.state_selected}, selected);
        bg.addState(new int[]{android.R.attr.state_pressed}, pressed);
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);

        return bg;
    }

    private static Drawable cornerDrawable(final int bgColor) {
        final GradientDrawable bg = new GradientDrawable();
        bg.setColor(bgColor);
        return bg;
    }

    public float dp2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private ColorStateList setTextColorList() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_enabled}
        };

        int[] colors = new int[]{
                NormalTextColor,
                NormalTextColor,
                SelectedTextColor
        };

        return new ColorStateList(states, colors);
    }

}
