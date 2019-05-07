package datasets;

public class DataSet {

    private static final String SCHEMA_NAME = "otusHomeWork";
    private static final String TABLE_NAME = "my_table";

    private long id;

    public DataSet() {
    }

    public DataSet(long id) {
        this.id = id;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSchemaName() {
        return SCHEMA_NAME;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
