import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  // test if empty
  @Test
  void testEmptyArguments() {
    // Arrange: Prepare an empty argument array
    String[] args = {};

    // Act & Assert: Creating a TruffulaOptions instance should throw an IllegalArgumentException
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });

    // Assert: Verify that the exception message contains information about the missing path
    assertTrue(exception.getMessage().contains("Path required"));
  }

  // test if null
  @Test
  void testNullArguments() {
    // Arrange: Set the arguments to null
    String[] args = null;

    // Act & Assert: Creating a TruffulaOptions instance with null should throw an IllegalArgumentException
    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  // test if path (put a file)
  @Test
  void testPathIsAFile(@TempDir File tempDir) throws Exception {
    // Arrange: Create a temporary file instead of a directory
    File file = new File(tempDir, "aFile.txt");
    file.createNewFile();
    String[] args = {"-h", file.getAbsolutePath()};

    // Act & Assert: Creating a TruffulaOptions instance with a file should throw a FileNotFoundException
    Exception exception = assertThrows(FileNotFoundException.class, () -> {
      new TruffulaOptions(args);
    });

    // Assert: Verify that the exception message indicates that the directory was not found
    assertTrue(exception.getMessage().contains("Directory not found"));
  }

  // test invalid command
  @Test
  void testInvalidFlag(@TempDir File tempDir) {
    // Arrange: Create a valid subdirectory and prepare arguments with an invalid flag
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-invalid", directory.getAbsolutePath()};

    // Act & Assert: Creating a TruffulaOptions instance with an invalid flag should throw an IllegalArgumentException
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });

    // Assert: Verify that the exception message indicates the unknown flag
    assertTrue(exception.getMessage().contains("Unknown arguement given: -invalid"));
  }

}
