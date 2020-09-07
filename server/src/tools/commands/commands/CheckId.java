package tools.commands.commands;

import data.LabWork;
import data.LabworksStorage;
import db.DBCommunicator;
import tools.commands.Command;

public class CheckId extends Command {
    public CheckId(){
        super("CheckId", "проверить, существует ли работа с данным id");
        hasData = true;
    }

    @Override
    public void execute(String data){
        res = "";
        int id = Integer.parseInt(data);
        LabWork lab = LabworksStorage.searchById(id);
        if (lab == null) res += "No Labwork with such Id";
        else if (!lab.getAuthor().equals(DBCommunicator.getLogin()))  res += "Labwork with such Id doesn't brlong to you";
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
