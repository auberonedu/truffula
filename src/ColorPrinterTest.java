import java.awt.print.PrinterAbortException;
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
  void testPrintlnWithDefaultColor(){
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.RESET);

    // Act: Print the message
    String message = "Testing Default Color";
    printer.println(message);

    String expectedOutput = "Testing Default Color" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());

  }

  @Test
  void testPrintlnWithBlueColorAndReset(){
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the message
    String message = "Blue skies and happy vibes!";
    printer.println(message);


    String expectedOutput = ConsoleColor.BLUE + "Blue skies and happy vibes!" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
    
  }

  @Test
  void testPrintlnWithMultipleColors(){
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    //Act: Printing the colored messages
    printer.setCurrentColor(ConsoleColor.GREEN);
    printer.println("Green tall grass");

    String expectedOutput = ConsoleColor.GREEN + "Green tall grass" + System.lineSeparator() + ConsoleColor.RESET +
    ConsoleColor.YELLOW + "Burning Yellow sun" + System.lineSeparator() + ConsoleColor.RESET;
    
    // Assert verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithEmptyString(){
    
  }

  @Test
  void testPrintlnWithNoReset(){
    
  }
}
