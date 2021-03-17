package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import dataPht.PhtSerializer;
import dataPht.Priority;
import dataPht.Task;
import dataPht.RelationEntry;


public class SerializerTests {
    
    /**
     * Test task serialization
     */
    @Test
    public void testTaskStringParsing() {
        Task t = new Task(1);
        t.rename("task1");
        t.setPriority(Priority.HIGH);
        t.setInfo("some task to be done");
        t.markAsDone();
        
        String expectedResult = "1|task1|3|true|some task to be done";
        assertEquals(expectedResult, PhtSerializer.parseString(t));
        
        t.rename("do something");
        t.setPriority(Priority.LOW);
        t.markAsIncomplete();
        expectedResult = "1|do something|1|false|some task to be done";
        assertEquals(expectedResult, PhtSerializer.parseString(t));
        
        t.setInfo("This is a String\nwith multiple lines");
        expectedResult = "1|do something|1|false|This is a String<<newline>>with multiple lines";
        assertEquals(expectedResult, PhtSerializer.parseString(t));
        
        t.rename("some | task");
        t.setInfo("This is | a String | with multiple | separators");
        expectedResult = "1|some <<separator>> task|1|false"
                + "|This is <<separator>> a String <<separator>> with multiple <<separator>> separators";
        assertEquals(expectedResult, PhtSerializer.parseString(t));
        
        t = new Task(2);
        expectedResult = "2|Uusi tehtävä 2|2|false|<<default>>";
        assertEquals(expectedResult, PhtSerializer.parseString(t));
        
    }
    
    
    /**
     * Test task deserialization
     */
    @Test
    public void testTaskParsing() {
        String str = "1|do something|1|false|some task to be done";
        Task t = PhtSerializer.parseTask(str);
        assertEquals(t.getId(), 1);
        assertEquals(t.getName(), "do something");
        assertEquals(t.getPriority(), Priority.LOW);
        assertEquals(t.isDone(), false);
        assertEquals(t.getInfo(), "some task to be done");
        
        str = "1|task1|2|true|This is a String<<newline>>with multiple lines";
        t = PhtSerializer.parseTask(str);
        assertEquals(t.getId(), 1);
        assertEquals(t.getName(), "task1");
        assertEquals(t.getPriority(), Priority.MEDIUM);
        assertEquals(t.isDone(), true);
        assertEquals(t.getInfo(), "This is a String\nwith multiple lines");
        
        str = "2|<<default>>|2|false|<<default>>";
        t = PhtSerializer.parseTask(str);
        assertEquals("", t.getName());
        assertEquals("", t.getInfo());
    }
    
    
    @Test
    public void testEntryParsing() {
        RelationEntry e = new RelationEntry(1, "tag");
        assertEquals("1|tag", PhtSerializer.parseString(e));
        
        e = new RelationEntry(1023123123, "some tag with spaces");
        assertEquals("1023123123|some tag with spaces", PhtSerializer.parseString(e));
        
        e = new RelationEntry(1, "tag | with separator");
        assertEquals("1|tag <<separator>> with separator", PhtSerializer.parseString(e));
    }
}
