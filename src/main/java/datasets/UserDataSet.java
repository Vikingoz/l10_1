package datasets;

public class UserDataSet extends  DataSet {


    private String name;
    private Integer age;


    public UserDataSet(String name, Integer age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id='" + super.getId() + '\'' +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}