package tools.commands.commands;

import data.LabworksStorage;
import tools.db.DBCommunicator;
import tools.commands.Command;

import java.io.Serializable;
import java.sql.SQLException;

public class Login extends Command implements Serializable {
    public Login(){
        super("Login","выполнить логин в БД");
        hasData = true;
    }
    @Override
    public void execute(String data) {
        this.res = "";

        try {
            if (DBCommunicator.login(data.split("&")[0], data.split("&")[1])){
                res += "y";
            }else res += "n";
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //res += Math.random() < 0.5 ? "n" : "y";
        //res += "n";
    }

    @Override
    public String getAnswer(){
        return res;
    }
}
