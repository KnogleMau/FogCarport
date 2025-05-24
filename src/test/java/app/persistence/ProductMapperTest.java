package app.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class ProductMapperTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = "vinkelvej20";
    private static final String URL = "jdbc:postgresql://46.101.114.35:5432/%s?currentSchema=public";
    private static final String DB = "fogcarport";

    MaterialMapper instance = new MaterialMapper();

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
    void SelectProduct() throws DatabaseException {
        int testId = 1;
        String expectedVariant = "Beam";
            Material product = instance.selectProduct(testId);
            assertNotNull(product);
            assertEquals(testId, product.getId());
            assertEquals(expectedVariant, product.getVariant());

    } */
}