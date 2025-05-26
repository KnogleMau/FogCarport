package app.persistence;

import app.entities.CarportRequest;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


import static app.Main.connectionPool;
import static org.junit.jupiter.api.Assertions.*;

// Integration test for RequestMapper
class RequestMapperTest {

    @BeforeAll
    static void setUpBeforeAllClass() throws SQLException {

        {
             /* Sets up a connectionPool using empty parameters because we want the main getInstance method to use the hidden environment variabels so digital ocean username, password, and ip are hidden
        when we display the project on Github for CramerShoupPublicKeyParameters display. */
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            try (Connection connection = connectionPool.getConnection()) {
                // same as prepare statement because we are just making an intern testing
                try (Statement stmt = connection.createStatement()) {
                    // The test schema is already created, so we only need to delete/create test tables
                    stmt.execute("DROP TABLE IF EXISTS test_requests.customer_information");
                    stmt.execute("DROP TABLE IF EXISTS test_requests.requests");

                    stmt.execute("DROP SEQUENCE IF EXISTS test_requests.customer_information_customer_id_seq CASCADE;");
                    stmt.execute("DROP SEQUENCE IF EXISTS test_requests.requests_request_id_seq CASCADE;");

                    // Create tables as copy of original public schema structure
                    stmt.execute("CREATE TABLE test_requests.customer_information AS (SELECT * from public.customer_information) WITH NO DATA");
                    stmt.execute("CREATE TABLE test_requests.requests AS (SELECT * from public.requests) WITH NO DATA");

                    // Create sequences for auto generating id's for users and orders
                    stmt.execute("CREATE SEQUENCE test_requests.customer_information_customer_id_seq");
                    stmt.execute("ALTER TABLE test_requests.customer_information ALTER COLUMN customer_id SET DEFAULT nextval('test_requests.customer_information_customer_id_seq')");

                    stmt.execute("CREATE SEQUENCE test_requests.requests_request_id_seq");
                    stmt.execute("ALTER TABLE test_requests.requests ALTER COLUMN request_id SET DEFAULT nextval('test_requests.requests_request_id_seq')");
                }
             catch (SQLException e) {
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
                stmt.execute("DELETE FROM test_requests.requests");
                stmt.execute("DELETE FROM test_requests.customer_information");

                stmt.execute("INSERT INTO test_requests.customer_information (first_name, last_name, address, phone_number, customer_email, zip_code)" +
                        "VALUES  ('Benny', 'Jensen', 'jernbane alle 4', '12312332','bygselv@stort.dk', '2800'), ('Bøje', 'Lassen', 'Vestergade 43', '54334566', 'byg@nu.dk', '2800')");

                stmt.execute("INSERT INTO test_requests.requests (request_length, request_width, request_height, customer_id) " +
                        "VALUES ( 780, 600, 210, 1)");
                stmt.execute("INSERT INTO test_requests.requests (request_length, request_width, request_height, customer_id) " +
                        "VALUES ( 450, 300, 210, 2)");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test_requests.requests_request_id_seq', COALESCE((SELECT MAX(request_id) + 1 FROM test_requests.requests), 1), false)");
                stmt.execute("SELECT setval('test_requests.customer_information_customer_id_seq', COALESCE((SELECT MAX(customer_id) + 1 FROM test_requests.customer_information), 1), false)");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Sql didn´t match database");
        }
    }


    @Test
    void requestMapper() throws DatabaseException {
        // Arrange   Makes to Request object with the expected values
        CarportRequest expectedRequest1 = new CarportRequest(1, 780, 600, 210, 1);
        CarportRequest expectedRequest2 = new CarportRequest(2, 450, 300, 210, 2);

        // Act Look after request rows in test database and make objects from the data
        CarportRequest actualRequest1 = RequestMapper.getCarportRequest(1);
        CarportRequest actualRequest2 = RequestMapper.getCarportRequest(2);

        // Assert  Check if the expected results match with requests in the database
        assertEquals(expectedRequest1.getRequestID(), actualRequest1.getRequestID());
        assertEquals(expectedRequest1.getRequestLength(), actualRequest1.getRequestLength());
        assertEquals(expectedRequest1.getRequestWidth(), actualRequest1.getRequestWidth());
        assertEquals(expectedRequest1.getRequestHeight(), actualRequest1.getRequestHeight());
        assertEquals(expectedRequest1.getCustomerID(), actualRequest1.getCustomerID());

        assertEquals(expectedRequest2.getRequestID(), actualRequest2.getRequestID());
        assertEquals(expectedRequest2.getRequestLength(), actualRequest2.getRequestLength());
        assertEquals(expectedRequest2.getRequestWidth(), actualRequest2.getRequestWidth());
        assertEquals(expectedRequest2.getRequestHeight(), actualRequest2.getRequestHeight());
        assertEquals(expectedRequest2.getCustomerID(), actualRequest2.getCustomerID());
    }

}
