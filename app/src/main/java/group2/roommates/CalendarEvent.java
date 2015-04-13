package group2.roommates;


public class CalendarEvent {

    private int _id;
    private int date;
    private String title;
    private String description;
    private String author;

    public CalendarEvent(){

    }

    public CalendarEvent(int date, String title, String description, String author){
        this.date = date;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public int getId(){
        return _id;
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

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
