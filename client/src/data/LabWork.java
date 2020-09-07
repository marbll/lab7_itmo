package data;

import com.sun.xml.internal.ws.util.QNameMap;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Класс, объекты которого хранятся в коллекции
 */
public class LabWork implements Comparable<LabWork>, Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long minimalPoint; //Значение поля должно быть больше 0
    private String description; //Строка не может быть пустой, Поле не может быть null
    private Double averagePoint; //Поле не может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле не может быть null
    private String author;

    public LabWork(String name, Coordinates coordinates, LocalDate creationDate, long minimalPoint, String description, Double averagePoint, Difficulty difficulty, Discipline discipline){
        //Optional<LabWork> maxlab= LabworksStorage.getData().stream().max(Comparator.comparing(LabWork::getId));
        //int realid = maxlab.map(labWork -> labWork.getId() + 1).orElse(1);
        //this.id = realid;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.description = description;
        this.averagePoint = averagePoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
    }

    /**
     * Сравнивает данную лабораторную работу с переданной в качестве параметра по averagePoint (другим полям в случае равенства)
     * @param e Лабораторная работа, сравниваемая с данной
     * @return число
     */
    @Override
    public int compareTo(LabWork e) {
        if (!averagePoint.equals(e.getAveragePoint())) return (int) (averagePoint.compareTo(e.getAveragePoint()));
        if (minimalPoint - e.getMinimalPoint() != 0) return (int) (minimalPoint - e.getMinimalPoint());
        if (difficulty != null && e.getDifficulty() == null) return 1;
        else if (difficulty == null && e.getDifficulty() != null) return -1;
        else if (difficulty != null && e.getDifficulty() != null && difficulty.compareTo(e.getDifficulty()) != 0) return difficulty.compareTo(e.getDifficulty());
        if (discipline.compareTo(e.getDiscipline()) != 0) return discipline.compareTo(e.getDiscipline());
        if (coordinates.compareTo(e.getCoordinates()) != 0) return coordinates.compareTo(e.getCoordinates());
        if (description.compareTo(e.getDescription()) != 0) return description.compareTo(e.getDescription());
        return name.compareTo(e.getName());
    }

    public String getName() {
        return name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public Integer getId() {
        return id;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public long getMinimalPoint() {
        return minimalPoint;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }
    public Discipline getDiscipline() {
        return discipline;
    }
    public String getDescription() {
        return description;
    }
    public Double getAveragePoint() {
        return averagePoint;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setAuthor(String author) { this.author = author; }
    public String getAuthor() {return this.author; }

    public String updateName(String name){
        this.name = name;
        return "Name successfully changed. New name is: " + name + "\n";
    }

    public String updateCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
        return "Coordinates successfully changed. New coordinates are: (" + coordinates.getX() + ":" + coordinates.getY() + ")" + "\n";
    }

    public String updateDate(){
        this.creationDate = LocalDate.now();
        return "Creation date successfully updated. New date is: " + creationDate + "\n";
    }

    public String updateMinPoint(Long minimalPoint){
        this.minimalPoint = minimalPoint;
        return "Minimal point successfully changed. New minimal point is: " + minimalPoint + "\n";
    }

    public String updateAveragePoint(Double averagePoint){
        this.averagePoint = averagePoint;
        return "Average point successfully changed. New average point is: " + averagePoint + "\n";
    }

    public String updateDescription(String description){
        this.description = description;
        return "Average point successfully changed. New average point is: " + description + "\n";
    }

    public String updateDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
        return "Difficulty successfully changed. New difficulty is: " + difficulty.toString() + "\n";
    }

    public String updateDiscipline(Discipline discipline){
        this.discipline = discipline;
        return "Discipline successfully changed. New discipline is: " + discipline.getName() + " with " + discipline.getLabsCount() + " labworks in the course" + "\n";
    }

    public String update(){
        String res = "";
        if (this.name.contains("v1.")){
            int numPart = Integer.parseInt(name.split("v1.")[1]);
            res += "LabWork " + name + " updated. ";
            name = name.replace(String.valueOf(numPart), String.valueOf(numPart + 1));
            res += "New name is " + name;
        }else {
            this.name += "v1.1";
        }
        return res;
    }

    @Override
    public String toString() {
        return "LabWork { " +
                "id: " + id + ", name: " + name + "," + coordinates.toString() + ", creationDate:"
                + creationDate + ", minimalPoint:" + minimalPoint + ", description:" + description + ", averagePoint:" + averagePoint +
                ", difficulty:" + difficulty + ", " + discipline.toString() + "}" ;
    }

    public String printString(){
        return "LabWork " + id + "\t" +
                "name: " + name + "\t" +
                "coordinates: " + coordinates.toString() + "\t" +
                "creationDate: " + creationDate + "\t" +
                "minimalPoint: " + minimalPoint + "\t" +
                "description: " + description + "\t" +
                "averagePoint: " + averagePoint + "\t" +
                "difficulty: " + difficulty + "\t" +
                "discipline: " + discipline.toString() + "\n" +
                "author: " + author + "\n";
    }

    /**
     * проверяет на равенство объекты LabWork (равенство только при равенстве всех полей)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabWork)) return false;
        LabWork labWork = (LabWork) o;
        return id.equals(labWork.getId()) &&
                name.equals(labWork.getName()) &&
                coordinates.equals(labWork.getCoordinates()) &&
                creationDate.equals(labWork.getCreationDate()) &&
                minimalPoint == labWork.getMinimalPoint() &&
                description.equals(labWork.getDescription()) &&
                averagePoint.equals(labWork.getAveragePoint()) &&
                difficulty.equals(labWork.getDifficulty()) &&
                discipline.equals(labWork.getDiscipline());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, minimalPoint, description, averagePoint, difficulty, discipline);
    }
}