package Util;

import Util.Objects.Node;

public class Functions {
    public static boolean validKeys(int input) {
        return (input <= 10 && input > 0) && (input % 2 == 0);
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
    public static int random(int max) {
        return (int) (Math.random() * max);
    }

    public static class Print {
        public static void text(String output) {
            System.out.print(output);
        }
        public static void text(int output) {
            System.out.print(output);
        }
        public static void textLine(String output) {
            System.out.println(output);
        }
        public static void textLine(int output) {
            System.out.println(output);
        }
        public static void textLine(Node node) {
            System.out.println(node.toString());
        }
    }
}
