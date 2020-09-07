package tools.commands.commands;

import data.*;
import tools.db.DBCommunicator;
import tools.commands.Command;


public class Update extends Command{
    private Coordinates changeCoordinates;
    private boolean updateDate;
    private Long changeMinPoint;
    private Double changeAveragepoint;
    private String changeDescription;
    private Difficulty changeDifficulty;
    private Discipline changeDiscipline;

    public void setChangeCoordinates(Coordinates changeCoordinates) {
        this.changeCoordinates = changeCoordinates;
    }

    public Update(){
        super("Update", "обновить значение элемента коллекции, id которого равен заданному");
        hasData = true;
        needsScanner = true;
    }
    @Override
    synchronized public void execute(String data){
        System.out.println(data);
        this.res = "";
        //String name =  this.changeName;
        try {
            int id = Integer.parseInt(data);
            LabWork lab = LabworksStorage.searchById(id);
            if (lab == null){
                throw new NumberFormatException();
            }
            if (changeName != null){
                DBCommunicator.updateLabName(lab, changeName);
                res += lab.updateName(changeName);
            }
            if (changeCoordinates != null){
                DBCommunicator.updateLabCoords(lab, changeCoordinates);
                res += lab.updateCoordinates(changeCoordinates);
            }
            if (changeMinPoint != null){
                DBCommunicator.updateLabMinPoint(lab, changeMinPoint);
                res += lab.updateMinPoint(changeMinPoint);
            }
            if (changeAveragepoint != null){
                DBCommunicator.updateLabAveragePoint(lab, changeAveragepoint);
                res += lab.updateAveragePoint(changeAveragepoint);
            }
            if (updateDate == true){
                DBCommunicator.updateLabDate(lab);
                res += lab.updateDate();
            }
            if (changeDescription != null){
                DBCommunicator.updateLabDescription(lab, changeDescription);
                res += lab.updateDescription(changeDescription);
            }
            if (changeDifficulty != null){
                DBCommunicator.updateLabDifficulty(lab, changeDifficulty);
                res += lab.updateDifficulty(changeDifficulty);
            }
            if (changeDiscipline != null){
                DBCommunicator.updateLabDiscipline(lab, changeDiscipline);
                res += lab.updateDiscipline(changeDiscipline);
            }


        }catch (NumberFormatException e){
            res += "Id некорректен";
        }
    }

    @Override
    public String getAnswer(){
        return res;
    }

    public void setUpdateDate(boolean updateDate) {
        this.updateDate = updateDate;
    }

    public void setChangeMinPoint(Long changeMinPoint) {
        this.changeMinPoint = changeMinPoint;
    }

    public void setChangeAveragepoint(Double changeAveragepoint) {
        this.changeAveragepoint = changeAveragepoint;
    }

    public void setDescription(String description) {
        this.changeDescription = description;
    }

    public void setChangeDifficulty(Difficulty changeDifficulty) {
        this.changeDifficulty = changeDifficulty;
    }

    public void setChangeDiscipline(Discipline changeDiscipline) {
        this.changeDiscipline = changeDiscipline;
    }
}