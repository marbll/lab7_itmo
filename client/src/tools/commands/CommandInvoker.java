package tools.commands;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import data.*;
import tools.Loader;
import tools.commands.commands.CheckId;
import tools.commands.commands.Update;
import tools.io.QuitException;
import tools.io.StreamReadWriter;
import tools.io.Transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class CommandInvoker {
    private static HashMap<String, Command> commands;
    private Thread hook;
    private StreamReadWriter ioServer;
    private StreamReadWriter io;
    private Scanner sc;

    public void setSc(Scanner sc) {
        this.sc = sc;
    }

    public void setIo(StreamReadWriter io) {
        this.io = io;
    }

    public CommandInvoker(HashMap<String, Command> commandList, StreamReadWriter ioServer) {
        this.commands = commandList;
        this.ioServer = ioServer;
    }

    public static Command getCommand(String name){
        Optional<Command> command = Optional.ofNullable(commands.get(name.toUpperCase()));
        return command.orElse(null);
    }

    public static String printSavedCommands(){
        return "";
    }

    public void run(String str) throws QuitException, IOException {
        //sc = scanner;

        try {
            if (!str.trim().equals("")) {
                String[] s = str.trim().split(" ");
                Command command = commands.get(s[0].toUpperCase());

                //System.out.println("IS "+ str.trim() + " a command : " +isCommand(str.trim()));
                if (str.trim().startsWith("update") && s.length > 1) {
                    //System.out.println("GOing check");
                    Command checkComm = new CheckId();
                    checkComm.data = s[1];
                    communicateServer(checkComm);
                }
                if (isCommand(str.trim())) {
                    communicateServer(command);
                }
            }
            } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("Unknown Command");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            io.writeln("Скрипт составлен неверно");
        }

    }

    private void communicateServer(Command command) throws IOException, QuitException {
        Transport trans = new Transport(command);
        this.ioServer.writeObj(trans);
        long start = System.currentTimeMillis();

        while (!this.ioServer.ready()) {
            long finish = System.currentTimeMillis();
            if (finish - start > 3000L) {
                throw new QuitException();
            }
        }

        while (this.ioServer.ready()) {

            String st = ioServer.readLine();
            if (command.getName().toLowerCase().startsWith("login") && st.equals("n")){ throw new IOException();}
            if (command.getName().toLowerCase().startsWith("register") && st.equals("n")){ throw new IOException();}
            if (command.getName().toLowerCase().startsWith("checkid") && !st.equals("")){
                System.out.println(st);
                throw new IOException();
            }
            else { io.writeln(st); }

        }
    }




    public boolean isCommand(String line)  {
        String[] parts = line.split(" ", 2);
        String COMMAND = parts[0];
        Command command = CommandInvoker.getCommand(COMMAND);


        if (command == null){
            return false;
        }

        if (command.needsScanner){
            Loader loader = new Loader();
            if (command.getName().trim().toLowerCase().equals("update")  ){
                if (parts.length > 1){
                    try {

                        //command = (Update) command;
                        String name = loader.getName();
                        command.changeName = name;

                        Coordinates coordinates = loader.getCoordinates();
                        ((Update) command).setChangeCoordinates(coordinates);

                        Boolean dateUpdate = loader.getDateChange();
                        ((Update) command).setUpdateDate(dateUpdate);

                        Long minimalPoint = loader.getMinPoint();
                        ((Update) command).setChangeMinPoint(minimalPoint);

                        Double averagePoint = loader.getAveragePoint();
                        ((Update) command).setChangeAveragepoint(averagePoint);

                        String description = loader.getDescription();
                        ((Update) command).setDescription(description);

                        Difficulty difficulty = loader.getDifficulty();
                        ((Update) command).setChangeDifficulty(difficulty);

                        Discipline discipline = loader.getDiscipline();
                        ((Update) command).setChangeDiscipline(discipline);

                        //System.out.println("All set");


                    }catch (NumberFormatException e){
                        System.out.println("Ошибка в формате команды. Введите команду в формате 'command argument'");
                    }
                }



            }else {

                LabWork lab = null;
                lab = loader.search(sc);
                command.lab = lab;
            }
        }

        try {
            if (command.hasData){
                command.data = parts[1];
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Command must have an argument");
            return false;
        }

        if (command.needsExecutor){
            try {
                command.data = parts[1];

                File file = new File(command.data.toLowerCase());
                if (file.exists()){
                    try {
                        Scanner scanner1 = new Scanner(file);
                        setSc(scanner1);
                        while (scanner1.hasNext()) {

                            String l = scanner1.nextLine();
                            if (!(l.trim().toUpperCase().equals("EXIT") | l.trim().toUpperCase().equals("QUIT") )) {
                                run(l);
                            }
                        }
                        setSc(new Scanner(System.in));

                        } catch (QuitException ex) {
                        ex.printStackTrace();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                catch (StackOverflowError e){ System.out.println("Invalid script. Please remove self calls."); }
                }else { System.out.println("No such file"); }
                return false;
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Command must have an argument");
                return false;
            }
        }
        return true;
    }
}