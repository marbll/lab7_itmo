package tools.commands.commands;

import data.LabworksStorage;
import db.DBCommunicator;
import tools.commands.Command;

import java.io.Serializable;

public class Clear extends Command implements Serializable {
    public Clear(){
        super("Clear","очистить коллекцию");
        hasData = false;
    }
    @Override
    synchronized public void execute(){
        this.res = "";
        try {
            LabworksStorage.getData().stream().filter(el -> el.getAuthor().equals(DBCommunicator.getLogin())).forEach(DBCommunicator::removeLab);
        }catch (Exception e){
            e.printStackTrace();
        }
        res += "Коллекция ваших лабораторных очищена.\n";
    }

    @Override
    public String getAnswer(){
        return res;
    }
}