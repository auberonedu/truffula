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
  void testPrintlnWithBlueAndNoReset() {

    //Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    //Act
    String message = "Hello World";
    printer.println(message, false);

    String expectedOutput = ConsoleColor.BLUE + "Hello World" + System.lineSeparator();

    //Assert
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintEmptyMessageAndResest() {
     //Arrange
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     PrintStream printStream = new PrintStream(outputStream);
 
     ColorPrinter printer = new ColorPrinter(printStream);
     printer.setCurrentColor(ConsoleColor.BLUE);
 
     //Act
     String message = "";
     printer.print(message, true);

     // Assert
    String expectedOutput = ConsoleColor.BLUE.getCode() + ConsoleColor.RESET.getCode();
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintWithBlackAndReset() {
    //Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLACK);

    String message = "This Message will be the color black";
    printer.print(message, true);

    // Assert
    String expectedOutput = ConsoleColor.BLACK.getCode() + message + ConsoleColor.RESET.getCode();
    assertEquals(expectedOutput, outputStream.toString());

  }

  @Test
  void testPrintWithBlackAndNoReset() {
    //Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLACK);

    String message = "This Message will be the color black";
    printer.print(message, false);

    // Assert
    String expectedOutput = ConsoleColor.BLACK.getCode() + message;
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintWithBlackandReset() {
    //Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLACK);

    String message = "This Message will be the color black";
    printer.print(message, true);

    // Assert
    String expectedOutput = ConsoleColor.BLACK.getCode() + message + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  }

}
