package app.persistence;

import app.dto.OrderCustomerDTO;
import app.dto.OrderDetailsMaterialLengthDTO;
import app.exceptions.DatabaseException;
import jdk.jfr.StackTrace;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import static app.Main.connectionPool;


class getOrderDetailsMapperTest {

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
                    statement.execute("DROP TABLE IF EXISTS test_orderdetails_dto.material_lengths");
                    statement.execute("DROP TABLE IF EXISTS test_orderdetails_dto.material_list");
                    statement.execute("DROP TABLE IF EXISTS test_orderdetails_dto.order_details");
                    statement.execute("DROP TABLE IF EXISTS test_orderdetails_dto.orders");

// removes the sequence nextval(order_id) in orders table and the same for customer_id in customer_information
                    statement.execute("DROP SEQUENCE IF EXISTS test_orderdetails_dto.material_lengths_length_id_seq CASCADE;");
                    statement.execute("DROP SEQUENCE IF EXISTS test_orderdetails_dto.material_list_material_id_seq CASCADE;");
                    statement.execute("DROP SEQUENCE IF EXISTS test_orderdetails_dto.orders_order_id_seq CASCADE;");

                    // Create tables as copy of original public schema structure
                    statement.execute("CREATE TABLE test_orderdetails_dto.orders AS (SELECT * from public.orders) WITH NO DATA");
                    statement.execute("CREATE TABLE test_orderdetails_dto.order_details AS (SELECT * from public.order_details) WITH NO DATA");
                    statement.execute("CREATE TABLE test_orderdetails_dto.material_list AS (SELECT * from public.material_list) WITH NO DATA");
                    statement.execute("CREATE TABLE test_orderdetails_dto.material_lengths AS (SELECT * from public.material_lengths) WITH NO DATA");
                    // Create sequences for auto generating id's for users and orders
                    statement.execute("CREATE SEQUENCE test_orderdetails_dto.orders_order_id_seq");
                    statement.execute("ALTER TABLE test_orderdetails_dto.orders ALTER COLUMN order_id SET DEFAULT nextval('test_orderdetails_dto.orders_order_id_seq')");

                    statement.execute("CREATE SEQUENCE test_orderdetails_dto.material_list_material_id_seq");
                    statement.execute("ALTER TABLE test_orderdetails_dto.material_list ALTER COLUMN material_id SET DEFAULT nextval('test_orderdetails_dto.material_list_material_id_seq')");

                    statement.execute("CREATE SEQUENCE test_orderdetails_dto.material_lengths_length_id_seq");
                    statement.execute("ALTER TABLE test_orderdetails_dto.material_lengths ALTER COLUMN length_id SET DEFAULT nextval('test_orderdetails_dto.material_lengths_length_id_seq')");
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
            stmt.execute("DELETE FROM test_orderdetails_dto.material_lengths");
            stmt.execute("DELETE FROM test_orderdetails_dto.material_list");
            stmt.execute("DELETE FROM test_orderdetails_dto.order_details");
            stmt.execute("DELETE FROM test_orderdetails_dto.orders");

            stmt.execute("INSERT INTO test_orderdetails_dto.orders (customer_id, request_id, order_totalprice, order_status) " +
                    "VALUES ( 1, 1, 24000, 'Payed')");
            stmt.execute("INSERT INTO test_orderdetails_dto.order_details (order_id, material_id, quantity, length_id, detail_price) " +
                    "VALUES ( 1, 1, 2, 1, 55)");
            stmt.execute("INSERT INTO test_orderdetails_dto.order_details (order_id, material_id, quantity, length_id, detail_price) " +
                    "VALUES ( 1, 2, 7, 2, 55)");
            stmt.execute("INSERT INTO test_orderdetails_dto.order_details (order_id, material_id, quantity, length_id, detail_price) " +
                    "VALUES ( 1, 3, 4, 3, 59)");
            stmt.execute("INSERT INTO test_orderdetails_dto.material_list (material_id, material_name, material_type, material_quantity, material_unit, material_price, material_description) " +
                    "VALUES ( 1, 'Rem', 'Beam', 20,'stk',45,'Remme')");
            stmt.execute("INSERT INTO test_orderdetails_dto.material_list (material_id, material_name, material_type, material_quantity, material_unit, material_price, material_description) " +
                    "VALUES ( 2, 'Spær', 'Rafter', 20,'stk',45,'Spær, monteres')");
            stmt.execute("INSERT INTO test_orderdetails_dto.material_list (material_id, material_name, material_type, material_quantity, material_unit, material_price, material_description) " +
                    "VALUES ( 3, 'Stolpe', 'Post', 20,'stk',50,'Stolper nedgraves')");
            stmt.execute("INSERT INTO test_orderdetails_dto.material_lengths (length_id, material_id, material_length) " +
                    "VALUES ( 1, 1, 300)");
            stmt.execute("INSERT INTO test_orderdetails_dto.material_lengths (length_id, material_id, material_length) " +
                    "VALUES ( 1, 2, 300)");
            stmt.execute("INSERT INTO test_orderdetails_dto.material_lengths (length_id, material_id, material_length) " +
                    "VALUES ( 1, 3, 300)");
            // Set sequence to continue from the largest member_id
           stmt.execute("SELECT setval('orderdetails_dto.orders_order_id_seq', COALESCE((SELECT MAX(order_id) + 1 FROM test_orderdetails_dto.orders), 1), false)");
           stmt.execute("SELECT setval('orderdetails_dto.material_lengths_length_id_seq', COALESCE((SELECT MAX(length_id) + 1 FROM test_orderdetails_dto.material_lengths), 1), false)");
            stmt.execute("SELECT setval('orderdetails_dto.material_list_material_id_seq', COALESCE((SELECT MAX(material_id) + 1 FROM test_orderdetails_dto.material_list), 1), false)");

        }
    } catch (SQLException e) {
        System.out.println(e.getErrorCode());
        System.out.println(e.getMessage());
        fail("Database fejl: " + e.getMessage());
    }
}

    @Test
    void getOrderDetailsMapper() {
        ArrayList<OrderDetailsMaterialLengthDTO> expected = new ArrayList<>();

        // Arrange Makes a OrderDetailsDTO list with the expected values
        OrderDetailsMaterialLengthDTO expectedDTO1 = new  OrderDetailsMaterialLengthDTO("Rem", 300,2,"stk","Remme");
        OrderDetailsMaterialLengthDTO expectedDTO2 = new  OrderDetailsMaterialLengthDTO("Spær", 300,7,"stk","Spær, monteres");
        OrderDetailsMaterialLengthDTO expectedDTO3 = new  OrderDetailsMaterialLengthDTO("Stolpe", 300,2,"stk","Stolper nedgraves");

        // Act Look after Order detail rows in the test database to make DTO objects that correspond with order and order details. orderId has to be set to 1 based on filled data
        try {
            ArrayList<OrderDetailsMaterialLengthDTO> expectedList = OrderAndDetailsDTOMapper.getOrderDetailsMapper(1);

            OrderDetailsMaterialLengthDTO actualDTO1 = expectedList.get(0);
            OrderDetailsMaterialLengthDTO actualDTO2 = expectedList.get(1);
            OrderDetailsMaterialLengthDTO actualDTO3 = expectedList.get(2);

            // Assert Check if the expected results match with data from the database
            assertEquals(expectedDTO1, actualDTO1);
            assertEquals(expectedDTO2, actualDTO2);
            assertEquals(expectedDTO3, actualDTO3);

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        }

    }
