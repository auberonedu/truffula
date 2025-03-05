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
  void testPrintlnWithRedColorNoReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    // Act: Print the message
    String message = "I speak for the trees fr";
    printer.print(message, false);

    String expectedOutput = ConsoleColor.RED + "I speak for the trees fr";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  
  @Test
  void testPrintlnWithDefaultColor() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);

    // Act: Print the message
    String message = "Sometimes you gotta pop out and show fellas";
    printer.print(message, false);

    String expectedOutput = ConsoleColor.WHITE + "Sometimes you gotta pop out and show fellas";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testColorPersistence() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the message
    printer.print("Hello", false);
    printer.print("Guten Tag", false);

    String expectedOutput = ConsoleColor.BLUE + "Hello" + ConsoleColor.BLUE + "Guten Tag";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testMultipleLinesWithReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.YELLOW);

    // Act: Print the message
    printer.println("Hello", true);
    printer.println("from the other side", true);

    String expectedOutput = 
      ConsoleColor.YELLOW + "Hello" + System.lineSeparator() + ConsoleColor.RESET +
      ConsoleColor.YELLOW + "from the other side" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }


  @Test
  void testEmptyTextEntry() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.WHITE);
    
    // Act: Print the message
    String message = "";
    printer.print(message, false);

    String expectedOutput =ConsoleColor.WHITE + "";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  
  @Test
  void testNullTextEntry() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.WHITE);

    // Act: Print the message
    String message = null;
    printer.print(message, false);

    String expectedOutput = ConsoleColor.WHITE + "null";

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

}