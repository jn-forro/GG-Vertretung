package de.forro_apps.ggvertretungsplan.menus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.forro_apps.ggvertretungsplan.R;
import de.forro_apps.ggvertretungsplan.update.Version;

public class InformationMenu extends AppCompatActivity {

    private boolean floatingButtonActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton mailButton = findViewById(R.id.contact_email_button);
        FloatingActionButton instagramButton = findViewById(R.id.contact_instagram_button);
        FloatingActionButton twitterButton = findViewById(R.id.contact_twitter_button);

        Animation showButtonsAnimation = AnimationUtils.loadAnimation(this, R.anim.show_floating_action_buttons);
        Animation hideButtonsAnimation = AnimationUtils.loadAnimation(this, R.anim.hide_floating_action_buttons);

        fab.setOnClickListener( (view) -> {
            if (!floatingButtonActive) {
                fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);

                mailButton.setVisibility(View.VISIBLE);
                instagramButton.setVisibility(View.VISIBLE);
                twitterButton.setVisibility(View.VISIBLE);

                mailButton.startAnimation(showButtonsAnimation);
                instagramButton.startAnimation(showButtonsAnimation);
                twitterButton.startAnimation(showButtonsAnimation);
            } else {
                fab.setImageResource(android.R.drawable.ic_dialog_email);

                mailButton.startAnimation(hideButtonsAnimation);
                instagramButton.startAnimation(hideButtonsAnimation);
                twitterButton.startAnimation(hideButtonsAnimation);

                mailButton.setVisibility(View.INVISIBLE);
                instagramButton.setVisibility(View.INVISIBLE);
                twitterButton.setVisibility(View.INVISIBLE);
            }
            floatingButtonActive = !floatingButtonActive;

        });

        mailButton.setOnClickListener((view) -> {
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:jnf2000@gmx.de"));
            startActivity(mailIntent);
        });

        twitterButton.setOnClickListener((view) -> {
            Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/jn_forro"));
            startActivity(twitterIntent);
        });

        instagramButton.setOnClickListener((view) -> {
            Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/jn_forro"));
            startActivity(instagramIntent);
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        TextView appInfo = findViewById(R.id.appInformation);
        appInfo.setText("GG Vertretungsplan (Version " + Version.CURRENT_VERSION + ") \n\u00A9 Jan Niklas Forro 2019");

        Button faq = findViewById(R.id.faqButton);
        faq.setOnClickListener((view) -> {
            // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gg-badems.org"));
            // startActivity(browserIntent);
            Toast.makeText(this, "Bald verfügbar", Toast.LENGTH_SHORT).show();
        });

        Button toDownload = findViewById(R.id.toDownload);
        toDownload.setOnClickListener((view) -> {
            Intent dropboxIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dropbox.com/sh/gejphe444qnz3v6/AADhmJniXnvkTnKDMp_cpeYva?dl=0"));
            startActivity(dropboxIntent);
        });

        Button licenses = findViewById(R.id.licenses);
        licenses.setOnClickListener((view) -> {
            Intent licenseIntent = new Intent(getBaseContext(), LicenseList.class);
            startActivity(licenseIntent);
        });

        Button privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyPolicy.setOnClickListener((view) -> {
            Intent policyIntent = new Intent(getBaseContext(), PrivacyPolicy.class);
            startActivity(policyIntent);
        });
    }

}
