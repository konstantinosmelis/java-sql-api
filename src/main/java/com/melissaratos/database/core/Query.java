package com.melissaratos.database.core;

import com.melissaratos.database.api.IDatabase;
import com.melissaratos.database.api.IQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Query implements IQuery {

    private final IDatabase database;
    private final String table;
    private String insert;
    private String select;
    private String update;
    private String delete;
    private String join;
    private String where;
    private String having;
    private String groupBy;
    private String orderBy;
    private String limit;

    public Query(IDatabase database, String table) {
        this.database = database;
        this.table = table;
    }

    @Override
    public IQuery insertInto(String[] columns, String... values) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        this.insert = "INSERT INTO " + this.table + " (" + String.join(",", columns) + ") VALUES (" + String.join(",", values) + ")";
        return this;
    }

    @Override
    public IQuery insertInto(String[] columns, List<String> values) throws IllegalCallerException {
        return this.insertInto(columns, values.toArray(new String[0]));
    }

    @Override
    public IQuery insertInto(List<String> columns, String... values) throws IllegalCallerException {
        return this.insertInto(columns.toArray(new String[0]), values);
    }

    @Override
    public IQuery insertInto(List<String> columns, List<String> values) throws IllegalCallerException {
        return this.insertInto(columns.toArray(new String[0]), values.toArray(new String[0]));
    }

    @Override
    public IQuery select(String... columns) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        this.select = "SELECT " + String.join(",", columns) + " FROM " + this.table;
        return this;
    }

    @Override
    public IQuery select(List<String> columns) throws IllegalCallerException {
        return this.select(columns.toArray(new String[0]));
    }

    @Override
    public IQuery update(String[] columns, String... values) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        if(columns.length != values.length)
            throw new IllegalArgumentException();
        String[] updates = new String[columns.length];
        for(int i = 0; i < columns.length; i++)
            updates[i] = columns[i] + "=" + values[i];
        this.update = "UPDATE " + this.table + " SET " + String.join(",", updates);
        return this;
    }

    @Override
    public IQuery update(String[] columns, List<String> values) throws IllegalCallerException {
        return this.update(columns, values.toArray(new String[0]));
    }

    @Override
    public IQuery update(List<String> columns, String... values) throws IllegalCallerException {
        return this.update(columns.toArray(new String[0]), values);
    }

    @Override
    public IQuery update(List<String> columns, List<String> values) throws IllegalCallerException {
        return this.update(columns.toArray(new String[0]), values.toArray(new String[0]));
    }

    @Override
    public IQuery delete() throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        this.delete = "DELETE FROM " + this.table;
        return this;
    }

    @Override
    public IQuery join(String table, String condition) {
        return this.join(Join.RIGHT, table, condition);
    }

    @Override
    public IQuery join(String table, String alias, String condition) {
        return this.join(Join.RIGHT, table, alias, condition);
    }

    @Override
    public IQuery join(Join join, String table, String condition) {
        if(this.join == null)
            this.join = "";
        this.join += " " + join.toString() + " " + this.table + " ON " + condition;
        return this;
    }

    @Override
    public IQuery join(Join join, String table, String alias, String condition) {
        return this.join(join, table + " AS " + alias, condition);
    }

    @Override
    public IQuery where(String where) {
        if(this.where == null)
            this.where = " WHERE";
        this.where += " " + where;
        return this;
    }

    @Override
    public IQuery where(String key, String condition, String value) {
        return this.where(key + " " + condition + " " + value);
    }

    @Override
    public IQuery where(String key, WhereCondition condition, String value) {
        return this.where(key, condition.toString(), value);
    }

    @Override
    public IQuery where(String key, String condition, IQuery iQuery) {
        return this.where(key, condition, "(" + iQuery.toString() + ")");
    }

    @Override
    public IQuery where(String key, WhereCondition condition, IQuery iQuery) {
        return this.where(key, condition.toString(), iQuery);
    }

    @Override
    public IQuery where(Logic logic, String where) {
        return this.where(logic.toString() + " " + where);
    }

    @Override
    public IQuery where(Logic logic, String key, String condition, String value) {
        return this.where(logic, key + " " + condition + " " + value);
    }

    @Override
    public IQuery where(Logic logic, String key, WhereCondition condition, String value) {
        return this.where(logic, key, condition.toString(), value);
    }

    @Override
    public IQuery where(Logic logic, String key, String condition, IQuery iQuery) {
        return this.where(logic, key, condition, "(" + iQuery + ")");
    }

    @Override
    public IQuery where(Logic logic, String key, WhereCondition condition, IQuery iQuery) {
        return this.where(logic, key, condition.toString(), iQuery);
    }

    @Override
    public IQuery having(String having) {
        if(this.having == null)
            this.having = " HAVING";
        this.having += " " + having;
        return this;
    }

    @Override
    public IQuery having(String key, String condition, String value) {
        return this.having(key + " " + condition + " " + value);
    }

    @Override
    public IQuery having(String key, WhereCondition condition, String value) {
        return this.having(key, condition.toString(), value);
    }

    @Override
    public IQuery having(String key, String condition, IQuery iQuery) {
        return this.having(key, condition, "(" + iQuery.toString() + ")");
    }

    @Override
    public IQuery having(String key, WhereCondition condition, IQuery iQuery) {
        return this.having(key, condition.toString(), iQuery);
    }

    @Override
    public IQuery having(Logic logic, String having) {
        return this.having(logic.toString() + " " + having);
    }

    @Override
    public IQuery having(Logic logic, String key, String condition, String value) {
        return this.having(logic, key + " " + condition + " " + value);
    }

    @Override
    public IQuery having(Logic logic, String key, WhereCondition condition, String value) {
        return this.having(logic, key, condition.toString(), value);
    }

    @Override
    public IQuery having(Logic logic, String key, String condition, IQuery iQuery) {
        return this.having(logic, key, condition, "(" + iQuery.toString() + ")");
    }

    @Override
    public IQuery having(Logic logic, String key, WhereCondition condition, IQuery iQuery) {
        return this.having(logic, key, condition.toString(), iQuery);
    }

    @Override
    public IQuery groupBy(String... columns) {
        if(this.groupBy == null)
            this.groupBy = " GROUP BY";
        this.groupBy += " " + String.join(",", columns);
        return this;
    }

    @Override
    public IQuery groupBy(List<String> columns) {
        return this.groupBy(columns.toArray(new String[0]));
    }

    @Override
    public IQuery orderBy(String column) {
        if(this.orderBy == null)
            this.orderBy = " ORDER BY";
        this.orderBy += " " + column;
        return this;
    }

    @Override
    public IQuery limit(int count) {
        if(this.limit == null)
            this.limit = " LIMIT " + count;
        return this;
    }

    @Override
    public IQuery limit(int count, int offset) {
        if(this.limit == null)
            this.limit = " LIMIT " + offset + "," + count;
        return this;
    }

    @Override
    public ResultSet getResultSet(Object... objects) throws SQLException {
        return this.database.getResultSet(this, objects);
    }

    @Override
    public void execute(Object... objects) throws SQLException {
        this.database.execute(this, objects);
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        if(this.insert != null) {
            builder.append(this.insert);
            return builder.toString();
        }
        else if(this.select != null)
            builder.append(this.select);
        else if(this.update != null)
            builder.append(this.update);
        else if(this.delete != null)
            builder.append(this.delete);

        if(this.join != null)
            builder.append(this.join);
        if(this.where != null)
            builder.append(this.where);
        if(this.groupBy != null)
            builder.append(this.groupBy);
        if(this.having != null)
            builder.append(this.having);
        if(this.orderBy != null)
            builder.append(this.orderBy);
        if(this.limit != null)
            builder.append(this.limit);

        return builder.toString();
    }

    @Override
    public String toString() {
        return this.build();
    }

    private boolean queryIsEmpty() {
        return this.insert == null || this.select == null || this.update == null || this.delete == null;
    }
}
