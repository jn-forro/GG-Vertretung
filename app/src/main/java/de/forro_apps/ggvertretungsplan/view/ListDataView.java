package de.forro_apps.ggvertretungsplan.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ListDataView extends LinearLayout {

    private boolean underline;

    public ListDataView(Context context, boolean underline) {
        super(context);

        this.underline = underline;

        setOrientation(HORIZONTAL);
        setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1F));

        addView(new TextView(getContext()));
        addView(new TextView(getContext()));
        addView(new TextView(getContext()));
    }

    public ListDataView(Context context) {
        this(context, false);
    }

    public void setTitle(String title) {
        TextView titleTextView = new TextView(getContext());
        titleTextView.setText(title.concat(": "));
        titleTextView.setTypeface(null, Typeface.BOLD);
        if (underline) {
            titleTextView.setPaintFlags(titleTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        addView(titleTextView, 0);
    }

    public void setStruckThroughText(String struckThroughText) {
        if (struckThroughText.isEmpty()) {
            struckThroughText = "---";
        }

        TextView struckThroughTextView = new TextView(getContext());
        struckThroughTextView.setText(struckThroughText);
        struckThroughTextView.setPaintFlags(struckThroughTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        if (underline) {
            struckThroughTextView.setPaintFlags(struckThroughTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        addView(struckThroughTextView, 1);
    }

    public void setNormalText(String text) {
        if (text.isEmpty()) {
            text = "---";
        }

        TextView normalTextView = new TextView(getContext());
        normalTextView.setText(text);
        if (underline) {
            normalTextView.setPaintFlags(normalTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        addView(normalTextView, 2);
    }

    public void organizeString(String html) {
        if (!html.contains("?")) {
            setNormalText(html);
        } else {
            String[] s = html.split("\\?");
            setNormalText(s[1]);
            setStruckThroughText(s[0] + " ");
        }
    }


}
