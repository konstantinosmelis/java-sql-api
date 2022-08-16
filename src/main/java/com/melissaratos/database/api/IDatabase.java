package com.melissaratos.database.api;

import com.melissaratos.database.api.exception.MissingAnnotationException;
import com.melissaratos.database.api.exception.MissingPrimaryKeyException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabase {

    Connection getConnection();

    IDatabase connect() throws SQLException;

    IDatabase disconnect() throws SQLException;

    IQuery query(String table);

    IQuery query(String table, String alias);

    ResultSet getResult(IQuery iQuery, Object... objects) throws SQLException;

    void execute(IQuery iQuery, Object... objects) throws SQLException;

    IDatabase close(ResultSet resultSet) throws SQLException;

    <T> void update(T table) throws MissingAnnotationException, IllegalAccessException, SQLException;

    <T> void build(T table) throws MissingAnnotationException, MissingPrimaryKeyException, SQLException;

    <T> void insert(T table) throws MissingAnnotationException, IllegalAccessException, SQLException;
}
