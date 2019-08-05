package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PackerTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void packSuccessful() throws Exception {
        final String actualOutput = Packer.pack("src/test/resources/correct.txt");
        final String expectedOutput = "4" + System.lineSeparator() + "-" + System.lineSeparator() + "7,2" + System.lineSeparator() + "8,9" + System.lineSeparator();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void packExceedingItemQuantity() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Items exceed maximum capacity of 15");
        final String actualOutput = Packer.pack("src/test/resources/exceedingItemQuantity.txt");
    }

    @Test
    public void packExceedingWeight() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("weight or cost exceed permitted values");
        final String actualOutput = Packer.pack("src/test/resources/exceedingWeight.txt");
    }

    @Test
    public void packExceedingCost() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("weight or cost exceed permitted values");
        final String actualOutput = Packer.pack("src/test/resources/exceedingCost.txt");
    }

    @Test
    public void packInvalidCapacity() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Invalid package capacity found");
        final String actualOutput = Packer.pack("src/test/resources/invalidCapacity.txt");
    }

    @Test
    public void packMissingData() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Missing data for index, value or weight");
        final String actualOutput = Packer.pack("src/test/resources/missingData.txt");
    }

    @Test
    public void packMissingInputFile() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Exception reading input file");
        final String actualOutput = Packer.pack("src/test/resources/notExistent.txt");
    }

    @Test
    public void packInvalidData() throws Exception {
        expectedEx.expect(APIException.class);
        expectedEx.expectMessage("Invalid data found for index, value or weight");
        final String actualOutput = Packer.pack("src/test/resources/invalidData.txt");
    }
}