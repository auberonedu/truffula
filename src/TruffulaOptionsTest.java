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
  void testValidDirectoryIsSetSwith(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", directoryPath, "-nc"};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testNoPathProvided() {
    String[] args = {"-h", "-nc"};
    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
    assertEquals("No path provided.", exception.getMessage());
  }

  @Test
  void testInvalidPath() {
    String[] args = {"-h", "-nc", "invalid/path"};
    Exception exception = assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
    assertEquals("Directory not found: invalid/path", exception.getMessage());
  }

  @Test
  void testPathIsNotDirectory(@TempDir File tempDir) throws Exception {
    File file = new File(tempDir, "file.txt");
    file.createNewFile();
    String[] args = {"-h", "-nc", file.getAbsolutePath()};

    Exception exception = assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
    assertEquals("Path is not a directory: " + file.getAbsolutePath(), exception.getMessage());
  }

  @Test
  void testUnknownArgument(@TempDir File tempDir) {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", "-nc", directoryPath, "--unknown"};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
    assertEquals("Error: --unknown", exception.getMessage());
  }

  @Test
  void testOnlyPathProvided(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {directoryPath};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }
}
