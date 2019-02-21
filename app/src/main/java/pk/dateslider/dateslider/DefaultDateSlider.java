package pk.dateslider.dateslider;

import android.content.Context;

import java.util.Calendar;

import pk.dateslider.R;

public class DefaultDateSlider extends DateSlider {

	public DefaultDateSlider(Context context, OnDateSetListener l, Calendar calendar,
			Calendar minTime, Calendar maxTime) {
        super(context, R.layout.defaultdateslider, l, calendar, minTime, maxTime);
    }

    public DefaultDateSlider(Context context, OnDateSetListener l, Calendar calendar) {
        super(context, R.layout.defaultdateslider, l, calendar);
    }
}
