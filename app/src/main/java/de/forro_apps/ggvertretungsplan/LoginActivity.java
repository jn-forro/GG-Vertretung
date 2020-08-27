package de.forro_apps.ggvertretungsplan;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.dropbox.core.DbxException;
import de.forro_apps.ggvertretungsplan.database.Database;
import de.forro_apps.ggvertretungsplan.occupation.Occupation;
import de.forro_apps.ggvertretungsplan.update.AppInactivity;
import de.forro_apps.ggvertretungsplan.update.DropboxClient;
import de.forro_apps.ggvertretungsplan.update.Version;
import de.forro_apps.ggvertretungsplan.web.Links;
import de.forro_apps.ggvertretungsplan.web.contentstorage.ContentStorage;
import de.forro_apps.ggvertretungsplan.web.html.ContentLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

/**
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private TextView authFailed;

    private EditText password;
    private EditText userName;

    private ToggleButton studentToggleButton;
    private ToggleButton teacherToggleButton;

    private String profession;

    private ProgressBar loading;

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        password = (EditText) findViewById(R.id.password);
        userName = (EditText) findViewById(R.id.userName);

        password.setHint(getString(R.string.password));
        userName.setHint(getString(R.string.userName));

        studentToggleButton = findViewById(R.id.studentToggleButton);
        teacherToggleButton = findViewById(R.id.teacherToggleButton);

        studentToggleButton.setOnClickListener(this);
        teacherToggleButton.setOnClickListener(this);

        ImageButton show = (ImageButton) findViewById(R.id.showPassword);
        show.setOnTouchListener(this);

        Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        loading = (ProgressBar) findViewById(R.id.progressBar);
        loading.setVisibility(View.INVISIBLE);

        Database.SQLMethods db = new Database.SQLMethods(getApplicationContext(), null);
        userName.setText(db.getLoginCredentials()[0]);
        password.setText(db.getLoginCredentials()[1]);
        profession = db.getLoginCredentials()[2];

        if (profession.equalsIgnoreCase("0")) {
            studentToggleButton.setChecked(true);
            teacherToggleButton.setChecked(false);
            Occupation.selected = Occupation.STUDENT;
        } else if (profession.equalsIgnoreCase("1")) {
            teacherToggleButton.setChecked(true);
            studentToggleButton.setChecked(false);
            Occupation.selected = Occupation.TEACHER;
        } else {
            studentToggleButton.setChecked(true);
            teacherToggleButton.setChecked(false);
            Occupation.selected = Occupation.STUDENT;
            profession = "0";
        }
        Links.setRequiredLinks(Occupation.selected);

        authFailed = findViewById(R.id.authFailed);

        TextView updateAvailable = findViewById(R.id.update);
        updateAvailable.setVisibility(TextView.INVISIBLE);
        try {
            DropboxClient dropboxClient = new DropboxClient(this);

            new Thread(() -> {
                try {
                    if (!dropboxClient.getVersion().equals(Version.CURRENT_VERSION)) {
                        runOnUiThread(() -> {
                            updateAvailable.setVisibility(TextView.VISIBLE);

                            updateAvailable.setOnClickListener((view) -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://www.dropbox.com/sh/gejphe444qnz3v6/AADhmJniXnvkTnKDMp_cpeYva?dl=0"));
                                startActivity(browserIntent);
                            });
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DbxException e) {
                    e.printStackTrace();
                    System.out.println("Dbx");
                }
            }).start();
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO");
        }

        db.close();

        initThread();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        if (v instanceof Button && !(v instanceof ToggleButton)) {
            authFailed.setText("");

            Database.SQLMethods db = new Database.SQLMethods(getBaseContext(), null);
            db.setLoginCredentials(userName.getText().toString(), password.getText().toString(), profession);
            db.close();

            if (loading.getVisibility() == View.INVISIBLE) {
                loading.setVisibility(View.VISIBLE);
                thread.start();
            }
        }

        if (v instanceof ToggleButton) {
            if (profession.equalsIgnoreCase("0")) {
                if (v == teacherToggleButton) {
                    teacherToggleButton.setChecked(true);
                    studentToggleButton.setChecked(false);
                    Occupation.selected = Occupation.TEACHER;
                    profession = "1";
                } else {
                    teacherToggleButton.setChecked(false);
                    studentToggleButton.setChecked(true);
                    Occupation.selected = Occupation.STUDENT;
                }
            } else {
                if (v == studentToggleButton) {
                    teacherToggleButton.setChecked(false);
                    studentToggleButton.setChecked(true);
                    Occupation.selected = Occupation.STUDENT;
                    profession = "0";
                } else {
                    teacherToggleButton.setChecked(true);
                    studentToggleButton.setChecked(false);
                    Occupation.selected = Occupation.TEACHER;
                }
            }
            Links.setRequiredLinks(Occupation.selected);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        loading.setVisibility(View.INVISIBLE);
        initThread();
    }

    @Override
    protected void onPause() {
        super.onPause();

        thread.interrupt();
        loading.setVisibility(View.INVISIBLE);
    }

    private void initThread() {
        thread = new Thread(() -> {

            int timeout = 0;

            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName.getText().toString(), password.getText().toString().toCharArray());
                }
            });


            authFailed.setTextColor(Color.RED);

            try {
                if (ContentLoader.TICKER_HTML.equals("") || ContentLoader.TODAY_HTML.equals("") || ContentLoader.TOMORROW_HTML.equals("")) {
                    URL url = new URL(Links.TODAY);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    reader.readLine();
                }
                Intent intent = new Intent(getBaseContext(), OverviewActivity.class);

                Thread cloader = new Thread(new ContentLoader());
                cloader.start();
                do {
                    if (new Random().nextInt(1000) == 2) {
                        timeout++;
                        if (timeout % 100 == 0) {
                            System.out.println(timeout);
                        }
                        if (timeout == 100000) {
                            throw new IOException("Bad Internet");
                        }
                    }
                }
                while (ContentLoader.TICKER_HTML.equals("") || ContentLoader.TODAY_HTML.equals("") || ContentLoader.TOMORROW_HTML.equals(""));

                startActivity(intent);

            } catch (ProtocolException e) {
                System.out.println("PROTOCOL EXCEPTION");
                runOnUiThread(() -> {
                    authFailed.setText("Login fehlgeschlagen");
                    loading.setVisibility(View.INVISIBLE);
                    initThread();
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    authFailed.setText("Fehler: Keine Serververbindung");
                    loading.setVisibility(View.INVISIBLE);
                    initThread();
                });
                e.printStackTrace();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ContentLoader.resetHTML();

        AppInactivity.reset();
        ContentStorage.clear();
    }
}
