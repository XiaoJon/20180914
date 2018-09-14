package kankan.wheel.widget.regionselect.model;

/**
 * @author Administrator
 * @date 2017/11/8
 */

public class WheelModel {

    private String name;

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Model [name=" + name + ", id=" + id + "]";
    }

}
