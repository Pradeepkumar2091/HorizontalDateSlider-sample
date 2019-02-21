package pk.dateslider.dateslider;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import pk.dateslider.R;



/**
 * A Dialog subclass that hosts a SliderContainer and a couple of buttons,
 * displays the current time in the header, and notifies an observer
 * when the user selectes a time.
 */
public class DateSlider extends Dialog {

//	private static String TAG = "DATESLIDER";

    protected OnDateSetListener onDateSetListener;
    protected Calendar mInitialTime, minTime, maxTime;
    protected int mLayoutID;
    protected TextView mTitleText;
    protected SliderContainer mContainer;
    protected int minuteInterval;


    public DateSlider(Context context, int layoutID, OnDateSetListener l, Calendar initialTime) {
    	this(context,layoutID,l,initialTime, null, null, 1);
    }

    public DateSlider(Context context, int layoutID, OnDateSetListener l, Calendar initialTime, int minInterval) {
    	this(context,layoutID,l,initialTime, null, null, minInterval);
    }

    public DateSlider(Context context, int layoutID, OnDateSetListener l,
            Calendar initialTime, Calendar minTime, Calendar maxTime) {
    	this(context,layoutID,l,initialTime, minTime, maxTime, 1);
    }

    public DateSlider(Context context, int layoutID, OnDateSetListener l,
            Calendar initialTime, Calendar minTime, Calendar maxTime, int minInterval) {
        super(context);
        this.onDateSetListener = l;
        this.minTime = minTime; this.maxTime = maxTime;
        mInitialTime = Calendar.getInstance(initialTime.getTimeZone());
        mInitialTime.setTimeInMillis(initialTime.getTimeInMillis());
        mLayoutID = layoutID;
        this.minuteInterval = minInterval;
        if (minInterval>1) {
        	int minutes = mInitialTime.get(Calendar.MINUTE);
    		int diff = ((minutes+minuteInterval/2)/minuteInterval)*minuteInterval - minutes;
    		mInitialTime.add(Calendar.MINUTE, diff);
        }
    }

    /**
     * Set up the dialog with all the views and their listeners
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null) {
            Calendar c = (Calendar)savedInstanceState.getSerializable("time");
            if (c != null) {
                mInitialTime = c;
            }
        }

        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(mLayoutID);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.dialogtitle);

        mTitleText = (TextView) this.findViewById(R.id.dateSliderTitleText);
        mContainer = (SliderContainer) this.findViewById(R.id.dateSliderContainer);

        mContainer.setOnTimeChangeListener(onTimeChangeListener);
        mContainer.setMinuteInterval(minuteInterval);
        mContainer.setTime(mInitialTime);
        if (minTime!=null) mContainer.setMinTime(minTime);
        if (maxTime!=null) mContainer.setMaxTime(maxTime);

        Button okButton = (Button) findViewById(R.id.dateSliderOkButton);
        okButton.setOnClickListener(okButtonClickListener);

        Button cancelButton = (Button) findViewById(R.id.dateSliderCancelButton);
        cancelButton.setOnClickListener(cancelButtonClickListener);
    }

    public void setTime(Calendar c) {
        mContainer.setTime(c);
    }

    private View.OnClickListener okButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (onDateSetListener!=null)
                onDateSetListener.onDateSet(DateSlider.this, getTime());
            dismiss();
        }
    };

    private View.OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            dismiss();
        }
    };

    private SliderContainer.OnTimeChangeListener onTimeChangeListener = new SliderContainer.OnTimeChangeListener() {

        public void onTimeChange(Calendar time) {
            setTitle();
        }
    };

    @Override
    public Bundle onSaveInstanceState() {
        Bundle savedInstanceState = super.onSaveInstanceState();
        if (savedInstanceState==null) savedInstanceState = new Bundle();
        savedInstanceState.putSerializable("time", getTime());
        return savedInstanceState;
    }

    /**
     * @return The currently displayed time
     */
    protected Calendar getTime() {
        return mContainer.getTime();
    }

    /**
     * This method sets the title of the dialog
     */
    protected void setTitle() {
        if (mTitleText != null) {
            final Calendar c = getTime();
            mTitleText.setText("Select Date" + String.format(": %te. %tB %tY", c, c, c));
        }
    }


    /**
     * Defines the interface which defines the methods of the OnDateSetListener
     */
    public interface OnDateSetListener {
        /**
         * this method is called when a date was selected by the user
         * @param view			the caller of the method
         *
         */
        public void onDateSet(DateSlider view, Calendar selectedDate);
    }
}
