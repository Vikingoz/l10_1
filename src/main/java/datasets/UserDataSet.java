package datasets;


import annotations.TableName;
import javax.persistence.*;
import java.util.List;

@TableName(schemaName = "otusHomeWork", tableName = "my_table")
@Entity
@Table(name = "user_data_set")
public class UserDataSet extends  DataSet {

    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private Integer age;
    @OneToMany(cascade = CascadeType.ALL)
    private List<PhoneDataSet> phones;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressDataSet adress;

    public UserDataSet() {
    }

    public UserDataSet(String name, Integer age, List<PhoneDataSet> phones, AddressDataSet adress) {
        this.name = name;
        this.age = age;
        this.phones = phones;
        this.adress = adress;
    }

    public UserDataSet(String name, String age) {
        this.name = name;
        this.age = Integer.valueOf(age);
        this.phones = null;
        this.adress = null;
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
