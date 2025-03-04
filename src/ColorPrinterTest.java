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

    printer.setCurrentColor(ConsoleColor.YELLOW);
    printer.println("Burning Yellow Sun");
    

    String expectedOutput = ConsoleColor.GREEN + "Green tall grass" + System.lineSeparator() + ConsoleColor.RESET +
    ConsoleColor.YELLOW + "Burning Yellow Sun" + System.lineSeparator() + ConsoleColor.RESET;
    
    // Assert verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithEmptyString(){
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.CYAN);

    //Act: Printing an empty string
    String message = "";
    printer.println(message);

    String expectedOutput = ConsoleColor.CYAN + "" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithNoReset(){
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.PURPLE);

    // Act: Print the message
    String message = "Grapes are always purple";
    printer.println(message, false);

    // Assert: Verify the printed output
    String expectedOutput = ConsoleColor.PURPLE + "Grapes are always purple" + System.lineSeparator();
    assertEquals(expectedOutput, outputStream.toString());
  }
}
