package de.forro_apps.ggvertretungsplan.web.contentstorage;

public class SubstitutionItem {

    private String lesson;
    private String classes;
    private String agent;
    private String type;
    private String subject;
    private String room;
    private String from;
    private String to;
    private String text;

    public String getLesson() {
        return lesson;
    }

    public String getClasses() {
        return classes;
    }

    public String getAgent() {
        return agent;
    }

    public String getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getRoom() {
        return room;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setText(String text) {
        this.text = text;
    }
}
