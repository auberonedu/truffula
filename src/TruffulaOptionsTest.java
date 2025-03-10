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
  void testFileNotFound(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    String randomInvalidPath = "random/nonexistent/directory/path";

    // Ensure that the FileNotFoundException is thrown when passing this invalid path
    assertThrows(FileNotFoundException.class, () -> {
      String[] args = {randomInvalidPath};
      new TruffulaOptions(args); 
    });
  }

  @Test
  void testJustColor(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isUseColor());
  }

  @Test
  void testDuplicateFlags(@TempDir File tempDir) throws IllegalArgumentException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-nc", directoryPath};


    assertThrows(IllegalArgumentException.class, () -> {
      // Act: Create TruffulaOptions instance
      TruffulaOptions options = new TruffulaOptions(args);
    }, "Illegal Argument Exception Expected");
  }

  @Test
  void testInvalidFlags(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nocolor", directoryPath};

    // Assert: Check that the root directory is set correctly
    assertThrows(IllegalArgumentException.class, () -> {
      // Act: Create TruffulaOptions instance
      TruffulaOptions options = new TruffulaOptions(args);
    }, "Illegal Argument Exception Expected");
  }
}
