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

  @Test
  void testNonValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
   //arrange

   String fakeDirectoryPath = "/f/d/s/a";

   String[] args = {"-h", "-nc", fakeDirectoryPath};


  //  TruffulaOptions options = new TruffulaOptions(args);


   assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testDefaultBehavior(@TempDir File tempDir) throws FileNotFoundException {
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
  void testInvalidArgument(@TempDir File tempDir) throws FileNotFoundException {
   //arrange

   String fakeDirectoryPath = "/f/d/s/a";

   String[] args = {"- ", "- ", fakeDirectoryPath};


  //  TruffulaOptions options = new TruffulaOptions(args);


   assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void noArgument(@TempDir File tempDir) throws FileNotFoundException {

   String fakeDirectoryPath = "/f/d/s/a";
   String[] args = {};
   assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

@Test
void testDuplicateFlags(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", "-h", "-nc", "-nc", directoryPath};  
    TruffulaOptions options = new TruffulaOptions(args);

    assertTrue(options.isShowHidden());  
    assertFalse(options.isUseColor());   
}

@Test
void testDirectoryNotLast(@TempDir File tempDir) {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = tempDir.getAbsolutePath();
    String[] args = {directoryPath, "-h"};  

    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
}


}
