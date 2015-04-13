package group2.roommates;


public class ToDoItem {

    private int id;
    private String description;
    private String author;
    private boolean checked;

    public ToDoItem(int id, String description, String author, boolean checked) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return id + "," + description + "," + author;
    }
}
