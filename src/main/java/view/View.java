package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import puzzle.Piece;
import puzzle.Puzzle;
import puzzle.PuzzleLocation;
import puzzle.action.ActionObservable;
import puzzle.action.ActionObserver;
import puzzle.agent.AgentPiece;
import puzzle.specials.PuzzlePathFinding;
import puzzle.specials.PuzzlePushRequest;
import utility.LoggerUtility;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class View extends Application implements ActionObserver {

    private Puzzle puzzle;

    private GridPane root;

    private List<ScheduledService<Void>> services;

    private static final int PUZZLE_SIZE = 5;

    private static final String FILE_CSS = "css/view.css";
    private static final String WINDOW_TITLE = "SMA/TP1";
    private static final int SCENE_HEIGHT = 800;
    private static final int SCENE_WIDTH = 800;

    private int number_pieces = 17;
    private int service_period = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        System.out.println("CONFIGURATION DE LA PARTIE");
        if (getParameters().getNamed().containsKey("p")) {
            try {
                service_period = Integer.parseInt(getParameters().getNamed().get("p"));
            } catch (NumberFormatException e) {
                System.err.println("ERR: La période des services doit être un entier positif !");
                stop();
            }
        }
        System.out.println(String.format("Période des services : %d seconde(s)", service_period));

        if (getParameters().getNamed().containsKey("n")) {
            try {
                number_pieces = Integer.parseInt(getParameters().getNamed().get("n"));
                if (number_pieces <= 0 || number_pieces > PUZZLE_SIZE * PUZZLE_SIZE - 1) {
                    System.err.println("ERR: Le nombre de pièces doit être un entier positif inférieur aux dimensions du plateau !");
                    stop();
                }
            } catch (NumberFormatException e) {
                System.err.println("ERR: Le nombre de pièces doit être un entier !");
                stop();
            }
        }
        System.out.println(String.format("Nombre de pièces : %d", number_pieces));

        String puzzleName;
        if (getParameters().getNamed().containsKey("t")) {
            switch (getParameters().getNamed().get("t")) {
                case "path-finding":
                    puzzle = new PuzzlePathFinding(this);
                    puzzleName = "Path-Finding-Puzzle";
                    break;
                case "push-request":
                    puzzle = new PuzzlePushRequest(this);
                    puzzleName = "Push-Request-Puzzle";
                    break;
                default:
                    puzzleName = "NONE";
                    System.err.println("ERR: Le nom de ce puzzle n'existe pas !");
                    stop();
            }
        } else {
            puzzle = new Puzzle(this, PUZZLE_SIZE, number_pieces);
            puzzleName = "Ordinary-Puzzle";
        }
        System.out.println(String.format("Puzzle instancié : %s", puzzleName));

        System.out.println("PUZZLE INSTANCIÉ");
        System.out.println(puzzle);
        System.out.println("Configuration initiale des pièces :");
        puzzle.getPieces().forEach(LoggerUtility::log);
        System.out.println();
    }

    @Override
    public void start(Stage primaryStage) throws ViewConfigurationException {
        root = new GridPane();

        // Ajout du fichier CSS.
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        URL path = this.getClass().getClassLoader().getResource(FILE_CSS);
        if (path == null) throw new ViewConfigurationException(
                String.format("Le fichier %s n'est pas trouvable.", FILE_CSS)
        );
        scene.getStylesheets().add(path.toExternalForm());

        // Construction de la grille.
        generateGrid();

        primaryStage.setScene(scene);
        primaryStage.setTitle(WINDOW_TITLE);

        // Arrêter l'application lors de la fermeture de la fenêtre.
        primaryStage.setOnCloseRequest(e -> {
                    stop();
                }
        );

        primaryStage.show();

        System.out.println("Lancement des threads :");
        runServices();
    }

    private void generateGrid() {
        StackPane stack;
        for (int row = 0; row < puzzle.getDimension(); row++) {
            for (int col = 0; col < puzzle.getDimension(); col++) {
                stack = generateStack(root, row, col);
                root.getChildren().add(stack);
            }
        }

        placePieces();
    }

    private StackPane generateStack(GridPane root, int row, int col) {
        StackPane stack = new StackPane();

        // Générer les noeuds à placer sur la case de la grille.
        Rectangle square = generateSquare(root);
        Text idLabel = generateIdLabel();
        Text destinationLabel = generateDestinationLabel();

        // Ajouter ces noeuds à la case et les positionner correctement.
        stack.getChildren().addAll(square, idLabel, destinationLabel);
        StackPane.setAlignment(destinationLabel, Pos.CENTER);
        StackPane.setAlignment(destinationLabel, Pos.TOP_RIGHT);

        // Position de la case sur la grille.
        GridPane.setRowIndex(stack, row + 1);
        GridPane.setColumnIndex(stack, col + 1);

        // Event listeners
        stack.setOnMouseClicked(e ->
                System.out.println(String.format("(%d, %d) clicked",
                        GridPane.getRowIndex((StackPane) e.getSource()),
                        GridPane.getColumnIndex((StackPane) e.getSource()))
                )
        );

        return stack;
    }

    private Rectangle generateSquare(GridPane root) {
        Rectangle square = new Rectangle();
        Color color = Color.WHITE;
        square.setFill(color);
        square.setStroke(Color.BLACK);
        square.setStrokeType(StrokeType.INSIDE);

        // Automatic square size.
        square.widthProperty().bind(root.widthProperty().divide(puzzle.getDimension()));
        square.heightProperty().bind(root.heightProperty().divide(puzzle.getDimension()));

        return square;
    }

    private Text generateIdLabel() {
        Text label = new Text();
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        return label;
    }

    private Text generateDestinationLabel() {
        Text label = new Text();
        label.setFont(Font.font("Verdana", FontWeight.LIGHT, 20));
        return label;
    }

    private void placePieces() {
        puzzle.getPieces().forEach(piece -> {
            // Current location : update square + id label.
            updateSquare(piece);

            // Final location : update destination label.
            PuzzleLocation destination = piece.getFinalLocation();
            StackPane destinationStack = getStack(destination.getX(), destination.getY());
            Text destinationLabel = (Text) destinationStack.getChildren().get(2);
            destinationLabel.setText(Long.toString(piece.getId()));
        });
    }

    private void updateSquare(Piece piece) {
        // Current location
        PuzzleLocation location = piece.getCurrentLocation();
        StackPane stack = getStack(location.getX(), location.getY());

        Rectangle square = (Rectangle) stack.getChildren().get(0);
        square.setFill(piece.getColor());

        Text idLabel = (Text) stack.getChildren().get(1);
        idLabel.setText(Long.toString(piece.getId()));
    }

    private StackPane getStack(int x, int y) throws ViewException {
        for (Node node : root.getChildren()) {
            if (GridPane.getRowIndex(node) == x && GridPane.getColumnIndex(node) == y) {
                return (StackPane) node;
            }
        }

        throw new ViewException(String.format("La case de coordonnées (%d, %d) n'existe pas.", x, y));
    }

    private void clearGrid() {
        root.getChildren().clear();
    }

    private void updateGrid() {
        clearGrid();
        generateGrid();
    }

    public void runServices() {
        this.services = puzzle.getPieces().stream()
                .map(piece ->
                        new ScheduledService<Void>() {
                            @Override
                            protected Task<Void> createTask() {
                                return new Task<Void>() {
                                    @Override
                                    protected Void call() throws Exception {
                                        if (!puzzle.isResolved()) {
                                            piece.run();
                                        }
                                        return null;
                                    }
                                };
                            }
                        })
                .collect(Collectors.toList());

        services.forEach(service -> {
            service.setOnSucceeded(event -> updateGrid());
            service.setRestartOnFailure(true);
            service.setPeriod(Duration.seconds(service_period));
            service.start();
        });

    }

    @Override
    public void updateActionObserver(ActionObservable observable) {
        if (puzzle.isResolved()) {
            System.out.println("==== PUZZLE RESOLVED ====");

            services.forEach(ScheduledService::cancel);
        }
    }

    @Override
    public void stop() {
        System.out.println("STOP");
        Platform.exit();
        System.exit(0);
    }
}
