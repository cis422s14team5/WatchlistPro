//package com.aquafx_project.controls.skin;
//
//import javafx.scene.control.PasswordField;
//
//import com.aquafx_project.controls.skin.effects.FocusBorder;
//import com.sun.javafx.scene.control.skin.PasswordFieldSkin;
//
//
//public class AquaPasswordFieldSkin extends PasswordFieldSkin implements AquaSkin{
//
//    public AquaPasswordFieldSkin(PasswordField passwordfield) {
//        super(passwordfield);
//
//        if (getSkinnable().isFocused()) {
//            setFocusBorder();
//        }
//
//        registerChangeListener(passwordfield.focusedProperty(), "FOCUSED");
//    }
//
//    private void setFocusBorder() {
//        getSkinnable().setEffect(new FocusBorder());
//    }
//
//    @Override protected void handleControlPropertyChanged(String p) {
//        super.handleControlPropertyChanged(p);
//
//        if (p == "FOCUSED") {
//            if (getSkinnable().isFocused()) {
//                setFocusBorder();
//            } else {
//                getSkinnable().setEffect(null);
//            }
//        }
//    }
//}
