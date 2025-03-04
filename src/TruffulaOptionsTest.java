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

  @Test
  void testInvalidArguments(@TempDir File tempDir) {
    // Arrange: Prepare the arguments with an invalid flag
    String directoryPath = tempDir.getAbsolutePath();
    String[] args = {"-invalid", directoryPath};

    // Act & Assert: Check that an IllegalArgumentException is thrown
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
    
    assertEquals("Unknown argument: -invalid", exception.getMessage());
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
    assertTrue(options.isUseColor());
  }
}
