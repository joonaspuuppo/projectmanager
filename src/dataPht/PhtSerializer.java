package dataPht;

import java.util.Arrays;

/**
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 0.6 Apr 1, 2021
 * Serialize Tasks into String format.
 */
public class PhtSerializer {
    
    private static final String SEPARATOR = "|";
    private static final String SEPARATOR_RE = "\\|";
    private static final String NEWLINE_ESCAPE = "<<newline>>";
    private static final String SEPARATOR_ESCAPE = "<<separator>>";
    private static final String DEFAULT = "<<default>>";
    
    
    private static class SerializationException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        public SerializationException(String info) {
            super(info);
        }
    }
    
    /**
     * Serialize a task to a String.
     * 
     * FORMAT (without spaces before & after separators for clarity):
     * 
     * ID | NAME | PRIORITY | DONE | INFO
     * 
     * Newlines are escaped in the INFO-section.
     * Separators are escaped in NAME & INFO.
     * 
     * @param t Task
     * @return a String representig a task
     */
    public static String parseString(Task t) {
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toString(t.getId()));
        sb.append(SEPARATOR);
        sb.append(parseTaskNameOrInfo(t.getName()));
        sb.append(SEPARATOR);
        sb.append(priorityToString(t.getPriority()));
        sb.append(SEPARATOR);
        sb.append(t.isDone());
        sb.append(SEPARATOR);
        sb.append(parseTaskNameOrInfo(t.getInfo()));
        return sb.toString();
    }
    
    
    /**
     * Deserialize a Task from a string.
     * @param line String representation of a Task
     *        as produced by .parseString().
     *        
     * @return Task instance
     */
    public static Task parseTask(String line) {
        final int I_ID          = 0;
        final int I_NAME        = 1;
        final int I_PRIORITY    = 2;
        final int I_DONE        = 3;
        final int I_INFO        = 4;
        
        final int COLUMNS       = 5;
        
        String[] parts = line.split(SEPARATOR_RE);
        if (parts.length != COLUMNS) {
            String info = "Failed to serialize Task";
            throw new SerializationException(info); 
        }
        
        String id       = parts[I_ID];
        String name     = parts[I_NAME];
        String priority = parts[I_PRIORITY];
        String done     = parts[I_DONE];
        String info     = parts[I_INFO];
        
        Task task = new Task(parseInt(id));
        task.rename(restoreTaskNameOrInfo(name));
        task.setInfo(restoreTaskNameOrInfo(info));
        task.setPriority(stringToPriority(priority));
        if (parseBoolean(done)) task.markAsDone();
        return task;
    }
    
    
    /**
     * Serialize a RelationEntry to a String.
     * 
     * FORMAT (without spaces before & after separators for clarity):
     * 
     * TASK_ID | TAG_NAME
     * 
     * Separators are escaped in TAG_NAME-section.
     * 
     * @param entry RelationEntry
     * @return a String representig a RelationEntry
     */
    public static String parseString(RelationEntry entry) {
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toString(entry.getTaskId()));
        sb.append(SEPARATOR);
        sb.append(escapeSeparators(entry.getTagName()));
        return sb.toString();
    }
    
    
    /**
     * Deserialize a Task from a string.
     * @param line String representation of a RelationEntry
     *        as produced by .parseString().
     * @return RelationEntry
     */
    public static RelationEntry parseRelationEntry(String line) {
        final int I_TASK_ID     = 0;
        final int I_TAG_NAME    = 1;
        
        final int COLUMNS       = 2;
        
        String[] parts = line.split(SEPARATOR_RE);
        if (parts.length != COLUMNS) {
            String info = "Failed to serialize RelationEntry";
            throw new SerializationException(info); 
        }
        
        String taskId   = parts[I_TASK_ID];
        String tagName  = parts[I_TAG_NAME];
        
        RelationEntry entry = new RelationEntry(parseInt(taskId), restoreSeparators(tagName));
        return entry;
    }
    
    
    private static String restoreNewlines(String str) {
        return str.replace(NEWLINE_ESCAPE, "\n");
    }


    private static String escapeNewlines(String str) {
        return str.replace("\n", NEWLINE_ESCAPE);
    }
    
    
    private static String restoreSeparators(String str) {
        return str.replace(SEPARATOR_ESCAPE, SEPARATOR);
    }
    
    
    private static String escapeSeparators(String str) {
        return str.replace(SEPARATOR, SEPARATOR_ESCAPE);
    }
    
    
    private static String priorityToString(Priority p) {
        switch (p) {
            case HIGH:
                return "3";
            case MEDIUM:
                return "2";
            case LOW:
                return "1";
            default:
                return "1";
        }
    }
    
    
    private static Priority stringToPriority(String str) {
        switch (str) {
            case "3":
                return Priority.HIGH;
            case "2":
                return Priority.MEDIUM;
            case "1":
                return Priority.LOW;
            default:
                return Priority.LOW;
        }
    }
    
    private static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            String info = "Failed to serialize Task ID";
            throw new SerializationException(info);
        }
    }
    
    
    private static boolean parseBoolean(String str) {
            return Boolean.parseBoolean(str);
    }
    
    
    private static String parseTaskNameOrInfo(String str) {
        if (str == null || str.equals("")) {
            return DEFAULT;
        }
        String parsedName = escapeNewlines(str);
        parsedName = escapeSeparators(parsedName);
        return parsedName;
    }
    
    
    private static String restoreTaskNameOrInfo(String str) {
        if (str.equals(DEFAULT)) return "";
        String restored = restoreNewlines(str);
        restored = restoreSeparators(restored);
        return restored;
    }
    
}
