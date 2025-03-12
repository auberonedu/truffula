import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    void testBasicStructure_NoColorNoHidden(@TempDir File tempDir) throws IOException {
        // Create:
        // root/
        //    subDir/
        //       inside.txt
        //    file1.txt
        File root = new File(tempDir, "root");
        assertTrue(root.mkdir());
        File subDir = new File(root, "subDir");
        assertTrue(subDir.mkdir());
        File inside = new File(subDir, "inside.txt");
        inside.createNewFile();
        File file1 = new File(root, "file1.txt");
        file1.createNewFile();

        TruffulaOptions opts = new TruffulaOptions(root, false, true); // showHidden doesn't matter for wave 4 yet

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        TruffulaPrinter printer = new TruffulaPrinter(opts, ps);
        printer.printTree();

        String output = baos.toString();

        assertTrue(output.contains("root/"));
        assertTrue(output.contains("subDir/"));
        assertTrue(output.contains("inside.txt"));
        assertTrue(output.contains("file1.txt"));
    }

    @Test
    void testHiddenFilesSkipped(@TempDir File tempDir) throws IOException {
        File root = new File(tempDir, "root");
        root.mkdir();
        File visible = new File(root, "visible.txt");
        visible.createNewFile();
        File hidden = new File(root, ".hidden.txt");
        hidden.createNewFile();

        TruffulaOptions opts = new TruffulaOptions(root, false, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        TruffulaPrinter printer = new TruffulaPrinter(opts, ps);
        printer.printTree();

        String output = baos.toString();
        assertTrue(output.contains("root/"));
        assertTrue(output.contains("visible.txt"));
        assertFalse(output.contains(".hidden.txt"));
    }

    @Test
    void testColorCycling(@TempDir File tempDir) throws IOException {
        File root = new File(tempDir, "root");
        root.mkdir();
        File child = new File(root, "child");
        child.mkdir();

        TruffulaOptions opts = new TruffulaOptions(root, false, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        TruffulaPrinter printer = new TruffulaPrinter(opts, ps);
        printer.printTree();

        String output = baos.toString();
        assertTrue(output.contains(ConsoleColor.WHITE.toString() + "root/"));
        assertTrue(output.contains(ConsoleColor.PURPLE.toString() + "   child/"));
    }
}
