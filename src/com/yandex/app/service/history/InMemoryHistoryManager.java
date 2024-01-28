package com.yandex.app.service.history;

import com.yandex.app.model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> browsingHistory = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;


    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (browsingHistory.get(taskId) == null) {
            browsingHistory.put(taskId, linkLast(task));
        } else {
            removeNode(browsingHistory.get(taskId));
            browsingHistory.put(taskId, linkLast(task));
        }


    }

    @Override
    public void remove(int id) {
        if (browsingHistory.containsKey(id)) {
            removeNode(browsingHistory.get(id));
            browsingHistory.remove(id);
        }

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> node = new Node<>(task);
        if (head == null) {
            head = node;

        } else if (tail == null) {
            tail = node;
            tail.setPrev(head);
            head.setNext(tail);

        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
            tail.setNext(null);
        }
        size++;
        return node;
    }

    private List<Task> getTasks() {
        List<Task> newList = new ArrayList<>();
        if (size == 0) {
            return new ArrayList<>();

        } else {
            Node<Task> node = head;
            do {
                newList.add(node.getData());
                node = node.getNext();
            } while (node != null);
            return newList;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null && size > 0) {
            Node<Task> next = node.getNext();
            Node<Task> prev = node.getPrev();

            if (prev == null) {
                head = node.getNext();
                if (size > 1) {
                    head.setPrev(null);
                }

            } else if (next == null) {
                tail = node.getPrev();
                tail.setNext(null);

            } else {
                prev.setNext(node.getNext());
                next.setPrev(node.getPrev());
            }
            size--;
        }

    }

}


