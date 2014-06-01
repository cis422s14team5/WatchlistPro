package view;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;


public class WarningDialog {

    public Action createDialog(String title, String message) {
        Action response = Dialogs.create()
                .title(title)
                .style(DialogStyle.NATIVE)
                .message(message)
                .showConfirm();
        return response;
    }
}
