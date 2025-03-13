import java.io.File;
import java.io.PrintStream;
import java.util.List;

/**
 * TruffulaPrinter is responsible for printing a directory tree structure
 * with optional colored output. It supports sorting files and directories
 * in a case-insensitive manner and cycling through colors for visual clarity.
 */
public class TruffulaPrinter {

  /**
   * Configuration options that determine how the tree is printed.
   */
  private TruffulaOptions options;

  /**
   * The sequence of colors to use when printing the tree.
   */
  private List<ConsoleColor> colorSequence;

  /**
   * The output printer for displaying the tree.
   */
  private ColorPrinter out;

  /**
   * Default color sequence used when no custom colors are provided.
   */
  private static final List<ConsoleColor> DEFAULT_COLOR_SEQUENCE = List.of(
      ConsoleColor.WHITE, ConsoleColor.PURPLE, ConsoleColor.YELLOW);

  /**
   * Constructs a TruffulaPrinter with the given options, using the default
   * output stream and the default color sequence.
   *
   * @param options the configuration options for printing the tree
   */
  public TruffulaPrinter(TruffulaOptions options) {
    this(options, System.out, DEFAULT_COLOR_SEQUENCE);
  }

  /**
   * Constructs a TruffulaPrinter with the given options and color sequence,
   * using the default output stream.
   *
   * @param options       the configuration options for printing the tree
   * @param colorSequence the sequence of colors to use when printing
   */
  public TruffulaPrinter(TruffulaOptions options, List<ConsoleColor> colorSequence) {
    this(options, System.out, colorSequence);
  }

  /**
   * Constructs a TruffulaPrinter with the given options and output stream,
   * using the default color sequence.
   *
   * @param options   the configuration options for printing the tree
   * @param outStream the output stream to print to
   */
  public TruffulaPrinter(TruffulaOptions options, PrintStream outStream) {
    this(options, outStream, DEFAULT_COLOR_SEQUENCE);
  }

  /**
   * Constructs a TruffulaPrinter with the given options, output stream, and color
   * sequence.
   *
   * @param options       the configuration options for printing the tree
   * @param outStream     the output stream to print to
   * @param colorSequence the sequence of colors to use when printing
   */
  public TruffulaPrinter(TruffulaOptions options, PrintStream outStream, List<ConsoleColor> colorSequence) {
    this.options = options;
    this.colorSequence = colorSequence;
    out = new ColorPrinter(outStream);
  }

  /**
   * WAVE 4: Prints a tree representing the directory structure, with directories
   * and files
   * sorted in a case-insensitive manner. The tree is displayed with 3 spaces of
   * indentation for each directory level.
   * 
   * WAVE 5: If hidden files are not to be shown, then no hidden files/folders
   * will be shown.
   *
   * WAVE 6: If color is enabled, the output cycles through colors at each
   * directory level
   * to visually differentiate them. If color is disabled, all output is displayed
   * in white.
   *
   * WAVE 7: The sorting is case-insensitive. If two files have identical
   * case-insensitive names,
   * they are sorted lexicographically (Cat.png before cat.png).
   *
   * Example Output:
   *
   * myFolder/
   * Apple.txt
   * banana.txt
   * Documents/
   * images/
   * Cat.png
   * cat.png
   * Dog.png
   * notes.txt
   * README.md
   * zebra.txt
   */

  public void printTree() {
    // TODO: Implement this!
    // REQUIRED: ONLY use java.io, DO NOT use java.nio

    // Hints:
    // - Add a recursive helper method
    // - For Wave 6: Use AlphabeticalFileSorter
    // DO NOT USE SYSTEM.OUT.PRINTLN
    // USE out.println instead (will use your ColorPrinter)

    // get the root directory

    File root = options.getRoot();

    if (root == null) {
      out.println("Root directory is null");
    }

    if (!root.exists() || !root.isDirectory()) {
      out.println("Invalid Root");
    }

    // print root name and set color to white
    out.setCurrentColor(ConsoleColor.WHITE);
    out.println(root.getName() + "/");

    // then start at depth 1
    printTreeHelper(root, 1);

  }

  public void printTreeHelper(File directory, int depth) {
    // if the directory doesn't exist or is null, do nothing
    if (directory == null || !directory.exists()) {
      return;
    }

    // Indentation based on the depth
    String indent = "   ".repeat(depth);

    // gets list of files and sorts them using AlphabeticalSorter.sort
    File[] files = directory.listFiles();
    if (files == null) {
      return;
    }

    files = AlphabeticalFileSorter.sort(files);

    // Loop through the files in the directory
    for (File file : files) {
      // Skip hidden files if the option is set to not show hidden files
      if (!options.isShowHidden() && file.isHidden()) {
        continue;
      }

      // gets name of file and append "/" for directories
      String fileName = file.getName();
      if (file.isDirectory()) {
        fileName += "/";
      }

      // use modulo to determine depth 
      int colorIndex = depth % colorSequence.size();
      out.setCurrentColor(DEFAULT_COLOR_SEQUENCE.get(colorIndex));

      // Print the file/directory name with the appropriate indentation
      out.println(indent + fileName);

      // if is a directory, recursively print its contents
      if (file.isDirectory()) {
        printTreeHelper(file, depth + 1);
      }
    }
  }

}