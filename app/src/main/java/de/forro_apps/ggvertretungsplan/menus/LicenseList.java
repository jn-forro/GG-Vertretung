package de.forro_apps.ggvertretungsplan.menus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import de.forro_apps.ggvertretungsplan.R;

public class LicenseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_list);

        TextView icons = findViewById(R.id.icon_license);
        icons.setOnClickListener((view) -> {
            Intent iconIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://icons8.de/icons"));
            startActivity(iconIntent);
        });
    }
}
