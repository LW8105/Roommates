package group2.roommates;

public class FeedObject {

    int _id;
    String type;
    String title;
    String author;
    String timeCreated;


    public FeedObject(){//empty constructor
    }

    public FeedObject(String type, String title, String author, String timeCreated){
        this.type = type;
        this.title = title;
        this.author = author;
        this.timeCreated = timeCreated;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String toString(){
        return author + " added a new " + type + ": \"" + title + "\"";
    }
}
