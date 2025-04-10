# Binomial Heap in Python

This is a full implementation of a Binomial Heap in Python, built without using any built-in data structure libraries. The implementation was done for a university assignment and supports common heap operations and performance tracking.

## Files Included
- `BinomialHeap.py`: Core implementation of the binomial heap structure.
- `BinomialNode.py`: Represents the nodes in the heap.
- `info.txt`: Author information (names and student IDs).
- `BinomialHeap_*.pdf`: Documentation explaining the classes, functions, and time complexities.

## Supported Operations
- `insert(k)`: Inserts a node with key `k` into the heap.
- `find_min()`: Returns the smallest key in the heap.
- `delete_min()`: Removes the node with the smallest key.
- `meld(heap2)`: Combines two binomial heaps.
- `decrease_key(x, delta)`: Decreases the key of node `x` by `delta`.
- `delete(x)`: Deletes a given node `x`.
- `get_size()`: Returns the total number of nodes.
- `counters_rep()`: Returns the number of trees of each degree.

## Notes
- The code avoids using Python’s built-in data structures.
- All operations follow their theoretical time complexity.
- The heap structure is made of a forest of binomial trees, linked by sibling pointers.

## Running
This code is meant to be imported into a test or driver script. It doesn't include `main()` or input/output.

## Testing
Includes both manual and automated tests (external), and a detailed PDF with experimental runtime measurements and complexity analysis.

---

Created for the Data Structures course, Tel Aviv University.

