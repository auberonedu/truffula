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

        // Create a hidden file in myFolder
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
    public void testPrintTree_ColorsApplied(@TempDir File tempDir) throws IOException {

        // Create the directory structure
        // World/
        //    Land/
        //    Ocean/
        //       Fish/

        // Create test directory structure
        File world = new File(tempDir, "World");
        assertTrue(world.mkdir(), "World folder should be created");

        File land = new File(world, "Land");
        assertTrue(land.mkdir(), "Land folder should be created");

        File ocean = new File(world, "Ocean");
        assertTrue(ocean.mkdir(), "Ocean folder should be created");

        File fish = new File(ocean, "Fish");
        assertTrue(fish.mkdir(), "Fish folder should be created");

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Initialize TruffulaPrinter
        TruffulaOptions options = new TruffulaOptions(world, false, true);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        printer.printTree();

        // Get actual output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // define ANSI color codes
        String reset = "\033[0m";
        String white = "\033[0;37m"; 
        String purple = "\033[0;35m"; 
        String yellow = "\033[0;33m"; 

        // build expected output
        StringBuilder expected = new StringBuilder();
        expected.append(white).append("World/").append(nl).append(reset);
        expected.append(purple).append("   Land/").append(nl).append(reset);
        expected.append(purple).append("   Ocean/").append(nl).append(reset);
        expected.append(yellow).append("      Fish/").append(nl).append(reset);
        
        // assert that the output matches the expected output
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_deeplyNestedDataTrees(@TempDir File tempDir) throws IOException {

        //Creating the directory structure
        //dataTree/
        //level1/
        //      File.1txt
        //      Level2/
        //         Level3/
        //            File2.txt
        //            Level4/
        //               File3.txt
        
        //create a root folder "dataTree"
        File dataTree = new File(tempDir, "DataTree");
        assertTrue(dataTree.mkdir(), "DataTree folder should be created");

        //create nested files and directories
        File level1 = new File(dataTree, "Level1");
        assertTrue(level1.mkdir(), "Level1 folder should be created");

        File level2 = new File(level1, "Level2");
        assertTrue(level2.mkdir(), "Level2 folder should be created");

        File level3 = new File(level2, "Level3");
        assertTrue(level3.mkdir(), "Level3 folder should be created");

        File level4 = new File(level3, "Level4");
        assertTrue(level4.mkdir(), "Level4 folder should be created");

        //create files at different levels
        File file1 = new File(level1, "File1.txt");
        File file2 = new File(level3, "File2.txt");
        File file3 = new File(level4, "File3.txt");
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();

        // Capture output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Initialize TruffulaPrinter
        TruffulaOptions options = new TruffulaOptions(dataTree, false, true);
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        printer.printTree();

        // Get actual output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // define ANSI color codes
        String reset = "\033[0m";
        String white = "\033[0;37m"; 
        String purple = "\033[0;35m"; 
        String yellow = "\033[0;33m"; 

        //Building the output expected
        StringBuilder expected = new StringBuilder();
        expected.append(white).append("DataTree/").append(nl).append(reset);
        expected.append(purple).append("   Level1/").append(nl).append(reset);
        expected.append(purple).append("      File1.txt").append(nl).append(reset);
        expected.append(purple).append("      Level2/").append(nl).append(reset);
        expected.append(purple).append("         Level3/").append(nl).append(reset);
        expected.append(purple).append("            File2.txt").append(nl).append(reset);
        expected.append(purple).append("            Level4/").append(nl).append(reset);
        expected.append(purple).append("               File3.txt").append(nl).append(reset);

        // assert that the output matches the expected output
        assertEquals(expected.toString(), output);
}
