package DAO;

import datasets.DataSet;
import executors.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDAO {


    /**
     *     private Connection connection;
     *
     *     public DataSetDAOImpl(Connection connection) {
     *         this.connection = connection;
     *     }
     *
     *     @Override
     *     public <T extends DataSet> T create(T t) throws MyOrmException {
     *         return Executor.save(connection, t);
     *     }
     *
     *     @Override
     *     public <T extends DataSet> T getById(long id, Class<T> t) throws MyOrmException {
     *         return Executor.load(connection, id, t);
     *     }
     */


    private Connection connection;

    public MyDAO(Connection connection) {
        this.connection = connection;
    }

    public void test() {


        try (final Statement statement = connection.createStatement()) {
            statement.executeUpdate("insert into \"otusHomeWork\".\"testTable\" (\"testColumn\") values ('j')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T extends DataSet> void save(T user){
        Executor.save(connection, user);
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) {
        return Executor.load(connection, id, clazz);
    }
}
