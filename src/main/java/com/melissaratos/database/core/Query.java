package com.melissaratos.database.core;

import com.melissaratos.database.api.IDatabase;
import com.melissaratos.database.api.IQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Query implements IQuery {

    private final IDatabase database;
    private final String table;
    private String insert;
    private String select;
    private String update;
    private String delete;
    private String create;
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
    public IQuery insertInto(String[] columns, Object... values) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        String vals = Arrays.stream(values).map(obj -> (obj instanceof String ? String.format("'%s'", obj) : String.valueOf(obj))).collect(Collectors.joining(", "));
        this.insert = "INSERT INTO " + this.table + " (" + String.join(",", columns) + ") VALUES (" + vals + ")";
        return this;
    }

    @Override
    public IQuery insertInto(String[] columns, List<Object> values) throws IllegalCallerException {
        return this.insertInto(columns, values.toArray(new Object[0]));
    }

    @Override
    public IQuery insertInto(List<String> columns, Object... values) throws IllegalCallerException {
        return this.insertInto(columns.toArray(new String[0]), values);
    }

    @Override
    public IQuery insertInto(List<String> columns, List<Object> values) throws IllegalCallerException {
        return this.insertInto(columns.toArray(new String[0]), values.toArray(new Object[0]));
    }

    @Override
    public IQuery select(String... columns) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        this.select = String.format("SELECT %s FROM %s", String.join(", ", columns), this.table);
        return this;
    }

    @Override
    public IQuery select(List<String> columns) throws IllegalCallerException {
        return this.select(columns.toArray(new String[0]));
    }

    @Override
    public IQuery update(String[] columns, Object... values) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        if(columns.length != values.length)
            throw new IllegalArgumentException();
        String[] updates = new String[columns.length];
        for(int i = 0; i < columns.length; i++)
            updates[i] = columns[i] + "=" + (values[i] instanceof String ? String.format("'%s'", values[i]) : values[i]);
        this.update = String.format("UPDATE %s SET %s", this.table, String.join(", ", updates));
        return this;
    }

    @Override
    public IQuery update(String[] columns, List<Object> values) throws IllegalCallerException {
        return this.update(columns, values.toArray(new Object[0]));
    }

    @Override
    public IQuery update(List<String> columns, Object... values) throws IllegalCallerException {
        return this.update(columns.toArray(new String[0]), values);
    }

    @Override
    public IQuery update(List<String> columns, List<Object> values) throws IllegalCallerException {
        return this.update(columns.toArray(new String[0]), values.toArray(new Object[0]));
    }

    @Override
    public IQuery delete() throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        this.delete = String.format("DELETE FROM %s", this.table);
        return this;
    }

    @Override
    public IQuery create(String[] columns, String... primaryKey) throws IllegalCallerException {
        if(!this.queryIsEmpty())
            throw new IllegalCallerException();
        this.create = String.format("CREATE TABLE %s (%s, PRIMARY KEY (%s))", this.table, String.join(", ", columns), String.join(", ", primaryKey));
        return this;
    }

    @Override
    public IQuery create(String[] columns, List<String> primaryKey) throws IllegalCallerException {
        return this.create(columns, primaryKey.toArray(new String[0]));
    }

    @Override
    public IQuery create(String[] columns, String[] types, String... primaryKey) throws IllegalCallerException {
        String[] data = new String[columns.length];
        for(int i = 0; i < columns.length; i++)
            data[i] = String.format("%s %s", columns[i], types[i]);
        return this.create(data, primaryKey);
    }

    @Override
    public IQuery create(String[] columns, String[] types, List<String> primaryKey) throws IllegalCallerException {
        return this.create(columns, types, primaryKey.toArray(new String[0]));
    }

    @Override
    public IQuery create(String[] columns, List<String> types, String... primaryKey) throws IllegalCallerException {
        return this.create(columns, types.toArray(new String[0]), primaryKey);
    }

    @Override
    public IQuery create(String[] columns, List<String> types, List<String> primaryKey) {
        return this.create(columns, types.toArray(new String[0]), primaryKey.toArray(new String[0]));
    }

    @Override
    public IQuery create(List<String> columns, String... primaryKey) throws IllegalCallerException {
        return this.create(columns.toArray(new String[0]), primaryKey);
    }

    public IQuery create(List<String> columns, List<String> primaryKey) throws IllegalCallerException {
        return this.create(columns.toArray(new String[0]), primaryKey.toArray(new String[0]));
    }

    @Override
    public IQuery create(List<String> columns, String[] types, String... primaryKey) throws IllegalCallerException {
        return this.create(columns.toArray(new String[0]), types, primaryKey);
    }

    @Override
    public IQuery create(List<String> columns, List<String> types, String... primaryKey) throws IllegalCallerException {
        return this.create(columns.toArray(new String[0]), types.toArray(new String[0]), primaryKey);
    }

    @Override
    public IQuery create(List<String> columns, String[] types, List<String> primaryKey) {
        return this.create(columns.toArray(new String[0]), types, primaryKey.toArray(new String[0]));
    }

    @Override
    public IQuery create(List<String> columns, List<String> types, List<String> primaryKey) {
        return this.create(columns.toArray(new String[0]), types.toArray(new String[0]), primaryKey.toArray(new String[0]));
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
        this.join += String.format(" %s %s ON %s", join.toString(), this.table, condition);
        return this;
    }

    @Override
    public IQuery join(Join join, String table, String alias, String condition) {
        return this.join(join, String.format("%s AS %s", table, alias), condition);
    }

    @Override
    public IQuery where(String where) {
        if(this.where == null)
            this.where = " WHERE";
        this.where += String.format(" %s", where);
        return this;
    }

    @Override
    public IQuery where(String key, String condition, Object value) {
        return this.where(String.format("%s %s %s", key, condition, value));
    }

    @Override
    public IQuery where(String key, WhereCondition condition, Object value) {
        return this.where(key, condition.toString(), value);
    }

    @Override
    public IQuery where(String key, String condition, IQuery iQuery) {
        return this.where(key, condition, String.format("(%s)", iQuery.toString()));
    }

    @Override
    public IQuery where(String key, WhereCondition condition, IQuery iQuery) {
        return this.where(key, condition.toString(), iQuery);
    }

    @Override
    public IQuery where(WhereCondition condition, IQuery iQuery) {
        return this.where(String.format("%s (%s)", condition.toString(), iQuery.toString()));
    }

    @Override
    public IQuery where(Logic logic, String where) {
        return this.where(String.format("%s %s", logic.toString(), where));
    }

    @Override
    public IQuery where(Logic logic, String key, String condition, Object value) {
        return this.where(logic, String.format("%s %s %s", key, condition, value));
    }

    @Override
    public IQuery where(Logic logic, String key, WhereCondition condition, Object value) {
        return this.where(logic, key, condition.toString(), value);
    }

    @Override
    public IQuery where(Logic logic, String key, String condition, IQuery iQuery) {
        return this.where(logic, key, condition, String.format("(%s)", iQuery));
    }

    @Override
    public IQuery where(Logic logic, String key, WhereCondition condition, IQuery iQuery) {
        return this.where(logic, key, condition.toString(), iQuery);
    }

    @Override
    public IQuery having(String having) {
        if(this.having == null)
            this.having = " HAVING";
        this.having += String.format(" %s", having);
        return this;
    }

    @Override
    public IQuery having(String key, String condition, Object value) {
        return this.having(String.format("%s %s %s", key, condition, value));
    }

    @Override
    public IQuery having(String key, WhereCondition condition, Object value) {
        return this.having(key, condition.toString(), value);
    }

    @Override
    public IQuery having(String key, String condition, IQuery iQuery) {
        return this.having(key, condition, String.format("(%s)", iQuery.toString()));
    }

    @Override
    public IQuery having(String key, WhereCondition condition, IQuery iQuery) {
        return this.having(key, condition.toString(), iQuery);
    }

    @Override
    public IQuery having(Logic logic, String having) {
        return this.having(String.format("%s %s", logic.toString(), having));
    }

    @Override
    public IQuery having(Logic logic, String key, String condition, Object value) {
        return this.having(logic, String.format("%s %s %s", key, condition, value));
    }

    @Override
    public IQuery having(Logic logic, String key, WhereCondition condition, Object value) {
        return this.having(logic, key, condition.toString(), value);
    }

    @Override
    public IQuery having(Logic logic, String key, String condition, IQuery iQuery) {
        return this.having(logic, key, condition, String.format("(%s)", iQuery.toString()));
    }

    @Override
    public IQuery having(Logic logic, String key, WhereCondition condition, IQuery iQuery) {
        return this.having(logic, key, condition.toString(), iQuery);
    }

    @Override
    public IQuery groupBy(String... columns) {
        if(this.groupBy == null)
            this.groupBy = " GROUP BY";
        this.groupBy += String.format(" %s", String.join(",", columns));
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
        this.orderBy += String.format(" %s", column);
        return this;
    }

    @Override
    public IQuery limit(int count) {
        if(this.limit == null)
            this.limit = String.format(" LIMIT %d", count);
        return this;
    }

    @Override
    public IQuery limit(int count, int offset) {
        if(this.limit == null)
            this.limit = String.format(" LIMIT %d,%d", offset, count);
        return this;
    }

    @Override
    public ResultSet getResultSet(Object... objects) throws SQLException {
        return this.database.getResult(this, objects);
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
        else if(this.create != null) {
            builder.append(this.create);
            return builder.toString();
        }

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
