import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.Console;
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


    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  // This Test ensures that print() does not reset the color when 'reset' is false
  @Test
  void testPrintWithoutReset() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.GREEN);

    String message = "That's one small step for man, one giant leap for mankind.";
    printer.print(message, false);

    String expectedOutput = ConsoleColor.GREEN.getCode() + message;

    assertEquals(expectedOutput, outputStream.toString());
  }

  // This Test ensures that print() does reset the color correctly when set to true
  @Test
  void testPrintWithReset() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.YELLOW);

    String message = "To the moooon!";
    printer.print(message, true);

    String expectedOutput = ConsoleColor.YELLOW.getCode() + message + ConsoleColor.RESET.getCode();

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testDefaultColorIsWhite() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    String message = "Default color test.";
    printer.print(message, true);

    String expectedOutput = ConsoleColor.WHITE.getCode() + message + ConsoleColor.RESET.getCode();

    assertEquals(expectedOutput, outputStream.toString());
  }
}
