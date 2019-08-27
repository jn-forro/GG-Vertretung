package de.forro_apps.ggvertretungsplan.update;

public class AppInactivity {

    private static long pauseTime;
    private static long resumeTime;

    public static void setPauseTime(long pauseTime) {
        AppInactivity.pauseTime = pauseTime;
    }

    public static void setResumeTime(long resumeTime) {
        if (pauseTime != 0)
            AppInactivity.resumeTime = resumeTime;
    }

    public static int getInactiveMinutes() {
        return Math.round(((resumeTime - pauseTime) / 1000) / 60);
    }

    public static void reset() {
        pauseTime = 0;
        resumeTime = 0;
    }
}
