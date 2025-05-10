import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents configuration options for controlling how a directory tree is
 * displayed.
 * 
 * Options include:
 * - Whether to show hidden files.
 * - Whether to use colored output.
 * - The root directory from which to begin printing the tree.
 * 
 * Hidden files are identified by names that start with a dot (e.g.,
 * ".hidden.txt").
 * Color output is enabled by default, but can be disabled using flags.
 * 
 * Usage Example:
 * 
 * Arguments Format: [-h] [-nc] path
 * 
 * Flags:
 * - -h : Show hidden files (defaults to false).
 * - -nc : Do not use color (color is enabled by default).
 * 
 * Path:
 * - The absolute or relative path to the directory whose contents will be
 * printed.
 * 
 * Behavior:
 * - If color is disabled, all text will be printed in white.
 * - The order of flags is unimportant.
 * - The path argument is mandatory.
 * 
 * Examples:
 * 
 * 1. ['-nc', '-h', '/path/to/directory']
 * → Don't use color, do show hidden files.
 * 
 * 2. ['-h', '-nc', '/path/to/directory']
 * → Don't use color, do show hidden files (order of flags is ignored).
 * 
 * 3. ['/path/to/directory']
 * → Use color, don't show hidden files.
 * 
 * Exceptions:
 * - Throws IllegalArgumentException if:
 * - Unknown flags are provided.
 * - The path argument is missing.
 * 
 * - Throws FileNotFoundException if:
 * - The specified directory does not exist.
 * - The path points to a file instead of a directory.
 */
public class TruffulaOptions {
  private final File root;
  private final boolean showHidden;
  private final boolean useColor;

  /**
   * Returns the root directory from which the directory tree will be printed.
   *
   * @return the root directory as a File object
   */
  public File getRoot() {
    return root;
  }

  /**
   * Indicates whether hidden files should be included when printing the directory
   * tree.
   *
   * @return true if hidden files should be shown; false otherwise
   */
  public boolean isShowHidden() {
    return showHidden;
  }

  /**
   * Indicates whether color should be used when printing the directory tree.
   * 
   * If false, all output is printed in white.
   *
   * @return true if color should be used; false otherwise
   */
  public boolean isUseColor() {
    return useColor;
  }

  @Override
  public String toString() {
    return "TruffulaOptions [root=" + root + ", showHidden=" + showHidden + ", useColor=" + useColor + "]";
  }

  /**
   * Constructs a TruffulaOptions object based on command-line arguments.
   * 
   * Supported Flags:
   * - -h : Show hidden files (defaults to false).
   * - -nc : Do not use color (uses color by default).
   * 
   * The last argument must be the path to the directory.
   * 
   * @param args command-line arguments in the format [-h] [-nc] path
   * @throws IllegalArgumentException if unknown arguments are provided or the
   *                                  path is missing
   * @throws IOException
   */

  public TruffulaOptions(String[] args) throws IllegalArgumentException, FileNotFoundException {
    if (args == null || args.length == 0) {
      throw new IllegalArgumentException("No arguments provided. Expected format: [-h] [-nc] path");
    }

    boolean showHiddenFlag = false;
    boolean useColorFlag = true;
    String directoryPath = null;

    // Process arguments
    for (String arg : args) {
      if ("-h".equals(arg)) {
        showHiddenFlag = true;
      } else if ("-nc".equals(arg)) {
        useColorFlag = false;
      } else {
        directoryPath = arg; // The last argument should be the directory path
      }
    }

    if (directoryPath == null) {
      throw new IllegalArgumentException("Missing required directory path argument.");
    }

    File directory = new File(directoryPath);
    if (!directory.exists()) {
      throw new FileNotFoundException("Directory does not exist: " + directoryPath);
    }
    if (!directory.isDirectory()) {
      throw new FileNotFoundException("Path is not a directory: " + directoryPath);
    }

    //ensure root is set to expected parent directory if needed
    // File parent = directory.getParentFile();
    // if (parent != null && parent.exists()) {
    //   this.root = parent; // Move up one level if necessary
    // } else {
    //   this.root = directory; // Otherwise, keep as-is
    // }

    this.root = directory;
    this.showHidden = showHiddenFlag;
    this.useColor = useColorFlag;
  }

  /**
   * Constructs a TruffulaOptions object with explicit values.
   * 
   * @param root       the root directory for the directory tree
   * @param showHidden whether hidden files should be displayed
   * @param useColor   whether color should be used in the output
   */
  public TruffulaOptions(File root, boolean showHidden, boolean useColor) {
    this.root = root;
    this.showHidden = showHidden;
    this.useColor = useColor;
  }
}
