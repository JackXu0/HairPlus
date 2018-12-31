package wingsoloar.com.hairplus.Objects;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wingsolarxu on 2018/10/20.
 */

public class Post implements Serializable{

    private int id;
    private String user_name;
    private String cutter;
    private String user_avator;
    private String[] imageNames;
    private String content;
    private String shop;
    private int price;
    private int size=0;
    private boolean isLiked;
    private String tags;
    private boolean is_tony_post;

    public Post(){

    }

    public Post(int id, String user_name,String user_avator,String content, String cutter,String shop, int price, String[] imageNames, boolean isLinked, String tags, boolean is_tony_post) {
        this.id = id;
        this.user_name = user_name;
        this.user_avator = user_avator;
        this.cutter = cutter;
        this.content=content;
        this.imageNames = imageNames;
        this.shop = shop;
        this.price = price;
        this.isLiked = isLinked;
        this.tags = tags;
        this.is_tony_post = is_tony_post;
    }

    public boolean is_tony_post() {
        return is_tony_post;
    }

    public void setIs_tony_post(boolean is_tony_post) {
        this.is_tony_post = is_tony_post;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user) {
        this.user_name = user_name;
    }

    public String getUser_avator() {
        return user_avator;
    }

    public void setUser_avator(String user_avator) {
        this.user_avator = user_avator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCutter() {
        return cutter;
    }

    public void setCutter(String cutter) {
        this.cutter = cutter;
    }

    public String[] getImageNames() {
        return imageNames;
    }

    public void addImageName(String name) {
        Log.e("temperary size", ""+size);
        this.imageNames[size]=name;
        size++;
    }

}
