package services;

import datasets.DataSet;

import java.util.List;

public interface DBService {

    <T extends DataSet> void save(T user);

    <T extends DataSet> T load(long id, Class<T> clazz);

    <T extends DataSet> List<T> loadAll(Class<T> clazz);
}
