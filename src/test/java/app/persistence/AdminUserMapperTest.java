package app.persistence;

import app.entities.AdminUser;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static app.Main.connectionPool;
import static org.junit.jupiter.api.Assertions.*;

class AdminUserMapperTest {
    private static final String USER = "postgres";
    private static final String PASSWORD = "vinkelvej20";
    private static final String URL = "jdbc:postgresql://46.101.114.35:5432/%s?currentSchema=test";
    private static final String DB = "fogcarport";

    AdminUserMapper instance = new AdminUserMapper();

    @BeforeAll
    static void setupConnectionPool() {
        ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
/*
    @Test
    void loginTest() throws DatabaseException {
            AdminUser user = instance.login("Fog@fog.dk", "1234", connectionPool);
            assertEquals("Fog@fog.dk", user.getEmail());
    }

 */

    @Test
    void createUser() throws DatabaseException{
        String testMail = "fog@fog.dk";
        String testPassword = "1234";
        boolean testAdmin = true;

        instance.createUser(testMail, testPassword, testAdmin, connectionPool);

       AdminUser user = instance.login(testMail, testPassword, connectionPool);

        assertEquals(testMail , user.getEmail());
    }
}