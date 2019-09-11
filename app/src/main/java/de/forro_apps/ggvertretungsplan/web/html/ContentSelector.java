package de.forro_apps.ggvertretungsplan.web.html;

import de.forro_apps.ggvertretungsplan.occupation.Occupation;
import de.forro_apps.ggvertretungsplan.web.contentstorage.ContentStorage;
import de.forro_apps.ggvertretungsplan.web.contentstorage.SubstitutionItem;
import de.forro_apps.ggvertretungsplan.web.script.ScriptParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

import static de.forro_apps.ggvertretungsplan.occupation.Occupation.STUDENT;

/**
 * This class gets the right information from the HTML Strings in {@link ContentLoader} and returns the
 * wanted information.
 */
public class ContentSelector {

    /**
     * Enum to divide between {@link ContentLoader#TODAY_HTML} and {@link ContentLoader#TOMORROW_HTML}.
     */
    public enum Day {
        TODAY, TOMORROW
    }

    /**
     * @param day The {@link Day} of the wanted information.
     * @return The time of the last update of the information on the webserver.
     */
    public static String getLastUpdated(Day day) {
        Document doc;

        if(day == Day.TODAY) {
            doc = Jsoup.parse(ContentLoader.TODAY_HTML);
        } else {
            doc = Jsoup.parse(ContentLoader.TOMORROW_HTML);
        }

        try {
            Element element = doc.getElementsByTag("tbody").get(0).getElementsByTag("td").get(0);
            element.getElementsByTag("span").remove();
            return element.text();

        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }


    /**
     * @param day The {@link Day} of the wanted information.
     * @return The date for when the information is.
     */
    public static String getDate(Day day) {
        Document doc;

        if(day == Day.TOMORROW) {
            doc = Jsoup.parse(ContentLoader.TOMORROW_HTML);
        } else {
            doc = Jsoup.parse(ContentLoader.TODAY_HTML);
        }

        try {
            return doc.body().getElementsByClass("mon_title").get(0).text();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return The ticker with relevant information. The information is already sorted so that there is no
     *      more script or HTML, only the information divided by linebreaks.
     */
    public static String getTicker() {
        String res = "";
        for(String s : ScriptParser.getInfo(ScriptParser.trim(Jsoup.parse(ContentLoader.TICKER_HTML).getElementsByTag("script").get(0).toString()))) {
            res += s +"\n";
        }
        return res;
    }


    private static ArrayList<String> getAffectedTeachers(Day day) {
        if (ContentStorage.Teacher.allTeachers.get(day) != null) {
            return ContentStorage.Teacher.allTeachers.get(day);
        }

        Element info;
        if (day == Day.TODAY) {
            info = Jsoup.parse(ContentLoader.TODAY_HTML).body().getElementsByClass("info").get(0).getElementsByTag("tbody").get(0);
        } else {
            info = Jsoup.parse(ContentLoader.TOMORROW_HTML).body().getElementsByClass("info").get(0).getElementsByTag("tbody").get(0);
            System.out.println(info.toString());
        }

        for (byte i = 0; i < info.getElementsByTag("tr").size(); i++) {
            if (info.getElementsByTag("tr").get(i).toString().contains("Betroffene Lehrer")) {
                String[] teachers;
                String teachersString = info.getElementsByTag("tr").get(i).getElementsByTag("td").get(1).text();
                teachers = teachersString.split(", ");
                ArrayList<String> teachersList = new ArrayList<>();
                Collections.addAll(teachersList, teachers);
                ContentStorage.Teacher.allTeachers.put(day, teachersList);
                break;
            }
        }
        return ContentStorage.Teacher.allTeachers.get(day);
    }


    static void sortInformation(Day day, Occupation occupation) {
        final Element information;

        if (occupation == STUDENT) {

            if (day == Day.TODAY) {
                information = Jsoup.parse(ContentLoader.TODAY_HTML).body().getElementsByTag("tbody").get(
                        Jsoup.parse(ContentLoader.TODAY_HTML).body().getElementsByTag("tbody").get(1).toString().contains("Nachrichten zum Tag") ?
                                2 : 1
                );
            } else {
                information = Jsoup.parse(ContentLoader.TOMORROW_HTML).body().getElementsByTag("tbody").get(
                        Jsoup.parse(ContentLoader.TOMORROW_HTML).body().getElementsByTag("tbody").get(1).toString().contains("Nachrichten zum Tag") ?
                                2 : 1
                );
            }

            Element tr;
            String form = "0";
            SubstitutionItem item;

            int classes = -1, lesson = -1, type = -1, agent = -1, room = -1, subject = -1, from = -1, to = -1;

            for (int i = 0; i < information.getElementsByTag("tr").size(); i++) {
                tr = information.getElementsByTag("tr").get(i);
                if (tr.getElementsByTag("th").size() >= 1) {
                    for (byte j = 0; j < tr.getElementsByTag("th").size(); j++) {
                        switch (tr.getElementsByTag("th").get(j).text()) {
                            case "Klasse(n)":
                                classes = j;
                                break;
                            case "Stunde":
                                lesson = j;
                                break;
                            case "Art":
                                type = j;
                                break;
                            case "(Lehrer)":
                                agent = j;
                                break;
                            case "Raum":
                                room = j;
                                break;
                            case "(Fach)":
                                subject = j;
                                break;
                            case "Vertr. von":
                                from = j;
                                break;
                            case "(Le.) nach":
                                to = j;
                                break;
                        }
                    }
                    continue;
                }
                if (tr.getElementsByTag("td").size() == 1) {
                    form = tr.getElementsByTag("td").text();
                    if (ContentStorage.Student.allForms.get(day) != null) {
                        ContentStorage.Student.allForms.get(day).add(form);
                    } else {
                        ArrayList<String> formList = new ArrayList<>();
                        formList.add(form);
                        ContentStorage.Student.allForms.put(day, formList);
                    }
                    continue;
                }
                item =  new SubstitutionItem();


                for (int j = 0; j < tr.getElementsByTag("td").size(); j++) {
                    String tableDataText = tr.getElementsByTag("td").get(j).text();
                    System.out.println(tableDataText);
                    if (j == classes) {
                        item.setClasses(tableDataText);
                    }
                    if (j == lesson) {
                        item.setLesson(tableDataText);
                    }
                    if (j == type) {
                        item.setType(tableDataText);
                    }
                    if (j == agent) {
                        item.setAgent(tableDataText);
                    }
                    if (j == room) {
                        item.setRoom(tableDataText);
                    }
                    if (j == subject) {
                        item.setSubject(tableDataText);
                    }
                    if (j == from) {
                        item.setFrom(tableDataText);
                    }
                    if (j == to) {
                        item.setTo(tableDataText);
                    }
                }
                new ContentStorage.Student(day, form, item);
            }
        } else {

            if (day == Day.TODAY) {
                information = Jsoup.parse(ContentLoader.TODAY_HTML).body().getElementsByClass("mon_list").get(0);
            } else {
                information = Jsoup.parse(ContentLoader.TOMORROW_HTML).body().getElementsByClass("mon_list").get(0);
            }

            Element tr;
            SubstitutionItem item;

            int classes = -1, lesson = -1, type = -1, agent = -1, room = -1, subject = -1, from = -1, to = -1, text = -1;

            tr = information.getElementsByTag("tr").get(0);
            for (int i = 0; i < tr.getElementsByTag("th").size(); i++) {
                switch (tr.getElementsByTag("th").get(i).text()) {
                    case "Klasse(n)":
                        classes = i;
                        break;
                    case "Stunde":
                        lesson = i;
                        break;
                    case "Art":
                        type = i;
                        break;
                    case "(Lehrer)":
                        agent = i;
                        break;
                    case "(Raum)":
                        room = i;
                        break;
                    case "(Fach)":
                        subject = i;
                        break;
                    case "Vertr. von":
                        from = i;
                        break;
                    case "(Le.) nach":
                        to = i;
                        break;
                    case "Text":
                        text = i;
                        break;
                }
            }

            ArrayList<String> unsupportedTeachers = new ArrayList<>();
            for (String t : getAffectedTeachers(day)) {
                for (int i = 1; i < information.getElementsByTag("tr").size(); i++) {
                    tr = information.getElementsByTag("tr").get(i).getElementsByTag("td").get(agent);
                    if (tr.text().contains(t)) {
                        item =  new SubstitutionItem();
                        Elements td = information.getElementsByTag("tr").get(i).getElementsByTag("td");
                        for (int j = 0; j < td.size(); j++) {
                            String data = td.get(j).text();
                            if (j == lesson) {
                                item.setLesson(data);
                            }
                            if (j == agent) {
                                item.setAgent(data);
                            }
                            if (j == classes) {
                                item.setClasses(data);
                            }
                            if (j == subject) {
                                item.setSubject(data);
                            }
                            if (j == type) {
                                item.setType(data);
                            }
                            if (j == room) {
                                item.setRoom(data);
                            }
                            if (j == from) {
                                item.setFrom(data);
                            }
                            if (j == to) {
                                item.setTo(data);
                            }
                            if (j == text) {
                                item.setText(data);
                            }
                        }
                        new ContentStorage.Teacher(day, t, item);
                    }
                }
            }
            ContentStorage.Teacher.removeNullSubstitutions(day);
            ContentStorage.Teacher.bubbleSort(day);
        }
    }

}
