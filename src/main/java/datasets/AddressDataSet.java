package datasets;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "adress_data_set")
public class AddressDataSet extends  DataSet {
    @Column(name = "adress")
    private String adress;

    public AddressDataSet() {
    }

    public AddressDataSet(String adress) {
        this.adress = adress;
    }
}
