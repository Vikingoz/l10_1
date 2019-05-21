package executors;

import annotations.TableName;
import org.apache.commons.lang3.StringUtils;
import datasets.DataSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Executor {
    private static Map<Class, String>  saveQueries = new HashMap<>();
    private static Map<Class, String>  loadQueries = new HashMap<>();

    static List<Field> getAllFields (Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fields;
    }

    static List<Field> getClassFields (Class<?> clazz) {
        return new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
    }

    private static String getSchema(Class clazz) {
        return Optional.ofNullable((TableName) clazz.getAnnotation(TableName.class))
                .map(a -> a.schemaName())
                .orElse(null);
    }

    private static String getTable(Class clazz) {
        return Optional.ofNullable((TableName) clazz.getAnnotation(TableName.class))
                .map(a -> a.tableName())
                .orElse(null);
    }

    private static <T extends DataSet> String getSaveQueryFromClass(T user) {
        String sqlQuery = saveQueries.get(user.getClass());
        if (StringUtils.isEmpty(sqlQuery)) {
            System.out.println("create new saveQuery to class " + user.getClass().getName().toString());
            List<Field> fields = getClassFields(user.getClass());
            if (fields.size() > 0) {

                sqlQuery = fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ",
                                "INSERT INTO \"" +
                                        getSchema(user.getClass()) +
                                        "\".\"" +
                                        getTable(user.getClass()) +
                                        "\" (", ")"));

                sqlQuery = sqlQuery + fields.stream()
                        .map(x -> "?")
                        .collect(Collectors.joining(", ", " VALUES (", ")"));

            }
            saveQueries.put(user.getClass(), sqlQuery);
        } else {
            System.out.println("load saveQuery to class " + user.getClass().getName().toString());
        }
        return sqlQuery;
    }

    private static <T extends DataSet> String getLoadQueryFromClass(Class<T> clazz) {
        String sqlQuery = loadQueries.get(clazz);
        if (StringUtils.isEmpty(sqlQuery)) {
            System.out.println("create new loadQuery to class " + clazz.getName().toString());
            List<Field> fields = getAllFields(clazz);
            if (fields.size() > 0) {

                sqlQuery = fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ",
                                " SELECT ",
                                " FROM \"" +
                                        getSchema(clazz) +
                                        "\".\"" +
                                        getTable(clazz) +
                                        "\" ")) + " WHERE id = ? ";

            }
            loadQueries.put(clazz, sqlQuery);
        } else {
            System.out.println("load loadQuery to class " + clazz.getName().toString());
        }
        return sqlQuery;
    }

    private static <T extends DataSet> String getLoadAllFieldsQueryFromClass(Class<T> clazz) {
        String sqlQuery = loadQueries.get(clazz);
        if (StringUtils.isEmpty(sqlQuery)) {
            System.out.println("create new loadQuery to class " + clazz.getName().toString());
            List<Field> fields = getAllFields(clazz);
            if (fields.size() > 0) {

                sqlQuery = fields.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(", ",
                                " SELECT ",
                                " FROM \"" +
                                        getSchema(clazz) +
                                        "\".\"" +
                                        getTable(clazz) +
                                        "\" "));

            }
            loadQueries.put(clazz, sqlQuery);
        } else {
            System.out.println("load loadAllFieldsQuery to class " + clazz.getName().toString());
        }
        return sqlQuery;
    }

    public static <T extends DataSet> void save(Connection connection, T user) {
        String query = getSaveQueryFromClass(user);

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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public static <T extends DataSet> T load(Connection connection, long id, Class<T> clazz) {
        String query = getLoadQueryFromClass(clazz);
        if (!query.isEmpty()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setObject(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                List<Field> fields = getAllFields(clazz)
                        .stream()
                        .collect(Collectors.toList());

                Constructor<?> ctor = clazz.getConstructor(String.class, Integer.class);
                T dataSet = (T) ctor.newInstance(null, null);

                resultSet.next();
                for (Field field : fields) {
                    field.setAccessible(true);
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

    public static <T extends DataSet> List<T> loadAll(Connection connection, Class<T> clazz) {
        String query = getLoadAllFieldsQueryFromClass(clazz);
        if (!query.isEmpty()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //preparedStatement.setObject(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();

                List<Field> fields = getAllFields(clazz)
                        .stream()
                        .collect(Collectors.toList());

                Constructor<?> ctor = clazz.getConstructor(String.class, Integer.class);

                List<T> list = new ArrayList<>();
                while(resultSet.next()) {
                    T dataSet = (T) ctor.newInstance(null, null);
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (field.getType().equals(Integer.class)) {
                            field.set(dataSet, resultSet.getInt(field.getName()));
                        } else {
                            field.set(dataSet, resultSet.getObject(field.getName()));
                        }

                    }
                    list.add(dataSet);
                }
                return list;
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
