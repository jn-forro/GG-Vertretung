package de.forro_apps.ggvertretungsplan.web.script;

/**
 * This class is used to select the needed information a JavaScript like
 * {@link de.forro_apps.ggvertretungsplan.web.html.ContentLoader#TICKER_HTML}
 */
public class ScriptParser {


    /**
     * @param script The Script which should be trimmed
     * @return The trimmed script. Only the needed the script is left. Useless HTML was removed.
     */
    public static String trim(String script) {
        script = script.substring(script.indexOf("<!--Anfang-->"), script.indexOf("<!--Ende-->"));
        script = script.replace("<!--Anfang-->", "");
        return script;
    }

    /**
     * @param script The script.
     * @return How many Arrays-indexes are in the script and how much information is in there.
     */
    private static int getHighestIndex(String script) {
        for(int i = 0; i < 32; i++) {
            if(!script.contains("MessageArray[" + i + "]")) {
                return i;
            }
        }
        return 1;
    }


    /**
     * @param trimmed The by {@link #trim(String)} trimmed script
     * @return A String-Array with the relevant information
     */
    public static String[] getInfo(final String trimmed) {
        String[] res = new String[getHighestIndex(trimmed)];
        for(int i = 0; i < getHighestIndex(trimmed); i++) {
            String sub = trimmed.substring(trimmed.indexOf("MessageArray[" + i + "]=\"") + ("MessageArray[" + i + "]=\"").length());
            res[i] = sub.substring(0, sub.indexOf("\";"));
        }
        return res;
    }
}
