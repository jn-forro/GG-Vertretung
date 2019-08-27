package de.forro_apps.ggvertretungsplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.forro_apps.ggvertretungsplan.menus.InformationMenu;
import de.forro_apps.ggvertretungsplan.update.AppInactivity;
import de.forro_apps.ggvertretungsplan.web.html.ContentSelector;

/**
 * Overview with choice to select the day and the ticker
 */
public class OverviewActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout todayContainer;
    private LinearLayout tomorrowContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_overview);

        TextView todayLastUpdated = (TextView) findViewById(R.id.todayLastUpdate);
        TextView tomorrowLastUpdated = (TextView) findViewById(R.id.tomorrowLastUpdate);

        todayLastUpdated.setText("zuletzt aktualisiert: " + (ContentSelector.getLastUpdated(ContentSelector.Day.TODAY) != null
                ? "\n" + ContentSelector.getLastUpdated(ContentSelector.Day.TODAY)
                : "--"));
        tomorrowLastUpdated.setText("zuletzt aktualisiert: " + (ContentSelector.getLastUpdated(ContentSelector.Day.TOMORROW) != null
                ? "\n" +  ContentSelector.getLastUpdated(ContentSelector.Day.TOMORROW)
                : "--"));

        TextView todayDate = (TextView) findViewById(R.id.todayDate);
        TextView tomorrowDate = (TextView) findViewById(R.id.tomorrowDate);

        todayDate.setText((ContentSelector.getDate(ContentSelector.Day.TODAY) != null
                ? ContentSelector.getDate(ContentSelector.Day.TODAY)
                : "--"));
        tomorrowDate.setText((ContentSelector.getDate(ContentSelector.Day.TOMORROW) != null
                ? ContentSelector.getDate(ContentSelector.Day.TOMORROW)
                : "--"));

        TextView ticker = (TextView) findViewById(R.id.ticker);
        try {
            ticker.setText(ContentSelector.getTicker());
        } catch (Exception e) {
            e.printStackTrace();
            ticker.setText("Fehler beim Laden des Tickers");
        }

        todayContainer = (LinearLayout) findViewById(R.id.containerToday);
        tomorrowContainer = (LinearLayout) findViewById(R.id.containerTomorrow);

        todayContainer.setOnClickListener(this);
        tomorrowContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(todayContainer)) {
            Intent intent = new Intent(getBaseContext(), TodayActivity.class);
            startActivity(intent);
        } else if(v.equals(tomorrowContainer)) {
            Intent intent = new Intent(getBaseContext(), TomorrowActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.info:
                Intent intent = new Intent(getBaseContext(), InformationMenu.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
