package de.forro_apps.ggvertretungsplan.web.html;

import de.forro_apps.ggvertretungsplan.occupation.Occupation;
import de.forro_apps.ggvertretungsplan.occupation.Variable;
import de.forro_apps.ggvertretungsplan.web.Links;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * This class loads the content from the webserver and stores them.
 */
public class ContentLoader implements Runnable {

    /**
     * The HTML of the today's substitions
     */
    public static String TODAY_HTML = "";

    /**
     * The HTML of the tomorrow's substitions
     */
    public static String TOMORROW_HTML = "";

    /**
     * The ticker containing relevant information. This String contains the whole site with HTML and JavaScripts.
     */
    public static String TICKER_HTML = "";


    // Loads the information from the webserver and stores it into fields.
    @Override
    public void run() {
        try {

            BufferedReader reader;
            String temp;

            StringBuilder builder;

            if (TICKER_HTML.equals("")) {
                URL ticker = new URL(Links.TICKER);
                reader = new BufferedReader(new InputStreamReader(ticker.openStream(), Charset.forName("ISO-8859-15")));

                builder = new StringBuilder();

                while ((temp = reader.readLine()) != null) {
                    builder.append(temp);
                }
                reader.close();
                TICKER_HTML = builder.toString();
            }

            if (TODAY_HTML.equals("")) {
                URL today = new URL(Links.TODAY);
                reader = new BufferedReader(new InputStreamReader(today.openStream(), Charset.forName("ISO-8859-15")));

                builder = new StringBuilder();

                while ((temp = reader.readLine()) != null) {
                    builder.append(temp);
                }
                reader.close();
                TODAY_HTML = builder.toString();
            }

            if (TOMORROW_HTML.equals("")) {
                URL tomorrow = new URL(Links.TOMORROW);
                reader = new BufferedReader(new InputStreamReader(tomorrow.openStream(), Charset.forName("ISO-8859-15")));

                builder = new StringBuilder();

                while ((temp = reader.readLine()) != null) {
                    builder.append(temp);
                }
                reader.close();
                TOMORROW_HTML = builder.toString();
            }

            System.out.println(1);
            ContentSelector.sortInformation(ContentSelector.Day.TODAY, Variable.occupation);
            System.out.println(2);
            ContentSelector.sortInformation(ContentSelector.Day.TOMORROW, Variable.occupation);
            System.out.println(3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetHTML() {
        TODAY_HTML = "";
        TOMORROW_HTML = "";
        TICKER_HTML = "";
    }

}
