package datasets;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phone_data_set")
public class PhoneDataSet extends DataSet {
    @Column(name="phone")
    private String phone;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String phone) {
        this.phone = phone;
    }
}
