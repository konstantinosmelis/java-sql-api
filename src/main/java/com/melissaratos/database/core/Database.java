package com.melissaratos.database.core;

import com.melissaratos.database.api.IDatabase;
import com.melissaratos.database.api.IQuery;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

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
    public ResultSet getResultSet(IQuery iQuery, Object... objects) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(iQuery.build());
        for(int i = 0; i < objects.length; i++)
            statement.setObject(i + 1, objects[i]);
        return statement.executeQuery();
    }

    @Override
    public void execute(IQuery iQuery, Object... objects) throws SQLException {
        PreparedStatement statement = this.connection.prepareStatement(iQuery.build());
        for(int i = 0; i < objects.length; i++)
            statement.setObject(i + 1, objects[i]);
        statement.execute();
        statement.close();
    }

    @Override
    public IDatabase close(ResultSet resultSet) throws SQLException {
        resultSet.close();
        return this;
    }
}
