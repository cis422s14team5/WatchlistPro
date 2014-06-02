package view;

import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import java.util.Optional;


public class DialogPane {

    public Action createConfirmDialog(String title, String message) {
        Action response = Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showConfirm();
        return response;
    }

    public Action createWarningDialog(String title, String message) {
        return Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showWarning();
    }

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