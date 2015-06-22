package menirabi.com.doggydogapp;

/**
 * Created by Oren on 23/05/2015.
 */
public class NewsFeedData {
    String name;
    String age;
    int photoId;
    int coverId;

    public NewsFeedData(String name, String age, int photoId, int coverId) {
        this.name = name;
        this.age = age;
        this.photoId = photoId;
        this.coverId = coverId;
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

    public int getCoverId() {
        return coverId;
    }
}