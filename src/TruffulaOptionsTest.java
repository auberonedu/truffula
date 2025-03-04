import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    assertFalse(options.isUseColor());  // Should now be true by default
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
    assertEquals("Unknown flag: -invalid", exception.getMessage());
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
}
