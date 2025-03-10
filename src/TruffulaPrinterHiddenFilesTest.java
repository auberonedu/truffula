import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterHiddenFilesTest {


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
        //File hidden = new File(myFolder, ".hidden.txt");
        //hidden.createNewFile();

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
        assertFalse(options.isShowHidden());
        // assertTrue(hidden.isHidden());
        assertEquals(expected.toString(), output);
    }



    @Test
    public void testPrintTree_WithHiddenFiles(@TempDir File tempDir) throws IOException {
        // Build the following directory structure:
        // testFolder/
        //    .hidden.txt
        //    subDir/
        //       .hiddenSub.txt
        //       file1.txt
        //    visible.txt

        // Create "testFolder"
        File testFolder = new File(tempDir, "testFolder");
        assertTrue(testFolder.mkdir(), "testFolder should be created");

        // Create files in testFolder
        File hiddenFile = new File(testFolder, ".hidden.txt");
        File visibleFile = new File(testFolder, "visible.txt");
        hiddenFile.createNewFile();
        visibleFile.createNewFile();

        // Create subdirectory "subDir"
        File subDir = new File(testFolder, "subDir");
        assertTrue(subDir.mkdir(), "subDir should be created");

        // Create files in subDir
        File hiddenSubFile = new File(subDir, ".hiddenSub.txt");
        File file1 = new File(subDir, "file1.txt");
        hiddenSubFile.createNewFile();
        file1.createNewFile();

        // Set up TruffulaOptions with showHidden = true and useColor = true
        TruffulaOptions options = new TruffulaOptions(testFolder, true, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with the custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree and output will go to printStream
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Define the ANSI escape codes for the colors used.
        // In the printer, level 0 is white, level 1 is purple, level 2 is yellow.
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        // Build expected output according to the printer's recursion:
        // testFolder/ (white)
        //    .hidden.txt (purple)
        //    subDir/ (purple)
        //       .hiddenSub.txt (yellow)
        //       file1.txt (yellow)
        //    visible.txt (purple)
        StringBuilder expected = new StringBuilder();
        expected.append(white).append("testFolder/").append(nl).append(reset);
        expected.append(purple).append("   .hidden.txt").append(nl).append(reset);
        expected.append(purple).append("   subDir/").append(nl).append(reset);
        expected.append(yellow).append("      .hiddenSub.txt").append(nl).append(reset);
        expected.append(yellow).append("      file1.txt").append(nl).append(reset);
        expected.append(purple).append("   visible.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly.
        assertEquals(expected.toString(), output);
    }

    //test single file
    @Test
    public void testPrintTree_SingleFile(@TempDir File tempDir) throws IOException {
        // singleFolder/
        //    single.txt

        File singleFolder = new File(tempDir, "singleFolder");
        assertTrue(singleFolder.mkdir(), "singleFolder should be created");

        // One file
        File singleFile = new File(singleFolder, "single.txt");
        singleFile.createNewFile();

        TruffulaOptions options = new TruffulaOptions(singleFolder, false, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        TruffulaPrinter printer = new TruffulaPrinter(options, ps);
        printer.printTree();

        // we will just do a partial check and focusing on lines and indentation
        String output = baos.toString().replace("\r\n", "\n");
        // First line
        assertTrue(output.contains("singleFolder/"), "Root directory line should be present");

        // Indented line for the file
        // The code uses 3 spaces for each level
        assertTrue(output.contains("   single.txt"), "Should have an indented line for single.txt");
    }

    //A test that prints multiple files and a subdirectory
    //and ensures sorting is alphabetical case-insensitive
    @Test
    public void testPrintTree_MixedContent(@TempDir File tempDir) throws IOException {
        // structure:
        // mixedFolder/
        //   Beta.txt
        //   alpha.txt
        //   Sub/
        //      Zed.txt

        File mixedFolder = new File(tempDir, "mixedFolder");
        assertTrue(mixedFolder.mkdir(), "mixedFolder should be created");

        File beta = new File(mixedFolder, "Beta.txt");
        File alpha = new File(mixedFolder, "alpha.txt");
        beta.createNewFile();
        alpha.createNewFile();

        // Subfolder with one file
        File sub = new File(mixedFolder, "Sub");
        assertTrue(sub.mkdir(), "Sub folder should be created");
        File zed = new File(sub, "Zed.txt");
        zed.createNewFile();

        // If the code is truly sorting case-insensitively,
        // alpha.txt should appear before Beta.txt in the output.
        TruffulaOptions options = new TruffulaOptions(mixedFolder, false, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        TruffulaPrinter printer = new TruffulaPrinter(options, ps);
        printer.printTree();

        String output = baos.toString().replace("\r\n", "\n");

        // The root
        assertTrue(output.contains("mixedFolder/"));

        // alpha.txt < Beta.txt, so check order
        int alphaIndex = output.indexOf("alpha.txt");
        int betaIndex = output.indexOf("Beta.txt");
        assertTrue(alphaIndex < betaIndex, "alpha.txt should be listed before Beta.txt (case-insensitive sort)");

        // Sub/ present
        assertTrue(output.contains("Sub/"), "Should contain the subdirectory");

        // Indentation in front of "Zed.txt"
        assertTrue(output.contains("      Zed.txt"), "Zed.txt should be indented by 6 spaces (2 levels deep)");
    }

    // started working on wave 5`s tests, need to figure out how to check if colors are properly cycled in this test
    @Test
    public void testPrintTree_CyclingColors(@TempDir File tempDir) throws IOException {
        // Folder structure:
        // rootFolder/
        //   subFolder1/
        //      file1.txt
        //   subFolder2/
        //      file2.txt

        File rootFolder = new File(tempDir, "rootFolder");
        assertTrue(rootFolder.mkdir(), "rootFolder should be created");

        File subFolder1 = new File(rootFolder, "subFolder1");
        assertTrue(subFolder1.mkdir(), "subFolder1 should be created");

        File subFolder2 = new File(rootFolder, "subFolder2");
        assertTrue(subFolder2.mkdir(), "subFolder2 should be created");

        File file1= new File(subFolder1, "file1.txt");
        file1.createNewFile();

        File file2 = new File(subFolder2, "file2.txt");
        file2.createNewFile();

        TruffulaOptions options = new TruffulaOptions(rootFolder, false, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        
        TruffulaPrinter printer = new TruffulaPrinter(options, ps);

        printer.printTree();

        String output = baos.toString().replace("\r\n", "\n");

        assertTrue(output.contains("rootFolder/"));
        assertTrue(output.contains("   subFolder1/"));
        assertTrue(output.contains("      file1.txt"));
        assertTrue(output.contains("   subFolder2/"));
        assertTrue(output.contains("      file2.txt"));
    }
    
}