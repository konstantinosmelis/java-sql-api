package com.melissaratos.database.core;

import com.melissaratos.database.api.IDatabase;
import com.melissaratos.database.api.IQuery;
import com.melissaratos.database.api.annotation.Column;
import com.melissaratos.database.api.annotation.Table;
import com.melissaratos.database.api.exception.MissingAnnotationException;
import com.melissaratos.database.api.exception.MissingPrimaryKeyException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements IDatabase {

    private Connection connection;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private final String charset;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        }
        catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |  InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public Database(String host, String port, String database, String username, String password, String charset) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.charset = charset;
    }

    public Database(String host, String port, String database, String username, String password) {
        this(host, port, database, username, password, "utf-8");
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public IDatabase connect() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useUnicode=true&characterEncoding=" + this.charset, this.username, this.password);
        return this;
    }

    @Override
    public IDatabase disconnect() throws SQLException {
        this.connection.close();
        return this;
    }

    @Override
    public IQuery query(String table) {
        return this.query(table, null);
    }

    @Override
    public IQuery query(String table, String alias) {
        return new Query(this, table + (alias == null ? "" : "AS " + alias));
    }

    @Override
    public ResultSet getResult(IQuery iQuery, Object... objects) throws SQLException {
        PreparedStatement statement = (PreparedStatement) this.execute(iQuery, objects);
        return statement.getResultSet();
    }

    @Override
    public Statement execute(IQuery iQuery, Object... objects) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(iQuery.build());
        for(int i = 0; i < objects.length; i++)
            statement.setObject(i + 1, objects[i]);
        statement.execute();
        return statement;
    }

    @Override
    public IDatabase close(ResultSet resultSet) throws SQLException {
        resultSet.close();
        return this;
    }

    @Override
    public <T> void update(T table) throws MissingAnnotationException, IllegalAccessException, SQLException {
        if(!table.getClass().isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("The class " + table.getClass().getSimpleName() + " is missing the annotation @Table");

        IQuery updateQuery = new Query(this, (table.getClass().getAnnotation(Table.class).name().equals("") ? table.getClass().getSimpleName() : table.getClass().getAnnotation(Table.class).name()));
        List<Field> primaryKeys = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for(Field field : table.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if(column.key() == Column.Key.PRIMARY)
                    primaryKeys.add(field);
                keys.add((column.name().equals("") ? field.getName() : column.name()));
                values.add(field.get(table));
            }
        }
        updateQuery.update(keys, values);
        updateQuery.where((primaryKeys.get(0).getAnnotation(Column.class).name().equals("") ? primaryKeys.get(0).getName() : primaryKeys.get(0).getAnnotation(Column.class).name()) + "=" + (primaryKeys.get(0).get(table) instanceof String ? "'" + primaryKeys.get(0).get(table) + "'" : primaryKeys.get(0).get(table)));
        for(int i = 1; i < primaryKeys.size(); i++)
            updateQuery.where(IQuery.Logic.AND, (primaryKeys.get(i).getAnnotation(Column.class).name().equals("") ? primaryKeys.get(i).getName() : primaryKeys.get(i).getAnnotation(Column.class).name()) + "=" + (primaryKeys.get(0).get(table) instanceof String ? "'" + primaryKeys.get(0).get(table) + "'" : primaryKeys.get(0).get(table)));

        this.execute(updateQuery);
    }

    @Override
    public <T> void build(T table) throws MissingAnnotationException, MissingPrimaryKeyException, SQLException {
        if(!table.getClass().isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("The class " + table.getClass().getSimpleName() + " is missing the annotation @Table");

        IQuery createQuery = new Query(this, (table.getClass().getAnnotation(Table.class).name().equals("") ? table.getClass().getSimpleName() : table.getClass().getAnnotation(Table.class).name()));
        List<String> columns = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<String> primaryKey = new ArrayList<>();
        for(Field field : table.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String fieldName = (column.name().equals("") ? field.getName() : column.name());
                columns.add(fieldName);
                types.add(String.format("%s %s %s", column.type(), (column.nullable() ? "" : "NOT NULL"), (column.autoincrement() ? "AUTO_INCREMENT" : "")));
                if(column.key() == Column.Key.PRIMARY)
                    primaryKey.add(fieldName);
            }
        }

        if(primaryKey.isEmpty())
            throw new MissingPrimaryKeyException("No primary key found for table " + (table.getClass().getAnnotation(Table.class).name().equals("") ? table.getClass().getSimpleName() : table.getClass().getAnnotation(Table.class).name()));

        createQuery.create(columns, types, primaryKey);

        this.execute(createQuery);
    }

    @Override
    public <T> void insert(T table) throws MissingAnnotationException, IllegalAccessException, SQLException {
        if(!table.getClass().isAnnotationPresent(Table.class))
            throw new MissingAnnotationException("The class " + table.getClass().getSimpleName() + " is missing the annotation @Table");

        IQuery insertQuery = new Query(this, (table.getClass().getAnnotation(Table.class).name().equals("") ? table.getClass().getSimpleName() : table.getClass().getAnnotation(Table.class).name()));
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for(Field field : table.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columns.add((column.name().equals("") ? field.getName() : column.name()));
                values.add(field.get(table));
            }
        }
        insertQuery.insertInto(columns, values);

        this.execute(insertQuery);
    }
}
