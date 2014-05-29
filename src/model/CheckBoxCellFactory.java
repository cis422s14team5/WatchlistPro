package model;

/**
 * Author: Jason Keller
 * Class Name: CheckBoxCellFactory.java
 * Class Description: Cell factory for generating
 * check boxes in a tree table view column.
 */

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.util.Callback;

public class CheckBoxCellFactory<S, T>
        implements Callback<TreeTableColumn<S, Boolean>, Callback<TreeTableColumn<S, Boolean>, TreeTableCell<S, Boolean>>> {

    public Callback<TreeTableColumn<S, Boolean>, TreeTableCell<S, Boolean>> call(TreeTableColumn<S, Boolean> paramAnonymousTreeTableColumn) {
        return (Callback<TreeTableColumn<S, Boolean>, TreeTableCell<S, Boolean>>) CheckBoxTreeTableCell.forTreeTableColumn(paramAnonymousTreeTableColumn).call(paramAnonymousTreeTableColumn);
    }
}
