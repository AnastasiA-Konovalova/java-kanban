package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private Map<Integer, Node> mapOfTasks = new HashMap<>();
    TaskManager taskManager;

    public InMemoryHistoryManager() {

    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        taskManager = Managers.getDefault();
        if (taskManager.validateTask(task)) {
            System.out.println("Задача пересекается с существующими задачами");
            return;
        }
        remove(task.getId());
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