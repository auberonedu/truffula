import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    //tempDir/subfolder
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testValidDirectoryWithNoFlags(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory and no flags
    String directoryPath = tempDir.getAbsolutePath();
    String[] args = {directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set and flags have default values
    assertEquals(directoryPath, options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());  // Should now be true by default
  }

  @Test
  void testInvalidFlag(@TempDir File tempDir) {
    // Arrange: Prepare the arguments with an invalid flag
    String directoryPath = tempDir.getAbsolutePath();
    String[] args = {"-invalid", directoryPath};

    // Act & Assert: Check that an IllegalArgumentException is thrown
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
    assertEquals("Unknown flag: " + args[0], exception.getMessage());
  }

  @Test
  void testPathFile(@TempDir File tempDir) throws IOException {
      // Arrange: Create a file (not a directory) in the temp directory
      File file = new File(tempDir, "testfile.txt");
      file.createNewFile();
      String filePath = file.getAbsolutePath();
      String[] args = {filePath};
  
      // Act & Assert: Check that a FileNotFoundException is thrown
      Exception exception = assertThrows(FileNotFoundException.class, () -> {
          new TruffulaOptions(args);
      });
      assertEquals("The path is not a directory: " + filePath, exception.getMessage());
  }

  @Test
  void testPathDoesNotExist() {
    // Arrange: Prepare the arguments with a path that does not exist
    String noPath = "/this/path/does/not/exist";
    String[] args = {noPath};

    // Act & Assert: Check that a FileNotFoundException is thrown
    Exception exception = assertThrows(FileNotFoundException.class, () -> {
        new TruffulaOptions(args);
    });
    assertEquals("The directory does not exist: " + noPath, exception.getMessage());
  }

  @Test
  void testToStringOutput() throws FileNotFoundException {
    File Dir = new File("/"); // assuming current dir exists
    TruffulaOptions options = new TruffulaOptions(new String[]{"-h", "-nc", Dir.getAbsolutePath()});

    String expected = "TruffulaOptions [root=" + Dir.getAbsoluteFile() + ", showHidden=true, useColor=false]";
    assertEquals(expected, options.toString());
  }

  // edge cases tests
  @Test
  void testFlagDifferentOrder() throws FileNotFoundException {
      File validDir = new File("/");
      
      TruffulaOptions options1 = new TruffulaOptions(new String[]{"-nc", "-h", validDir.getAbsolutePath()});
      TruffulaOptions options2 = new TruffulaOptions(new String[]{"-h", "-nc", validDir.getAbsolutePath()});
      
      assertEquals(options1.isShowHidden(), options2.isShowHidden());
      assertEquals(options1.isUseColor(), options2.isUseColor());
      assertEquals(options1.getRoot(), options2.getRoot());
    }

  @Test
  void testRelativeVsAbsolutePath() throws FileNotFoundException {
      File validDir = new File("/"); 
      String absolutePath = validDir.getAbsolutePath();
      
      TruffulaOptions optionsRelative = new TruffulaOptions(new String[]{ "/" });
      TruffulaOptions optionsAbsolute = new TruffulaOptions(new String[]{ absolutePath });
      
      assertEquals(optionsAbsolute.getRoot(), optionsRelative.getRoot());
  }

  @Test
  void testPathWithSpaces() throws FileNotFoundException {
      File validDir = new File("Test Directory");
      if (!validDir.exists()) validDir.mkdir(); // Creates the test directory if missing

      TruffulaOptions options = new TruffulaOptions(
        new String[]{validDir.getAbsolutePath()});
      assertEquals(validDir.getAbsoluteFile(), options.getRoot());
  }

  @Test
  void testEmptyPathArgument() {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
          new TruffulaOptions(new String[]{""}); // empty path argument
      });
      assertTrue(exception.getMessage().contains("No arguments found!"));
  }

  @Test
  void testMultipleUnknownFlags() {
      Exception exception = assertThrows(IllegalArgumentException.class, () -> {
          new TruffulaOptions(new String[]{"-vfv", "-x", "/valid/path"}); // unrecognizable flags
      });
      assertTrue(exception.getMessage().contains("Unknown flag"));
  }

  @Test
  void testExtremelyLongPath() {
      StringBuilder longPath = new StringBuilder();
      for (int i = 0; i < 100; i++) {
          longPath.append("a"); 
      }

      File longPathDir = new File(longPath.toString()); //creates a long path file

      if (!longPathDir.exists()) longPathDir.mkdir(); 

      assertDoesNotThrow(() -> new TruffulaOptions(
        new String[]{longPathDir.getAbsolutePath()}));
  }

  @Test
  void testDirectoryNameResemblingFlag() throws FileNotFoundException {
    File trickyDir = new File("-nc"); // Directory name looks like a flag
    if (!trickyDir.exists()) trickyDir.mkdir(); 

    TruffulaOptions options = new TruffulaOptions(new String[]{trickyDir.getAbsolutePath()});
    assertEquals(trickyDir.getAbsoluteFile(), options.getRoot());
  }
  
  @Test
  void testFlagsWithoutPath() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new TruffulaOptions(new String[]{"-h", "-nc"}); // flags without a path specified
    });
    assertTrue(exception.getMessage().contains("A path is required"));
  }

  @Test
  void testPathWithNewline() {
    Exception exception = assertThrows(FileNotFoundException.class, () -> {
        new TruffulaOptions(new String[]{"\n/path/to/directory"}); // a directory starting with a newLine character
    });
    assertTrue(exception.getMessage().contains("The directory does not exist: "));
  }
}
