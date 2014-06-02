package view;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import java.util.Optional;

/**
 * A JOptionalPane style dialog for JavaFX.
 */
public class DialogPane {

    /**
     * Create a confirmationdialog with yes, no, cancel options.
     * @param title is the title of the dialog.
     * @param message is the message of the dialog.
     * @return the Action response.
     */
    public Action createConfirmDialog(String title, String message) {
        Action response = Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showConfirm();
        return response;
    }

    /**
     * Create a warning dialog with only an OK button.
     * @param title is the title of the dialog.
     * @param message is the message of the dialog.
     * @return the Action response.
     */
    public Action createWarningDialog(String title, String message) {
        return Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showWarning();
    }

    /**
     * Create an input dialog with a field, an OK button, and a cancel button.
     * @param title is the title of the dialog.
     * @param masthead is the masthead of the dialog.
     * @param message is the message of the dialog.
     * @param defaultName is the field's default text.
     * @return an Optional with the user inputted text.
     */
    public Optional<String> createInputDialog(String title, String masthead, String message, String defaultName) {
        Optional<String> obj = Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .masthead(masthead)
                .message(message)
                .showTextInput(defaultName);

        return obj;
    }
}