package com.example.expensetrackerapi.repository;

import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.expensetrackerapi.domain.User;
import com.example.expensetrackerapi.exceptions.EtAuthException;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private static final String SQL_CREATE = "INSERT INTO et_users(first_name,last_name, email, password) VALUES( ?, ?, ?, ?) ";
	private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";
	private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM ET_USERS WHERE USER_ID = ?";
	private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD " + "FROM ET_USERS WHERE EMAIL = ?";

	@Autowired
	JdbcTemplate jdbcTemplate;

	/*
	 * SimpleJdbcInsert messageInsert;
	 * 
	 * @Autowired public UserRepositoryImpl(DataSource dataSource) {
	 * System.out.println(dataSource); messageInsert = new
	 * SimpleJdbcInsert(dataSource).withTableName("et_users").
	 * usingGeneratedKeyColumns("user_id"); System.out.println("here"); }
	 */

	@Override
	public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			System.out.println("here");
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[] {"user_id"});
				ps.setString(1, firstName);
				ps.setString(2, lastName);
				ps.setString(3, email);
				ps.setString(4, hashedPassword);
				return ps;
			}, keyHolder);
			// System.out.println(keyHolder.getKey());
			return keyHolder.getKey().intValue();
		} catch (Exception e) {
			// System.out.println(e);
			throw new EtAuthException(e.toString());
		}
	}

	@Override
	public User findByEmailAndPassword(String email, String password) throws EtAuthException {
		try {
			User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, new Object[] {email}, userRowMapper);
			
			if(!BCrypt.checkpw(password, user.getPassword())) {
				throw new EtAuthException("Invalid email/password");
			}
			return user;
		} catch(EmptyResultDataAccessException e) {
			throw new EtAuthException(e.toString());
		}
	}

	@Override
	public Integer getCountByEmail(String email) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, new Object[] { email });
	}

	@Override
	public User findById(Integer userId) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, new Object[] { userId });
	}

	private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
		return new User(rs.getInt("USER_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"),
				rs.getString("EMAIL"), rs.getString("PASSWORD"));
	});

}
