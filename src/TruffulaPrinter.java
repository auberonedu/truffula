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
      ConsoleColor.WHITE, ConsoleColor.PURPLE, ConsoleColor.YELLOW
  );

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
   * @param options the configuration options for printing the tree
   * @param colorSequence the sequence of colors to use when printing
   */
  public TruffulaPrinter(TruffulaOptions options, List<ConsoleColor> colorSequence) {
    this(options, System.out, colorSequence);
  }

  /**
   * Constructs a TruffulaPrinter with the given options and output stream,
   * using the default color sequence.
   *
   * @param options the configuration options for printing the tree
   * @param outStream the output stream to print to
   */
  public TruffulaPrinter(TruffulaOptions options, PrintStream outStream) {
    this(options, outStream, DEFAULT_COLOR_SEQUENCE);
  }

  /**
   * Constructs a TruffulaPrinter with the given options, output stream, and color sequence.
   *
   * @param options the configuration options for printing the tree
   * @param outStream the output stream to print to
   * @param colorSequence the sequence of colors to use when printing
   */
  public TruffulaPrinter(TruffulaOptions options, PrintStream outStream, List<ConsoleColor> colorSequence) {
    this.options = options;
    this.colorSequence = colorSequence;
    out = new ColorPrinter(outStream);
  }

  /**
   * WAVE 4: Prints a tree representing the directory structure, with directories and files
   * sorted in a case-insensitive manner. The tree is displayed with 3 spaces of
   * indentation for each directory level.
   * 
   * WAVE 5: If hidden files are not to be shown, then no hidden files/folders will be shown.
   *
   * WAVE 6: If color is enabled, the output cycles through colors at each directory level
   * to visually differentiate them. If color is disabled, all output is displayed in white.
   *
   * WAVE 7: The sorting is case-insensitive. If two files have identical case-insensitive names,
   * they are sorted lexicographically (Cat.png before cat.png).
   *
   * Example Output:
   *
   * myFolder/
   *    Apple.txt
   *    banana.txt
   *    Documents/
   *       images/
   *          Cat.png
   *          cat.png
   *          Dog.png
   *       notes.txt
   *       README.md
   *    zebra.txt
   */
  public void printTree() {
    // TODO: Implement this!
    // REQUIRED: ONLY use java.io, DO NOT use java.nio
    
    // Hints:
    // - Add a recursive helper method
    // - For Wave 6: Use AlphabeticalFileSorter
    // DO NOT USE SYSTEM.OUT.PRINTLN
    // USE out.println instead (will use your ColorPrinter)

    // out.println("printTree was called!");
    // out.println("My options are: " + options);
    out.println(options.getRoot().getName() + "/");
    printTreeHelper(options.getRoot(), 1);
  }

  // Helper method with param levelDepth for storing level depth
  // Create a file array to store the files from File dir using listFiles
  // Base Case: check if dir is null, if so return 
  // Second Base Case: Check if dir has no children, if so print only dir and return
  // For loop: Loop the children of dir using list files.
  //    Increment levelDepth by one
  //    Check if child is a directory using .isDirectory(), if so
  //    out.println("indent" * levelDepth + child.getName()) and recurse
  //    helper method with level depth.
  //    Else out.println("indent" * levelDepth + child.getName()).

  private void printTreeHelper(File dir, int levelDepth) {
    // base case: if dir null or doesn't exist, return
    if (dir == null || !dir.exists()) return;

    // indentation thats repeated based on how deep it is in folder level
    String indent = "   ".repeat(levelDepth);

    File[] children = dir.listFiles();

    // if the directory has no children, then return
    if (children == null) return;

   // Sort the children by name alphabetically by using AlphabeticalFileSorter class
    AlphabeticalFileSorter.sort(children);
 
    // loop through directory sub files/folders
    for (File child : children) {
      // check if hidden files should be displayed
      if (!options.isShowHidden() && child.isHidden()) {
        continue;
      }

      // check if colors should be used
      if (options.isUseColor()) {
        // Determine the color for a level based on modulus result
        ConsoleColor color = colorSequence.get(levelDepth % colorSequence.size());
        out.setCurrentColor(color);
      }

      // if child is a directory print appropriately and recurse 1 lvl deeper
      // else, just print subfile name with indentation
      if (child.isDirectory()) {
        out.println(indent + child.getName() + "/");
        printTreeHelper(child, levelDepth + 1);
      } else {
        out.println(indent + child.getName());
      }
    }
  }
}