package com.melissaratos.database.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IQuery {

    IQuery insertInto(String[] columns, String... values);

    IQuery insertInto(String[] columns, List<String> values);

    IQuery insertInto(List<String> columns, String... values);

    IQuery insertInto(List<String> columns, List<String> values);

    IQuery select(String... columns);

    IQuery select(List<String> columns);

    IQuery update(String[] columns, String... values);

    IQuery update(String[] columns, List<String> values);

    IQuery update(List<String> columns, String... values);

    IQuery update(List<String> columns, List<String> values);

    IQuery delete();

    IQuery join(String table, String condition);

    IQuery join(String table, String alias, String condition);

    IQuery join(Join join, String table, String condition);

    IQuery join(Join join, String table, String alias, String condition);

    IQuery where(String where);

    IQuery where(String key, String condition, String value);

    IQuery where(String key, WhereCondition condition, String value);

    IQuery where(String key, String condition, IQuery iQuery);

    IQuery where(String key, WhereCondition condition, IQuery iQuery);

    IQuery where(Logic logic, String where);

    IQuery where(Logic logic, String key, String condition, String value);

    IQuery where(Logic logic, String key, WhereCondition condition, String value);

    IQuery where(Logic logic, String key, String condition, IQuery iQuery);

    IQuery where(Logic logic, String key, WhereCondition condition, IQuery iQuery);

    IQuery having(String having);

    IQuery having(String key, String condition, String value);

    IQuery having(String key, WhereCondition condition, String value);

    IQuery having(String key, String condition, IQuery iQuery);

    IQuery having(String key, WhereCondition condition, IQuery iQuery);

    IQuery having(Logic logic, String having);

    IQuery having(Logic logic, String key, String condition, String value);

    IQuery having(Logic logic, String key, WhereCondition condition, String value);

    IQuery having(Logic logic, String key, String condition, IQuery iQuery);

    IQuery having(Logic logic, String key, WhereCondition condition, IQuery iQuery);

    IQuery groupBy(String... columns);

    IQuery groupBy(List<String> columns);

    IQuery orderBy(String column);

    IQuery limit(int count);

    IQuery limit(int count, int offset);

    ResultSet getResultSet(Object... objects) throws SQLException;

    void execute(Object... objects) throws SQLException;

    String build();

    enum Logic {
        AND,
        OR;
    }

    enum WhereCondition {
        IS,
        IS_NOT,
        IS_NULL,
        IN,
        NOT_IN,
        NOT_EXISTS,
        LIKE,
        ALL;

        @Override
        public String toString() {
            return super.toString().replace('_', ' ');
        }
    }

    enum Order {
        ASC,
        DESC;
    }

    enum Join {
        LEFT,
        RIGHT,
        INNER,
        CROSS,
        FULL;

        @Override
        public String toString() {
            return super.toString() + " JOIN";
        }
    }
}
