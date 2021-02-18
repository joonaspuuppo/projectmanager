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



   
}
