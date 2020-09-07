package tools.commands.commands;

import data.LabWork;
import data.LabworksStorage;
import db.DBCommunicator;
import tools.commands.Command;

import java.util.concurrent.atomic.AtomicReference;

public class Remove_first extends Command {
    public Remove_first(){
        super("Remove_first","удалить элемент c минимальным ID из принадлежащих вам");
        hasData = false;
    }
    @Override
    synchronized public void execute() {
        res = "";
        if (LabworksStorage.getData().size() != 0) {

            AtomicReference<LabWork> removeLab = new AtomicReference<>();

            LabworksStorage.getData().forEach(el -> {
                //System.out.println(el.getAuthor());
                if(el.getAuthor().equals(DBCommunicator.getLogin())){
                    if (removeLab.get() == null || el.getId() < removeLab.get().getId() ){
                        removeLab.set(el);
                    }
                }
            });
            //System.out.println(LabworksStorage.getData());
            if (removeLab.get() != null){
                DBCommunicator.removeLab(removeLab.get());
                LabworksStorage.remove(removeLab.get());
                res += "Ваша LabWork c ID = " + removeLab.get().getId() + " успешно удалена";
            } else res += "В коллекции отсутствуют ваши элементы. Выполнение команды не возможно.";
        } else res += "В коллекции отсутствуют ваши элементы. Выполнение команды не возможно.";
    }

    @Override
    public String getAnswer(){
        return res;
    }
}