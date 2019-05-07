package executors;

import datasets.DataSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Executor {

    private static final List<String> SYSTEM_FIELDS = Arrays.asList("TABLE_NAME", "SCHEMA_NAME");

    static List<Field> getAllFields (Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fields;
    }

    static List<Field> getClassFields (Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        return fields;
    }


    private static <T extends DataSet> String getInsertFromClass(T user) {
        String sqlQuery = null;
        List<Field> fields = getClassFields(user.getClass());
        if (fields.size() > 0) {


            sqlQuery = fields.stream()
                    .map(Field::getName).filter(x -> !SYSTEM_FIELDS.contains(x))
                    .collect(Collectors.joining(", ",
                            "INSERT INTO \"" +
                                    user.getSchemaName() +
                                    "\".\""+
                                    user.getTableName() +
                                    "\" (", ")"));

            sqlQuery = sqlQuery + fields.stream().filter(x -> !SYSTEM_FIELDS.contains(x.getName()))
                    .map(x -> "?")
                    .collect(Collectors.joining(", ", " VALUES (", ")"));

        }
        return sqlQuery;
    }

    private static <T extends DataSet> String getSelectFromClass(Class<T> clazz) {
        String sqlQuery = null;
        List<Field> fields = getAllFields(clazz);
        if (fields.size() > 0) {

            sqlQuery = fields.stream()
                    .map(Field::getName).filter(x -> !SYSTEM_FIELDS.contains(x))
                    .collect(Collectors.joining(", ",
                            " SELECT ",
                            " FROM \"" +
                                    T.getSchemaName() +
                                    "\".\""+
                                    T.getTableName() +
                            "\" ")) + " WHERE id = ? ";

        }
        return sqlQuery;
    }


    public static <T extends DataSet> void save(Connection connection, T user) {
        String query = getInsertFromClass(user);

        if (!query.isEmpty()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
                List<Field> fields = getClassFields(user.getClass());
                for (int i = 0; i < fields.size(); i++) {
                    fields.get(i).setAccessible(true);
                    preparedStatement.setObject(i + 1, fields.get(i).get(user));
                }
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    user.setId(resultSet.getLong(1));
                    //return user;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //return null;
    }


    public static <T extends DataSet> T load(Connection connection, long id, Class<T> clazz) {
        String query = getSelectFromClass(clazz);
        if (!query.isEmpty()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1, id);


                ResultSet resultSet = preparedStatement.executeQuery();

                List<Field> fields = getAllFields(clazz)
                        .stream()
                        .filter(f -> !SYSTEM_FIELDS.contains(f.getName()))
                        .collect(Collectors.toList());

                Constructor<?> ctor = clazz.getConstructor(String.class, Integer.class);
                T dataSet = (T) ctor.newInstance(null, null);

                resultSet.next();
                //T dataSet = new T();
                for (Field field : fields) {
                    field.setAccessible(true);
                    System.out.println("load:" + field.getType() + " " + field.getName());
                    if (field.getType().equals(Integer.class)) {
                        field.set(dataSet, resultSet.getInt(field.getName()));
                    } else {
                        field.set(dataSet, resultSet.getObject(field.getName()));
                    }

                }
                return dataSet;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        return null;
    }
}
