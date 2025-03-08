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
        //the example directory structure:
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
        File catCapital = new File(images, "Cat.png");
        cat.createNewFile();
        dog.createNewFile();
        catCapital.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        //TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree output goes to printStream
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        // Expected output:
        // Level 0: white, Level 1: purple, Level 2: yellow, Level 3: white (cycling through colors)
        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        // In myFolder the visible entries sorted case insensitively are
        // Apple.txt, banana.txt, Documents, zebra.txt
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        // In Documents, entries sorted: images, README.md, notes.txt
        expected.append(yellow).append("      images/").append(nl).append(reset);
        // In images sorted entries case-insensitive with lexicographical tie-breakers
        // Cat.png, cat.png, Dog.png
        expected.append(white).append("         Cat.png").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(yellow).append("      README.md").append(nl).append(reset);
        expected.append(yellow).append("      notes.txt").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        assertEquals(expected.toString(), output);
    }

    //test for no color
    @Test
    public void testPrintTree_NoColor(@TempDir File tempDir) throws IOException {
        //a simple structure:
        // Folder/
        //    file.txt
        //    SubDir/
        //       subfile.txt

        File folder = new File(tempDir, "Folder");
        assertTrue(folder.mkdir(), "Folder should be created");
        File file = new File(folder, "file.txt");
        file.createNewFile();
        File subDir = new File(folder, "SubDir");
        assertTrue(subDir.mkdir(), "SubDir should be created");
        File subFile = new File(subDir, "subfile.txt");
        subFile.createNewFile();

        //do not show hidden, do not use color.
        TruffulaOptions options = new TruffulaOptions(folder, false, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
        printer.printTree();

        String output = baos.toString();
        String nl = System.lineSeparator();

        // When useColor is false, every line should be printed in white.
        String reset = "\033[0m";
        String white = "\033[0;37m";

        // Expected ordering for "Folder", children sorted: file.txt then SubDir.
        StringBuilder expected = new StringBuilder();
        expected.append(white).append("Folder/").append(nl).append(reset);
        expected.append(white).append("   file.txt").append(nl).append(reset);
        expected.append(white).append("   SubDir/").append(nl).append(reset);
        expected.append(white).append("      subfile.txt").append(nl).append(reset);

        assertEquals(expected.toString(), output);
    }
}
