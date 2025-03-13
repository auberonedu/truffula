import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;


public class AlphabeticalFileSorterTest {
 
    @Test //test with simple abc put in array not in order
    public void testSortFileNames() {
        
        File fileA = new File("a.txt");
        File fileB = new File("b.txt");
        File fileC = new File("c.txt");
        File fileD = new File("d.txt");
        File fileE = new File("e.txt");
        File fileF = new File("f.txt");
        File fileG = new File("g.txt");
    
        File[] files = {fileF, fileC, fileE, fileA, fileD, fileB, fileG};

        File[] sortedFiles = AlphabeticalFileSorter.sort(files);
        
        
        assertEquals("a.txt", sortedFiles[0].getName());
        assertEquals("b.txt", sortedFiles[1].getName());
        assertEquals("c.txt", sortedFiles[2].getName());
        assertEquals("d.txt", sortedFiles[3].getName());
        assertEquals("e.txt", sortedFiles[4].getName());
        assertEquals("f.txt", sortedFiles[5].getName());
        assertEquals("g.txt", sortedFiles[6].getName());
    }
    @Test // test file name with 2 letters put in array in order but files were created in non alpha order
    public void testSortFileNamesMultiplesameletter() {
        
        File file1 = new File("bb.txt");
        File file2 = new File("bc.txt");
        File file3 = new File("ba.txt");
        File file4 = new File("ab.txt");
        File file5 = new File("cb.txt");
        File file6 = new File("aa.txt");
        File file7 = new File("ca.txt");
        
        
        File[] files = {file1, file2, file3, file4, file5, file6, file7};
        
        
        File[] sortedFiles = AlphabeticalFileSorter.sort(files);
        
        
        assertEquals("aa.txt", sortedFiles[0].getName());
        assertEquals("ab.txt", sortedFiles[1].getName());
        assertEquals("ba.txt", sortedFiles[2].getName());
        assertEquals("bb.txt", sortedFiles[3].getName());
        assertEquals("bc.txt", sortedFiles[4].getName());
        assertEquals("ca.txt", sortedFiles[5].getName());
        assertEquals("cb.txt", sortedFiles[6].getName());
    }
    @Test //if capitolization is not working taco would be first due to Upper case being a lower ascii number
    public void testCapitolization() {

    
        
        File file1 = new File("Tacos.txt");
        File file2 = new File("burrito.txt");
        
        
        File[] files = {file1, file2};
        
        
        File[] sortedFiles = AlphabeticalFileSorter.sort(files);
        
        
        assertEquals("burrito.txt", sortedFiles[0].getName());
        assertEquals("Tacos.txt", sortedFiles[1].getName());
    }
}
