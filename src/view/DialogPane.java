package view;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;


public class DialogPane {

    public Action createConfirmDialog(String title, String message) {
        return Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showConfirm();
    }

    public Action createWarningDialog(String title, String message) {
        return Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showWarning();
    }
}
