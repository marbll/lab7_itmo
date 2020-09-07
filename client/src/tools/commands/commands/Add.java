package tools.commands.commands;

import data.LabWork;
import data.LabworksStorage;
import tools.db.DBCommunicator;
import tools.commands.Command;

import javax.xml.bind.ValidationException;
import java.sql.SQLException;

public class Add extends Command {

    public Add(){
        super("Add","добавить новый элемент в коллекцию");
        hasData = false;
        needsScanner = true;
    }

    synchronized public void execute(){
        res = "";
        try {
            res += LabworksStorage.put(DBCommunicator.writeLab(lab));
        } catch (ValidationException | SQLException e) {
            e.printStackTrace();
            res = "Ошибка при добавлении Labwork";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}