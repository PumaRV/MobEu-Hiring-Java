package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Item;
import com.mobiquityinc.util.ItemComparator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Packer {

    private final static Logger LOGGER = Logger.getLogger(Packer.class);
    private final static Properties PROPERTIES = getProperties();

    private Packer() {
    }

    public static String pack(final String filePath) throws APIException {
        final int maxItems = Integer.parseInt(PROPERTIES.getProperty("items.max"));
        final int maxCapacity = Integer.parseInt(PROPERTIES.getProperty("capacity.max")) * 100;

        final List<String> lines = getDataFromFile(filePath);
        if (lines != null) {
            if (lines.size() > maxItems) {
                throw new APIException("Items exceed maximum capacity of " + maxItems);
            }
            final StringBuilder stringBuilder = new StringBuilder();
            for (final String line : lines) {
                final int packageCapacity = getPackageCapacity(line);
                if (packageCapacity < 0 || packageCapacity > maxCapacity) {
                    throw new APIException("Invalid package capacity found");
                }
                final List<Item> items = getItemsFromData(line);
                stringBuilder.append(solveForItemsAndCapacity(items, packageCapacity));
                stringBuilder.append(System.lineSeparator());
            }

            return stringBuilder.toString();
        }
        return null;
    }

    //To solve the calculation I'm using Dynamic programming.
    private static String solveForItemsAndCapacity(final List<Item> items, final int capacity) {
        //Sort by weight so Dynamic programming returns the values with less weight in case of combinations with same price
        items.sort(new ItemComparator());
        final StringBuilder stringBuilder = new StringBuilder();
        final int[][] matrix = new int[items.size() + 1][capacity + 1];

        for (int i = 0; i <= capacity; i++) {
            matrix[0][i] = 0;
        }

        for (int i = 1; i <= items.size(); i++) {
            for (int j = 0; j <= capacity; j++) {
                final Item previousItem = items.get(i - 1);
                final int previousWeight = previousItem.getWeight();
                final int previousValue = previousItem.getValue();
                if (previousWeight > j) {
                    matrix[i][j] = matrix[i - 1][j];
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - previousWeight] + previousValue);
                }
            }
        }

        //Once we have calculated our DP matrix we need to get the items for output.
        int total = matrix[items.size()][capacity];
        int totalWeight = capacity;
        for (int i = items.size(); i > 0 && total > 0; i--) {
            if (total != matrix[i - 1][totalWeight]) {
                final Item previousItem = items.get(i - 1);
                total -= previousItem.getValue();
                totalWeight -= previousItem.getWeight();
                if (stringBuilder.length() == 0) {
                    stringBuilder.append(previousItem.getId());
                } else {
                    stringBuilder.append(", ");
                    stringBuilder.append(previousItem.getId());
                }
            }
        }
        if (stringBuilder.length() == 0) {
            return "-";
        }
        return stringBuilder.toString();
    }

    private static Properties getProperties() {
        try {
            final InputStream input = Packer.class.getClassLoader().getResourceAsStream("application.properties");
            final Properties properties = new Properties();
            if (input == null) {
                LOGGER.error("Unable to find application properties");
                return null;
            }
            properties.load(input);
            return properties;
        } catch (IOException ioException) {
            LOGGER.error("Error reading Application properties");
        }
        return null;
    }

    private static int getPackageCapacity(final String data) {
        try {
            final String rawNumber = data.substring(0, data.lastIndexOf(':'));
            return Integer.parseInt(rawNumber.trim()) * 100;
        } catch (NumberFormatException numberFormatExpeption) {
            LOGGER.error("Exception while trying to parse package capacity: ", numberFormatExpeption);
        }
        return -1;
    }

    private static List<String> getDataFromFile(final String filePath) {
        try {
            final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath));
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException ioException) {
            LOGGER.error("Exception while trying to read input file: ", ioException);
        }
        return null;
    }

    private static List<Item> getItemsFromData(final String data) throws APIException {
        //Matches the contents of each parenthesis
        final Matcher matcher = Pattern.compile("\\(([^)]+)\\)").matcher(data);
        final List<Item> items = new ArrayList<>();
        while (matcher.find()) {
            final String parenthesisContents = matcher.group(1);
            final String[] itemProperties = parenthesisContents.split(",");
            if (itemProperties.length < 3) {
                throw new APIException("Missing data for index, value or weight");
            }
            try {
                final int id = Integer.parseInt(itemProperties[0].trim());
                //Had to convert weights to integer in order to use dynamic programming.
                //weights with more than 2 decimal places will be rounded.
                final int weight = (int) Double.parseDouble(itemProperties[1].trim()) * 100;
                final int value = Integer.parseInt(itemProperties[2].substring(1).trim());

                final int maxWeight = Integer.parseInt(PROPERTIES.getProperty("weight.max")) * 100;
                final int maxValue = Integer.parseInt(PROPERTIES.getProperty("value.max"));

                if (weight > maxWeight || value > maxValue) {
                    throw new APIException("weight or cost exceed permitted values");
                }
                final Item item = new Item(id, weight, value);
                items.add(item);
            } catch (NumberFormatException numberFormatException) {
                throw new APIException("Invalid data found for index, value or weight");
            }
        }
        return items;
    }


}
