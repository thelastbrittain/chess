package ui;

import serverhandling.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class ClientMenu {
//    private final ServerFacade facade;

    public ClientMenu(String serverUrl) {
//        facade = new ServerFacade(serverUrl, this);
    }

    private void run(){
        System.out.println("Welcome to Chess! Select an option below: ");
        printOptions();

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printOptions(){
        System.out.println("1: Help");
        System.out.println("2: Quit");
        System.out.println("3: Login");
        System.out.println("4: Register");
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "quit" -> "quit";
                case "login" -> login();
                case "register" -> register();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String register(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        return null;

    }

    private String login(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        return null;

    }

    private String help(){
        return "Enter 1 to see help options" +
                "Enter 2 to Quit" +
                "Enter 3 to Login" +
                "Enter 4 to Register";
    }


}
