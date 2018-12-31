package wingsoloar.com.hairplus.Objects;

import android.util.Log;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class CutterProfile {

    private int id;
    private String shopName;
    private String name;
    private String[] imageNames;
    private String avator;
    private int size=0;
    private String style;
    private String skills;
    private int rating;
    private int minPrice;
    private int maxPrice;
    private int experience;
    private String tel;
    private String location;
    private String introduction;



//    public CutterProfile(int id, String shopName, String name, String[] imageName, String avator, String style, int rating, int minPrice, int maxPrice ) {
//        this.id = id;
//        this.shopName = shopName;
//        this.name = name;
//        this.imageNames = imageName;
//        this.avator = avator;
//        this.style = style;
//        this.
//
//
//    }


    public CutterProfile(int id, String shopName, String name, String[] imageNames, String avator, String style, String skills, int rating, int minPrice, int maxPrice, int experience, String tel, String location, String introduction) {
        this.id = id;
        this.shopName = shopName;
        this.name = name;
        this.imageNames = imageNames;
        this.avator = avator;
        this.style = style;
        this.skills = skills;
        this.rating = rating;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.experience = experience;
        this.tel = tel;
        this.location = location;
        this.introduction = introduction;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String[] getImageNames() {
        return imageNames;
    }

    public void setImageNames(String[] imageNames) {
        this.imageNames = imageNames;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addImageName(String name) {
        Log.e("temperary size", ""+size);
        this.imageNames[size]=name;
        size++;
    }
}
