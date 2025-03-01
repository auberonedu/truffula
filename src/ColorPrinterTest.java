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
  @Test
  void testPrintlnWithBlueColorAndNoReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.println(message, false);


    String expectedOutput = ConsoleColor.BLUE + "I speak for the trees" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  @Test
  void testPrintlnWithResetAndColorSwitches() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);
    printer.setCurrentColor(ConsoleColor.RED);
    printer.setCurrentColor(ConsoleColor.BLACK);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.println(message, false);


    String expectedOutput = ConsoleColor.BLACK + "I speak for the trees" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  @Test
  void testPrintWithYellowAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.YELLOW);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.print(message);


    String expectedOutput = ConsoleColor.YELLOW + "I speak for the trees" + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
}
