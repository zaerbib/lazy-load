package com.tuto.utils;

import net.datafaker.Faker;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EmbeddedPostgres {
    private DataSource dataSource;

    public record Product(int id,
                   String name,
                   double price,
                   int quantity) {}

    public void start() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        this.dataSource = ds;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("""
                        CREATE TABLE IF NOT EXISTS products (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            price DECIMAL(10,2) NOT NULL,
                            quantity INTEGER NOT NULL
                        )
                    """);
        } catch (SQLException e) {
            System.err.println("Connection and init failed !!!");
        }

        // init something here
        initDatabase();
    }

    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Get All Products failed !!!");
        }
        return products;
    }

    private void initDatabase() {
        Faker faker = new Faker();
        var products = IntStream.rangeClosed(1, 1000)
                .mapToObj(number -> createProduct(faker.commerce().productName(),
                        Double.parseDouble(faker.commerce().price()),
                        faker.number().numberBetween(1, 1000)))
                .toList();

        System.out.printf("Initialized with %+d products\n", products.size());
    }

    private int createProduct(String name, double price, int quantity) {
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Create product in database failed !!!");
        }
        return -1;
    }

}
