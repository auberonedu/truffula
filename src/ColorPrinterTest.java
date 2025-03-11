import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPrinterTest {

  @Test
  void testPrintlnWithRedColorAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.println(message);

    String expectedOutput = ConsoleColor.RESET + "I speak for the trees" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithBlueColorAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the message
    String message = "Testing Blue";
    printer.println(message);

    String expectedOutput = ConsoleColor.RESET + "Testing Blue" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithGreenColorAndNoReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.GREEN);

    // Act: Print the message
    String message = "Testing Green and NO RESET";
    printer.println(message, false);

    String expectedOutput = "Testing Green and NO RESET" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnGreenColorBlankMessageandNoReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.GREEN);

    // Act: Print the message
    String message = "";
    printer.println(message, false);

    String expectedOutput =System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintMultipleMessagesWithDifferentColors() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print the message
    printer.setCurrentColor(ConsoleColor.RED);
    printer.println(ConsoleColor.RESET + "This is red", false);

    printer.setCurrentColor(ConsoleColor.GREEN);
    printer.println(ConsoleColor.RESET + "This is green", false);

    printer.setCurrentColor(ConsoleColor.BLUE);
    printer.println(ConsoleColor.RESET + "This is blue", false);

    String expectedOutput = ConsoleColor.RESET + "This is red" + System.lineSeparator() +
        ConsoleColor.RESET + "This is green" + System.lineSeparator() +
        ConsoleColor.RESET + "This is blue" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintWithoutSettingColor() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream); // No color set

    // Act: Print the message
    printer.println("Default color text");

    String expectedOutput = ConsoleColor.RESET + "Default color text" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

}
