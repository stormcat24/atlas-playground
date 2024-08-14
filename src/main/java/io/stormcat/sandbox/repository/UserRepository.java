package io.stormcat.sandbox.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import io.stormcat.sandbox.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class UserRepository {
    
    private final Connection connection;

    @SneakyThrows
    public Optional<User> findById(Integer id) {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        st.setInt(1, id); 

        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
