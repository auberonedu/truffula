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

  // Additional tests for ColorPrinter

  @Test
  void testPrintlnWithBlueColorAndReset() {
    
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    // Act: Print the message
    String message = "I speak for the ocean";
    printer.println(message);


    String expectedOutput = ConsoleColor.BLUE + "I speak for the ocean" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());  
  }

  @Test
  void testPrintlnWithPurpleColorAndReset() {

    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.PURPLE);

    // Act: Print the message
    String message = "I speak for... uh, eggplants?";
    printer.println(message);


    String expectedOutput = ConsoleColor.PURPLE + "I speak for... uh, eggplants?" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnWithGreenColorAndReset() {
  
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.GREEN);

    // Act: Print the message
    String message = "I speak for the turtles";
    printer.println(message);


    String expectedOutput = ConsoleColor.GREEN + "I speak for the turtles" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString()); 
  }

  @Test
  void testPrintlnWithBlackColorAndReset() {

    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLACK);

    // Act: Print the message
    String message = "I speak for the kitties";
    printer.println(message);


    String expectedOutput = ConsoleColor.BLACK + "I speak for the kitties" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());

  }

  @Test
  void testPrintCurrentColorWithReset() {
  
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
  
    ColorPrinter print = new ColorPrinter(printStream);
    print.setCurrentColor(ConsoleColor.CYAN);
  
    // Act
    ConsoleColor currentColor = print.getCurrentColor();
  
    // Assert
    assertEquals(ConsoleColor.CYAN, currentColor);
  }

  @Test
  void testPrintCurrentColorWithColorChangesSimple() {
  
  }

  @Test
  void testPrintCurrentColorWithColorChangesComplex() {
  
  }

}

