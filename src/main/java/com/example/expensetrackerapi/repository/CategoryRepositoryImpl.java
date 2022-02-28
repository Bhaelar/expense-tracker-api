package com.example.expensetrackerapi.repository;

import com.example.expensetrackerapi.domain.Category;
import com.example.expensetrackerapi.domain.User;
import com.example.expensetrackerapi.exceptions.EtBadRequestException;
import com.example.expensetrackerapi.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository{

    private static final String SQL_FIND_ALL = "SELECT * FROM ET_CATEGORIES WHERE USER_ID = ?";
    private static final String SQL_CREATE = "INSERT INTO ET_CATEGORIES (USER_ID, TITLE, DESCRIPTION) VALUES(?, ?, ?)";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM ET_CATEGORIES WHERE USER_ID = ? AND ID = ?";
    private static final String SQL_UPDATE = "UPDATE ET_CATEGORIES SET TITLE = ?, DESCRIPTION = ? " + "WHERE USER_ID = ? AND ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> findAll(Integer userId) throws EtResourceNotFoundException {
        try {

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_FIND_ALL, new Object[]{userId});
            List<Category> categoryList = new ArrayList<>();

            for (Map row : rows) {
                Category category = new Category();
                category.setId((Integer) row.get("ID"));
                category.setUserId((Integer) row.get("USER_ID"));
                category.setTitle((String) row.get("TITLE"));
                category.setDescription((String) row.get("DESCRIPTION"));
                category.setTotalExpense((Double) row.get("TOTAL_EXPENSE"));
                categoryList.add(category);
            }
            return categoryList;
        } catch(Exception e) {
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_FIND_BY_ID, new Object[]{userId, categoryId});

            Category category = new Category();

            for (Map row : rows) {
                category.setId((Integer) row.get("ID"));
                category.setUserId((Integer) row.get("USER_ID"));
                category.setTitle((String) row.get("TITLE"));
                category.setDescription((String) row.get("DESCRIPTION"));
                category.setTotalExpense((Double) row.get("TOTAL_EXPENSE"));
            }
            return category;
        } catch(Exception e) {
            throw new EtResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws EtBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);
            return keyHolder.getKey().intValue();

        }catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{category.getTitle(), category.getDescription(), userId, categoryId});
        } catch (Exception e) {
            throw new EtBadRequestException(e.getMessage());
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId) {

    }

    private RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> {
        return new Category(rs.getInt("ID"), rs.getInt("USER_ID"), rs.getString("TITLE"),
                rs.getString("DESCRIPTION"), rs.getDouble("TOTAL_EXPENSE"));
    });
}
