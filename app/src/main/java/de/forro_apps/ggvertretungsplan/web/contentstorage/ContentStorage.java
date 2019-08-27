package de.forro_apps.ggvertretungsplan.web.contentstorage;

import de.forro_apps.ggvertretungsplan.web.html.ContentSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ContentStorage {

    public static void clear() {
        Student.allForms.clear();
        if (Student.substitutions != null) {
            Student.substitutions.clear();
        }

        Teacher.allTeachers.clear();
        if (Teacher.substitutions != null) {
            Teacher.substitutions.clear();
        }
    }

    public static class Student {

        public static HashMap<ContentSelector.Day, ArrayList<String>> allForms = new HashMap<>();
        private static LinkedHashMap<ContentSelector.Day,
                LinkedHashMap<String, ArrayList<SubstitutionItem>>> substitutions;

        public Student(ContentSelector.Day day, String form, SubstitutionItem item) {

            LinkedHashMap<String, ArrayList<SubstitutionItem>> studentSubstitution = new LinkedHashMap<>();
            ArrayList<SubstitutionItem> substitutionItems = new ArrayList<>();

            if (substitutions == null) {
                System.out.println("INITIALZING 'SUBSTITUTIONS'");
                substitutions = new LinkedHashMap<>();

            } else {
                if (substitutions.get(day) != null) {
                    studentSubstitution = substitutions.get(day);
                }
                if (substitutions.get(day) != null && substitutions.get(day).get(form) != null) {
                    substitutionItems = substitutions.get(day).get(form);
                }
            }

            substitutionItems.add(item);
            studentSubstitution.put(form, substitutionItems);
            substitutions.put(day, studentSubstitution);
        }

        public static int getNumberOfForms(ContentSelector.Day day) {
            return allForms.get(day).size();
        }

        public static int getNumberOfClassesSubstitutions(ContentSelector.Day day, int id) {
            return substitutions.get(day).get(allForms.get(day).get(id)).size();
        }

        public static String getForm(ContentSelector.Day day, int id) {
            return allForms.get(day).get(id);
        }

        public static SubstitutionItem getSubstitutionItem(ContentSelector.Day day, int formID, int itemID) {
            return substitutions.get(day).get(allForms.get(day).get(formID)).get(itemID);
        }
    }

    public static class Teacher {

        public static HashMap<ContentSelector.Day, ArrayList<String>> allTeachers = new HashMap<>();
        private static LinkedHashMap<ContentSelector.Day,
                LinkedHashMap<String, ArrayList<SubstitutionItem>>> substitutions;

        public Teacher(ContentSelector.Day day, String teacherName, SubstitutionItem item) {

            LinkedHashMap<String, ArrayList<SubstitutionItem>> teacherSubstitution = new LinkedHashMap<>();
            ArrayList<SubstitutionItem> substitutionItems = new ArrayList<>();

            if (substitutions == null) {
                System.out.println("INITIALZING 'SUBSTITUTIONS'");
                substitutions = new LinkedHashMap<>();

            } else {
                if (substitutions.get(day) != null) {
                    teacherSubstitution = substitutions.get(day);
                }
                if (substitutions.get(day) != null && substitutions.get(day).get(teacherName) != null) {
                    substitutionItems = substitutions.get(day).get(teacherName);
                }
            }

            substitutionItems.add(item);
            teacherSubstitution.put(teacherName, substitutionItems);
            substitutions.put(day, teacherSubstitution);
        }

        public static int getNumberOfTeachers(ContentSelector.Day day) {
            return allTeachers.get(day).size();
        }

        public static int getNumberOfTeachersSubstitutions(ContentSelector.Day day, int id) {
            return substitutions.get(day).get(allTeachers.get(day).get(id)).size();
        }

        public static String getTeacherName(ContentSelector.Day day, int id) {
            return allTeachers.get(day).get(id);
        }

        public static SubstitutionItem getSubstitutionItem(ContentSelector.Day day, int teacherID, int itemID) {
            return substitutions.get(day).get(allTeachers.get(day).get(teacherID)).get(itemID);
        }


        /**
         * Removes substitutions which are null. Sometimes the header table displays
         * concerned teachers which do not actually are concerned
         * @param day
         */
        public static void removeNullSubstitutions(ContentSelector.Day day) {
            ArrayList<String> unsupportedTeachers = new ArrayList<>();
            for (String teacher : allTeachers.get(day)) {
                if (substitutions.get(day).get(teacher) == null) {
                    substitutions.get(day).remove(teacher);
                    unsupportedTeachers.add(teacher);
                    System.out.println("REMOVING " + teacher + " from " + day.toString());
                }
            }
            allTeachers.get(day).removeAll(unsupportedTeachers);
        }

        /**
         * Method to sort the teacher substitutions by the lesson
         * @param day Tomorrow or Today
         */
        public static void bubbleSort(ContentSelector.Day day) {
            for (String teacher : substitutions.get(day).keySet()) {
                ArrayList<SubstitutionItem> list = substitutions.get(day).get(teacher);
                for (int i = 0; i < list.size() - 1; i++) {
                    for (int j = 0; j < list.size() - 1 - i; j++) {
                        System.out.println(teacher + "; i: " + i + "; j: " + j);
                        if (Integer.parseInt(list.get(j).getLesson().substring(0, 1)) > Integer.parseInt(list.get(j + 1).getLesson().substring(0, 1))) {
                            SubstitutionItem temp = list.get(j);
                            list.set(j, list.get(j + 1));
                            list.set(j + 1, temp);
                        }
                    }
                }
                substitutions.get(day).put(teacher, list);
            }

        }

    }


}
