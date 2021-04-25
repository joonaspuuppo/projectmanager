package dataPht;


/**
 * Serializes Tasks into String format.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 14, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
 */
public class PhtSerializer {
    
    private static final String SEPARATOR = "|";
    private static final String SEPARATOR_RE = "\\|";
    private static final String NEWLINE_ESCAPE = "<<newline>>";
    private static final String SEPARATOR_ESCAPE = "<<separator>>";
    private static final String DEFAULT = "<<default>>";
    
    
    /**
     * Exception thrown if the serialization fails.
     */
    public static class SerializationException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        
        
        /**
         * Constructor for SerializationException. 
         */
        public SerializationException() {
            super();
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
     * @return a String representing a task
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
            throw new SerializationException(); 
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
     * Constructs a task from an array.
     * Used when reading project data from SQL database.
     * @param taskAsArray String array of task data
     * @return task
     */
    public static Task parseTask(String[] taskAsArray) {
        Task t = new Task(parseInt(taskAsArray[0]));
        t.rename(taskAsArray[1]);
        t.setInfo(taskAsArray[2]);
        if (parseBoolean(taskAsArray[3])) t.markAsDone();
        t.setPriority(stringToPriority(taskAsArray[4]));
        
        
        
        return t;
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
            throw new SerializationException(); 
        }
        
        String taskId   = parts[I_TASK_ID];
        String tagName  = parts[I_TAG_NAME];
        
        RelationEntry entry = new RelationEntry(parseInt(taskId), restoreSeparators(tagName));
        return entry;
    }
    
    
    /**
     * Restores line breaks.
     * Used when reading task data from file.
     * @param str string
     * @return string with line breaks.
     */
    private static String restoreNewlines(String str) {
        return str.replace(NEWLINE_ESCAPE, "\n");
    }


    /**
     * Removes line breaks from string.
     * Used when writing task data to file.
     * @param str string
     * @return string without line breaks.
     */
    private static String escapeNewlines(String str) {
        return str.replace("\n", NEWLINE_ESCAPE);
    }
    
    
    /**
     * Restores separator characters to string
     * @param str string
     * @return string with separators
     */
    private static String restoreSeparators(String str) {
        return str.replace(SEPARATOR_ESCAPE, SEPARATOR);
    }
    
    
    /**
     * Escapes separators in string
     * @param str string
     * @return string with separators escaped
     */
    private static String escapeSeparators(String str) {
        return str.replace(SEPARATOR, SEPARATOR_ESCAPE);
    }
    
    
    /**
     * Gets string representation of priority settings
     * @param p priority
     * @return string representation of priority setting
     */
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
    
    
    /**
     * Gets priority setting from string.
     * @param str string
     * @return priority setting
     */
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
    
    /**
     * Parses integer from string.
     * @param str string
     * @return integer parsed from string
     * @throws SerializationException if parsing fails
     */
    private static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new SerializationException();
        }
    }
    
    
    /**
     * Parses boolean from string.
     * @param str string
     * @return boolean value
     */
    private static boolean parseBoolean(String str) {
            return Boolean.parseBoolean(str);
    }
    
    
    /**
     * Used for parsing task name or info from string.
     * @param str string
     * @return task name or info as string with escaped line breaks and separators.
     */
    private static String parseTaskNameOrInfo(String str) {
        if (str == null || str.equals("")) {
            return DEFAULT;
        }
        String parsedName = escapeNewlines(str);
        parsedName = escapeSeparators(parsedName);
        return parsedName;
    }
    
    
    /**
     * Used for restoring line breaks and separators to task name or info.
     * @param str string
     * @return string with line breaks and separators restored
     */
    private static String restoreTaskNameOrInfo(String str) {
        if (str.equals(DEFAULT)) return "";
        String restored = restoreNewlines(str);
        restored = restoreSeparators(restored);
        return restored;
    }
    
}
