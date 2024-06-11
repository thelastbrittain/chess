package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import serverhandling.ServerFacade;

import java.util.HashMap;
import java.util.Scanner;

public class ClientMenu {
    private final ServerFacade facade;
    HashMap<Integer, Integer> gameIDMap;

    public ClientMenu(int port) {
        facade = new ServerFacade(port);
        gameIDMap = new HashMap<>();  //key is temporary game int, value is primary key of table
    }


    /**
     * Pre Login UI
     */


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

    /**
     * Pre login methods
     */

    private String preLoginHelp(){
        return "Enter 1 to see help options" + "\n" +
                "Enter 2 to Quit" + "\n" +
                "Enter 3 to Login" + "\n" +
                "Enter 4 to Register" + "\n";
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


    /**
     * Post login UI
     */

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




    public String evalPostLogin(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> logout(authToken);
                case "3" -> createGame(authToken);
                case "4" -> listGames(authToken);
                case "5" -> playGame(authToken);
                case "6" -> observeGame(authToken);
                default -> postLoginHelp();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    /**
     * Post login methods
     */

    private void printPostLoginOptions() {
        System.out.println("1: Help");
        System.out.println("2: Logout");
        System.out.println("3: Create Game");
        System.out.println("4: List Games");
        System.out.println("5: Play Game");
        System.out.println("6: Observer Game");
    }

    private String postLoginHelp(){
        return "Enter 1 to see help options" + "\n" +
                "Enter 2 to Logout" + "\n" +
                "Enter 3 to Create a Game" + "\n" +
                "Enter 4 to List the Games" + "\n" +
                "Enter 5 to Play a Game" + "\n" +
                "Enter 6 to Observe a Game" + "\n";
    }

    private String logout(String authToken) {
        facade.logout(authToken);

        return "Logged out";
    }

    private String observeGame(String authToken) {
        listGames(authToken);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the game number you'd like to observe: ");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        showGame(gameNumber,authToken);

        return "";
    }

    private String playGame(String authToken) {
        listGames(authToken);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the game number you'd like to join: ");
        int gameNumber = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the color you'd like to join as, select w for white or b for black. If you don't select either, white will be selected: ");
        String color = scanner.nextLine();
        ChessGame.TeamColor teamColor = null;
        if (color.equals("b")){
            teamColor = ChessGame.TeamColor.BLACK;
        } else
        {
            teamColor = ChessGame.TeamColor.WHITE;
        }

        facade.joinGame(new JoinGameRequest(teamColor, gameIDMap.get(gameNumber), authToken));
        showGame(gameNumber,authToken);

        return "";
    }

    private String listGames(String authToken) {
        ListGamesResponse listGamesResponse =  facade.listGames(authToken);

        int gameNumber = 1;
        for (GameData game : listGamesResponse.games()){
            System.out.println("Game Number: " + gameNumber + ", Game Name: " + game.getGameName() + ", White Username: " + game.getWhiteUsername() + ", Black Username: " + game.getBlackUsername());
            gameIDMap.put(gameNumber, game.getGameID());
            gameNumber ++;
        }
        return "";
    }

    private String createGame(String authToken) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new game name: ");
        String gameName = scanner.nextLine();

        facade.createGame(new CreateGameRequest(gameName, null), authToken);


        return "";
    }


    /**
     * Gameplay UI
     */

    public void gameplayUI(String authToken){
        boolean inProgress = true;
        while (inProgress == true){
            printGameplayOptions();
            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();
            try {
                inProgress = evalGameplay(line, authToken);
//                System.out.print(loggedIn);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public boolean evalGameplay(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> redrawBoard(authToken);
                case "3" -> leaveGame(authToken);
                case "4" -> makeMove(authToken);
                case "5" -> resign(authToken);
                case "6" -> highlightLegalMoves(authToken);
                default -> gameplayHelp();
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;   //remove this later
    }

    private boolean redrawBoard(String authToken) {
        return true;
    }
    private boolean leaveGame(String authToken) {
        return false;
    }
    private boolean makeMove(String authToken) {
        return true;
    }
    private boolean resign(String authToken) {
        return false;
    }

    private boolean highlightLegalMoves(String authToken) {
        //takes in the position of the piece to check for
        System.out.println("Enter the location of the piece you would like to check (Example: a4): ");
        Scanner scanner = new Scanner(System.in);
        String location = scanner.nextLine();
        //makes sure that there is a piece there and that it is for the right team potentially
        int row = translateRow(location);
        int col = translateCol(location);
        if (row < 9 && col < 9){

        }
        //get the list of potential moves for that piece
        //put that into a list of lists
        //give that list of lists to the gamebaord drawer to redraw the board

        return true;
    }


    private void printGameplayOptions() {
        System.out.println("1: Help ");
        System.out.println("2: Redraw Chess Board ");
        System.out.println("3: Leave ");
        System.out.println("4: Make Move ");
        System.out.println("5: Resign");
        System.out.println("6: Highlight Legal Moves");
    }

    private boolean gameplayHelp(){
        System.out.println("Enter 1 to see help options" + "\n" +
                "Enter 2 to Redraw the Chess Board" + "\n" +
                "Enter 3 to Leave the Game" + "\n" +
                "Enter 4 to Make a Move" + "\n" +
                "Enter 5 to Resign" + "\n" +
                "Enter 6 to Highlight Legal Moves" + "\n");
        return true;
    }


    /**
     * Helper functions
     */

    private void showGame(int gameID, String authToken){
        BoardCreator boardCreator = new BoardCreator();
        ChessBoard board = getBoard(gameID, authToken);
        System.out.println("White Orientation");
        boardCreator.createBoard(ChessGame.TeamColor.WHITE, board);
        System.out.println("Black Orientation");
        boardCreator.createBoard(ChessGame.TeamColor.BLACK, board);
    }

    private ChessBoard getBoard(int gameID, String authToken) {
        ListGamesResponse listGamesResponse = facade.listGames(authToken);
        for (GameData game : listGamesResponse.games()) {
            if (gameIDMap.get(gameID) == game.getGameID()) {
                return game.getGame().getBoard();
            }
        }
        return null;
    }

    private int translateRow(String location){
        if (location.length() == 2 && Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
            // Get the second character and convert it to an integer
            char numberChar = location.charAt(1);
            return Character.getNumericValue(numberChar);
        } else {
            return 10; // out of range
        }
    }

    private int translateCol(String location){
        if (location.length() == 2 && Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
            // Get the second character and convert it to an integer
            char columnLetter = location.charAt(0);
            int col;
            switch (columnLetter){
                case 'a':
                    col = 1;
                case 'b':
                    col = 2;
                case 'c':
                    col = 3;
                case 'd':
                    col = 4;
                case 'e':
                    col = 5;
                case 'f':
                    col = 6;
                case 'g':
                    col = 7;
                case 'h':
                    col = 8;
                default:
                    col = 10;
            }
            return col;
        } else {
            return 10; // out of range
        }
    }

}
