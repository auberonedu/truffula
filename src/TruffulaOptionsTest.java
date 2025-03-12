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
  void testNoFlagsDirectoryOnly(@TempDir File tempDir) throws FileNotFoundException {
    // No -h or -nc => showHidden=false, useColor=true
    String[] args = { tempDir.getAbsolutePath() };
    TruffulaOptions options = new TruffulaOptions(args);
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
    assertEquals(tempDir.getAbsolutePath(), options.getRoot().getAbsolutePath());
  }

  @Test
  void testUnknownFlagThrows() {
    String[] args = {"-fakeFlag", "some/path"};
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testMissingPathThrows() {
    // No arguments => no path
    String[] args = {};
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testDirectoryDoesNotExist() {
    String[] args = {"-h", "-nc", "/no/such/dir"};
    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testPathIsFile(@TempDir File tempDir) throws Exception {
    // Make a file instead of a directory
    File someFile = new File(tempDir, "test.txt");
    someFile.createNewFile();

    String[] args = { someFile.getAbsolutePath() };
    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }
}
