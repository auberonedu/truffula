import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPrinterTest {

  @Test
  void testPrintWithReset() {
    // Arrange: Capture output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    // Set color and print message
    printer.setCurrentColor(ConsoleColor.RED);
    printer.print("Hello, world!", true);

    // Expected output: Color + Message + Reset
    String expectedOutput = ConsoleColor.RED + "Hello, world!" + ConsoleColor.RESET;

    // Assert
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testMultiplePrintsWithoutReset() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.YELLOW);

    // Act
    printer.print("First", false);
    printer.print(" Second", false);
    printer.print(" Third", false);

    // Expected output: All text should be in YELLOW with no RESET applied
    String expectedOutput = ConsoleColor.YELLOW + "First" +
        ConsoleColor.YELLOW + " Second" +
        ConsoleColor.YELLOW + " Third";

    // Assert
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintOnlyReset() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.GREEN);

    // Act
    printer.print("", true);

    // Expected output: Only RESET code should be printed
    String expectedOutput = ConsoleColor.GREEN + "" + ConsoleColor.RESET;

    // Assert
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testSwitchingColorsMidway() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    // Act
    printer.setCurrentColor(ConsoleColor.BLUE);
    printer.print("Blue text", true);

    printer.setCurrentColor(ConsoleColor.PURPLE);
    printer.print(" Purple text", true);

    // Expected output: First message in BLUE, second in PURPLE, both reset after
    // printing
    String expectedOutput = ConsoleColor.BLUE + "Blue text" + ConsoleColor.RESET +
        ConsoleColor.PURPLE + " Purple text" + ConsoleColor.RESET;

    // Assert
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintNullMessage() {
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    // Act
    printer.setCurrentColor(ConsoleColor.CYAN);
    printer.print(null, true);

    // Expected output: Should print "null" in CYAN with RESET after
    String expectedOutput = ConsoleColor.CYAN + "null" + ConsoleColor.RESET;

    // Assert
    assertEquals(expectedOutput, outputStream.toString());
  }

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

    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
}
