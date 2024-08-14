package io.stormcat.sandbox.testutil;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class MySQLExtension implements BeforeAllCallback, AfterAllCallback, ExtensionContext.Store.CloseableResource, ParameterResolver {

    private static final Logger logger = LoggerFactory.getLogger(MySQLExtension.class);

    private static MySQLContainer<?> mysql;

    private static GenericContainer<?> atlas;

    private Connection connection;

    public MySQLExtension() {
        Network network = Network.newNetwork();
        mysql = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("example")
            .withUsername("atlas")
            .withPassword("pass")
            .withNetwork(network)
            .withNetworkAliases("mysql");
        mysql.start();

        atlas = new GenericContainer<>("arigaio/atlas:0.26.0")
            .withFileSystemBind("db/schema/example.hcl", "/db/schema/example.hcl", BindMode.READ_ONLY)
            .withCommand(
                "schema", "apply",
                "--url", String.format("mysql://%s:%s@mysql:%d/%s", mysql.getUsername(), mysql.getPassword(), 3306, mysql.getDatabaseName()),
                "--to", "file://db/schema/example.hcl",
                "--auto-approve"
            )
            .withNetwork(network)
            .withLogConsumer(new Slf4jLogConsumer(logger));

        atlas.start();
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String jdbcUrl = mysql.getJdbcUrl();
        String username = mysql.getUsername();
        String password = mysql.getPassword();
        connection = DriverManager.getConnection(jdbcUrl, username, password);
        context.getStore(ExtensionContext.Namespace.GLOBAL).put("connection", connection);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public void close() throws Throwable {
        if (mysql != null) {
            mysql.stop();
        }
        if (atlas != null) {
            atlas.stop();
        }
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return connection;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Connection.class;
    }
   
}
