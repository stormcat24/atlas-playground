package io.stormcat.sandbox.testutil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import io.stormcat.sandbox.util.FileLoader;

public class TestDataLoader {

    public static void loadSqlFile(Connection connection, String sqlPath) {
        String sql = FileLoader.load(sqlPath);
        loadSql(connection, sql);
    }

    public static void loadSql(Connection connection, String sql) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
