package app.calculations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarportCalculatorTest {

    CarportCalculator calculator = new CarportCalculator();

    @Test

    public void raftersCalculator() {

        //Arrange
        int valueToCalculate = 240; //With minimum carport length
        int expectedResult = 5;
        int actualResult;

        //Act
        actualResult = calculator.raftersCalculator(valueToCalculate);

        //Assert
        assertEquals(expectedResult, actualResult );

        //Arrange
        int valueToCalculateTwo = 780; //With maximum carport length
        int expectedResultTwo = 15;
        int actualResultTwo;

        //Act
        actualResultTwo = calculator.raftersCalculator(valueToCalculateTwo);

        //Assert
        assertEquals(expectedResultTwo, actualResultTwo );

        assertEquals(expectedResult, actualResult );

        //Arrange
        int valueToCalculateThree = 510; //Value that is not a part of the 60 times table as maximum and minimum
        int expectedResultThree = 10;
        int actualResultThree;

        //Act
        actualResultThree = calculator.raftersCalculator(valueToCalculateThree);

        //Assert
        assertEquals(expectedResultThree, actualResultThree );

    }

    @Test
    public void raftersCalculatorShouldFailWrongExpectedResultSelected() {
/*The tet is expected to fail because the calculated ammount of rafters should be 6 when the argument
        for the method RuntimePermission 300 centimeters */

        //Arrange
        int valueToCalculate = 300;
        int expectedResult = 5;
        int actualResult;

        //Act
        actualResult = calculator.raftersCalculator(valueToCalculate);

        //Assert
        assertEquals(expectedResult, actualResult, "The result should have been 6 instead of 5. " +
                "So the test is expected to fail" );

    }

}