package de.forro_apps.ggvertretungsplan.web;

import de.forro_apps.ggvertretungsplan.occupation.Occupation;

/**
 * Just constant links to the web contents.
 */
public abstract class Links {

    /**
     * Links accessible for students
     */
    public static class Students {
        public static final String TODAY = "https://vplan-gg-badems.org/VPlan/Schueler/Schueler_Heute_Lang/subst_001.htm";
        public static final String TOMORROW = "https://vplan-gg-badems.org/VPlan/Schueler/Schueler_Morgen_Lang/subst_001.htm";
        public static final String TICKER = "https://vplan-gg-badems.org/VPlan/Schueler/Ticker/ticker.htm";
    }

    /**
     * Links accessible for teachers only
     */
    public static class Teachers {
        public static final String TODAY = "https://vplan-gg-badems.org/VPlan/Lehrer/Lehrer_Heute_Lang/subst_001.htm";
        public static final String TOMORROW = "https://vplan-gg-badems.org/VPlan/Lehrer/Lehrer_Morgen_Lang/subst_001.htm";
        public static final String TICKER = "https://vplan-gg-badems.org/VPlan/Lehrer/Ticker/ticker.htm";
    }

    /**
     * Sets the links to the outer class
     * @param profession 0: student, 1: teacher
     */
    public static void setRequiredLinks(Occupation profession) {
        if (profession == Occupation.TEACHER) {
            TODAY = Teachers.TODAY;
            TOMORROW = Teachers.TOMORROW;
            TICKER = Teachers.TICKER;
        } else {
            TODAY = Students.TODAY;
            TOMORROW = Students.TOMORROW;
            TICKER = Students.TICKER;
        }
    }

    public static String TODAY;
    public static String TOMORROW;
    public static String TICKER;


}
