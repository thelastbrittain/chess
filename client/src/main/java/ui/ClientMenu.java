package ui;

import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;
import serverhandling.ServerFacade;

import java.util.Scanner;

public class ClientMenu {
    private final ServerFacade facade;

    public ClientMenu(int port) {
        facade = new ServerFacade(port);
    }

    public void run(){


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.println("Welcome to Chess! Select an option below: ");
            printPreLoginOptions();
            String line = scanner.nextLine();

            try {
                result = evalPreLogin(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printPreLoginOptions(){
        System.out.println("1: Help");
        System.out.println("2: Quit");
        System.out.println("3: Login");
        System.out.println("4: Register");
    }

    public String evalPreLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> "quit";
                case "3" -> login();
                case "4" -> register();
                default -> preLoginHelp();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String register(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        RegisterResponse regResponse = facade.register(new RegisterRequest(username,password, email));
        if (regResponse.authToken() != null){
            postLoginUI(regResponse.authToken());
            return "";
        } else {
            return regResponse.message();
        }
    }

    private String login(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        LoginResponse response =  facade.login(new LoginRequest(username, password));
        if (response.authToken() != null){
            postLoginUI(response.authToken());
            return "";
        } else {
            return response.message();
        }
    }

    private String preLoginHelp(){
        return "Enter 1 to see help options" + "\n" +
                "Enter 2 to Quit" + "\n" +
                "Enter 3 to Login" + "\n" +
                "Enter 4 to Register" + "\n";
    }

    public void postLoginUI(String authToken){
        System.out.println("Log in success. Select an option below: ");

        String loggedIn = "Logged In";
        while (!loggedIn.equals("Logged out")){
            printPostLoginOptions();
            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();
            try {
                loggedIn = evalPostLogin(line, authToken);
                System.out.print(loggedIn);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPostLoginOptions() {
        System.out.println("1: Help");
        System.out.println("2: Logout");
        System.out.println("3: Create Game");
        System.out.println("4: List Games");
        System.out.println("5: Play Game");
        System.out.println("6: Observer Game");
    }

    public String evalPostLogin(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> logout(authToken);
                case "3" -> createGame(authToken);
                case "4" -> listGames();
                case "5" -> playGame();
                case "6" -> observeGame();
                default -> preLoginHelp();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String logout(String authToken) {
        facade.logout(authToken);

        return "Logged out";
    }

    private String observeGame() {
        return null;
    }

    private String playGame() {
        return null;
    }

    private String listGames() {
        return null;
    }

    private String createGame(String authToken) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new game name: ");
        String gameName = scanner.nextLine();

        facade.createGame(new CreateGameRequest(gameName, null), authToken);

        return "";
    }


}
