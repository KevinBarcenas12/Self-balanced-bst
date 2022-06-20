import Util.FileManager;
import Util.Functions;
import Util.Objects.BTree;
import Util.Objects.Node;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import java.nio.file.Paths;

import static Util.Functions.Print.text;
import static Util.Functions.Print.textLine;

public class Main {
    private static Scanner reader;

    public static void main(String[] args) {
        // Main App
        reader = new Scanner(System.in);

        try {
            App();
        }
        catch (Exception e) {
            textLine("An exception ocurred: " + e.getMessage());
        }
    }

    public static void App() {
        // Initial data -> { Tree Degree }
        int maxKeys;

        // Request initial data;
        while (true) {
            text("¿Cuántas llaves podrá tener como máximo cada página? (2, 4, 6, 8, 10): ");
            int input = reader.nextInt();
            if (Functions.validKeys(input)) {
                maxKeys = input;
                break;
            }
            else textLine("El número de llaves no es válido.");
        }

        // Calculate tree degree
        double degree = (double) (maxKeys - 1) / 2;
        // Create tree
        BTree tree = new BTree(degree);

        textLine("De qué forma ingresará los datos iniciales del árbol?");
        textLine("1. Un archivo");
        textLine("2. No ingresar datos iniciales");
        boolean fileReaded = false;

        // Ask for file path
        if (requestInt() == 1) {
            FileManager file = null;
            while (file == null) {
                try {
                    textLine("Ingrese la dirección del archivo (Puede estar en una carpeta externa o en la misma del ejecutable):");
                    textLine("C:\\path\\to\\file.txt [Carpeta externa] | path\\to\\file.txt [Carpeta local]");
                    String currentPath = Paths.get("").toAbsolutePath() + "\\";
                    String filepath = reader.next();
                    // Check if starts from [root | external disk] directory
                    if (filepath.substring(1).startsWith(":\\") || filepath.startsWith("/")) file = new FileManager(filepath);
                    // else is in current path
                    else file = new FileManager(currentPath + filepath);
                    // Got no error to read the file, setting fileReaded to true
                    fileReaded = true;
                }
                catch (FileNotFoundException e) {
                    textLine("No se ha encontrado el archivo.");
                    textLine("Desea intentar nuevamete?\n1. Si\n2. No");
                    if (requestInt() == 2) break;
                }
            }
            if (file != null) {
                List<Integer> inputs = file.getInputs();
                for (int input : inputs) tree.insert(input);
            }
        }

        tree.print();
        if (fileReaded) {
            // Print current tree info
            textLine("Altura: " + tree.getHeight());
            textLine("Llaves leídas por el programa: " + tree.getAllKeysAsString());
        }

        // Cycle through options
        while (true) {
            boolean keep = true;
            textLine("\nTiene las siguientes opciones:");
            textLine("1. Ingresar una llave");
            textLine("2. Buscar una llave");
            textLine("3. Consultar estadísticas");
            textLine("4. Salir");

            switch (requestInt()) {
                // Insert key
                case 1 -> {
                    textLine("\nIngrese la llave que desea ingresar: ");
                    int key = requestInt();
                    if (!tree.contains(key)) {
                        textLine("La llave no existe, insertando..");
                        tree.insert(key);
                    }
                    else textLine("La llave que desea ingresar ya existe en el árbol!");
                }
                // Search key
                case 2 -> {
                    textLine("\nInrese la llave que desea buscar: ");
                    tree.search(requestInt());
                }
                // Display tree stats
                case 3 -> {
                    while (true) {
                        boolean _keep = true;
                        textLine("\nLas estadísticas disponibles son:");
                        textLine("1. Claves actuales en el árbol");
                        textLine("2. Páginas existentes");
                        textLine("3. Altura del árbol");
                        textLine("4. Páginas con más llaves");
                        textLine("5. Páginas con menos llaves");
                        textLine("6. Páginas con más hijos");
                        textLine("7. Páginas con menos hijos");
                        textLine("8. Imprimir todo el árbol");
                        textLine("9. Volver");

                        switch (requestInt()) {
                            // Current keys
                            case 1 -> {
                                List<Integer> keys = tree.getAllKeys();
                                StringBuilder string = new StringBuilder("[");
                                for (int i = 0; i < keys.size(); i++) string.append(i < keys.size() - 1 ? keys.get(i) + "," : keys.get(i));
                                string.append("]");
                                textLine("Las llaves actuales del árbol son: " + string);
                            }
                            // Current nodes
                            case 2 -> {
                                List<Node> nodes = tree.getAllNodes();
                                textLine("Las páginas existentes son:\n");
                                for (Node node : nodes) textLine("Node " + node);
                            }
                            // Tree height
                            case 3 -> {
                                textLine("La altura actual del árbol es: " + tree.getHeight());
                            }
                            // Current nodes with the most keys
                            case 4 -> {
                                List<Node> list = tree.getMaxKeys();
                                textLine("Las páginas con más llaves son:");
                                for (Node node : list) textLine("Node " + node);
                            }
                            // Current nodes with the less keys
                            case 5 -> {
                                List<Node> list = tree.getLessKeys();
                                textLine("Las páginas con menos llaves son:");
                                for (Node node : list) textLine("Node " + node);
                            }
                            // Current nodes with the most children
                            case 6 -> {
                                List<Node> list = tree.getMaxChildren();
                                textLine("Las páginas con más hijos son:");
                                for (Node node : list) {
                                    textLine("Node " + node);
                                    text("With children: { ");
                                    for (int i = 0; i < node.getChildCount(); i++) {
                                        text(node.getChild()[i].toString());
                                        if (i < node.getChildCount() - 1) text(", ");
                                    }
                                    text(" }\n");
                                }
                            }
                            // Current nodes with the less children
                            case 7 -> {
                                List<Node> list = tree.getLessChildren();
                                textLine("Las páginas con más hijos son:");
                                for (Node node : list) {
                                    textLine("Node " + node);
                                    text("With children: { ");
                                    for (int i = 0; i < node.getChildCount(); i++) {
                                        text(node.getChild()[i].toString());
                                        if (i < node.getChildCount() - 1) text(", ");
                                    }
                                    text(" }\n");
                                }
                            }
                            case 8 -> tree.print();
                            // Close cycle
                            case 9 -> _keep = false;
                            default -> textLine("No se ingresó una opción válida.");
                        }
                        if (!_keep) break;
                    }
                }
                // Close cycle
                case 4 -> keep = false;
                default -> textLine("No se ingresó una opción válida.");
            }
            if (!keep) break;
        }
    }
    public static int requestInt() {
        int opt = 0;
        while (true) {
            boolean keep = false;
            try {
                opt = reader.nextInt();
                if (opt == 0) throw new InputMismatchException("Input must not be equal to 0.");
            }
            catch (InputMismatchException e) {
                textLine("Error de lectura: " + e.getMessage());
                keep = true;
            }
            if (!keep) break;
        }
        return opt;
    }
}