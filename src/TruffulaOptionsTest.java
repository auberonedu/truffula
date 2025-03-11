import static org.junit.Assert.assertThat;
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

  // Additional Tests

  @Test
  void testValidDirectoryIsSetWithColor(@TempDir File tempDir) throws FileNotFoundException {

    // Arrange
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", directoryPath};

    // Act
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testValidDirectoryIsSetWithHiddenFoldersAndColor(@TempDir File tempDir) throws FileNotFoundException {

    // Arrange
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {directoryPath};

    // Act
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testInvalidDirectory(@TempDir File tempDir) {
    // is a file :(
      File directory = new File(tempDir, "subfolder");
      directory.mkdir();
      String directoryPath = directory.getAbsolutePath();
      
      String[] args = {directoryPath};

      directory.delete();

      assertThrows(FileNotFoundException.class, () -> {
        new TruffulaOptions(args);
      });
  }
    
  @Test
  void testPathArgumentMissing(@TempDir File tempDir) throws FileNotFoundException {
      String[] args = {"-nc", "-h"};
  
      assertThrows(FileNotFoundException.class, () -> {
        new TruffulaOptions(args);
      });

  }
}
