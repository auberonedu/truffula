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
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

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

        // // Create a hidden file in myFolder
        // File hidden = new File(myFolder, ".hidden.txt");
        // hidden.createNewFile();

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
    public void testPrintTree_MultipleFiles(@TempDir File tempDir) throws IOException {
        //  myFolder/
        //    file1.txt
        //    file2.txt
        //    file3.txt

        // Create multiple files inside myFolder
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        File file1 = new File(tempDir, "file1.txt");
        File file2 = new File(tempDir, "file2.txt");
        File file3 = new File(tempDir, "file3.txt");

        assertTrue(file1.createNewFile(), "file1.txt should be created");
        assertTrue(file2.createNewFile(), "file2.txt should be created");
        assertTrue(file3.createNewFile(), "file3.txt should be created");

        // Capture output using a PrintStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Set up TruffulaOptions and TruffulaPrinter
        TruffulaOptions options = new TruffulaOptions(tempDir, false, false);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Run printTree()
        printer.printTree();

        // Get output
        String output = outputStream.toString();

        // Verify that all files are printed in the output
        assertTrue(output.contains("file1.txt"), "Output should contain 'file1.txt'");
        assertTrue(output.contains("file2.txt"), "Output should contain 'file2.txt'");
        assertTrue(output.contains("file3.txt"), "Output should contain 'file3.txt'");
    }

    @Test
    public void testPrintTree_ColorsAssignedCorrectly(@TempDir File tempDir) throws IOException {
        // Create structure:
        // tempDir/
        //    fileA.txt
        //    subDir/
        //       nestedDir/
        //          fileB.txt

    // Create directories and files
    File subDir = new File(tempDir, "subDir");
    assertTrue(subDir.mkdir(), "subDir should be created");

    File nestedDir = new File(subDir, "nestedDir");
    assertTrue(nestedDir.mkdir(), "nestedDir should be created");

    File fileA = new File(tempDir, "fileA.txt");
    assertTrue(fileA.createNewFile(), "fileA.txt should be created");

    File fileB = new File(nestedDir, "fileB.txt");
    assertTrue(fileB.createNewFile(), "fileB.txt should be created");

    // Capture output using a PrintStream
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    // Set up TruffulaOptions and TruffulaPrinter
    TruffulaOptions options = new TruffulaOptions(tempDir, false, true); // Enable color output
    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

    // Run printTree()
    printer.printTree();

    // Get output
    String output = outputStream.toString();
    System.out.println("Captured Output:\n" + output);

    // Verify that color codes are applied correctly based on indent level
    String white = ConsoleColor.WHITE.toString();
    String purple = ConsoleColor.PURPLE.toString();
    String yellow = ConsoleColor.YELLOW.toString();
    String reset = ConsoleColor.RESET.toString();

    // Assert using trimmed output to avoid formatting mismatches
    assertTrue(output.replaceAll("\\s+", "").contains(white + "subDir/"), "SubDir should be white (root level)");
    assertTrue(output.replaceAll("\\s+", "").contains(purple + "nestedDir/"), "NestedDir should be purple (level 1)");
    assertTrue(output.replaceAll("\\s+", "").contains(yellow + "fileB.txt"), "fileB.txt should be yellow (level 2)");
    assertTrue(output.contains(reset), "Output should reset color after printing");
    }
}
