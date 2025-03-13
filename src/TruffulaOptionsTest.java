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
  void testValidDirectoryNoFlags(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testValidDirectoryHiddenFlagOnly(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testValidDirectoryNoColorFlagOnly(@TempDir File tempDir) throws FileNotFoundException {
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
    assertFalse(options.isShowHidden());
  }

  @Test
  void testValidDirectoryBothFlagsInDifferentOrder(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", "-nc", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testInvalidFlag(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    // Pass invalid/unknown flags
    String[] args = {"-xyz", "-v", directoryPath};

    // Act & Assert: Expect a thrown exception
    assertThrows(IllegalArgumentException.class, () -> {new TruffulaOptions(args);});
  }

  @Test
  void testNullPath() {
    // Arrange: null args entered
    String[] args = null;

    // Act & Assert: Expect a thrown exception
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testMissingPath() {
    // Arrange: No args entered
    String[] args = {};

    // Act & Assert: Expect a thrown exception
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testFlagsWithNoPath() {
    // Arrange: args entered, but no file path provided
    String[] args = {"-nc", "-h"};

    // Act & Assert: Expect a thrown exception
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testCaseSensitiveFlags(@TempDir File tempDir) {
    // Arrange: args entered with capital flags
    String[] args = {"-NC", "-H", tempDir.getAbsolutePath()};

    // Act & Assert: Expect a thrown exception
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testRepeatedFlagsWithValidPath(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: args entered with multiple repeating flags
    String[] args = {"-h", "-h", "-nc", "-nc", tempDir.getAbsolutePath()};  
    TruffulaOptions options = new TruffulaOptions(args);
    
    // Act & Assert: Expect intact functionality
    assertTrue(options.isShowHidden());  
    assertFalse(options.isUseColor());  
  }

  @Test
  void testUnknownPath() {
    // Arrange: Pass an unknown directory path
    String[] args = {"/unknown/path/doesnt/exist"};

    // Act & Assert: Expect a thrown exception
    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testInvalidPath() {
    // Arrange: Pass a path that isn't a directory
    String[] args = {"-nc", "-h", "penguin.jpg"};

    // Act & Assert: Expect a thrown exception
    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testFlagsAfterPath() {
    // Arrange: Pass a path and flags in reversed order
    String[] args = {"myfolder/subfolder", "-h", "-nc"};

    // Act & Assert: Expect a thrown exception
    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }
}