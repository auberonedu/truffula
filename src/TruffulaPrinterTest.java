import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

        // System.out.println("Root directory: " + options.getRoot().getAbsolutePath());

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);

        System.out.println("Expected" + expected.toString());
        System.out.println(output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream_Retest(@TempDir File tempDir)
            throws IOException {

        File testFolder = new File(tempDir, "testFolder");
        assertTrue(testFolder.mkdir(), "testFolder should be created");

        File file1 = new File(testFolder, "File1.txt");
        File file2 = new File(testFolder, "file2.txt");
        File file3 = new File(testFolder, "file3.txt");
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        File subfolder = new File(testFolder, "Subfolder");
        assertTrue(subfolder.mkdir(), "Subfolder directory should be created");

        File subFile1 = new File(subfolder, "subfile1.txt");
        File subFile2 = new File(subfolder, "subfile2.txt");
        subFile1.createNewFile();
        subFile2.createNewFile();

        File images = new File(subfolder, "Images");
        assertTrue(images.mkdir(), "Images directory should be created");

        File img1 = new File(images, "image1.jpg");
        File img2 = new File(images, "image2.jpg");
        img1.createNewFile();
        img2.createNewFile();

        TruffulaOptions options = new TruffulaOptions(testFolder, false, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        printer.printTree();

        String output = baos.toString();
        String nl = System.lineSeparator();

        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("testFolder/").append(nl).append(reset);
        expected.append(purple).append("   File1.txt").append(nl).append(reset);
        expected.append(purple).append("   file2.txt").append(nl).append(reset);
        expected.append(purple).append("   file3.txt").append(nl).append(reset);
        expected.append(purple).append("   Subfolder/").append(nl).append(reset);
        expected.append(yellow).append("      Images/").append(nl).append(reset);
        expected.append(white).append("         image1.jpg").append(nl).append(reset);
        expected.append(white).append("         image2.jpg").append(nl).append(reset);
        expected.append(yellow).append("      subfile1.txt").append(nl).append(reset);
        expected.append(yellow).append("      subfile2.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);

        // System.out.println("Root directory: " + options.getRoot().getAbsolutePath());

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
    public void testPrintTreeWithColorPurple(@TempDir File tempDir) throws IOException {
        File dir1 = new File(tempDir, "dir1");
        dir1.mkdir();
        File file1 = new File(tempDir, "file1.txt");
        file1.createNewFile();

        // options for printing with color
        TruffulaOptions options = new TruffulaOptions(tempDir, false, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // instance of TruffulaPrinter
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        printer.printTree();

        String output = baos.toString();

        assertNotNull(output, "Output should not be null");

        System.out.println(output);

        // check that the output contains directory/file names
        assertTrue(output.contains("dir1"), "Output should contain dir1");
        assertTrue(output.contains("file1.txt"), "Output should contain file1.txt");

        // verify that direct children are purple (\033[0;35m)
        assertTrue(output.contains("\033[0;35m"));
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
        File[] files = { file1, file2, file3 };
        File[] sortedFiles = AlphabeticalFileSorter.sort(files);

        // Verify that files are sorted correctly
        assertEquals("bear.txt", sortedFiles[0].getName());
        assertEquals("lion.txt", sortedFiles[1].getName());
        assertEquals("zebra.txt", sortedFiles[2].getName());
    }

    @Test
    public void testRootDirectoryUsed(@TempDir File tempDir) throws IOException {
        // make sure the directory is empty
        assertTrue(tempDir.exists());
        assertTrue(tempDir.isDirectory());

        // setup the TruffulaOptions with the temporary directory as the root
        String[] args = { tempDir.getAbsolutePath() };
        TruffulaOptions options = new TruffulaOptions(args);

        // check if the root directory in the TruffulaOptions matches the tempDir
        assertEquals(tempDir, options.getRoot());
    }

    @Test
    public void testPrintTreeDirectory(@TempDir File tempDir) throws IOException {
        // Create sample files and directories in the tempDir
        File dir1 = new File(tempDir, "dir1");
        dir1.mkdir();
        File file1 = new File(tempDir, "file1.txt");
        file1.createNewFile();

        // Set up TruffulaOptions
        TruffulaOptions options = new TruffulaOptions(new String[] { tempDir.getAbsolutePath() });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        printer.printTree();

        String output = baos.toString().trim();

        // Check if the output contains the expected directory and file names
        assertTrue(output.contains("dir1/"));
        assertTrue(output.contains("file1.txt"));
    }



}
