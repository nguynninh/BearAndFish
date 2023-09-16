module com.nguyenninh.bearandfish {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.nguyenninh.bearandfish to javafx.fxml;
    exports com.nguyenninh.bearandfish;
    exports com.nguyenninh.bearandfish.controllers;
    opens com.nguyenninh.bearandfish.controllers to javafx.fxml;
}