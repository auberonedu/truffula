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
    String[] args = { "-nc", "-h", directoryPath };

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testEmptyArgs() {
    // Arrange : Empty args
    String[] args = {};

    // Act and Assert : throw an excpetion for an empty args
    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });
  }

  @Test
  void testInValidDirectory() {
    //Arrange
    String invalidPath = "invalid/directory";
    String[] args = {"-nc", "-h", invalidPath};

    //Act and assert: Invalid file directory  *+
    assertThrows(FileNotFoundException.class, () -> {
      new TruffulaOptions(args);
  });
  }

  // support in JUNIT TempDir : https://www.baeldung.com/junit-5-temporary-directory
  @Test 
  void testInvalidArgs(@TempDir File testDir) {
    //Arrange
    File directory = new File(testDir, "folder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"nq", "-c", directoryPath};

    // Act and assert
    assertThrows(IllegalArgumentException.class, () -> {
      new TruffulaOptions(args);
    });

  }

}
