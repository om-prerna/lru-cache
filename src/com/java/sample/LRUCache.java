package com.java.sample;

import java.util.HashMap;

class Node<K, V>{
    final K key;
    V val;
    Node<K,V> pre;
    Node<K,V> next;

    public Node(K key, V val){
        this.key = key;
        this.val = val;
    }
}

public class LRUCache<K,V> {
    private int capacity ;
    private HashMap<Integer, Node<K, V>> cacheMap;
    private Node head, tail;

      /*Insert or update
       First check if key is present or not
       if present
            update value and move to front of list
       else
            a) When list is full
                 remove lru element from tail
            b) When list is not full
           insert new at head
           Update Map
       */

    public LRUCache(int capacity){
        this.capacity = capacity;
        cacheMap = new HashMap<Integer, Node<K, V>>();
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);
        head.next = tail;
        tail.pre = head;
    }

    public void add(K key, V val){
        Node node;
        if(cacheMap.containsKey(key)){
            node = cacheMap.get(key);
            node.val = val;
            removeNode(node);
            addToFront(node);
        }else {
            if( cacheMap.size() == capacity)
                removeLastNode();

            node = new Node(key, val);
            cacheMap.put((Integer) key, node);  //update map
            addToFront(node);
        }
    }

    /* Get the Element
            Check map if key is present
            if present
                - get the node from map using key
                - move to front
            else return null */

    public V get(K key){
        if(cacheMap.containsKey(key)){
            Node<K, V> node = cacheMap.get(key);
            removeNode(node);
            addToFront(node);
            return  node.val;
        }
        else
            return null;

    }

    void removeLastNode(){
        Node<K, V> nodeToRemove = tail.pre;
        removeNode(nodeToRemove);
        cacheMap.remove(nodeToRemove.key);
    }

    void  removeNode(Node temp){
        if(temp.next != null)
            temp.next.pre  = temp.pre;
        if(temp.pre != null)
            temp.pre.next = temp.next;
    }

    void addToFront(Node node){
        head.next.pre = node;
        node.next = head.next;
        head.next = node;
        node.pre = head;
    }

   /* public int getNode(int val){
        int temp;
        if (head == null) {
            System.out.println("No data present in the cache");
            return -1;
        }else{
            temp = head.val;
            head = head.next;
        }
       return temp;
    }*/

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3);
        cache.add(1, 10);
        cache.add(2, 20);

        System.out.println(cache.get(2));
        System.out.println(cache.head.next.val);

        System.out.println(cache.get(1));
        System.out.println(cache.head.next.val); //list head value changed because it was traversed most recent

        System.out.println(cache.get(3)); //output: null , no node with this key

        cache.add(3, 30);
        cache.add(4, 40);

        System.out.println(cache.get(2)); //output: null LRU node<2, 20> has been removed when cache size exceeded

        cache.add(2, 80);
        System.out.println(cache.get(2));
        System.out.println(cache.get(1)); //output: null LRU node<1,10> has been removed

        cache.add(2, 800);
        System.out.println(cache.get(2)); //value update successfully for key=2

    }
}


/*Complexity Analysis:

Time Complexity:
Adding a node: O(1)
Searching in the cache: O(1)

Auxiliary Space:
Linked list nodes: O(N)
Hashmap
Other variables: O(1)*/
