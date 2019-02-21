package pk.dateslider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Date;

import pk.horizontaldayslider.HorizontalDaySlider;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        HorizontalDaySlider daySlider = (HorizontalDaySlider) findViewById(R.id.horizontal_linear_layout);
        daySlider.setDayLimits(10);
        daySlider.setCurrentSelectedDayPosition(3);
        daySlider.setDayListener(new HorizontalDaySlider.DayListener() {
            @Override
            public void onDayClickListener(int position, Date date) {
                Toast.makeText(MainActivity.this, ""+date.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
