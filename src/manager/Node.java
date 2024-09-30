package manager;

import tasks.Task;

public class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Task data) {
        this.data = data;
    }
}
