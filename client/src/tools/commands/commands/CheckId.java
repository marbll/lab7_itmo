package tools.commands.commands;

import data.LabWork;
import data.LabworksStorage;
import tools.db.DBCommunicator;
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
        res += lab.getAuthor().equals(DBCommunicator.getLogin());
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
