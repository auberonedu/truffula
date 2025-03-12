import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TruffulaPrinterTest {

    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        // .hidden.txt
        // Apple.txt
        // banana.txt
        // Documents/
        // images/
        // Cat.png
        // cat.png
        // Dog.png
        // notes.txt
        // README.md
        // zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create a hidden file in myFolder
        File hidden = new File(myFolder, ".hidden.txt");
        hidden.createNewFile();

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        expected.append(yellow).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(yellow).append("      notes.txt").append(nl).append(reset);
        expected.append(yellow).append("      README.md").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree(@TempDir File tempDir) throws IOException {
        // Set up a few files and directories inside the tempDir for testing
        File dir1 = new File(tempDir, "dir1");
        dir1.mkdir();
        File file1 = new File(tempDir, "file1.txt");
        file1.createNewFile();

        TruffulaOptions options = new TruffulaOptions(tempDir, false, true);  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Create an instance of TruffulaPrinter
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
        
        // Call the printTree method
        printer.printTree();
        
        String output = baos.toString();

        assertNotNull(output, "Output should not be null");

        assertTrue(output.contains("dir1"), "Output should contain dir1");
        assertTrue(output.contains("file1.txt"), "Output should contain file1.txt");
    }

    @Test
    public void testPrintTreeAlphabetization(@TempDir File tempDir) throws IOException {
    
        // create test files in an unsorted order
        File file1 = new File(tempDir, "zebra.txt");
        File file2 = new File(tempDir, "lion.txt");
        File file3 = new File(tempDir, "bear.txt");
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        // sort files using AlphabeticalFileSorter
        File[] files = {file1, file2, file3};
        File[] sortedFiles = AlphabeticalFileSorter.sort(files);

        // Verify that files are sorted correctly
        assertEquals("bear.txt", sortedFiles[0].getName());
        assertEquals("lion.txt", sortedFiles[1].getName());
        assertEquals("zebra.txt", sortedFiles[2].getName());
    }
}
