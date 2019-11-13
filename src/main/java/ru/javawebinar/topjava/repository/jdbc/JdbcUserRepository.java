package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final RowMapper<Role> ROLE_ROW_MAPPER = new RowMapper<Role>() {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            String roleName = rs.getString("role");
            if (roleName != null) {
                return Role.valueOf(roleName);
            }
            return null;
        }
    };
    private static final ResultSetExtractor<List<User>> USER_EXTRACTOR = new ResultSetExtractor<List<User>>() {
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> userMap = new HashMap<>();
            int row = 0;
            while (rs.next()) {
                final int finalRow = row;
                Integer userId = rs.getInt("id");
                User user = userMap.computeIfAbsent(userId, key -> {
                    User user1 = null;
                    try {
                        user1 = USER_ROW_MAPPER.mapRow(rs, finalRow);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    user1.setRoles(EnumSet.noneOf(Role.class));
                    return user1;
                });
                if (user == null) {
                    continue;
                }
                Role userRole = ROLE_ROW_MAPPER.mapRow(rs, row);
                if (userRole != null) {
                    user.getRoles().add(userRole);
                }
                row++;
            }
            return userMap.values().stream().collect(Collectors.toList());
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user.getId(), user.getRoles());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            updateRoles(user.getId(), user.getRoles());
        }
        return user;
    }

    private void insertRoles(int userId, Collection<Role> roles) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) values(?,?)",
                roles,
                200,
                (ps, argument) -> {
                    ps.setInt(1, userId);
                    ps.setString(2, argument.name());
                });
    }

    private void updateRoles(int userId, Collection<Role> roles) {
        // Select all user's roles from db
        List<Role> dbRoles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", ROLE_ROW_MAPPER, userId);
        // Copy collection to list
        List<Role> userRoles = new ArrayList<>(roles);

        // remove all items of userRoles which already exist in database
        Iterator<Role> dbIterator = dbRoles.iterator();
        while (dbIterator.hasNext()) {
            Role role = dbIterator.next();
            if (userRoles.contains(role)) {
                userRoles.remove(role);
                dbIterator.remove();
            }
        }

        // delete roles
        if (!dbRoles.isEmpty()) {
            jdbcTemplate.batchUpdate("DELETE FROM user_roles WHERE user_id=? AND role=?",
                    dbRoles,
                    200,
                    (ps, argument) -> {
                        ps.setInt(1, userId);
                        ps.setString(2, argument.name());
                    });
        }

        // insert roles
        if (!userRoles.isEmpty()) {
            insertRoles(userId, userRoles);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON r.user_id = u.id WHERE u.id=?", USER_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON r.user_id = u.id WHERE email=?", USER_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles r ON r.user_id = u.id ORDER BY name, email", USER_EXTRACTOR);
    }
}
