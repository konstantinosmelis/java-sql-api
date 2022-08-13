package com.melissaratos.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabase {

    Connection getConnection();

    IDatabase connect() throws SQLException;

    IDatabase disconnect() throws SQLException;

    IQuery query(String table);

    IQuery query(String table, String alias);

    ResultSet getResultSet(IQuery iQuery, Object... objects) throws SQLException;

    void execute(IQuery iQuery, Object... objects) throws SQLException;

    IDatabase close(ResultSet resultSet) throws SQLException;
}
