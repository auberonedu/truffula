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
    assertThrows(IllegalArgumentException.class, () -> printer.print(message));
  }

  //test default color
  @Test
  void testDefaultColorIsWhite() {
    // Arrange: Capture the output and set up printer without explicitly setting a color
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    // default color should be WHITE
    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print a message and expect default color WHITE with reset
    printer.print("Default test", true);

    // Assert: Verify that the output is in WHITE followed by a reset
    String expectedOutput = ConsoleColor.WHITE + "Default test" + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  }
   
  //test printing multiple strings in sequence
  @Test
  void testMultipleStringsPrintedInSequence() {
    // Arrange: Capture the output and set up printer with PURPLE color
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream, ConsoleColor.PURPLE);

    // Act: Print multiple strings sequentially, first without reset then with reset
    printer.print("First part, ", false);
    // reset after printing
    printer.println("second part");

    // Assert: Verify the concatenated output is as expected
    String expectedOutput = ConsoleColor.PURPLE + "First part, " + ConsoleColor.PURPLE + "second part" + System.lineSeparator() + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  }
 
  //test multiple resets in separate messages
  @Test
  void testMultipleResets() {
    // Arrange: capture the output and set up printer with GREEN color
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream, ConsoleColor.GREEN);

    // Act: Print two separate messages, each resetting the color afterward
    printer.print("Line one", true);
    printer.print("Line two", true);

    // Assert: Verify that each printed line is followed by a reset code
    String expectedOutput = ConsoleColor.GREEN + "Line one" + ConsoleColor.RESET + ConsoleColor.GREEN + "Line two" + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  }
}
