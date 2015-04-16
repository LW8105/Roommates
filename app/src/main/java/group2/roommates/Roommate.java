package group2.roommates;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class Roommate {

    private int id;
    private String name;
    private Drawable avatar;

    public Roommate() {
    }

    public Roommate(int id, String name, Drawable avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setAvatar(Drawable avatar) {
        this.avatar = avatar;
    }
}
