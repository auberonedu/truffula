import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    @Test
    public void testPrintTreeNoHiddenNoColors(@TempDir File tempDir) throws IOException {
        // Create test directory structure:
        // tempDir/
        // Apple.txt
        // banana.txt
        // Documents/
        // notes.txt
        // README.md
        // zebra.txt

        // Create main test folder inside tempDir
        File myFolder = new File(tempDir, "testFolder");
        assertTrue(myFolder.mkdir(), "testFolder should be created");

        // Create visible files in testFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create subdirectory "Documents"
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Set up TruffulaOptions with:
        // - showHidden = false (do NOT show hidden files)
        // - useColor = false (do NOT use colors)
        TruffulaOptions options = new TruffulaOptions(myFolder, false, false);

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

        // Adding the reset
        String reset = "\033[0m";
        String blue = "\033[0;34m";
        
        // Build expected output (without colors and without hidden files)
        StringBuilder expected = new StringBuilder();
        expected.append(reset).append(blue).append("Starting directory: testFolder/").append(nl);
        expected.append(reset).append("Apple.txt").append(nl);
        expected.append(reset).append("banana.txt").append(nl);
        expected.append(reset).append("Documents/").append(nl);
        expected.append(reset).append("  notes.txt").append(nl);
        expected.append(reset).append("  README.md").append(nl);
        expected.append(reset).append("zebra.txt").append(nl);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTreeNoHiddenYesColors(@TempDir File tempDir) throws IOException {
        // Create test directory structure:
        // tempDir/
        // Apple.txt
        // banana.txt
        // Documents/
        // notes.txt
        // README.md
        // zebra.txt

        // Create main test folder inside tempDir
        File myFolder = new File(tempDir, "testFolder");
        assertTrue(myFolder.mkdir(), "testFolder should be created");

        // Create visible files in testFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create subdirectory "Documents"
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Set up TruffulaOptions with:
        // - showHidden = false (do NOT show hidden files)
        // - useColor = false (do NOT use colors)
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

        // Adding the reset
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String blue = "\033[0;34m";
        // Build expected output (without colors and without hidden files)
        StringBuilder expected = new StringBuilder();
        expected.append(reset).append(blue).append("Starting directory: testFolder/").append(nl);
        
        expected.append(reset).append(white).append("Apple.txt").append(nl);
        expected.append(reset).append(white).append("banana.txt").append(nl);
        expected.append(reset).append(white).append("Documents/").append(nl);
        expected.append(reset).append(purple).append("  notes.txt").append(nl);
        expected.append(reset).append(purple).append("  README.md").append(nl);
        expected.append(reset).append(white).append("zebra.txt").append(nl);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }


}