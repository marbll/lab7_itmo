package tools.login;

import tools.commands.CommandInvoker;
import tools.io.QuitException;

import java.io.IOException;
import java.util.Scanner;

public class LoginManager {
    private boolean logged;
    private Scanner scanner = new Scanner(System.in);
    private CommandInvoker commandInvoker;

    public LoginManager(CommandInvoker ci){
        commandInvoker = ci;
    }

    public boolean isLogged() {
        return logged;
    }

    public void readData() {
        System.out.println("Введите логин для входа на сайт или введите register для регистрации");
        String login = scanner.nextLine();

        if (login.trim().equals("register")){
            System.out.println("Введите логин, под екоторым вы желаете заегистрироваться");
            login = scanner.nextLine();
            System.out.println("Введите пароль");
            String password = scanner.nextLine();
            try {
                commandInvoker.run("register " + login + "&" + password);
                System.out.println("Регистрация успешна. Пожалуйста, ");
            } catch (QuitException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Регистрация не прошла");
            }
        }else {
            System.out.println("Введите пароль");
            String password = scanner.nextLine();

            try {
                commandInvoker.run("login " + login + "&" + password);
                logged = true;
                System.out.println("Логин успешен. Пожалуйста, введите команду");
            } catch (QuitException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Логин & пароль не верны. Повторите ввод");
            }
        }
    }
}
