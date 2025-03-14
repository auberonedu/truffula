import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  void testValidDirectoryWithNoColorOnly(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", directoryPath};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden(), "Hidden files should NOT be shown.");
    assertFalse(options.isUseColor(), "Color should be disabled");
  }

  @Test
  void testValidDirectoryWithShowHiddenOnly(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", directoryPath};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden(), "Hidden files should be shown.");
    assertTrue(options.isUseColor(), "Color should be enabled by default.");
  }

  @Test
  void testValidDirectoryWithInvalidFlags() {
    String[] args = {"-x", "/valid/path/to/directory"};
                                                // lambda expression. v .to create a new instance of TrufflaOptions
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
    assertEquals("-x is not a valid argument.", exception.getMessage());
  }

  @Test
  void testValidDirectoryWithFlagsInDifferentOrder(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoyPath = directory.getAbsolutePath();
    String[] args = {"-h", "-nc", directoyPath};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden(), "Hidden files should be shown.");
    assertFalse(options.isUseColor(), "Color should be disabled.");
  }

  @Test
  void testInvalidDirectory() {
    String invalidPath = "this/path/does/not/exist";
    String[] args = {"-nc", "-h", invalidPath};

    Exception exception = assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
    assertEquals(invalidPath + " does not exist.", exception.getMessage());
  }

  @Test
  void testMissingDirectory() {
    String[] args = {"-nc", "-h"};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
    assertEquals("Filepath missing.", exception.getMessage());
  }

  @Test
  void testEmptyArguments() {
    String[] args = {};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
    assertEquals("Filepath missing.", exception.getMessage());
  }
}
