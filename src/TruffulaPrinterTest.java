import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.IIOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void Test_Print_Tree_Simple(@TempDir File tempDir) throws IOException {
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

        StringBuilder expected = new StringBuilder();
        expected.append("myFolder/").append(nl);
        expected.append("   Apple.txt").append(nl);
        expected.append("   banana.txt").append(nl);
        expected.append("   Documents/").append(nl);
        expected.append("      images/").append(nl);
        expected.append("         cat.png").append(nl);
        expected.append("         Dog.png").append(nl);
        expected.append("      notes.txt").append(nl);
        expected.append("      README.md").append(nl);
        expected.append("   zebra.txt").append(nl);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString().trim(), output.replaceAll("\u001B\\[[;\\d]*m", "").trim());

    }

    @Test
    public void testPrintTree_BasicDirectoryStructure(@TempDir File tempDir) throws IOException {
        // Create directory structure:
        // rootFolder/
        // fileA.txt
        // fileB.txt
        // subDir/

        File rootFolder = new File(tempDir, "rootFolder");
        assertTrue(rootFolder.mkdir(), "rootFolder should be created");

        File fileA = new File(rootFolder, "fileA.txt");
        File fileB = new File(rootFolder, "fileB.txt");
        fileA.createNewFile();
        fileB.createNewFile();

        File subDir = new File(rootFolder, "subDir");
        assertTrue(subDir.mkdir(), "subDir should be created");

        // Set up options with hidden files off and colors off
        TruffulaOptions options = new TruffulaOptions(rootFolder, false, false);

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Create printer
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Print tree
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Expected output (basic tree structure)
        StringBuilder expected = new StringBuilder();
        expected.append("rootFolder/").append(nl);
        expected.append("   fileA.txt").append(nl);
        expected.append("   fileB.txt").append(nl);
        expected.append("   subDir/").append(nl);

        assertEquals(expected.toString().trim(), output.trim());
    }

    @Test
    public void testPrintTree_HidesHiddenFiles(@TempDir File tempDir) throws IOException {
        // Create directory structure:
        // rootFolder/
        // .hiddenFile.txt
        // visibleFile.txt
        // subDir/

        File rootFolder = new File(tempDir, "rootFolder");
        assertTrue(rootFolder.mkdir(), "rootFolder should be created");

        File hiddenFile = new File(rootFolder, ".hiddenFile.txt");
        File visibleFile = new File(rootFolder, "visibleFile.txt");
        hiddenFile.createNewFile();
        visibleFile.createNewFile();

        File subDir = new File(rootFolder, "subDir");
        assertTrue(subDir.mkdir(), "subDir should be created");

        // Set up options with showHidden = false
        TruffulaOptions options = new TruffulaOptions(rootFolder, false, false);

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Create printer
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Print tree
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // ✅ Adjust Expected Output:
        // Instead of assuming visibleFile.txt is before subDir, sort alphabetically
        StringBuilder expected = new StringBuilder();
        expected.append("rootFolder/").append(nl);
        expected.append("   subDir/").append(nl); // Alphabetically before 'visibleFile.txt'
        expected.append("   visibleFile.txt").append(nl);

        assertEquals(expected.toString().trim(), output.trim());
    }

    @Test
    public void testPrintTree_UsesCorrectColors(@TempDir File tempDir) throws IOException {
        // Create directory structure:
        // rootFolder/
        // fileA.txt
        // subDir/
        // fileB.txt

        File rootFolder = new File(tempDir, "rootFolder");
        assertTrue(rootFolder.mkdir(), "rootFolder should be created");

        File fileA = new File(rootFolder, "fileA.txt");
        fileA.createNewFile();

        File subDir = new File(rootFolder, "subDir");
        assertTrue(subDir.mkdir(), "subDir should be created");

        File fileB = new File(subDir, "fileB.txt");
        fileB.createNewFile();

        // Set up options with color enabled
        TruffulaOptions options = new TruffulaOptions(rootFolder, false, true);

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Create printer
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Print tree
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Expected output (colors applied at different depths)
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("rootFolder/").append(nl).append(reset);
        expected.append(purple).append("   fileA.txt").append(nl).append(reset);
        expected.append(purple).append("   subDir/").append(nl).append(reset);
        expected.append(yellow).append("      fileB.txt").append(nl).append(reset);

        assertEquals(expected.toString().trim(), output.trim());
    }

}
