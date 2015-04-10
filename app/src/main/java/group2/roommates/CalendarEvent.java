package group2.roommates;


public class CalendarEvent {

    private int id;
    private int date;
    private String title;
    private String description;
    private String author;

    public CalendarEvent(int id, int date, String title, String description, String author){
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public int getId(){
        return id;
    }

    public int getDate(){
        return date;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getAuthor(){
        return author;
    }

    @Override
    public String toString(){
        return id + "," + date + "," + title + "," + description + "," + author;
    }



}
