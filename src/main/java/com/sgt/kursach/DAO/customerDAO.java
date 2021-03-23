package com.sgt.kursach.DAO;

import com.sgt.kursach.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class customerDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public customerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> index() {
        return jdbcTemplate.query("SELECT * FROM Customer", new BeanPropertyRowMapper<>(Customer.class));
    }

    public Customer show(int id) {
        return jdbcTemplate.query("SELECT * FROM Customer WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Customer.class))
                .stream().findAny().orElse(null);
    }

    public void save(Customer customer) {
        jdbcTemplate.update("INSERT INTO Customer VALUES(1, ?, ?, ?, ?, ?, ?)",
                customer.getName(), customer.getPassword(),
                customer.getLogin(), customer.getAdress(),
                customer.getBirthday());
    }

    public void update(int id, Customer updatedCustomer) {
        jdbcTemplate.update("UPDATE Customer SET name=?, email=?, password=?, login=?, adress?, birthday=? WHERE id=?",
                updatedCustomer.getName(), updatedCustomer.getEmail(),
                updatedCustomer.getPassword(),updatedCustomer.getLogin(),
                updatedCustomer.getAdress(), updatedCustomer.getBirthday(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Customer WHERE id=?", id);
    }

    //////////////////////
    // Тестируем производительность
    //////////////////////

    public void testMultipleUpdate() {
        List<Customer> people = create1000People();

        long before = System.currentTimeMillis();

        for (Customer customer : people) {
            jdbcTemplate.update("INSERT INTO Customer VALUES(?, ?, ?, ?, ?, ?, ?)",
                    customer.getId(), customer.getName(),
                    customer.getPassword(), customer.getLogin(),
                    customer.getAdress(), customer.getBirthday());
        }

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    public void testBatchUpdate() {
        List<Customer> people = create1000People();

        long before = System.currentTimeMillis();

        jdbcTemplate.batchUpdate("INSERT INTO Customer VALUES(?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, people.get(i).getId());
                        ps.setString(2, people.get(i).getName());
                        ps.setString(3, people.get(i).getEmail());
                        ps.setString(4, people.get(i).getPassword());
                        ps.setString(4, people.get(i).getLogin());
                        ps.setString(4, people.get(i).getAdress());
                        ps.setDate(4, people.get(i).getBirthday());
                    }

                    @Override
                    public int getBatchSize() {
                        return people.size();
                    }
                });

        long after = System.currentTimeMillis();
        System.out.println("Time: " + (after - before));
    }

    private List<Customer> create1000People() {
        List<Customer> people = new ArrayList<>();

        for (int i = 0; i < 1000; i++)
            people.add(new Customer(i, "Имя" + i, "test" + i + "@mail.ru", "password to ID " + i, "LoginID " + i, "Улица " + i +
                    "-ая имени Пупкина",  Date.valueOf(LocalDate.of(1900+ i/100, Month.JULY, 12))));

        return people;
    }
}