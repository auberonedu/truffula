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
    public void testPrintTree_WithColors(@TempDir File tempDir) throws IOException {
        // Arrange
        File rootFolder = new File(tempDir, "root");
        assertTrue(rootFolder.mkdir(), "Root folder should be created");

        File subFolder = new File(rootFolder, "sub");
        assertTrue(subFolder.mkdir(), "Sub-folder should be created");

        File fileX = new File(subFolder, "X.txt");
        File fileY = new File(subFolder, "Y.txt");
        assertTrue(fileX.createNewFile(), "X.txt should be created");
        assertTrue(fileY.createNewFile(), "Y.txt should be created");

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Create TruffulaPrinter instance (With colors)
        TruffulaOptions options = new TruffulaOptions(rootFolder, false, true);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Act
        printer.printTree();

        // Assert
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Expected output with color codes
        String white = ConsoleColor.WHITE.getCode();
        String purple = ConsoleColor.PURPLE.getCode();
        String yellow = ConsoleColor.YELLOW.getCode();
        String reset = ConsoleColor.RESET.getCode();

        String expectedOutput =
            white + "root/" + nl + reset +
            purple + "   sub/" + nl + reset +
            yellow + "      X.txt" + nl + reset +
            yellow + "      Y.txt" + nl + reset;

        assertEquals(expectedOutput, output, "Output should match expected nested folder structure with colors");
    }

    @Test
    public void testPrintTree_ColoredTextNoColors() {
        TruffulaOptions options = new TruffulaOptions(new File("."), false, false);
        TruffulaPrinter printer = new TruffulaPrinter(options);
    
        assertEquals("A.txt", printer.coloredText("A.txt", 0));
        assertEquals("   A.txt", printer.coloredText("A.txt", 1));
        assertEquals("      B.txt", printer.coloredText("B.txt", 2));
    }
    
    @Test
    public void testPrintIndentedSpaces() {
        assertEquals("A.txt", TruffulaPrinter.printIndentedSpaces("A.txt", 0));
        assertEquals("   A.txt", TruffulaPrinter.printIndentedSpaces("A.txt", 1));
        assertEquals("      A.txt", TruffulaPrinter.printIndentedSpaces("A.txt", 2));
    }

        @Test
        public void testPrintTree_EmptyDirectory(@TempDir File tempDir) throws IOException {
        // Arrange
        File emptyDir = new File(tempDir, "emptyDir");
        assertTrue(emptyDir.mkdir(), "emptyDir should be created");

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Set up TruffulaOptions and TruffulaPrinter
        TruffulaOptions options = new TruffulaOptions(emptyDir, false, true);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Act
        printer.printTree();

        // Assert
        String output = baos.toString();
        String nl = System.lineSeparator();
        String expectedOutput = ConsoleColor.WHITE.getCode() + "emptyDir/" + nl + ConsoleColor.RESET.getCode();

        assertEquals(expectedOutput, output, "Output should only contain the root directory name");
    }

    @Test
    public void testPrintTree_SingleFileInRoot(@TempDir File tempDir) throws IOException {
        // Arrange
        File rootDir = new File(tempDir, "rootDir");
        assertTrue(rootDir.mkdir(), "rootDir should be created");

        File singleFile = new File(rootDir, "file.txt");
        assertTrue(singleFile.createNewFile(), "file.txt should be created");

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Set up TruffulaOptions and TruffulaPrinter
        TruffulaOptions options = new TruffulaOptions(rootDir, false, true);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Act
        printer.printTree();

        // Assert
        String output = baos.toString();
        String nl = System.lineSeparator();
        String expectedOutput = ConsoleColor.WHITE.getCode() + "rootDir/" + nl + ConsoleColor.RESET.getCode() +
                                ConsoleColor.PURPLE.getCode() + "   file.txt" + nl + ConsoleColor.RESET.getCode();

        assertEquals(expectedOutput, output, "Output should contain the root directory and the single file");
    }

    @Test
    public void testPrintTree_SingleSubdirectory(@TempDir File tempDir) {
        // Arrange
        File rootDir = new File(tempDir, "rootDir");
        assertTrue(rootDir.mkdir(), "rootDir should be created");

        File subDir = new File(rootDir, "subDir");
        assertTrue(subDir.mkdir(), "subDir should be created");

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Set up TruffulaOptions and TruffulaPrinter
        TruffulaOptions options = new TruffulaOptions(rootDir, false, true);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Act
        printer.printTree();

        // Assert
        String output = baos.toString();
        String nl = System.lineSeparator();
        String expectedOutput = ConsoleColor.WHITE.getCode() + "rootDir/" + nl + ConsoleColor.RESET.getCode() +
                                ConsoleColor.PURPLE.getCode() + "   subDir/" + nl + ConsoleColor.RESET.getCode();

        assertEquals(expectedOutput, output, "Output should contain the root directory and the subdirectory");
    }
}
