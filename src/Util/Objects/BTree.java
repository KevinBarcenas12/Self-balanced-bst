package Util.Objects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static Util.Functions.Print.textLine;

public class BTree {
    Node root;
    double deg;
    private final int full;

    public BTree(double degree) {
        this.deg    = degree;
        root        = new Node(degree);
        full        = (int) ((degree * 2) + 2);
    }

    public void insert(int key) {
        Node root = this.root;
        insert(root, key);

        if (root.count == full) {
            Node node = new Node(deg);
            this.root = node;
            node.leaf = false;
            node.count = 0;
            node.child[0] = root;
            textLine("Dividida la raiz " + root);
            split(node, 0, root);
        }
    }

    public void insert(@NotNull Node node, int key) {
        textLine("Insertando llave " + key + " en página con llaves " + node);
        if (node.leaf) {
            int i = node.count;
            while (i > 0 && key < node.key[i - 1]) {
                node.key[i] = node.key[i - 1];
                i--;
            }
            node.key[i] = key;
            node.count++;
//            textLine("Inserting in current node -> " + node);
        } else {
//            textLine("Node " + node + " has children, searching propper position");
            int i = 0;
            while (i < node.count && key > node.key[i]) i++;
            if (node.child[i] == null) node.child[i] = new Node(deg);
            insert(node.child[i], key);

            if (node.child[i].count == full) {
                textLine("Dividida la página con llaves " + node.child[i]);
                split(node, i, node.child[i]);
            }
        }
    }

    public void split(Node parent, int position, Node child) {
//        textLine("\nSplitting node " + child + " with parent " + parent);
        Node node = new Node(deg);
        node.leaf = child.leaf;

        int deg = ((int) this.deg) + 1;

        for (int i = 1; i <= deg; i++) {
//            textLine("Key " + child.key[deg + i] + " from left to right node at position " + (i - 1));
            node.key[i - 1] = child.key[deg + i];
            child.key[deg + i] = 0;
        }
        if (!child.leaf) for (int i = (this.deg == .5 ? 1 : 0); i <= (this.deg == .5 ? deg + 1 : deg); i++) {
//            textLine("Child " + child.child[deg + i] + " from left to right node at position " + (i - 1));
            node.child[(this.deg == .5 ?  i - 1 : i)] = child.child[deg + (this.deg == .5 ? i : i + 1)];
            child.child[deg + (this.deg == .5 ? i : i + 1)] = null;
        }

        for (int i = parent.count - 1; i >= position; i--) {
//            textLine("Moving key " + parent.key[i] + " from " + i + " to " + (i + 1));
            parent.key[i + 1] = parent.key[i];
        }
        for (int i = parent.count; i > position; i--) {
//            textLine("Moving child " + parent.child[i] + " from " + i + " to " + (i + 1));
            parent.child[i + 1] = parent.child[i];
        }

//        textLine("Setting child " + node + " in position " + (position + 1) + " in parent");
        parent.child[position + 1] = node;
//        textLine("Setting key " + child.key[deg - 1] + " in position " + position + " in parent");
        parent.key[position] = child.key[deg];

        node.count = deg;
        child.count = deg;
        parent.count++;
//        textLine("\nSplit result with parent " + parent + " with child left " + parent.child[position] + " and right " + parent.child[position + 1]);
    }

    public void print() {
        print(root, 0);
    }
    private void print(Node node, int level) {
        if (node == null) {
            textLine("Página en la altura " + level + " es nula");
            return;
        }
        textLine(level > 0 ? ("Página en la altura " + level + " con llaves " + node) : ("Raíz con llaves " + node));
        if (!node.leaf) for (int i = 0; i <= node.count; i++) print(node.child[i], level + 1);
    }

    public void search(int key) {
        Node result = search(root, key);
        textLine(result == null ? ("Página con llave " + key + " no encontrado.") : ("Se encontró en la página con las llaves " + result));
    }
    private Node search(@NotNull Node current, int key) {
        textLine("Buscando llave " + key + " en nodo " + (current == root ? "raiz" : current));
        int i = 0;
        while (i < current.count && key > current.key[i]) i++;
        if (i < current.count && key == current.key[i]) return current;
        if (current.leaf) return null;
        return search(current.child[i], key);
    }

    public boolean contains(int key) {
        return contains(root, key);
    }
    private boolean contains(@NotNull Node current, int key) {
        textLine("Buscando llave " + key + " en página " + current);
        int i = 0;
        while (i < current.count && key > current.key[i]) i++;
        if (i < current.count && key == current.key[i]) return true;
        if (current.leaf) return false;
        return contains(current.child[i], key);
    }

    public int getHeight() {
        return height(root, 1);
    }
    private int height(@NotNull Node node, int level) {
        if (node.leaf) return level;
        int greater = 0;
        for (Node child : node.child) {
            if (child == null) continue;
            int l = height(child, level + 1);
            if (l > greater) greater = l;
        }
        return greater;
    }

    public List<Integer> getAllKeys() {
        return getKeys(root);
    }
    public String getAllKeysAsString() {
        StringBuilder str = new StringBuilder("[");
        List<Integer> keys = getAllKeys();
        for (int i = 0; i < keys.size(); i++) str.append(i < keys.size() - 1 ? keys.get(i) + "," : keys.get(i));
        str.append("]");
        return str.toString();
    }
    private List<Integer> getKeys(Node node) {
        if (node == null) return new ArrayList<>();
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < node.count; i++) {
            if (!node.leaf && node.child[i] != null) list.addAll(getKeys(node.child[i]));
            list.add(node.key[i]);
        }
        if (!node.leaf && node.child[node.count] != null) list.addAll(getKeys(node.child[node.count]));

        return list;
    }

    public List<Node> getAllNodes() {
        List<Node> list = new ArrayList<>();
        list.add(root);
        list.addAll(getNodes(root));
        return list;
    }
    private List<Node> getNodes(Node node) {
        if (node == null) return new ArrayList<>();
        List<Node> list = new ArrayList<>();

        for (int i = 0; i <= node.count; i++) {
            if (node.child[i] != null) list.add(node.child[i]);
            if (!node.leaf && node.child[i] != null) for (Node child : getNodes(node.child[i])) if (!list.contains(child)) list.add(child);
        }

        return list;
    }

    private enum Action {
        MAX,
        MIN,
        EQUAL
    }
    private enum Type {
        KEY,
        CHILD
    }
    private boolean validate(@NotNull Node a, @NotNull Node b, @NotNull Action action, @NotNull Type type) {
        switch (action) {
            case MIN -> {
                if (type == Type.KEY) return a.count < b.count;
                else return a.getChildCount() < b.getChildCount();
            }
            case MAX -> {
                if (type == Type.KEY) return a.count > b.count;
                else return a.getChildCount() > b.getChildCount();
            }
            case EQUAL -> {
                if (type == Type.KEY) return a.count == b.count;
                else return a.getChildCount() == b.getChildCount();
            }
        }
        return false;
    }

    public List<Node> getLessKeys() {
        return getList(root, new ArrayList<>(), Action.MIN, Type.KEY);
    }
    public List<Node> getMaxKeys() {
        return getList(root, new ArrayList<>(), Action.MAX, Type.KEY);
    }
    public List<Node> getMaxChildren() {
        return getList(root, new ArrayList<>(), Action.MAX, Type.CHILD);
    }
    public List<Node> getLessChildren() {
        return getList(root, new ArrayList<>(), Action.MIN, Type.CHILD);
    }

    private List<Node> getList(Node node, List<Node> list, Action action, Type type) {
        if (node == null) return list;
        if (type == Type.CHILD && node.getChildCount() == 0) return list;
        if (list.size() > 0) {
            boolean equal = true;
            for (Node element : list) if (!validate(element, node, Action.EQUAL, type)) {
                equal = false;
                break;
            }
            if (equal) { if (!list.contains(node)) list.add(node); }
            else for (Node element : list) {
                if (validate(node, element, action, type)) {
                    list = new ArrayList<>();
                    list.add(node);
                    break;
                }
            }
        }
        else list.add(node);
        if (!node.leaf) for (int i = 0; i <= node.count; i++) if (node.child[i] != null) list = getList(node.child[i], list, action, type);
        return list;
    }
}
