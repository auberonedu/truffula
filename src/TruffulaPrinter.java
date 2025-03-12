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
    /*
truffula/
   lib/
   src/
      folder1/
         file1.java
         file2.java
         file3.java
      alphabeicalFileSorter.java
      App.java
      ColorPrinter.java
      ...
    .gitignore
    instructions.md
    LISENCE
    README.md

    */

  public void printTree() {
  //   // TODO: Implement this!
  //   // REQUIRED: ONLY use java.io, DO NOT use java.nio
    
  //   // Hints:
  //   // - Add a recursive helper method
  //   // - For Wave 6: Use AlphabeticalFileSorter
  //   // DO NOT USE SYSTEM.OUT.PRINTLN
  //   // USE out.println instead (will use your ColorPrinter)
    // out.println("printTree was called!");
    // out.println("My options are: " + options);

    /*
    PRINT TREE METHOD
      get the root directory  
      then call recursively to the printTreeMethodHelper
     */

    File root = options.getRoot();
    // We had issue here where it duplicates the name of the root directory. This prints root once.
    out.println(coloredText(root.getName() + "/", 0)); 
    printTreeMethodHelper(root,1);
  }

  public void printTreeMethodHelper(File currentDirectory, int indentLevel) {
  /*
  WAVE 4: PSUEDO CODE AND IMPLEMENTATION

    In this directory we're going to pass in the directory and the indentLevel
    Consider the base case
      if directory is null or if the directory isn't a directory or doesn't exists
      simply return 
    Logic
      Create concactnation of indent sn string based on the level
        something like, indent = "   " repeated indentLevel times
  
      then print the directory with the proper indentation => directory + "/"
      
      after that get the list of files and directories

      loop
        
      loop 

      print the file name with the proper intentations
    */
    
    if (currentDirectory == null || !currentDirectory.exists()) return;

    // To list the files and directories in the current directory
    File[] files = AlphabeticalFileSorter.sort(currentDirectory.listFiles());
    if (files == null) return;

    for (File file : files) {
    // if (file.isHidden() && !options.isShowHidden()) {
    //   continue;
    // }

    // This prints the root once
    out.println(coloredText(file.getName() + (file.isDirectory() ? "/" : ""), indentLevel));
    if (file.isDirectory()) {
      printTreeMethodHelper(file, indentLevel + 1); // Recursive call for directories
    }
  }
}

  public static String printIndentedSpaces(String name, int indentLevel){
    StringBuilder indent = new StringBuilder();
    for (int i = 0; i < indentLevel; i++) {
      indent.append("   "); 
    }
    return indent.toString() + name;
  }

  public String coloredText(String name, int indentLevel) {
    String indent = printIndentedSpaces(name, indentLevel);

    //if color is disabled use white 
    if (!options.isUseColor()) {
      out.setCurrentColor(ConsoleColor.WHITE);
      return indent;  // Return with white text 
    }

    // Choose color based on indentation level
    ConsoleColor color;
    switch (indentLevel % 3) {
      case 0:
        color = ConsoleColor.WHITE;
        break;
      case 1:
        color = ConsoleColor.PURPLE;
        break;
      default:
        color = ConsoleColor.YELLOW;
      }
      out.setCurrentColor(color);
      return indent;
  }
} 