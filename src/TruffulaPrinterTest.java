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
    public void testPrintTree_ExactOutput_WithCustomPrintStream_Simple(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    Apple.txt
        //    Banana.txt
        //    RecipeFolder/

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "Banana.txt");
        apple.createNewFile();
        banana.createNewFile();

        // Create subdirectory "RecipeFolder" in myFolder
        File recipeFolder = new File(myFolder, "RecipeFolder");
        assertTrue(recipeFolder.mkdir(), "RecipeFolder directory should be created");

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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(purple).append("   Apple.txt").append(nl);
        expected.append(reset).append(purple).append("   Banana.txt").append(nl);
        expected.append(reset).append(purple).append("   RecipeFolder/").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithEmptyDirectory(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // emptyFolder/

        // Create "emptyFolder"
        File emptyFolder = new File(tempDir, "emptyFolder");
        assertTrue(emptyFolder.mkdir(), "myFolder should be created");

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(emptyFolder, false, true);

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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("emptyFolder/").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithAlphabeticalSorting(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // aFolder/
        //    X.txt
        //    Z.txt
        //    A.txt
        //    dFolder/
        //    cFolder/
        //    bFolder/

        // Create "aFolder"
        File aFolder = new File(tempDir, "aFolder");
        assertTrue(aFolder.mkdir(), "aFolder should be created");

        // Create visible files in myFolder
        File X = new File(aFolder, "X.txt");
        File Z = new File(aFolder, "Z.txt");
        File A = new File(aFolder, "A.txt");
        X.createNewFile();
        Z.createNewFile();
        A.createNewFile();

        // Create subdirectory "dFolder" in aFolder
        File dFolder = new File(aFolder, "dFolder");
        assertTrue(dFolder.mkdir(), "dFolder directory should be created");
        // Create subdirectory "cFolder" in aFolder
        File cFolder = new File(aFolder, "cFolder");
        assertTrue(cFolder.mkdir(), "cFolder directory should be created");
        // Create subdirectory "bFolder" in aFolder
        File bFolder = new File(aFolder, "bFolder");
        assertTrue(bFolder.mkdir(), "bFolder directory should be created");

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(aFolder, false, true);

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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("aFolder/").append(nl);
        expected.append(reset).append(purple).append("   A.txt").append(nl);
        expected.append(reset).append(purple).append("   bFolder/").append(nl);
        expected.append(reset).append(purple).append("   cFolder/").append(nl);
        expected.append(reset).append(purple).append("   dFolder/").append(nl);
        expected.append(reset).append(purple).append("   X.txt").append(nl);
        expected.append(reset).append(purple).append("   Z.txt").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithHiddenFilesEnabled(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    Banana.txt
        //    RecipeFolder/
        //       Kiwi.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "Banana.txt");
        apple.createNewFile();
        banana.createNewFile();

        // Create hidden file in myFolder
        File hidden = new File(myFolder, ".hidden.txt");
        hidden.createNewFile();

        // Create subdirectory "RecipeFolder" in myFolder
        File recipeFolder = new File(myFolder, "RecipeFolder");
        assertTrue(recipeFolder.mkdir(), "RecipeFolder directory should be created");

        // Create visible file in RecipeFolder
        File kiwi = new File(recipeFolder, "Kiwi.txt");
        kiwi.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(myFolder, true, true);

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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(purple).append("   .hidden.txt").append(nl);
        expected.append(reset).append(purple).append("   Apple.txt").append(nl);
        expected.append(reset).append(purple).append("   Banana.txt").append(nl);
        expected.append(reset).append(purple).append("   RecipeFolder/").append(nl);
        expected.append(reset).append(yellow).append("      Kiwi.txt").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithHiddenFilesDisabled(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    Banana.txt
        //    RecipeFolder/
        //       Kiwi.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "Banana.txt");
        apple.createNewFile();
        banana.createNewFile();

        // Create hidden file in myFolder
        File hidden = new File(myFolder, ".hidden.txt");
        hidden.createNewFile();

        // Create subdirectory "RecipeFolder" in myFolder
        File recipeFolder = new File(myFolder, "RecipeFolder");
        assertTrue(recipeFolder.mkdir(), "RecipeFolder directory should be created");

        // Create visible file in RecipeFolder
        File kiwi = new File(recipeFolder, "Kiwi.txt");
        kiwi.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(purple).append("   Apple.txt").append(nl);
        expected.append(reset).append(purple).append("   Banana.txt").append(nl);
        expected.append(reset).append(purple).append("   RecipeFolder/").append(nl);
        expected.append(reset).append(yellow).append("      Kiwi.txt").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithColorsEnabled(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    Apple.txt
        //    Banana.txt
        //    RecipeFolder/

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "Banana.txt");
        apple.createNewFile();
        banana.createNewFile();

        // Create subdirectory "RecipeFolder" in myFolder
        File documents = new File(myFolder, "RecipeFolder");
        assertTrue(documents.mkdir(), "RecipeFolder directory should be created");

        // Set up TruffulaOptions with showHidden = false and useColor = false
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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(purple).append("   Apple.txt").append(nl);
        expected.append(reset).append(purple).append("   Banana.txt").append(nl);
        expected.append(reset).append(purple).append("   RecipeFolder/").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithColorsDisabled(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    Apple.txt
        //    Banana.txt
        //    RecipeFolder/

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "Banana.txt");
        apple.createNewFile();
        banana.createNewFile();

        // Create subdirectory "RecipeFolder" in myFolder
        File documents = new File(myFolder, "RecipeFolder");
        assertTrue(documents.mkdir(), "RecipeFolder directory should be created");

        // Set up TruffulaOptions with showHidden = false and useColor = false
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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(white).append("   Apple.txt").append(nl);
        expected.append(reset).append(white).append("   Banana.txt").append(nl);
        expected.append(reset).append(white).append("   RecipeFolder/").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithOnlySubdirectories(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    documentFolder/
        //    photoFolder/
        //    recipeFolder/
        //       desertFolder/

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create subdirectory "DocumentFolder" in myFolder
        File documentFolder = new File(myFolder, "documentFolder");
        assertTrue(documentFolder.mkdir(), "documentFolder should be created");

        // Create subdirectory "photoFolder" in myFolder
        File photoFolder = new File(myFolder, "photoFolder");
        assertTrue(photoFolder.mkdir(), "photoFolder should be created");

        // Create subdirectory "recipeFolder" in myFolder
        File recipeFolder = new File(myFolder, "recipeFolder");
        assertTrue(recipeFolder.mkdir(), "recipeFolder should be created");

        // Create subdirectory "DesertFolder" in recipeFolder
        File desertFolder = new File(recipeFolder, "desertFolder");
        assertTrue(desertFolder.mkdir(), "desertFolder should be created");

        // Set up TruffulaOptions with showHidden = false and useColor = false
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
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(purple).append("   documentFolder/").append(nl);
        expected.append(reset).append(purple).append("   photoFolder/").append(nl);
        expected.append(reset).append(purple).append("   recipeFolder/").append(nl);
        expected.append(reset).append(yellow).append("      desertFolder/").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTree_ExactOutput_WithSpecialCharacterInFileNames(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    A&B.txt
        //    R#B*3.txt
        //    RecipeFolder/
        //       &XYZ^.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File a = new File(myFolder, "A&B.txt");
        File r = new File(myFolder, "R#B*3.txt");
        a.createNewFile();
        r.createNewFile();

        // Create subdirectory "RecipeFolder" in myFolder
        File recipe = new File(myFolder, "RecipeFolder");
        assertTrue(recipe.mkdir(), "RecipeFolder directory should be created");

        // Create file in RecipeFolder
        File xyz = new File(recipe, "&XYZ^.txt");
        xyz.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
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

        //// Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(purple).append("   A&B.txt").append(nl);
        expected.append(reset).append(purple).append("   R#B*3.txt").append(nl);
        expected.append(reset).append(purple).append("   RecipeFolder/").append(nl);
        expected.append(reset).append(yellow).append("      &XYZ^.txt").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }
    
    @Test
    public void testPrintTree_WithEmptyFileNames(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .txt
        //     .docx
        //    $.txt
        //    normalFile.txt

        // Create myFolder and files with special or empty filenames
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File emptyFile = new File(myFolder, " .txt");
        File emptyFile2 = new File(myFolder, " .docx");
        File specialFile = new File(myFolder, "$.txt");
        File normalFile = new File(myFolder, "normalFile.txt");

        emptyFile.createNewFile();
        emptyFile2.createNewFile();
        specialFile.createNewFile();
        normalFile.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
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

        // Build expected output with exact colors and indentation
        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl);
        expected.append(reset).append(white).append("    .docx").append(nl);
        expected.append(reset).append(white).append("    .txt").append(nl);
        expected.append(reset).append(white).append("   $.txt").append(nl);
        expected.append(reset).append(white).append("   normalFile.txt").append(nl);
        expected.append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }
}