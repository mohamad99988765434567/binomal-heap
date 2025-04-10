//username1 - mahajna3
//id1      - 324887488
//name1    - Mahajna Mohamad
//username2 - amanaessa
//id2      - 213127905
//name2    - Amana Essa

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap {
    public int size;
    public HeapNode last;
    public HeapNode min;
    public int numOfLinks = 0 ;
    public int sumOfRanksDeleted = 0 ;

    public BinomialHeap() { // Constructor, O(1)
        this.size =0 ;
        this.last=null;
        this.min=null;
    }



    /**
     * Inserts a new HeapItem into the heap with the given key and information.
     *
     */
    public HeapItem insert(int key, String info) { //O(log(n))

        HeapNode node = new HeapNode(key, info);
        BinomialHeap heap2 = Insert_Helper(node);
        // Meld the new heap (heap2) with the current heap
        this.meld(heap2);
        return node.item;
    }


    /**
     * Delete the minimal item from the binomial heap.
     *
     */
    public void deleteMin() { //O(log(n)) time complexity
        // If the heap is empty, do nothing
        if (this.size == 0) {
            return;
        }
        // If the heap size is 1, then after delete min the heap is empty
        if (this.size == 1) {
            this.size = 0;
            this.last = new HeapNode();
            this.min = new HeapNode();
        } else if (this.min.child != null) {
            // Get the child of the current minimum node
            HeapNode tmp = this.min.child;
            tmp.parent = new HeapNode();
            HeapNode minTmp = tmp;
            HeapNode iter = tmp.next;
            while (iter != tmp) {
                iter.parent = new HeapNode(); // Disconnect the child from its parent
                if (iter.item.key <= minTmp.item.key) {
                    minTmp = iter;
                }
                iter = iter.next;
            }
            // Create a new heap to hold the children of the deleted node
            BinomialHeap heap2 = new BinomialHeap();
            heap2.last = tmp;
            heap2.min = minTmp;

            // Calculate the size of the new heap
            int k = this.min.rank;
            heap2.size = (int) Math.pow(2, k) - 1;

            // If the heap used to be one binomial tree, then heap 2 is the new heap
            if (this.numTrees() == 1) {
                this.min = heap2.min;
                this.size = heap2.size;
                this.last = heap2.last;
            } else {
                // Remove the minimum node from the current heap
                this.delete_Min_helper();
                // Merge the two heaps together
                this.meld(heap2);
            }
        }
        else this.delete_Min_helper();


    }



    /**
     * Return the minimal HeapItem
     */
    public HeapItem findMin() { //O(1)
        if (this.empty()) return null;
        return this.min.item;
    }


    /**
     * Decreases the key of the given HeapItem by the specified difference and adjusts the heap to maintain its properties.
     *
     *
     *
     */
    public void decreaseKey(HeapItem item, int diff) { //O(log(n))
        HeapNode node = item.node;
        item.key -= diff; // Decrease the key by the specified difference

        // Fix the heap property by swapping the node with its parent if necessary
        while (node.parent != null && node.item.key <= node.parent.item.key) {
            // Swap the HeapItems of the node and its parent
            HeapItem temp = node.parent.item;
            node.parent.item = node.item;
            node.item = temp;
            node = node.parent;
        }

        // Update the minimum node if necessary
        if (node.item.key < this.min.item.key) {
            this.min = node;
        }
    }


    /**
     * Deletes the specified item from the heap.
     *
     *
     */
    public void delete(HeapItem item) { //O(log(n))
        if(item==null) return;
        //since keys are non -negative so decreaseKey makes item.key = -2 always so its the min
        this.decreaseKey(item, item.key+2);
        this.deleteMin();
    }


    /**
     * Melds the current heap with another heap.
     *
     *
     */
    public void meld(BinomialHeap heap2) { //O(log(n))
        // If heap2 empty no merging is needed
        if (heap2.empty()) {
            return;
        }
        // If the current heap is empty, assign heap2 to the current heap
        else if (this.empty()) {
            this.min = heap2.min;
            this.last = heap2.last;
            this.size = heap2.size;
            return;
        }

        else {
            // Compare the minimum items of both heaps and update the minimum item of the current heap if needed
            if (heap2.min.item.key < this.min.item.key) {
                this.min = heap2.min;
            }
            // Update the size of the current heap by adding the size of heap2
            this.size = this.size + heap2.size;
            // Initialize pointers for iterating through the heaps
            HeapNode counter1 = this.last.next;
            HeapNode counter2 = heap2.last.next;
            HeapNode tailHeapOne = this.last;
            HeapNode tailHeapTwo = heap2.last;

            // Flags to track the completion of iteration for each heap
            int this_numTrees  = this.numTrees();
            int heap2_numTrees = heap2.numTrees();


            // Create a dummy node for merging
            HeapNode point = new HeapNode(-1, "");
            HeapNode setup = point;
            HeapNode curr = new HeapNode();

            // Iterate through the heaps until all nodes are merged
            while ((heap2_numTrees != 0 && this_numTrees != 0) || curr.rank != -1) {
                if (curr.rank != -1) {
                    if (counter1.rank == curr.rank && counter2.rank == curr.rank && heap2_numTrees != 0 && this_numTrees != 0) {
                        HeapNode point1 = counter1.next;
                        HeapNode point2 = counter2.next;
                        counter1.next = null;
                        counter2.next = null;
                        // Append the current result node to the linked list and move to the next node
                        setup.next = curr;
                        setup = setup.next;
                        curr = new HeapNode(); // Reset the current result node
                        // Link the nodes with the same rank and update the pointers and flags
                        curr = this.link(counter1, counter2);
                        counter1 = point1; // Move to the next node in the first heap
                        this_numTrees -= 1;
                        counter2 = point2; // Move to the next node in the second heap
                        heap2_numTrees -= 1;
                    } else if (counter1.rank == curr.rank && this_numTrees  != 0) {
                        HeapNode point1 = counter1.next;
                        counter1.next = null;
                        curr = this.link(counter1, curr);
                        counter1 = point1;
                        this_numTrees -= 1;
                    } else if (counter2.rank == curr.rank && heap2_numTrees != 0) {
                        HeapNode point2 = counter2.next;
                        counter2.next = null;
                        curr = this.link(counter2, curr);
                        counter2 = point2;
                        heap2_numTrees -= 1;
                    } else {
                        // Append the current result node to the linked list and move to the next node
                        setup.next = curr;
                        setup = setup.next;
                        curr = new HeapNode(); // Reset the current result node
                    }
                } else {
                    // Merge nodes with the same rank from both heaps
                    if (counter1.rank == counter2.rank) {
                        HeapNode point1 = counter1.next;
                        HeapNode point2 = counter2.next;
                        counter1.next = null;
                        counter2.next = null;
                        // Link the nodes with the same rank and update the pointers and flags
                        curr = this.link(counter1, counter2);
                        counter1 = point1; // Move to the next node in the first heap
                        this_numTrees --;
                        counter2 = point2; // Move to the next node in the second heap
                        heap2_numTrees --;
                    } else if (counter1.rank < counter2.rank) {
                        HeapNode point1 = counter1.next;
                        counter1.next = null;
                        setup.next = counter1;
                        setup = setup.next;
                        counter1 = point1; // Move to the next node in the first heap
                        this_numTrees --;
                    } else {
                        HeapNode point2 = counter2.next;
                        counter2.next = null;
                        setup.next = counter2;
                        setup = setup.next;
                        counter2 = point2; // Move to the next node in the first heap
                        heap2_numTrees --;
                    }
                }
            }
            if (this_numTrees != 0) {
                setup.next = counter1;
                tailHeapOne.next = point.next;
                point.next = null;
                this.last = tailHeapOne;
            } else if (heap2_numTrees != 0) {
                setup.next = counter2;
                tailHeapTwo.next = point.next;
                point.next = null;
                this.last = tailHeapTwo;
            } else {
                // Removing the dummy node
                setup.next = point.next;
                point.next = null;
                this.last = setup;
            }
        }
    }

    /**
     *
     * Return the number of elements in the heap
     *
     */
    public int size() { //O(1) time complexity
        return this.size;
    }

    /**
     *
     * The method returns true if and only if the heap is empty.
     *
     *
     */
    public boolean empty() { //O(1) time complexity
        return this.size == 0;
    }

    /**
     *
     * Return the number of trees in the heap.
     *
     */
    public int numTrees()//O(logn)
    {
        if(empty()) return 0;
        HeapNode iter = last;
        int count = 1;
        while(iter.next != last) {
            iter = iter.next;
            count++;
        }
        return count;
    }
    /** helper functions we used
     *
     */
    /**
     * Links two heap nodes together and returns the parent node.
     */
    public HeapNode link(HeapNode x, HeapNode y) { //O(1)
        // make sure x has smaller key than y
        if (x.item.key > y.item.key) {
            return link(y, x);
        }
        // Link y as a child of x
        if (x.child == null) {
            y.next = y;
        } else {
            y.next = x.child.next;
            x.child.next = y;
        }
        // Updates
        y.parent = x;
        x.child = y;
        x.rank ++;
        return x;
    }
    /**
     *
     * @ pre minimum node  it has been identified.
     * deletes the min node and, updates the minimum node and adjusts the size of the heap .
     *
     */
    private void delete_Min_helper() { //O(log(n))
        // Get the current minimum node
        HeapNode Prev_Min = this.min;
        // Find the next node in the circular linked list
        HeapNode nextPoint = Prev_Min.next;
        // Initialize variables for traversal
        HeapNode current = nextPoint;
        HeapNode newMin = nextPoint;
        HeapNode prev = nextPoint;
        // Traverse the linked list to find the new minimum node
        while (current != Prev_Min) {
            if (current.item.key <= newMin.item.key) {
                newMin = current;
            }
            if (current.next == Prev_Min) {
                prev = current;
            }
            current = current.next;
        }

        // Removing the old minimum node from heap
        prev.next = nextPoint;

        // Update the last node if necessary
        if (this.last == Prev_Min) {
            this.last = prev;
        }

        // Disconnect the old minimum node
        Prev_Min.next = null;
        // Update the minimum node and heap size
        this.min = newMin;
        this.size -= (int) Math.pow(2, Prev_Min.rank);
    }
    /**
     *
     */
    public BinomialHeap Insert_Helper(HeapNode node) { //O(1)
        BinomialHeap heap2 = new BinomialHeap();
        heap2.size = 1;
        heap2.last = node;
        heap2.min = node;
        node.next = node;
        return heap2;
    }

    /**
     * Class implementing a node in a Binomial Heap.
     *
     */
    public class HeapNode{ //O(1)
        public HeapItem item;
        public HeapNode child;
        public HeapNode next;
        public HeapNode parent;
        public int rank;


        /**
         * Constructor to create a HeapNode with the provided key and info.
         *
         */
        public HeapNode(int key, String info) {
            this.item = new HeapItem(key, info, this);
            this.rank = 0;
            this.next = this; // Set the next pointer to itself, as it's a circular linked list by default
        }


        /**
         * Default constructor for HeapNode.
         * Initializes the rank to -1, indicating it's not part of any heap.
         */
        public HeapNode() {
            this.rank = -1; //uninitialized
        }
    }


    /**
     * Class implementing an item in a Binomial Heap.
     *
     */
    public class HeapItem{ //O(1)
        public HeapNode node;
        public int key;
        public String info;

        /**
         * Constructor for creating a HeapItem object with the provided key, info, and node.
         *
         */
        public HeapItem(int key, String info, HeapNode node){
            this.key = key;
            this.info = info;
            this.node = node;
        }

        /**
         * Default constructor for HeapItem.
         * Initializes the key to -1, indicating an uninitialized state.
         */
        public HeapItem(){
            this.key = -1;
        }
    }

}