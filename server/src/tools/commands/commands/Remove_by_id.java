package tools.commands.commands;

import data.LabworksStorage;
import data.LabWork;
import db.DBCommunicator;
import tools.commands.Command;

public class Remove_by_id extends Command {
    public Remove_by_id(){
        super("Remove_by_id", "удалить элемент из коллекции по его id");
        hasData = true;
    }

    @Override
    synchronized public void execute(String data){
        res = "";
        try {
            int id = Integer.parseInt(data);
            LabWork lab = LabworksStorage.searchById(id);
            if (DBCommunicator.removeLab(lab)){
                res += LabworksStorage.remove(lab);
            }else {
                res += ("Ошибка при удалении работы, проверьте, принадлежит ли вам работа с Id = " + id);
            }
        }catch (NumberFormatException e){
            res += "Укажите id";
        }catch (NullPointerException e){
            res += "Не найден элемент с указанным Id.";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }
}