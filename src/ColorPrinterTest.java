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
  void testPrintlnWithCyanColorAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.CYAN);

    // Act: Print the message
    String message = "I speak for the trees but in CYAN color yippee";
    printer.println(message);

    String expectedOutput = ConsoleColor.CYAN + "I speak for the trees but in CYAN color yippee" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  
  @Test
  void testPrintlnWithDefaultColor() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RESET);  

    // Act: Print the message
    String message = "This text is in the default color";
    printer.println(message);

    // Assert: Verify the printed output
    String expectedOutput = "This text is in the default color" + System.lineSeparator() + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithEmptyString() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);  

    // Act: Print an empty string
    String message = "";
    printer.println(message);

    // Assert: Verify the printed output
    String expectedOutput = ConsoleColor.BLUE + "" + System.lineSeparator() + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  } 

  @Test
  void testPrintlnWithMultipleColors() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print messages in different colors
    printer.setCurrentColor(ConsoleColor.GREEN);
    printer.println("This is green text");

    printer.setCurrentColor(ConsoleColor.YELLOW);
    printer.println("This is yellow text");

    String expectedOutput = ConsoleColor.GREEN + "This is green text" + System.lineSeparator() + ConsoleColor.RESET +
                            ConsoleColor.YELLOW + "This is yellow text" + System.lineSeparator() + ConsoleColor.RESET;
  }

  @Test
  void testPrintlnWithNoReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.PURPLE);

    // Act: Print message without resetting color
    String message = "This text should remain purple";
    printer.println(message, false);

    // Assert: Verify the printed output (no reset code at the end)
    String expectedOutput = ConsoleColor.PURPLE + "This text should remain purple" + System.lineSeparator();
    assertEquals(expectedOutput, outputStream.toString());
  }
}
