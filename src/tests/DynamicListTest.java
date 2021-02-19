package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import fxPht.DynamicList;


/**
 * @author varajala
 * @version Feb 18, 2021
 */
public class DynamicListTest {
    
    private final int LARGE_NUMBER = 100000;
   
    /**
     * Testing the empty contructor with different types.
     */
    @Test 
    public void testEmptyConstructor() {
        DynamicList<Integer> intList = new DynamicList<Integer>();
        assertEquals(intList.count(), 0);
    
        DynamicList<String> strList = new DynamicList<String>();
        assertEquals(strList.count(), 0);
   }
    

    /**
     * Testing the contructor with provided array.
     */
    @Test 
    public void testArrayConstructor() {
        String[] array1 = {"1", "2", "3", "4", "5"}; 
        DynamicList<String> list1 = new DynamicList<String>(array1);
        assertEquals(list1.count(), array1.length);
    
        String[] array2 = {
            "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20",
            }; 
        DynamicList<String> list2 = new DynamicList<String>(array2);
        assertEquals(list2.count(), array2.length);
   }
    
    
    /**
     * Test toString-method.
     */
    @Test
    public void testToString() {
        String[] array1 = {"1", "2", "3", "4", "5"}; 
        DynamicList<String> list1 = new DynamicList<String>(array1);
        assertEquals(list1.toString(), Arrays.toString(array1));
    
        String[] array2 = {
            "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20",
            }; 
        DynamicList<String> list2 = new DynamicList<String>(array2);
        assertEquals(list2.toString(), Arrays.toString(array2));
    }
    
    
    /**
     * Test indexing with valid indexes.
     */
    @Test
    public void testValidIndexing() {
        String[] array1 = {"0", "1", "2", "3", "4"}; 
        DynamicList<String> list1 = new DynamicList<String>(array1);
        assertEquals(list1.get(0), "0");
        assertEquals(list1.get(1), "1");
        assertEquals(list1.get(2), "2");
        assertEquals(list1.get(3), "3");
        assertEquals(list1.get(4), "4");
        
        assertEquals(list1.get(-1), "4");
        assertEquals(list1.get(-2), "3");
        assertEquals(list1.get(-3), "2");
        assertEquals(list1.get(-4), "1");
        assertEquals(list1.get(-5), "0");
    }
    
    
    /**
     * Test indexing with indexes known to fail.
     */
    @Test
    public void testInvalidIndexing() {
        String[] array1 = {"0", "1", "2", "3", "4"}; 
        DynamicList<String> list1 = new DynamicList<String>(array1);
        int[] indexes = {-10, 6, 5, -11};
        for (int i : indexes) {
            try {
                list1.get(i);
                System.out.printf("Indexing succeeded with index: %d%n", i);
                fail();
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }
    
    
    /**
     * Test appending and expansion.
     */
    @Test
    public void testAppending() {
        DynamicList<Integer> list = new DynamicList<Integer>();
        for (int i = 0; i < LARGE_NUMBER; i++) {
            list.append(Integer.valueOf(i));
        }
        assertEquals(list.count(), LARGE_NUMBER);
        testPopping(list);
    }
    
    
    /**
     * Test popping items and shrinking the list.
     * @param list List generated by testAppending - method.
     */
    public void testPopping(DynamicList<Integer> list) {
        for (int i = LARGE_NUMBER-1; i >= 0; i--) {
            list.pop(i);
        }
        assertEquals(list.count(), 0);
    }
    
    
    /**
     * Test the resizing and replacement of the items in the list
     * after removing items. Check if there is any gaps left in the
     * internal array by printing the list. 
     */
    @Test
    public void testResizing() {
        String[] startArray = {
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30",
        };
        DynamicList<String> list = new DynamicList<String>(startArray);
        assertEquals(list.toString(), Arrays.toString(startArray));
        
        int[] indexes1 = {
                    5, 5, 5, 5, 5,
                    -2, -2, -2, -2, -2,
                    5, 5, 5, 5, 5,
                    -2, -2, -2, -2, -2,
                };
        for (int i : indexes1) {
            list.pop(i);
        }
        String[] excpectedResult1 = {
                "1", "2", "3", "4", "5",
                "16", "17", "18", "19", "30",
        };
        assertEquals(list.toString(), Arrays.toString(excpectedResult1));
        
        assertEquals(list.get(-1), "30");
        assertEquals(list.get(-2), "19");
        assertEquals(list.get(-3), "18");
        assertEquals(list.get(0), "1");
        assertEquals(list.get(2), "3");
        assertEquals(list.get(5), "16");
        assertEquals(list.get(6), "17");
        assertEquals(list.get(7), "18");
        
        int[] indexes2 = {
                5, 5, 5, 5,
                -2, -2, -2, -2
            };
        for (int i : indexes2) {
            list.pop(i);
        }
        String[] excpectedResult2 = {"1", "30"};
        assertEquals(list.toString(), Arrays.toString(excpectedResult2));
        
        assertEquals(list.get(-1), "30");
        assertEquals(list.get(-2), "1");
        assertEquals(list.get(0), "1");
        assertEquals(list.get(1), "30");
    }
}
