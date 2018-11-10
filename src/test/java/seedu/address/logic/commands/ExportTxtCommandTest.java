package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.storage.XmlAddressBookStorage;
import seedu.address.testutil.TypicalPersons;

/**
 * The test for importTxtCommand integrates with model and storage.
 */
public class ExportTxtCommandTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private String dirPath;
    private Storage storage;
    private Model model;
    private CommandHistory commandHistory;

    @Before
    public void setUp() {
        dirPath = testFolder.getRoot().getPath() + File.separator;;
        AddressBookStorage addressBookStorage =
                new XmlAddressBookStorage(Paths.get(dirPath + "addressbook.xml"));
        UserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(Paths.get(dirPath + "preference.json"));
        storage = new StorageManager(addressBookStorage, userPrefsStorage);
        model = new ModelManager(TypicalPersons.getTypicalPersonsAddressBook(), new UserPrefs());
        commandHistory = new CommandHistory();
    }

    @Test
    public void execute_validFilePath_success() {
        String filePath = dirPath + "validExport.txt";
        ExportTxtCommand exportTxtCommand =
                new ExportTxtCommand(ParserUtil.parseFilePath(filePath), ExportCommand.FileType.TXT);
        exportTxtCommand.setStorage(storage);

        String expectedMessage = String.format(ExportCommand.MESSAGE_SUCCESS, filePath);

        assertCommandSuccess(exportTxtCommand, model, commandHistory, expectedMessage);
    }

    @Test
    public void execute_invalidFilePath_throwsCommandException() {
        // filePath contains invalid character "&"
        String filePath = "/desktop/fakefolder/invalid&Export.txt";
        ExportTxtCommand exportTxtCommand =
                new ExportTxtCommand(ParserUtil.parseFilePath(filePath), ExportCommand.FileType.TXT);
        exportTxtCommand.setStorage(storage);
        String expectedMessage = String.format(ExportCommand.MESSAGE_INVALID_FILE_PATH);

        assertCommandFailure(exportTxtCommand, model, commandHistory, expectedMessage, filePath);
    }


    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualCommandHistory} remains unchanged. <br>
     * - the user date stored in {@code model} matches the data stores at {@code filePath}
     */
    public void assertCommandSuccess(ExportTxtCommand command, Model actualModel, CommandHistory actualCommandHistory,
                                     String expectedMessage) {
        CommandHistory expectedCommandHistory = new CommandHistory(actualCommandHistory);
        try {
            CommandResult result = command.execute(actualModel, actualCommandHistory);
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedCommandHistory, actualCommandHistory);
        } catch (CommandException e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - user data is not saved at {@code filePath}
     */
    public void assertCommandFailure(ExportTxtCommand command, Model actualModel, CommandHistory actualCommandHistory,
                                     String expectedMessage, String filePath) {
        try {
            command.execute(actualModel, actualCommandHistory);
            throw new AssertionError("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            System.out.println(filePath);
            assertEquals(expectedMessage, e.getMessage());
            assertFalse((new File(filePath)).exists());
        }
    }

}
