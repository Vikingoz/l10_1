package services.DAO;

import datasets.DataSet;
import executors.Executor;
import java.sql.Connection;

public class MyDAO {
    private Connection connection;

    public MyDAO(Connection connection) {
        this.connection = connection;
    }
    public <T extends DataSet> void save(T user){
        Executor.save(connection, user);
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        return Executor.load(connection, id, clazz);
    }
}
