import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertFalse;
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

    @Test //test for color make file directory so it gets to white 3 times
        public void testPrintTree_levelColor(@TempDir File tempDir) throws IOException {
            
            File level0 = new File(tempDir, "level0");
            assertTrue(level0.mkdir(), "level0 should be created");
            
            File level1 = new File(level0, "level1");
            assertTrue(level1.mkdir(), "level1 directory should be created");
            
            File level2 = new File(level1, "level2");
            assertTrue(level2.mkdir(), "level2 directory should be created");

            File level3 = new File(level2, "level3");
            assertTrue(level3.mkdir(), "level3 directory should be created");

            File level4 = new File(level3, "level4");
            assertTrue(level4.mkdir(), "level4 directory should be created");

            File level5 = new File(level4, "level5");
            assertTrue(level5.mkdir(), "level5 directory should be created");
    
            File level6 = new File(level5, "level6.txt");
            level6.createNewFile();
          
    
            TruffulaOptions options = new TruffulaOptions(level0, false, true);
    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(baos);
    
            TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    
            printer.printTree();
    
            String output = baos.toString();

            String white = "\033[0;37m";
            String purple = "\033[0;35m";
            String yellow = "\033[0;33m";

            // Level 0: White
            assertTrue(output.contains(white), "Output for level 0 should be white");
    
            // Level 1: Purple
            assertTrue(output.contains(purple), "Output for level 1 should be purple");
    
            // Level 2: Yellow
            assertTrue(output.contains(yellow), "Output for level 2 should be yellow");
    
            // Level 3: White
            assertTrue(output.contains(white), "Output for level 3 should be white");
    
            // Level 4: Purple
            assertTrue(output.contains(purple), "Output for level 4 should be purple");
    
            // Level 5: Yellow
            assertTrue(output.contains(yellow), "Output for level 5 should be yellow");
    
            // Level 6: White
            assertTrue(output.contains(white), "Output for level 6 should be white");
    
            assertTrue(output.contains("\u001B[0m"), "Output should contain reset color code after each print");
        }
    
        @Test // tests when an empty directory is used
    public void testEmptyDirectory(@TempDir File tempDir) throws IOException{
        File emptyDir = new File(tempDir, "emptyDirectory");
        assertTrue(emptyDir.mkdir(), "emptyDir should be created");

        TruffulaOptions options = new TruffulaOptions(emptyDir, false, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        TruffulaPrinter Printer = new TruffulaPrinter(options, printStream);

        Printer.printTree();

        String output = baos.toString();
        String n1 = System.lineSeparator();

        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("emptyDirectory/").append(n1).append(reset);

        assertEquals(expected, output);
    }



    @Test // tests that lexicographic situations are handled properly
    public void testLexicographicFiles(@TempDir File tempDir) throws IOException{

    }

    @Test // checks to see that colors are properly disabled
    public void testDisabledColor(@TempDir File tempDir) throws IOException{

    }

    @Test // tests when there are a ton of nested directories
    public void testSuperNestedDirectories(@TempDir File tempDir) throws IOException{

    }

    @Test // tests what happens when special characters are in the directory
    public void testSpecialCharacters(@TempDir File tempDir) throws IOException{

    }

}