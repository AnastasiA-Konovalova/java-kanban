package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyList;
    public Node head;
    public Node tail;
    public HashMap<Integer, Node> mapOfTasks = new HashMap<>();

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (mapOfTasks.containsKey(task.getId())) {
            mapOfTasks.remove(task.getId());
            removeNode(new Node(task));
        }
        linkAddLast(task);
    }

    @Override
    public void remove(Integer id) {
        if (!mapOfTasks.containsKey(id)) {
            System.out.println("История не содердит такого id");
            return;
        }
        Node node = mapOfTasks.get(id);
        removeNode(node);
        mapOfTasks.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkAddLast(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = head;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        mapOfTasks.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        Node current = head;
        if (current == null) {
            return List.of();
        }
        List<Task> listOfHistory = new ArrayList<>();
        while (current != null) {
            listOfHistory.add(current.data);
            current = current.next;
        }
        return listOfHistory;
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }
}