package app.persistence;

import app.dto.OrderCustomerDTO;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.Main.connectionPool;
import static org.junit.jupiter.api.Assertions.*;

class getAllOrderCustomerDTOsMapper {

    @BeforeAll
    static void setUpBeforeAllClass() throws SQLException {
        {
         /* Sets up a connectionPool using empty parameters because we want the main getInstance method to use the hidden environment variabels so digital ocean username, password, and ip are hidden
        when we display the project on Github for CramerShoupPublicKeyParameters display. */
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        // same as prepare statement because we are just making an intern testing
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                // The test schema is already created, so we only need to delete/create test tables
                statement.execute("DROP TABLE IF EXISTS test_getallordercustomerdtos.orders");
                statement.execute("DROP TABLE IF EXISTS test_getallordercustomerdtos.customer_information");

// removes the sequence nextval(order_id) in orders table and the same for customer_id in customer_information
                statement.execute("DROP SEQUENCE IF EXISTS test_getallordercustomerdtos.orders_order_id_seq CASCADE;");
                statement.execute("DROP SEQUENCE IF EXISTS test_getallordercustomerdtos.customer_information_customer_id_seq CASCADE;");

                // Create tables as copy of original public schema structure
                statement.execute("CREATE TABLE test_getallordercustomerdtos.customer_information AS (SELECT * from public.customer_information) WITH NO DATA");
                statement.execute("CREATE TABLE test_getallordercustomerdtos.orders AS (SELECT * from public.orders) WITH NO DATA");

                // Create sequences for auto generating id's for users and orders
                statement.execute("CREATE SEQUENCE test_getallordercustomerdtos.customer_information_customer_id_seq");
                statement.execute("ALTER TABLE test_getallordercustomerdtos.customer_information ALTER COLUMN customer_id SET DEFAULT nextval('test_getallordercustomerdtos.customer_information_customer_id_seq')");


                statement.execute("CREATE SEQUENCE test_getallordercustomerdtos.orders_order_id_seq");
                statement.execute("ALTER TABLE test_getallordercustomerdtos.orders ALTER COLUMN order_id SET DEFAULT nextval('test_getallordercustomerdtos.orders_order_id_seq')");
            } catch (SQLException e) {
                e.printStackTrace();
                fail("SQL string didn´t match database");
            }
        }
    }
    }

    @BeforeEach
    void setUp() {
        try (Connection connection = connectionPool.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test_getallordercustomerdtos.orders");
                stmt.execute("DELETE FROM test_getallordercustomerdtos.customer_information");

                stmt.execute("INSERT INTO test_getallordercustomerdtos.customer_information (first_name, last_name, address, phone_number, customer_email, zip_code)" +
                        "VALUES  ('Benny', 'Jensen', 'jernbane alle 4', '12312332','bygselv@stort.dk', '2800'), ('Bøje', 'Lassen', 'Vestergade 43', '54334566', 'byg@nu.dk', '2800')");

                        stmt.execute("INSERT INTO test_getallordercustomerdtos.orders (customer_id, request_id, order_totalprice, order_status) " +
                        "VALUES ( 1, 1, 24000, 'Payed')");
                stmt.execute("INSERT INTO test_getallordercustomerdtos.orders (customer_id, request_id, order_totalprice, order_status) " +
                        "VALUES ( 2, 2, 36000, 'Send')");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test_getallordercustomerdtos.orders_order_id_seq', COALESCE((SELECT MAX(request_id) + 1 FROM test_getallordercustomerdtos.orders), 1), false)");
                stmt.execute("SELECT setval('test_getallordercustomerdtos.customer_information_customer_id_seq', COALESCE((SELECT MAX(customer_id) + 1 FROM test_getallordercustomerdtos.customer_information), 1), false)");

            }
        } catch (SQLException e) {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void getAllCustomerDTOs() {

        OrderCustomerDTO actualRequest1;
        OrderCustomerDTO actualRequest2;

// Arrange Makes two OrderCustomerDTO with the expected values
        OrderCustomerDTO expectedDTO1 = new OrderCustomerDTO(1,24000,"Payed","Benny","Jensen", "12312332", "bygselv@stort.dk");
        OrderCustomerDTO expectedDTO2 = new OrderCustomerDTO(2,36000,"Send","Bøje","Lassen", "54334566","byg@nu.dk" );

        // Act Look after Orders rows in the test database to make DTO objects that correspond with  customers in customer_information
       try {
           ArrayList<OrderCustomerDTO> actualList = OrderAndDetailsDTOMapper.getAllOrderCustomerDTOsMapper();

           actualRequest1 = actualList.get(0);
           actualRequest2 = actualList.get(1);

       } catch (DatabaseException e) {
           throw new RuntimeException(e);
       }
       // Assert Check if the expected results match with data from the database
        assertEquals(expectedDTO1, actualRequest1);
        assertEquals(expectedDTO2, actualRequest2);
    }
}