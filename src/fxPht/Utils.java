package fxPht;

import java.util.List;

import dataPht.Priority;
import dataPht.Tag;
import dataPht.Task;
import javafx.scene.paint.Color;


/**
 * Utility functions for UI.
 * @author Joonas Puuppo, Valtteri Rajalainen
 * @version 1.0 Apr 14, 2021
 * valtteri.a.rajalainen@student.jyu.fi
 * joonas.a.j.puuppo@student.jyu.fi
 */
public class Utils {

    /**
     * Gets the string representation of a Task's priority setting.
     * Used to setting taskPriorityLabel text.
     * @param task Task
     * @return priority as string or empty string if medium priority is selected.
     */
    public static  String taskPriorityToString(Task task) {
        String result = "";
        if (task.getPriority() == Priority.LOW) result =  "(kiireetön)";
        if (task.getPriority() == Priority.HIGH) result = "(kiireellinen)";
        return result;
    }

    /**
     * Gets a Task's tags as a String where a #-character precedes every tagName.
     * @param tagList List of all tags
     * @return tags as hashtags or an empty string if task has no tags.
     */
    public static String formatTags(List<Tag> tagList) {
        if (tagList.isEmpty()) return "";
        StringBuffer sb = new StringBuffer();
        for (Tag tag : tagList) {
            sb.append("#");
            sb.append(tag.getName().trim());
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Gets text fill color based on whether task is done or not.
     * @param task Task
     * @return Color for text in UI based on if Task is done or not.
     */
    public static Color getTextFillColor(Task task) {
        Color result = Color.BLACK;
        if (task.isDone()) {
            result = Color.GREY;
        }
        return result;
    }

    /**
     * Gets string for the "mark as done" button based on whether task is done or not.
     * @param task Task
     * @return A string that can be displayed in 'mark as done' - button.
     */
    public static String getDoneIndicatorString(Task task) {
        String str = "Merkitse valmiiksi";
        if (task.isDone()) {
            str = "Merkitse keskeneräiseksi";
        }
        return str;
    }

}