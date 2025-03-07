import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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


    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  // test no reset
  @Test
  void testPrintlnWithBlueColorAndNoReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the message
    String message = "This is blue!!!";
    printer.print(message, false);


    String expectedOutput = ConsoleColor.BLUE + "This is blue!!!";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  // test empty string
  @Test
  void testPrintlnWithEmptyString() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print the message
    String message = "";
    printer.print(message);

    String expectedOutput = "";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  // test null
  @Test
  void testPrintNullString() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print the message
    String message = null;

    // Assert: Verify the printed output
    assertThrows(IllegalArgumentException.class, () -> {
      printer.print(message);
    }, "Expects an IllegalArgumentException when a null message is trying to print");
  }
  // test default color
  // test multiple strings
  // test multiple resets
}
