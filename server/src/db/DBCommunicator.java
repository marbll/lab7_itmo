package db;

import data.*;

import javax.xml.bind.ValidationException;
import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class DBCommunicator {

    private static Connection connection;
    private static String tablename;

    private static String login;

    public static void setLogin(String login) {
        DBCommunicator.login = login;
        //System.out.println(login);
    }

    public static String getLogin() {
        return login;
    }

    public static void setDB() throws ClassNotFoundException, SQLException {
        //try {

            Scanner sc = new Scanner(System.in);
            System.out.println("Введите логин для доступа к БД > ");
            String user = sc.nextLine();

        Console console = System.console();
        char[] passwordArray = console.readPassword("Введите пароль для доступа к БД > \n");
        //console.printf("Password entered was: %s%n", new String(passwordArray));

        //System.out.println("Введите пароль для доступа к БД > ");
            String pass = new String(passwordArray);

            Class.forName("org.postgresql.Driver");
            tablename = "users";
            connection = DriverManager.getConnection("jdbc:postgresql://pg/studs", user, pass);


            //connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ritadb", user, pass);
        //} catch (SQLException | ClassNotFoundException e) {e.printStackTrace();}
    }


    public static boolean register(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where users.name = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (rs.next()){ return false; }

        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + tablename + " VALUES(?, ?, ?) ");
        String salt = getSalt();
        ps.setString(1, username);
        ps.setString(2, hash(password, salt));
        ps.setString(3, salt);
        ps.executeUpdate();
        return true;
    }

    public static boolean login(String username, String password) throws SQLException{
        PreparedStatement statement = connection.prepareStatement("select * from " + tablename + " where users.name = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return rs.next() && hash(password, rs.getString("salt")).equals(rs.getString("pass"));
    }

    private static String hash(String password, String salt) {
        try {
            String pepper = "22&3CdsFgh2cL97#3";
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] data = (pepper + password + salt).getBytes(StandardCharsets.UTF_8);
            byte[] hashbytes = md.digest(data);
            String s = Base64.getEncoder().encodeToString(hashbytes);
            return s;
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }

    private static String getSalt() {
        byte[] salt = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }


    public static void getLabs() {
        try {
            //ArrayList<LabWork> labs = new ArrayList<LabWork>();
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement("SELECT * from labworks");
            }catch (NullPointerException e){
                System.out.println("Логин & пароль для доступа к БД неверны");
                System.exit(1);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String name = rs.getString(1);
                Coordinates coordinates = new Coordinates(rs.getInt(2), rs.getDouble(3));
                String creationDate = rs.getString(4);
                Long minimalPoint = rs.getLong(5);
                String description = rs.getString(6);
                Double averagePoint = rs.getDouble(7);

                Difficulty difficulty;
                difficulty = Difficulty.values()[rs.getInt(8)];
                if (rs.wasNull()){
                    difficulty = null;
                }
                Discipline discipline;
                discipline = new Discipline(rs.getString(9));
                discipline.setLabsCount(rs.getLong(10));
                int id = rs.getInt("id");
                LabWork lab = new LabWork(name, coordinates, LocalDate.parse(creationDate) , minimalPoint, description, averagePoint, difficulty, discipline);
                lab.setId(id);
                lab.setAuthor(rs.getString("author"));
                //ticket.setCreationDate(LocalDate.now());

                //labs.add(lab);
                LabworksStorage.put(lab);
            }
            //return labs;
        }catch (Exception e){
            e.printStackTrace();
            //return null;
        }
    }



    public static LabWork writeLab(LabWork lab) throws ValidationException, SQLException {


            PreparedStatement ps = connection.prepareStatement("INSERT INTO labworks" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

            ps.setString(1, lab.getName());
            ps.setInt(2, lab.getCoordinates().getX());
            ps.setDouble(3, lab.getCoordinates().getY());
            java.util.Date date = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            ps.setString(4, sqlDate.toString());
            ps.setLong(5, lab.getMinimalPoint());
            ps.setString(6, lab.getDescription());
            ps.setDouble(7, lab.getAveragePoint());

            try{
                ps.setInt(8, lab.getDifficulty().ordinal());
            }catch (Exception e){
                ps.setNull(8, Types.INTEGER);
            }
            ps.setString(9, lab.getDiscipline().getName());
            try {
                ps.setLong(10, lab.getDiscipline().getLabsCount());
                //ps.setFloat(9, ticket.getEvent().getNumber());
            }catch (Exception e){
                //ps.setNull(7, Types.INTEGER);
                //ps.setString(9, null);
                ps.setNull(10, Types.BIGINT);
            }
            ps.setString(11, login);

            ps.executeUpdate();

            PreparedStatement pps = connection.prepareStatement("SELECT * from labworks where name = ? and author = ?" );
            pps.setString(1, lab.getName().trim());
            pps.setString(2, login);
            ResultSet r = pps.executeQuery();
            if (r.next()){
                Integer id = r.getInt("id");
                //Integer eventId = r.getInt("event_id");
                lab.setId(id);
                //ticket.getEvent().setId(eventId);
                lab.setAuthor(login);
                //.putTicket(ticket);
                //addId = id;
                //addEventId = eventId;
                return lab;
            }else {
                throw new SQLException();
            }

    }


    public static boolean removeLab(LabWork lab){
        try {
            if (login.equals(lab.getAuthor())){
                PreparedStatement ps = connection.prepareStatement("DELETE FROM labworks where id = ?");
                ps.setInt(1, lab.getId());
                ps.executeUpdate();
                LabworksStorage.remove(lab);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void updateLabName(LabWork lab, String name){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET name = ? where (id = ?)");
            ps.setString(1, name);
            ps.setLong(2, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabCoords(LabWork lab, Coordinates coordinates){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET x = ?, y = ? where (id = ?)");
            ps.setInt(1, coordinates.getX());
            ps.setDouble(2, coordinates.getY());
            ps.setInt(3, lab.getId());
            //System.out.println("DATA: " + coordinates.getX() + " " + coordinates.getY() + " " + lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabMinPoint(LabWork lab, Long num){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET minimal_point = ? where (id = ?)");
            ps.setLong(1, num);
            ps.setInt(2, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabAveragePoint(LabWork lab, Double num){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET average_point = ? where (id = ?)");
            ps.setDouble(1, num);
            ps.setInt(2, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabDate(LabWork lab){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET creation_date = ? where (id = ?)");
            ps.setString(1, LocalDate.now().toString());
            ps.setInt(2, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabDescription(LabWork lab, String description) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET description = ? where (id = ?)");
            ps.setString(1, description);
            ps.setInt(2, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabDifficulty(LabWork lab, Difficulty difficulty) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET difficulty = ? where (id = ?)");
            ps.setInt(1, difficulty.ordinal());
            ps.setInt(2, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public static void updateLabDiscipline(LabWork lab, Discipline discipline) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE labworks SET discipline_name = ?, discipline_labs_count = ? where (id = ?)");
            ps.setString(1, discipline.getName());
            ps.setLong(2, discipline.getLabsCount());
            ps.setInt(3, lab.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }
}
