package de.forro_apps.ggvertretungsplan.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import de.forro_apps.ggvertretungsplan.occupation.Occupation;
import de.forro_apps.ggvertretungsplan.occupation.Variable;
import de.forro_apps.ggvertretungsplan.view.ListDataView;
import de.forro_apps.ggvertretungsplan.web.contentstorage.ContentStorage;
import de.forro_apps.ggvertretungsplan.web.contentstorage.SubstitutionItem;
import de.forro_apps.ggvertretungsplan.web.html.ContentSelector;

/**
 * This class is the adapter for the {@link ExpandableListView} used to display the information
 * about substitution. It is defined to be used in {@link de.forro_apps.ggvertretungsplan.TodayActivity} and
 * {@link de.forro_apps.ggvertretungsplan.TomorrowActivity}.
 */
public class CustomListAdapter extends BaseExpandableListAdapter {

    /**
     * This field defines the day for which the content is chosen by the {@link ContentSelector}.
     */
    private ContentSelector.Day day;


    /**
     * This stores the {@link Context} in which the {@link CustomListAdapter} is used.
     */
    private Context context;

    /**
     * Enum to specify the screen orientation
     */
    public enum Orientation {
        PORTRAIT, LANDSCAPE
    }

    /**
     * Specifies the screen orientation to decide which layout/design to apply
     */
    private Orientation orientation;

    /**
     * @return The screen Orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation to the current state.
     */
    public void updateOrientation() {
        orientation = context.getResources().getConfiguration().orientation == 1 ? Orientation.PORTRAIT : Orientation.LANDSCAPE;
    }

    /**
     * @param day     The {@link de.forro_apps.ggvertretungsplan.web.html.ContentSelector.Day} of the substitutions which
     *                shall be displayed in the list.
     * @param context The {@link Context} in which the {@link CustomListAdapter} is used.
     */
    public CustomListAdapter(ContentSelector.Day day, Context context) {
        this.day = day;
        this.context = context;
        updateOrientation();
    }

    @Override
    public int getGroupCount() {
        return (Variable.occupation == Occupation.STUDENT ? ContentStorage.Student.getNumberOfForms(day) : ContentStorage.Teacher.getNumberOfTeachers(day));
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (Variable.occupation == Occupation.STUDENT ? ContentStorage.Student.getNumberOfClassesSubstitutions(day, groupPosition) : ContentStorage.Teacher.getNumberOfTeachersSubstitutions(day, groupPosition));
    }

    @Override
    public Object getGroup(int groupPosition) {
        return 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView t = new TextView(context);
        t.setText(Variable.occupation == Occupation.STUDENT ? ContentStorage.Student.getForm(day, groupPosition) : ContentStorage.Teacher.getTeacherName(day, groupPosition));
        t.setTextSize(20);
        t.setTypeface(t.getTypeface(), Typeface.BOLD);
        return t;
    }

    // Strike-Through: Fach, Raum, Vertreter
    // NEW: Klassen

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // Parent-Layout which will be returned later
        LinearLayout layout = new LinearLayout(context);

        layout.setOrientation(LinearLayout.VERTICAL);

        // The Views which store the information for the child
        ListDataView subject = new ListDataView(context, true);
        ListDataView lesson = new ListDataView(context);
        ListDataView type = new ListDataView(context);
        ListDataView room = new ListDataView(context);
        ListDataView substitute = new ListDataView(context);
        ListDataView from = new ListDataView(context);
        ListDataView to = new ListDataView(context);

        if (Variable.occupation == Occupation.STUDENT) {
            // * DEBUG
            long startTime = System.currentTimeMillis();
            System.out.println("1: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            SubstitutionItem item = ContentStorage.Student.getSubstitutionItem(day, groupPosition, childPosition);


            System.out.println("2: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();


            subject.organizeString(item.getSubject());
            subject.setTitle("Fach");

            lesson.setTitle("Stunde");
            lesson.setNormalText(item.getLesson());

            type.setTitle("Art");
            type.setNormalText(item.getType());

            room.setTitle("Raum");
            room.organizeString(item.getRoom());

            substitute.setTitle("Vertreter");
            substitute.organizeString(item.getAgent());

            from.setTitle("Vertr. von");
            from.setNormalText(item.getFrom());

            to.setTitle("(Le.) nach");
            to.setNormalText(item.getTo());


            System.out.println("3: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            // A space for a better readability of the listed information
            Space separator = new Space(context);
            separator.setMinimumHeight(50);

            System.out.println("5: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            // Layout for Orientation.PORTRAIT
            if (orientation == Orientation.PORTRAIT) {
                // Child-Layouts which give the information an order
                LinearLayout heading = new LinearLayout(context);
                heading.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout header = new LinearLayout(context);
                header.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout secondRow = new LinearLayout(context);
                secondRow.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout thirdRow = new LinearLayout(context);
                thirdRow.setOrientation(LinearLayout.HORIZONTAL);


                // The child-layouts are added to the parent
                layout.addView(heading);
                layout.addView(header);
                layout.addView(secondRow);
                layout.addView(thirdRow);

                // Adding the TextViews to the child-layouts
                heading.addView(subject);

                header.addView(lesson);
                header.addView(room);

                secondRow.addView(type);
                secondRow.addView(substitute);

                thirdRow.addView(from);
                thirdRow.addView(to);


            } else {
                // Child-Layouts which give the information an order
                LinearLayout upperRow = new LinearLayout(context);
                upperRow.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout lowerRow = new LinearLayout(context);
                lowerRow.setOrientation(LinearLayout.HORIZONTAL);

                // The child-layouts are added to the parent
                layout.addView(subject);
                layout.addView(upperRow);
                layout.addView(lowerRow);

                // Adding the TextViews to the child-layouts
                upperRow.addView(lesson);
                upperRow.addView(substitute);
                upperRow.addView(room);
                lowerRow.addView(type);
                lowerRow.addView(from);
                lowerRow.addView(to);
            }
            System.out.println("6: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            // The Space is added as last child
            layout.addView(separator);

            System.out.println("7: " + (System.currentTimeMillis() - startTime) + "ms");
            return layout;
        } else {
            // * DEBUG
            long startTime = System.currentTimeMillis();

            System.out.println("1: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            SubstitutionItem item = ContentStorage.Teacher.getSubstitutionItem(day, groupPosition, childPosition);


            // The TextViews which store the information for the child specifically for teachers
            ListDataView text = new ListDataView(context);
            ListDataView forms = new ListDataView(context);

            System.out.println("2: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();


            System.out.println("3: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            subject.setTitle("Fach");
            subject.organizeString(item.getSubject());

            lesson.setTitle("Stunde");
            lesson.setNormalText(item.getLesson());

            type.setTitle("Art");
            type.setNormalText(item.getType());

            room.setTitle("Raum");
            room.organizeString(item.getRoom());

            substitute.setTitle("Vertreter");
            substitute.organizeString(item.getAgent());

            from.setTitle("Vertr. von");
            from.setNormalText(item.getFrom());

            to.setTitle("(Le.) nach");
            to.setNormalText(item.getTo());

            forms.setTitle("Klasse(n)");
            forms.organizeString(item.getClasses());

            if (!(item.getText().equals("&nbsp;") || item.getText().isEmpty() || item.getText().equals(" ") || item.getText().equals(" "))) {
                text.setNormalText(item.getText());
                text.setTitle("Text");
            }

            System.out.println("4: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            // A space for a better readability of the listed information
            Space separator = new Space(context);
            separator.setMinimumHeight(50);

            System.out.println("5: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            // Layout for Orientation.PORTRAIT
            if (orientation == Orientation.PORTRAIT) {
                // Child-Layouts which give the information an order
                LinearLayout heading = new LinearLayout(context);
                heading.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout header = new LinearLayout(context);
                header.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout secondRow = new LinearLayout(context);
                secondRow.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout thirdRow = new LinearLayout(context);
                thirdRow.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout extraRow = new LinearLayout(context);
                extraRow.setOrientation(LinearLayout.HORIZONTAL);


                // The child-layouts are added to the parent
                layout.addView(heading);
                layout.addView(header);
                layout.addView(secondRow);
                layout.addView(thirdRow);
                layout.addView(extraRow);

                // Adding the TextViews to the child-layouts
                heading.addView(subject);

                header.addView(lesson);
                header.addView(room);

                secondRow.addView(type);
                secondRow.addView(substitute);

                thirdRow.addView(from);
                thirdRow.addView(to);

                extraRow.addView(forms);
                extraRow.addView(text);

            } else {
                // Child-Layouts which give the information an order
                LinearLayout upperRow = new LinearLayout(context);
                upperRow.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout lowerRow = new LinearLayout(context);
                lowerRow.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout extraRow = new LinearLayout(context);
                extraRow.setOrientation(LinearLayout.HORIZONTAL);

                // The child-layouts are added to the parent
                layout.addView(subject);
                layout.addView(upperRow);
                layout.addView(lowerRow);
                layout.addView(extraRow);

                // Adding the TextViews to the child-layouts
                upperRow.addView(lesson);
                upperRow.addView(substitute);
                upperRow.addView(room);
                lowerRow.addView(type);
                lowerRow.addView(from);
                lowerRow.addView(to);
                extraRow.addView(forms);
                extraRow.addView(text);
            }
            System.out.println("6: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            // The Space is added as last child
            layout.addView(separator);

            System.out.println("7: " + (System.currentTimeMillis() - startTime) + "ms");

            return layout;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    /**
     * @param text The text which should be added HTML-Tags to.
     * @return The same text as given as argument but with the <plaintext><s></plaintext> tag at the beginning
     * and before a questionmark. If the text does not contain a questionmark, it is not changed.
     */
    private String strikeThrough(String text) {
        if (text.contains("?")) {
            String firstPart, secondPart;
            firstPart = text.substring(0, text.indexOf('?'));
            secondPart = text.substring(text.indexOf('?'));
            return "<s>-" + firstPart + "</s>-" + secondPart;
        }
        return text;
    }
}

