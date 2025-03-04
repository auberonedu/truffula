import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
  // Checks if color and hidden file isn't changed
  @Test
  void testValidFlagsAreSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {directoryPath};

    // Act:
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert:
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());  
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  // Checks for Illegal Argument if unknown flags are provided
  @Test
  void testForUnkownFlags(@TempDir File tempDir) throws IllegalArgumentException, FileNotFoundException {
    // Arrange:
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-hm", "-y",directoryPath};

    // Assert:
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args) );
  }

  // Checks for Illegal Argument if path argument is missing
  @Test
  void testForMissingPathArgument(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:

    // Act:

    // Assert:
  }

  // Checks for File Not Found if specified directory doesn't exist
  @Test
  void testNonexistentDirectory(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:
    File directory = new File(tempDir, "subfolder");

    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-hm", "-y",directoryPath};

    // Assert:
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args) );
  }

  // Checks for File Not found if path points to a file instead of directory
  @Test
  void testsForAFile(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:

    // Act:

    // Assert:
  }
}
