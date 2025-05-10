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
        String[] args = { "-nc", "-h", directoryPath };

        // Act: Create TruffulaOptions instance
        TruffulaOptions options = new TruffulaOptions(args);

        // Assert: Check that the root directory is set correctly
        assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
        assertTrue(options.isShowHidden());
        assertFalse(options.isUseColor());
    }

    // missing directory argument should throw IllegalArgumentException
    @Test
    void testMissingDirectoryArgument() {
        String[] args = { "-h", "-nc" }; // No directory path provided

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
        assertEquals("Missing required directory path argument.", exception.getMessage());
    }

    // invalid directory should throw FileNotFoundException
    @Test
    void testInvalidDirectoryThrowsException() {
        String[] args = { "-h", "-nc", "/path/that/does/not/exist" };

        // Act & Assert
        Exception exception = assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
        assertTrue(exception.getMessage().contains("Directory does not exist"));
    }

    // path is a file instead of directory should throw FileNotFoundException
    @Test
    void testFileInsteadOfDirectory(@TempDir File tempDir) throws Exception {
        // Arrange: Create a file instead of a directory
        File tempFile = new File(tempDir, "testFile.txt");
        tempFile.createNewFile();
        String[] args = { tempFile.getAbsolutePath() };

        // Act & Assert
        Exception exception = assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
        assertTrue(exception.getMessage().contains("Path is not a directory"));
    }

    // THIS FINAL TEST IS NOT PASSING CURRENTLY PLEASE CHECK OUT

    // default behavior with only a valid directory (should enable color and hide
    // hidden files)
    @Test
    void testDefaultBehavior(@TempDir File tempDir) throws FileNotFoundException {
        // Arrange
        String[] args = { tempDir.getAbsolutePath() };

        // Act
        TruffulaOptions options = new TruffulaOptions(args);

        // Assert: Ensure the correct directory is set, and default flags are applied
        assertEquals(tempDir.getAbsolutePath(), options.getRoot().getAbsolutePath(),
                "Root directory should be set correctly");
        assertFalse(options.isShowHidden(), "Hidden files should NOT be shown by default");
        assertTrue(options.isUseColor(), "Color should be ENABLED by default");
    }
}
