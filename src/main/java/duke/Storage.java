package duke;

import duke.exceptions.InvalidCommandFormatException;
import duke.tasks.Deadline;
import duke.tasks.Event;
import duke.tasks.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the storage of task list into the data file and
 * the read/write operations between data file and taskList.
 */
public class Storage {
    private static final String DATA_FILE_PATH = "data/data.txt";

    /**
     * Creates the data file if it does not exist.
     */
    public static void createDataFile() {
        try {
            File file = new File(DATA_FILE_PATH);
            if (!file.getParentFile().mkdirs()) {
                Ui.printCreateParentFolderErrorText();
            }
            if (file.createNewFile()) {
                Ui.printFilePath(true, DATA_FILE_PATH);
            } else {
                Ui.printFilePath(false, DATA_FILE_PATH);
            }
        } catch (IOException ioException) {
            Ui.printCreateFileErrorText(DATA_FILE_PATH);
        }
    }

    /**
     * Loads the tasks from the data file to taskList
     * @param taskList taskList that contains tasks stored in data file
     */
    public static void loadTasksToTasksList(TaskList taskList) {
        try {
            File dataFile = new File(DATA_FILE_PATH);
            Scanner s = new Scanner(dataFile);
            int taskNumber = -1;
            while (s.hasNext()) {
                String[] fileWords = s.nextLine().split("\\| ");
                taskNumber += 1;
                switch (fileWords[0].charAt(0)) {
                case 'T':
                    Todo todoTask = new Todo(fileWords[2], 'T');
                    taskList.addToTasksList(todoTask);
                    String isMarked = fileWords[1].replaceAll("\\s+","");
                    if (isMarked.equals("1")) {
                        taskList.setTaskDoneStatus(taskNumber, true);
                    }
                    break;
                case 'D':
                    Deadline deadlineTask = new Deadline(fileWords[2], 'D', fileWords[3]);
                    taskList.addToTasksList(deadlineTask);
                    isMarked = fileWords[1].replaceAll("\\s+","");
                    if (isMarked.equals("1")) {
                        taskList.setTaskDoneStatus(taskNumber, true);
                    }
                    break;
                case 'E':
                    Event eventTask = new Event(fileWords[2], 'E', fileWords[3]);
                    taskList.addToTasksList(eventTask);
                    isMarked = fileWords[1].replaceAll("\\s+","");
                    if (isMarked.equals("1")) {
                        taskList.setTaskDoneStatus(taskNumber, true);
                    }
                    break;
                default:
                    Ui.printReadFileErrorText();
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found :< Bob is creating your data file now...");
            createDataFile();
        }
    }

    /**
     * Loads the tasks from the taskList to data file
     * @param taskList taskList that contains tasks to be loaded into data file
     */
    public static void loadTasktoDataFile(TaskList taskList) {
        try {
            FileWriter fw = new FileWriter(DATA_FILE_PATH, true);
            int taskNumber = taskList.getTaskNumberOfInterest();
            fw.write(taskList.loadTaskToDataFile(taskNumber));
            fw.close();
        } catch (IOException ioException) {
            Ui.printLoadTaskToDataFileErrorText();
        }
    }

    /**
     * Updates the task that is already in the data file
     * @param taskList taskList that contains tasks
     * @param commandType the command on how the task is to updated in the data file
     */
    public static void updateTaskInDataFile(TaskList taskList, String commandType) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));
            int taskNumber = taskList.getTaskNumberOfInterest();
            String updatedTaskToLoadInDataFile = "";
            if (commandType.equals("edit")) {
                updatedTaskToLoadInDataFile = taskList.loadTaskToDataFile(taskNumber).replace("\n", "");
            }
            lines.set(taskNumber, updatedTaskToLoadInDataFile);
            lines.removeIf(String::isEmpty);
            Files.write(Paths.get(DATA_FILE_PATH), lines);
        } catch (IOException ioException) {
            Ui.printUpdateTaskToDataFileErrorText(ioException);
        }
    }
}
