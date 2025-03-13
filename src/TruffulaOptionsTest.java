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
  
  @Test //test will check to make sure default -h is showhidden is false, and that it works with only use color and file
  void testWith2Arg(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());  
    assertTrue(!options.isShowHidden());
    assertFalse(options.isUseColor());
  }


  @Test
  void testOnlyOneArg(@TempDir File tempDir) throws FileNotFoundException {
    //arrange
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args ={directoryPath};
    
    //act:
    TruffulaOptions options = new TruffulaOptions(args);

    //assert
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
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
  void testForUnkownFlags(@TempDir File tempDir) throws IllegalArgumentException {
    // Arrange:
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-hm", "-y",directoryPath};

    // Act:
    //TruffulaOptions options = new TruffulaOptions(args);

    // Assert:
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args) );
    assertThrows (IllegalArgumentException.class, () ->  new TruffulaOptions(args));
    
  }

  // Checks for Illegal Argument if path argument is missing
  @Test
  void testForMissingPathArgument(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();

    File directory2 = new File(tempDir, "subfolder2");
    // Act:
    String[] args = {"-h",directory2.toString()};
    //TruffulaOptions options = new TruffulaOptions(args);
    // Assert:
    assertThrows (FileNotFoundException.class, () ->  new TruffulaOptions(args));

  }

  // Checks for File Not Found if specified directory doesn't exist
  @Test
  void testNonexistentDirectory(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:
    File directory = new File(tempDir, "subfolder");

    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-hm", "-y",directoryPath};
    // Act:

    // Assert:
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args) );
  }

  // Checks for File Not found if path points to a file instead of directory
  @Test
  void testsForAFile(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange:
    File directory = new File("/src/test.java");

    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-hm", "-y",directoryPath};
    // Act:

    // Assert:
    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args) );
  }
}
