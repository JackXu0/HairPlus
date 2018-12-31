package wingsoloar.com.hairplus.Objects;

/**
 * Created by wingsolarxu on 2018/10/22.
 */

public class Shop {

    private String name;
    private int id;

    public Shop(int id,String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
