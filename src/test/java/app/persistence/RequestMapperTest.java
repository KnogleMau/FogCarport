package app.persistence;

import app.entities.CarportRequest;
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

// Integration test for RequestMapper

class RequestMapperTest {

 private static final String USER = "postgres";
 private static final String PASSWORD = "vinkelvej20";
 private static final String URL = "jdbc:postgresql://46.101.114.35:5432/%s?currentSchema=test";
 private static final String DB = "fogcarport";

  /*private static ConnectionPool connectionPool = ConnectionPool.getInstance("postgres", "vinkelvej20"URL=jdbc:postgresql://46.101.114.35:5432/%s?currentSchema\=test  );   */



 @BeforeAll
 static void setupConnectionPool() {
  ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
 }
 /*
 @BeforeAll
    static void setUpBeforeAllClass() throws Exception {

     {
      try (Connection connection = connectionPool.getConnection())
      {
       // same as prepares statement because we are just making a intern testing
       try (Statement stmt = connection.createStatement())
       {
        // The test schema is already created, so we only need to delete/create test tables
        stmt.execute("DROP TABLE IF EXISTS test.customer_information");
        stmt.execute("DROP TABLE IF EXISTS test.requests");

        stmt.execute("DROP SEQUENCE IF EXISTS test.customer_information_customer_id_seq CASCADE;");
        stmt.execute("DROP SEQUENCE IF EXISTS test.request_request_id_seq CASCADE;");


        // Create tables as copy of original public schema structure
        stmt.execute("CREATE TABLE test.customer_information AS (SELECT * from public.customer_information) WITH NO DATA");
        stmt.execute("CREATE TABLE test.requests AS (SELECT * from public.requests) WITH NO DATA");

        // Create sequences for auto generating id's for users and orders
        stmt.execute("CREATE SEQUENCE test.customer_information_customer_id_seq");
        stmt.execute("ALTER TABLE test.customer_information ALTER COLUMN customer_id SET DEFAULT nextval('test.customer_information_customer_id_seq')");



        stmt.execute("CREATE SEQUENCE test.requests_request_id_seq");
        stmt.execute("ALTER TABLE test.requests ALTER COLUMN request_id SET DEFAULT nextval('test.requests_request_id_seq')");
       }
      }
      catch (SQLException e)
      {
       e.printStackTrace();
       fail("Database connection failed");
      }
     }
    }
    */
/*
    @BeforeEach
    void setUp() {


     try (Connection connection = connectionPool.getConnection())
     {
      try (Statement stmt = connection.createStatement())
      {
       // Remove all rows from all tables
       stmt.execute("DELETE FROM test.requests");
       stmt.execute("DELETE FROM test.customer_information");

       stmt.execute("INSERT INTO test.customer_information (first_name, last_name, address, phonenumber, customer_email, zip_code ) " +
               "VALUES  ('Benny', 'Jensen', 'jernbane alle 4', '12312332','bygselv@stort.dk', '2800'), ('Bøje', 'Lassen', 'Vestergade 43', '54334566', 'byg@nu.dk', '2800')");

       stmt.execute("INSERT INTO test.requests (request_length, carport_width, carport_height, customer_id) " +
               "VALUES ( 780, 600, 210, 1)");
       stmt.execute("INSERT INTO test.requests (request_length, carport_width, carport_height, customer_id) " +
               "VALUES ( 450, 300, 210, 2)");
       // Set sequence to continue from the largest member_id
       stmt.execute("SELECT setval('test.requests_request_id_seq', COALESCE((SELECT MAX(request_id) + 1 FROM test.requests), 1), false)");
       stmt.execute("SELECT setval('test.customer_information_customer_id_seq', COALESCE((SELECT MAX(customer_id) + 1 FROM test.customer_information), 1), false)");

      }
     }
     catch (SQLException e)
     {
      e.printStackTrace();
      fail("Database connection failed");
     }
    }


    @Test
    void requestMapper() throws DatabaseException {

     // Arrange   Makes to Request object with the expected values
       CarportRequest expectedRequest1 = new CarportRequest(1,780,600,210,1);
       CarportRequest expectedRequest2 = new CarportRequest(2,450,300,210,2);

     // Act Look after request rows in test database and make objects from the data
        CarportRequest actualRequest1 = RequestMapper.getCarportRequest(1);
        CarportRequest actualRequest2 = RequestMapper.getCarportRequest(2);

        // Assert  Check if the expectet results match with requests in the database
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

      } catch (DatabaseException e) {
       e.printStackTrace();
       System.out.println("Failed to get all requests from database");
      }
      catch (SQLException e){
       System.out.println("Ikke noget resultat af at køre sql queryen");
      }

 */

    @Test
   void requestInsertIntoDatabase() throws DatabaseException {
     //CarportRequest expectedRequest1 = new CarportRequest(1,780,600,210,1);
     RequestMapper instance = new RequestMapper();

     int cHeight = 210;
     int cWidth = 600;
     int cLength = 780;
     instance.requestMapper(600,780, 1);

     CarportRequest carport = instance.getCarportRequest(1);

     assertNotNull(carport);
     assertEquals(cHeight,carport.getRequestHeight());
     assertEquals(cWidth,carport.getRequestWidth());
     assertEquals(cLength,carport.getRequestLength());
     assertEquals(1,carport.getCustomerID());

    }

}