package ui;

import chess.*;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import serverhandling.ServerFacade;
import serverhandling.ServerMessageObserver;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class ClientMenu implements ServerMessageObserver {
    private final ServerFacade facade;
    HashMap<Integer, Integer> gameIDMap;


    public ClientMenu(int port) {
        facade = new ServerFacade(port, this);
        gameIDMap = new HashMap<>(); //key is temporary game int, value is primary key of table

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
        showBoard(gameNumber,authToken, null);

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
//        showBoard(gameNumber,authToken, null);

        gameplayUI(authToken, gameNumber);

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

    public void gameplayUI(String authToken, int gameID){
        boolean inProgress = true;
        while (inProgress == true){
            printGameplayOptions();
            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();
            try {
                inProgress = evalGameplay(line, authToken, gameID);
//                System.out.print(loggedIn);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public boolean evalGameplay(String input, String authToken, int gameID) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> redrawBoard(authToken);
                case "3" -> leaveGame(authToken);
                case "4" -> makeMove(authToken);
                case "5" -> resign(authToken);
                case "6" -> highlightLegalMoves(authToken, gameID);
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

    private boolean highlightLegalMoves(String authToken, int gameID) {
        //takes in the position of the piece to check for
        System.out.println("Enter the location of the piece you would like to check (Example: a4): ");
        Scanner scanner = new Scanner(System.in);
        String location = scanner.nextLine();
        //makes sure that there is a piece there and that it is for the right team potentially
        int row = translateRow(location);
        int col = translateCol(location);
        System.out.println("Row = " + row + ". Col = " + col);
        if (row >= 9 || col >= 9){
            System.out.println("Invalid input");
            return highlightLegalMoves(authToken, gameID);
        }
        //get the list of potential moves for that piece
        Collection<ChessMove> chessMoves;
        ChessGame game = getGame(gameID,authToken);
        assert game != null;
        if (game.getBoard().getPiece(new ChessPosition(row, col))!= null){
            chessMoves = game.validMoves(new ChessPosition(row, col));
        } else {
            System.out.println("There are no valid moves");
            return true;
        }

        showBoard(gameID, authToken, chessMoves);

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

    private void showBoard(int gameID, String authToken, Collection<ChessMove> validMoves){
        BoardCreator boardCreator = new BoardCreator();
        ChessGame game = getGame(gameID, authToken);
        System.out.println("White Orientation");
        assert game != null;
        boardCreator.createBoard(ChessGame.TeamColor.WHITE, game.getBoard(), validMoves);
        System.out.println("Black Orientation");
        boardCreator.createBoard(ChessGame.TeamColor.BLACK, game.getBoard(), validMoves);
    }

    private ChessGame getGame(int gameID, String authToken) {
        ListGamesResponse listGamesResponse = facade.listGames(authToken);
        for (GameData game : listGamesResponse.games()) {
            if (gameIDMap.get(gameID) == game.getGameID()) {
                return game.getGame();
            }
        }
        return null;
    }

    private int translateRow(String location){
        if (location.length() == 2 && Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
            // Get the second character and convert it to an integer
            char numberChar = location.charAt(1);
            System.out.println("Row number = " + numberChar);
            return Character.getNumericValue(numberChar);
        } else {
            return 10; // out of range
        }
    }

    private int translateCol(String location){
        if (location.length() == 2 && Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
            // Get the second character and convert it to an integer
            char columnLetter = location.charAt(0);
            System.out.println("Column letter = " + columnLetter);
            int col;
            switch (columnLetter){
                case 'a':
                    col = 1;
                    break;
                case 'b':
                    col = 2;
                    break;
                case 'c':
                    col = 3;
                    break;
                case 'd':
                    col = 4;
                    break;
                case 'e':
                    col = 5;
                    break;
                case 'f':
                    col = 6;
                    break;
                case 'g':
                    col = 7;
                    break;
                case 'h':
                    col = 8;
                    break;
                default:
                    col = 10;
                    break;
            }
            return col;
        } else {
            return 10; // out of range
        }
    }

    /**
     * Client Messaging
     */
    @Override
    public void notify(ServerMessage message) {
        System.out.println("Entering client notifier. Recieved: " + message);
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    private void displayNotification(String message){
        System.out.println(message);
    }

    private void displayError(String message){
        System.out.println(message);
    }

    private void loadGame(ChessGame game){
        BoardCreator boardCreator = new BoardCreator();
        boardCreator.createBoard(ChessGame.TeamColor.WHITE, game.getBoard(), null);
    }
}
