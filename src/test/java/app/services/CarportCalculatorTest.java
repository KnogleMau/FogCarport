package app.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarportCalculatorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void carportCalculator() {
        CarportCalculator instance = new CarportCalculator();
        int result = instance.calculatePole(780, true);
        int expected = 10;
        assertEquals(expected, result);
    }
}