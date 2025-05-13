import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    /**
     * Checks if the current operating system is Windows.
     *
     * This method reads the "os.name" system property and checks whether it
     * contains the substring "win", which indicates a Windows-based OS.
     * 
     * You do not need to modify this method.
     *
     * @return true if the OS is Windows, false otherwise
     */
    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    /**
     * Creates a hidden file in the specified parent folder.
     * 
     * The filename MUST start with a dot (.).
     *
     * On Unix-like systems, files prefixed with a dot (.) are treated as hidden.
     * On Windows, this method also sets the DOS "hidden" file attribute.
     * 
     * You do not need to modify this method, but you SHOULD use it when creating hidden files
     * for your tests. This will make sure that your tests work on both Windows and UNIX-like systems.
     *
     * @param parentFolder the directory in which to create the hidden file
     * @param filename the name of the hidden file; must start with a dot (.)
     * @return a File object representing the created hidden file
     * @throws IOException if an I/O error occurs during file creation or attribute setting
     * @throws IllegalArgumentException if the filename does not start with a dot (.)
     */
    private static File createHiddenFile(File parentFolder, String filename) throws IOException {
        if(!filename.startsWith(".")) {
            throw new IllegalArgumentException("Hidden files/folders must start with a '.'");
        }
        File hidden = new File(parentFolder, filename);
        hidden.createNewFile();
        if(isWindows()) {
            Path path = Paths.get(hidden.toURI());
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
        return hidden;
    }

    @Test
    public void printTreeColorNullRoot() {

        // Set up TruffulaOptions with null root directory
        TruffulaOptions options = new TruffulaOptions(null, true, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree and expect exception (since root is null)
        assertThrows(IllegalArgumentException.class, () -> printer.printTree());

        // Verify that no output was printed
        assertTrue(baos.toString().isEmpty(), "Output should be empty when root directory is null");
    }

    @Test
    public void printTreeColorRootNotDirectory (@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        image.createNewFile();

        // Set up TruffulaOptions with non-directory root directory
        TruffulaOptions options = new TruffulaOptions(image, true, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree and expect exception (since root is null)
        assertThrows(IllegalArgumentException.class, () -> printer.printTree());

        // Verify that no output was printed
        assertTrue(baos.toString().isEmpty(), "Output should be empty when root directory is null");
    }

    @Test
    public void printTreeColorRootHidden (@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // .hidden-folder/
        //  image.png

        // Create hidden folder
        File hiddenFolder = new File(tempDir, ".hidden-folder");
        hiddenFolder.mkdir();
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());

        // Create files
        File image = new File(hiddenFolder, "image.png");
        image.createNewFile();

        // Set up TruffulaOptions with non-directory root directory
        TruffulaOptions options = new TruffulaOptions(image, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree and expect exception (since root is null)
        assertThrows(IllegalArgumentException.class, () -> printer.printTree());

        // Verify that no output was printed
        assertTrue(baos.toString().isEmpty(), "Output should be empty when root directory is null");
    }

    @Test
    public void printTreeColorTrueHiddenTrue(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(folder, true, true);

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
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(purple).append("   image.png").append(nl).append(reset);
        expected.append(purple).append("   nested-folder/").append(nl).append(reset);
        expected.append(yellow).append("      .hidden-folder/").append(nl).append(reset);
        expected.append(white).append("         42.png").append(nl).append(reset);
        expected.append(yellow).append("      .hidden.txt").append(nl).append(reset);
        expected.append(yellow).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(purple).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void printTreeColorFalseHiddenTrue(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = true and useColor = false
        TruffulaOptions options = new TruffulaOptions(folder, true, false);

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
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(white).append("   image.png").append(nl).append(reset);
        expected.append(white).append("   nested-folder/").append(nl).append(reset);
        expected.append(white).append("      .hidden-folder/").append(nl).append(reset);
        expected.append(white).append("         42.png").append(nl).append(reset);
        expected.append(white).append("      .hidden.txt").append(nl).append(reset);
        expected.append(white).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(white).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void printTreeColorTrueHiddenFalse(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(folder, false, true);

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
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(purple).append("   image.png").append(nl).append(reset);
        expected.append(purple).append("   nested-folder/").append(nl).append(reset);
        expected.append(yellow).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(purple).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void printTreeColorFalseHiddenFalse(@TempDir File tempDir) throws IOException {
        // Sample Directory:
        //
        // folder/
        //  image.png
        //  nested-folder/
        //      .hidden-folder/
        //          42.png
        //      .hidden.txt
        //      not-hidden.txt
        //  text.txt

        // Create folders
        File folder = new File(tempDir, "folder");
        folder.mkdir();
        File nestedFolder = new File(folder, "nested-folder");
        nestedFolder.mkdir();
        File hiddenFolder = new File(nestedFolder, ".hidden-folder");
        hiddenFolder.mkdir();

        // Create files
        File image = new File(folder, "image.png");
        File fortyTwo = new File(hiddenFolder, "42.png");
        File text = new File(folder, "text.txt");
        File notHidden = new File(nestedFolder, "not-hidden.txt");
        File hidden = new File(nestedFolder, ".hidden.txt");
        image.createNewFile();
        fortyTwo.createNewFile();
        text.createNewFile();
        notHidden.createNewFile();
        hidden.createNewFile();

        // set hidden files (for Windows)
        Path path = Paths.get(hiddenFolder.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        path = Paths.get(hidden.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        assertTrue(hiddenFolder.isHidden());
        assertTrue(hidden.isHidden());

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(folder, false, false);

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
        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(white).append("   image.png").append(nl).append(reset);
        expected.append(white).append("   nested-folder/").append(nl).append(reset);
        expected.append(white).append("      not-hidden.txt").append(nl).append(reset);
        expected.append(white).append("   text.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testNoOrderWithColor(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
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
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTreeSorted(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // Alphabet/
        //    a.txt
        //    b.txt
        //    c/
        //      d.txt
        //      e.txt
        //    zebra.txt

        // Create "Alphabet"
        File alphabet = new File(tempDir, "Alphabet");
        assertTrue(alphabet.mkdir(), "Alphabet should be created");

        // Create files in Alphabet
        File zebra = new File(alphabet, "zebra.txt");
        File b = new File(alphabet, "b.txt");
        File a = new File(alphabet, "a.txt");
        zebra.createNewFile();
        b.createNewFile();
        a.createNewFile();
        
        // Create subdirectory "c" in myFolder
        File c = new File(alphabet, "c");
        assertTrue(c.mkdir(), "Documents directory should be created");

        // Create files in myFolder
        File e = new File(c, "e.txt");
        File d = new File(c, "d.txt");
        e.createNewFile();
        d.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(alphabet, false, false);

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

        String reset = "\033[0m";
        String white = "\033[0;37m";

        StringBuilder expected = new StringBuilder();

        expected.append(white).append("Alphabet/").append(nl).append(reset);
        expected.append(white).append("   a.txt").append(nl).append(reset);
        expected.append(white).append("   b.txt").append(nl).append(reset);
        expected.append(white).append("   c/").append(nl).append(reset);
        expected.append(white).append("      d.txt").append(nl).append(reset);
        expected.append(white).append("      e.txt").append(nl).append(reset);
        expected.append(white).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

    @Test
    public void testPrintTreeSuperDeepDirectory(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        //  folder/
        //      folder/
        //          folder/
        //              folder/
        //                  folder/
        //                      folder/
        // ... 25 total folders


        // Create "Alphabet"
        File folder1 = new File(tempDir, "folder");
        folder1.mkdir();
        File folder2 = new File(folder1, "folder");
        folder2.mkdir();
        File folder3 = new File(folder2, "folder");
        folder3.mkdir();
        File folder4 = new File(folder3, "folder");
        folder4.mkdir();
        File folder5 = new File(folder4, "folder");
        folder5.mkdir();
        File folder6 = new File(folder5, "folder");
        folder6.mkdir();
        File folder7 = new File(folder6, "folder");
        folder7.mkdir();
        File folder8 = new File(folder7, "folder");
        folder8.mkdir();
        File folder9 = new File(folder8, "folder");
        folder9.mkdir();
        File folder10 = new File(folder9, "folder");
        folder10.mkdir();
        File folder11 = new File(folder10, "folder");
        folder11.mkdir();
        File folder12 = new File(folder11, "folder");
        folder12.mkdir();
        File folder13 = new File(folder12, "folder");
        folder13.mkdir();
        File folder14 = new File(folder13, "folder");
        folder14.mkdir();
        File folder15 = new File(folder14, "folder");
        folder15.mkdir();
        File folder16 = new File(folder15, "folder");
        folder16.mkdir();
        File folder17 = new File(folder16, "folder");
        folder17.mkdir();
        File folder18 = new File(folder17, "folder");
        folder18.mkdir();
        File folder19 = new File(folder18, "folder");
        folder19.mkdir();
        File folder20 = new File(folder19, "folder");
        folder20.mkdir();
        File folder21 = new File(folder20, "folder");
        folder21.mkdir();
        File folder22 = new File(folder21, "folder");
        folder22.mkdir();
        File folder23 = new File(folder22, "folder");
        folder23.mkdir();
        File folder24 = new File(folder23, "folder");
        folder24.mkdir();
        File folder25 = new File(folder24, "folder");
        folder25.mkdir();

        // Set up TruffulaOptions with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(folder1, false, true);

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

        String reset = "\033[0m";
        String white = "\033[0;37m";
        String purple = "\033[0;35m";
        String yellow = "\033[0;33m";

        StringBuilder expected = new StringBuilder();

        expected.append(white).append("folder/").append(nl).append(reset);
        expected.append(purple).append("   folder/").append(nl).append(reset);
        expected.append(yellow).append("      folder/").append(nl).append(reset);
        expected.append(white).append("         folder/").append(nl).append(reset);
        expected.append(purple).append("            folder/").append(nl).append(reset);
        expected.append(yellow).append("               folder/").append(nl).append(reset);
        expected.append(white).append("                  folder/").append(nl).append(reset);
        expected.append(purple).append("                     folder/").append(nl).append(reset);
        expected.append(yellow).append("                        folder/").append(nl).append(reset);
        expected.append(white).append("                           folder/").append(nl).append(reset);
        expected.append(purple).append("                              folder/").append(nl).append(reset);
        expected.append(yellow).append("                                 folder/").append(nl).append(reset);
        expected.append(white).append("                                    folder/").append(nl).append(reset);
        expected.append(purple).append("                                       folder/").append(nl).append(reset);
        expected.append(yellow).append("                                          folder/").append(nl).append(reset);
        expected.append(white).append("                                             folder/").append(nl).append(reset);
        expected.append(purple).append("                                                folder/").append(nl).append(reset);
        expected.append(yellow).append("                                                   folder/").append(nl).append(reset);
        expected.append(white).append("                                                      folder/").append(nl).append(reset);
        expected.append(purple).append("                                                         folder/").append(nl).append(reset);
        expected.append(yellow).append("                                                            folder/").append(nl).append(reset);
        expected.append(white).append("                                                               folder/").append(nl).append(reset);
        expected.append(purple).append("                                                                  folder/").append(nl).append(reset);
        expected.append(yellow).append("                                                                     folder/").append(nl).append(reset);
        expected.append(white).append("                                                                        folder/").append(nl).append(reset);
                  
        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }

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
        createHiddenFile(myFolder, ".hidden.txt");

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
        ConsoleColor reset = ConsoleColor.RESET;
        ConsoleColor white = ConsoleColor.WHITE;
        ConsoleColor purple = ConsoleColor.PURPLE;
        ConsoleColor yellow = ConsoleColor.YELLOW;

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
}
