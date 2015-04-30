package group2.roommates;


public class ToDoItem {

    private int _id;
    private String description;
    private String author;
    private int checked; //should be set to 0 for false, 1 for true

    public ToDoItem(String description, String author, int checked) {
        this.description = description;
        this.author = author;
        this.checked = checked;
    }

    public int getChecked() {
        return checked;
    }

    public int getId() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return _id + "," + description + "," + author;
    }
}
