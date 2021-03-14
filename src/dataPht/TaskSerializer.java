package dataPht;


/**
 * @author varajala
 * @version Mar 14, 2021
 * Serialize Tasks into String format.
 */
public class TaskSerializer {
    
    private static final String SEPARATOR = "|";
    private static final String SEPARATOR_RE = "\\|";
    private static final String NEWLINE_ESCAPE = "<<newline>>";
    
    private static final int I_ID          = 0;
    private static final int I_NAME        = 1;
    private static final int I_PRIORITY    = 2;
    private static final int I_DONE        = 3;
    private static final int I_INFO        = 4;
    
    
    private static class SerializationException extends RuntimeException {
        
        private static final long serialVersionUID = 1L;
        
        public SerializationException(String info) {
            super(info);
        }
    }
    
    /**
     * Serialize a task to a String.
     * 
     * @param t Task
     * @return a String representig a task
     */
    public static String parseString(Task t) {
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toString(t.getId()));
        sb.append(SEPARATOR);
        sb.append(t.getName());
        sb.append(SEPARATOR);
        sb.append(priorityToString(t.getPriority()));
        sb.append(SEPARATOR);
        sb.append(t.isDone());
        sb.append(SEPARATOR);
        sb.append(escapeNewlines(t.getInfo()));
        return sb.toString();
    }
    
    
    public static Task parseTask(String line) {
        String[] parts = line.split(SEPARATOR_RE);
        String id       = parts[I_ID];
        String name     = parts[I_NAME];
        String priority = parts[I_PRIORITY];
        String done     = parts[I_DONE];
        String info     = parts[I_INFO];
        Task task = new Task(parseInt(id));
        task.rename(name);
        task.setInfo(restoreNewlines(info));
        task.setPriority(stringToPriority(priority));
        if (parseBoolean(done)) task.markAsDone();
        return task;
    }
    
    
    private static String restoreNewlines(String str) {
        return str.replace(NEWLINE_ESCAPE, "\\n");
    }


    private static String escapeNewlines(String str) {
        return str.replace("\\n", NEWLINE_ESCAPE);
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
    
}
