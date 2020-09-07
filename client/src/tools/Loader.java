package tools;

import data.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

public class Loader {

    private String name;
    private Coordinates coordinates; //Поле не может быть null
    private Float minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private long maximumPoint = 0; //Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле может быть null

    public String getName(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить название лабораторной работы? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchName(reader);
        }
        return null;
    }

    public Coordinates getCoordinates(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить координаты лабораторной работы? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchCoordinates(reader);
        }
        return null;
    }

    public boolean getDateChange(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете обновить время создания лабораторной работы? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return true;
        }
        return false;
    }

    public Long getMinPoint(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить минимальнвй балл за лабораторную работу? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchMinPoint(reader);
        }
        return null;
    }

    public Double getAveragePoint(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить средний балл за лабораторную работу? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchAveragePoint(reader);
        }
        return null;
    }

    public String getDescription(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить описание лабораторной работы? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchDescription(reader);
        }
        return null;
    }

    public Difficulty getDifficulty(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить сложность лабораторной работы? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchDifficulty(reader);
        }
        return null;
    }

    public Discipline getDiscipline(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Желаете изменить предмет лабораторной работы? Введите y если да, n если нет");
        String desire = reader.nextLine();
        while ( !( desire.equals("n") || desire.equals("y") ) ){
            desire = reader.nextLine();
        }
        if (desire.trim().equals("y")){
            return searchDiscipline(reader);
        }
        return null;
    }




    public LabWork search(Scanner sc){
        // name
        Scanner reader = new Scanner(System.in);
        String name = searchName(reader);
        Coordinates coordinates = searchCoordinates(reader);
        Long minimalPoint = searchMinPoint(reader);
        Double averagePoint = searchAveragePoint(reader);
        String description = searchDescription(reader);
        Difficulty difficulty = searchDifficulty(reader);
        Discipline discipline = searchDiscipline(reader);

        LocalDate creationDate;
        creationDate = LocalDate.now();
        System.out.println("Все значения элемента успешно получены");
        return new LabWork(name, coordinates, creationDate, minimalPoint, description, averagePoint, difficulty, discipline);
    }

    private String searchName(Scanner reader){
        System.out.print("Введите name: ");
        String name = reader.nextLine();
        while (name.equals("")) {
            System.out.println("Поле не может быть null или пустой строкой ");
            System.out.print("Введите name: ");
            name = reader.nextLine();
        }
        return name;
    }

    private Coordinates searchCoordinates(Scanner reader){
        System.out.println("Введите coordinates: ");
        int x = 0;
        while (true) {
            System.out.print("Введите x: ");
            try {
                x = Integer.parseInt(reader.nextLine());
                if (x > -985)
                    break;
                else {
                    System.out.println("Значение поля должно быть больше -985");
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа int");
            }
        }
        Double y = null;
        while (true) {
            System.out.print("Введите y: ");
            try {
                y = Double.parseDouble(reader.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Double");
            }
        }
        return new Coordinates(x, y);
    }


    private Long searchMinPoint(Scanner reader){
        long minimalPoint = 0;
        while (true) {
            System.out.print("Введите minimalPoint: ");
            try {
                minimalPoint = Long.parseLong(reader.nextLine());
                if (minimalPoint > 0){
                    break;
                }
                else {
                    System.out.println("Значение поля должно быть больше 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа long");
            }
        }
        return minimalPoint;
    }

    private Double searchAveragePoint(Scanner reader){
        Double averagePoint = null;
        while (true) {
            System.out.print("Введите averagePoint: ");
            try {
                averagePoint = Double.parseDouble(reader.nextLine());
                if (averagePoint > 0)
                    break;
                else {
                    System.out.println("Значение поля должно быть больше 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Double");
            }
        }
        return averagePoint;
    }

    private String searchDescription(Scanner reader){
        System.out.print("Введите description: ");
        String description = reader.nextLine();
        while (description.equals("")) {
            System.out.println("Поле не может быть null или пустой строкой ");
            System.out.print("Введите description: ");
            description = reader.nextLine();
        }
        return description;
    }

    private Difficulty searchDifficulty(Scanner reader){
        System.out.print("Введите Difficulty (VERY_EASY, EASY, NORMAL, INSANE, null): ");
        String difficulty_s = reader.nextLine();
        while (!difficulty_s.equals("") && !difficulty_s.equals("VERY_EASY") && !difficulty_s.equals("EASY") && !difficulty_s.equals("NORMAL") && !difficulty_s.equals("INSANE")) {
            System.out.println("Значение поля неверное");
            System.out.print("Введите Difficulty (VERY_EASY, EASY, NORMAL, INSANE, null): ");
            difficulty_s = reader.nextLine();
        }
        Difficulty difficulty = null;
        if (!difficulty_s.equals("")) difficulty = Difficulty.valueOf(difficulty_s);
        return difficulty;
    }

    private Discipline searchDiscipline(Scanner reader){
        System.out.println("Введите Discipline: ");
        System.out.print("Введите name: ");
        String nameDiscipline = reader.nextLine();
        while (nameDiscipline.equals("")) {
            System.out.println("Поле не может быть null");
            System.out.print("Введите name: ");
            nameDiscipline = reader.nextLine();
        }
        Long labsCount = null;
        while (true) {
            System.out.print("Введите labsCount (Поле не обязательное, введите пустую строку чтобы оставить поле пустым): ");
            String a = reader.nextLine();
            try {
                if (a.equals("")) break;
                else {
                    labsCount = Long.parseLong(a);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Аргумент не является значением типа Long");
            }
        }
        return new Discipline(nameDiscipline, labsCount);
    }


    public void load(LabWork lab){
        LabworksStorage.put(lab);
    }
}