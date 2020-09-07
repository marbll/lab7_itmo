import IO.IOClient;
import IO.IOinterface;
import data.LabWork;
import data.LabworksStorage;
import db.DBCommunicator;
import org.jdom2.JDOMException;
import tools.*;
import tools.commands.Command;
import tools.commands.CommandInvoker;
import tools.commands.commands.Add;
import tools.connector.ClientHandler;
import tools.io.Transport;


import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    public static boolean sent;

    public static void main(String[] args) {
        try {

            System.out.println("Введите порт\n>");
            Scanner sc = new Scanner(System.in);
            String port = sc.nextLine();

            DBCommunicator.setDB();

            loadLabs();

            ClientHandler clientHandler = null;

            try {

                clientHandler = new ClientHandler(Integer.parseInt(port.trim()));


                Transport collectionSender = new Transport("map");
                HashMap<String, Command> l = CommandInvoker.getCommands();
                collectionSender.putObject(l);

                Transport trans = null;
                IOinterface ioClient = null;
                Command comm = null;


                ExecutorService executor = Executors.newCachedThreadPool();




                while (true) {
                    clientHandler.getSelector().select();
                    Iterator iter = clientHandler.getSelector().selectedKeys().iterator();

                    while (iter.hasNext()) {
                        SelectionKey selKey = (SelectionKey) iter.next();
                        iter.remove();

                        try {
                            if (selKey.isValid()) {
                                if (selKey.isAcceptable()) {
                                    clientHandler.acceptConnect();
                                    sent = false;
                                }

                                if (selKey.isWritable()) {
                                    if (!sent) {
                                        ioClient = new IOClient((SocketChannel) selKey.channel(), true);
                                        CommandProcessor.setIoClient(ioClient);
                                        ioClient.writeObj(collectionSender);
                                        sent = true;
                                    } else {
                                        //System.out.println("COMMAND1 " + comm);

                                        //ExecutorService executor = Executors.newCachedThreadPool();
                                        //for (int i = 1; i <= 4; ++i) {
                                        CommandProcessor.setCommand(comm);
                                        executor.execute(new CommandProcessor());
                                        //}

                                        //processCommand(comm, ioClient);

                                    }

                                    selKey.interestOps(1);
                                }

                                if (selKey.isReadable()) {
                                    ioClient = new IOClient((SocketChannel) selKey.channel(), true);
                                    CommandProcessor.setIoClient(ioClient);
                                    trans = (Transport) ioClient.readObj();
                                    comm = (Command) trans.getObject();
                                    selKey.interestOps(4);
                                }
                            }
                        } catch (ConnectException e) {
                            System.out.println(e.getMessage());
                            sent = false;
                        }
                    }
                }
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
                System.out.println("Please provide PORT");
                System.exit(0);
            } catch (BindException e) {
                System.out.println("Address & port already in use");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());;
        }

    }

    private static class CommandProcessor implements Runnable {

        private static Command command;
        private static IOinterface ioClient;

        public static void setCommand(Command comm){
            command = comm;
        }

        public static void setIoClient(IOinterface ioClient) {
            CommandProcessor.ioClient = ioClient;
        }


        @Override
        public void run() {
            try {
                processCommand(command, ioClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processCommand(Command comm, IOinterface ioClient) throws IOException {

        //System.out.println("COMMAND " + comm);

        String res = "";
        if (comm.getName().equals("QUIT")){
            Java2XML j2 = new Java2XML();
            j2.writeXML();
            //System.out.println("Collection saved");
        }else {
            if (comm.needsScanner && !comm.getName().trim().toLowerCase().equals("update")){
                // add Command

                //comm = (Add) comm;
                //((Add) comm).setLab();
                //LabWork labWork = comm.lab;
                //Optional<LabWork> maxlab= LabworksStorage.getData().stream().max(Comparator.comparing(LabWork::getId));
                //int id = maxlab.map(lab -> lab.getId() + 1).orElse(1);
                //labWork.setId(id);
                //comm.res += LabworksStorage.put(labWork);

            }
            CommandInvoker.loadToSavedCommands(comm);
            if (comm.hasData){
                comm.execute(comm.data);
            }else {
                comm.execute();
            }

            res = comm.getAnswer();
        }

        try {
            ioClient.writeln(res);
        }catch (NullPointerException e){ }
    }

    private static void loadLabs(){

        DBCommunicator.getLabs();
        System.out.println("Collection of labworks loaded from database.");
        //File file = new File("Collection.xml");
        //XMLScanner xsc = new XMLScanner();
        //try{
        //    xsc.scan(file);
        //}catch (IOException | JDOMException e){
        //    System.out.println("File not found or is of wrong format, loading empty collection");
        //}
    }
}
