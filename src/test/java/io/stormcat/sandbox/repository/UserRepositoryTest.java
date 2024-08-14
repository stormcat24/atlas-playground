package io.stormcat.sandbox.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.stormcat.sandbox.entity.User;
import io.stormcat.sandbox.testutil.MySQLExtension;
import io.stormcat.sandbox.testutil.TestDataLoader;

@ExtendWith(MySQLExtension.class)
public class UserRepositoryTest {

    @Test
    public void test_findById(Connection connection) throws Exception {
        TestDataLoader.loadSqlFile(connection, "/UserRepositoryTest/data.sql");

        UserRepository userRepository = new UserRepository(connection);
        Optional<User> actual = userRepository.findById(1);
        
        assertTrue(actual.isPresent());
        actual.ifPresent(user -> {
            assertEquals(1, user.getId());
            assertEquals("John Doe", user.getName());
        });
    }
}
