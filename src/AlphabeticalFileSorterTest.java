import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;


public class AlphabeticalFileSorterTest {
 
    @Test
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
        
        // Check the sorted order by names
        assertEquals("a.txt", sortedFiles[0].getName());
        assertEquals("b.txt", sortedFiles[1].getName());
        assertEquals("c.txt", sortedFiles[2].getName());
        assertEquals("d.txt", sortedFiles[3].getName());
        assertEquals("e.txt", sortedFiles[4].getName());
        assertEquals("f.txt", sortedFiles[5].getName());
        assertEquals("g.txt", sortedFiles[6].getName());
    }
    @Test
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
    @Test
    public void testCapitolization() {

        //if capitolization is not working taco would be first due to lower case being a lower ascii number
        
        File file1 = new File("tacos.txt");
        File file2 = new File("Burrito.txt");
        
        
        File[] files = {file1, file2};
        
        
        File[] sortedFiles = AlphabeticalFileSorter.sort(files);
        
        
        assertEquals("Burrito.txt", sortedFiles[0].getName());
        assertEquals("tacos.txt", sortedFiles[1].getName());
    }
}
