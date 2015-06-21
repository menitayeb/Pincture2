package menirabi.com.doggydogapp;

/**
 * Created by Oren on 23/05/2015.
 */
public class NewsFeedData {
    String name;
    String age;
    int photoId;

    public NewsFeedData(String name, String age, int photoId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public int getPhotoId() {
        return photoId;
    }
}