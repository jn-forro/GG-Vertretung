package de.forro_apps.ggvertretungsplan.ads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class AdBase extends View implements View.OnClickListener {

    private String target;
    private ArrayList<ImageView> images;

    private Context context;

    public AdBase(Context context) {
        super(context);
        this.context = context;
        this.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(target));
        context.startActivity(intent);
    }

}
