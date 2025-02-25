package duke.commands;

import duke.Storage;
import duke.TaskList;

/**
 * When executed, it unmarks the specified task
 */
public class UnmarkTaskCommand extends Command {
    public static final String UNMARK_COMMAND = "unmark";
    protected TaskList taskList;
    protected String[] commandDescription;

    public UnmarkTaskCommand(String[] commandDescription, TaskList taskList) {
        this.commandDescription = commandDescription;
        this.taskList = taskList;
        taskList.doUnmarkTask(commandDescription);
        Storage.updateTaskInDataFile(taskList, "edit");
    }
}
