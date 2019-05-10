package services;

import datasets.DataSet;

public interface DBService {

    <T extends DataSet> void save(T user);

    <T extends DataSet> T load(long id, Class<T> clazz);
}
