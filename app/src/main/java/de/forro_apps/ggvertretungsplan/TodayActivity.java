package de.forro_apps.ggvertretungsplan;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Display;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.forro_apps.ggvertretungsplan.adapter.CustomListAdapter;
import de.forro_apps.ggvertretungsplan.update.AppInactivity;
import de.forro_apps.ggvertretungsplan.web.html.ContentLoader;
import de.forro_apps.ggvertretungsplan.web.html.ContentSelector;

/**
 * Today's information
 */
public class TodayActivity extends AppCompatActivity {

    private ExpandableListView list;
    private CustomListAdapter adapter;
    private Point size;
    private CustomListAdapter.Orientation initialOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        setTitle(ContentSelector.getDate(ContentSelector.Day.TODAY));

        list = findViewById(R.id.expandedList);
        adapter = new CustomListAdapter(ContentSelector.Day.TODAY, this);
        list.setAdapter(adapter);

        initialOrientation = adapter.getOrientation();


        size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        list.setIndicatorBounds(size.x - 250, size.x - 150);


        if (list.getAdapter().getCount() == 0) {
            TextView noInformationFound = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                noInformationFound.setText(Html.fromHtml(ContentLoader.TODAY_HTML, Html.FROM_HTML_MODE_LEGACY));
            } else {
                noInformationFound.setText(Html.fromHtml(ContentLoader.TODAY_HTML));
            }
            RelativeLayout rl = findViewById(R.id.activity_today);
            rl.addView(noInformationFound);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adapter.updateOrientation();

        if (initialOrientation == CustomListAdapter.Orientation.PORTRAIT) {
            if (newConfig.orientation == 1) {
                list.setIndicatorBounds(size.x - 250, size.x - 150);
            } else {
                list.setIndicatorBounds(size.y - 250, size.y - 150);
            }
        } else {
            if (newConfig.orientation == 1) {
                list.setIndicatorBounds(size.y - 250, size.y - 150);
            } else {
                list.setIndicatorBounds(size.x - 250, size.x - 150);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppInactivity.setPauseTime(System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppInactivity.setResumeTime(System.currentTimeMillis());

        if (AppInactivity.getInactiveMinutes() >= 15) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

}
