module com.projetpoo.demotpfinale {
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    requires com.fasterxml.jackson.datatype.jsr310;
    requires javafx.controls;
    requires javafx.fxml;
    opens com.projetpoo.demotpfinale.modele to com.fasterxml.jackson.databind;
    opens com.projetpoo.demotpfinale.Interface to com.fasterxml.jackson.databind;
    opens com.projetpoo.demotpfinale.Serialisation to com.fasterxml.jackson.databind;
    exports com.projetpoo.demotpfinale;
    exports com.projetpoo.demotpfinale.Interface;
    exports com.projetpoo.demotpfinale.modele;
    exports com.projetpoo.demotpfinale.Serialisation;
}