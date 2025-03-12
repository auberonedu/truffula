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

    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintNoReset() {
    // Test: print two messages in a row without resetting until the end
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream, ConsoleColor.GREEN);

    printer.print("FirstPart-", false);
    printer.print("SecondPart", true);

    String expectedOutput = ""
      + ConsoleColor.GREEN + "FirstPart-"
      + ConsoleColor.GREEN + "SecondPart"
      + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testColorChangeMidPrints() {
    // Test: print messages in a row in a different colors
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.RED);
    printer.println("RED message");

    printer.setCurrentColor(ConsoleColor.GREEN);
    printer.print("GREEN part", false);
    printer.println(" then reset to default after line", true);

    String nl = System.lineSeparator();
    String expected = ""
      + ConsoleColor.RED + "RED message" + nl + ConsoleColor.RESET
      + ConsoleColor.GREEN + "GREEN part"
      + ConsoleColor.GREEN + " then reset to default after line" + nl
      + ConsoleColor.RESET;

    assertEquals(expected, outputStream.toString());
  }

  @Test
  void testPrintEmptyString() {
    // Test: print empty message
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream, ConsoleColor.YELLOW);

    printer.println("");
    printer.println("Next line with text");

    String nl = System.lineSeparator();
    String expected = ""
      + ConsoleColor.YELLOW + "" + nl + ConsoleColor.RESET
      + ConsoleColor.YELLOW + "Next line with text" + nl + ConsoleColor.RESET;

    assertEquals(expected, outputStream.toString());
}

  @Test
  void testPrintUnicodeCharacters() {
    // Test: print non-ASCII characters
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream, ConsoleColor.CYAN);

    String message = "你好, 世界! 🌳";
    printer.println(message);

    String nl = System.lineSeparator();
    String expected = ConsoleColor.CYAN + message + nl + ConsoleColor.RESET;

    assertEquals(expected, outputStream.toString());
  }
}
