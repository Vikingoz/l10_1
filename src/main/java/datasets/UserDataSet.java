package datasets;


import annotations.TableName;

@TableName(schemaName = "otusHomeWork", tableName = "my_table")
public class UserDataSet extends  DataSet {


    private String name;
    private Integer age;


    public UserDataSet(String name, Integer age) {
        this.name = name;
        this.age = age;
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
