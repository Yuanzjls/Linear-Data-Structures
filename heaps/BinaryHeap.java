package heaps;

import java.util.Random;

import heaps.BinaryHeap;


//BinaryHeap class
//
//CONSTRUCTION: with optional capacity (that defaults to 100)
//          or an array containing initial items
//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//Comparable deleteMin( )--> Return and remove smallest item
//Comparable findMin( )  --> Return smallest item
//boolean isEmpty( )     --> Return true if empty; else false
//void makeEmpty( )      --> Remove all items
//******************ERRORS********************************
//Throws UnderflowException as appropriate

/**
* Implements a binary heap.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
*/

public class BinaryHeap<AnyType extends Comparable<? super AnyType>>
{
/**
* Construct the binary heap.
*/
public BinaryHeap( )
{
   this( DEFAULT_CAPACITY );
}

/**
* Construct the binary heap.
* @param capacity the capacity of the binary heap.
*/
public BinaryHeap( int capacity )
{
   currentSize = 0;
   array = (AnyType[]) new Comparable[ capacity + 1 ];
}

/**
* Construct the binary heap given an array of items.
*/
public BinaryHeap( AnyType [ ] items )
{
       currentSize = items.length;
       array = (AnyType[]) new Comparable[ ( currentSize + 2 ) * 11 / 10 ];

       int i = 1;
       for( AnyType item : items )
           array[ i++ ] = item;
       buildHeap( );
}

/**
* Insert into the priority queue, maintaining heap order.
* Duplicates are allowed.
* @param x the item to insert.
*/
public void insert( AnyType x )
{
   if( currentSize == array.length - 1 )
       enlargeArray( array.length * 2 + 1 );

       // Percolate up
   int hole = ++currentSize;
   for( array[ 0 ] = x; x.compareTo( array[ hole / 2 ] ) < 0; hole /= 2 )
       array[ hole ] = array[ hole / 2 ];
   array[ hole ] = x;
}


private void enlargeArray( int newSize )
{
       AnyType [] old = array;
       array = (AnyType []) new Comparable[ newSize ];
       for( int i = 0; i < old.length; i++ )
           array[ i ] = old[ i ];        
}

/**
* Find the smallest item in the priority queue.
* @return the smallest item, or throw an UnderflowException if empty.
*/
public AnyType findMin( )
{
   if( isEmpty( ) )
       //throw new UnderflowException( );
  	 return null;
   return array[ 1 ];
}

/**
* Remove the smallest item from the priority queue.
* @return the smallest item, or throw an UnderflowException if empty.
*/
public AnyType deleteMin( )
{
   if( isEmpty( ) )
       //throw new UnderflowException( );
  	 return null;

   AnyType minItem = findMin( );
   array[ 1 ] = array[ currentSize-- ];
   percolateDown( 1 );

   return minItem;
}

/**
* Establish heap order property from an arbitrary
* arrangement of items. Runs in linear time.
*/
private void buildHeap( )
{
   for( int i = currentSize / 2; i > 0; i-- )
       percolateDown( i );
}

/**
* Test if the priority queue is logically empty.
* @return true if empty, false otherwise.
*/
public boolean isEmpty( )
{
   return currentSize == 0;
}

/**
* Make the priority queue logically empty.
*/
public void makeEmpty( )
{
   currentSize = 0;
}

private static final int DEFAULT_CAPACITY = 10;

private int currentSize;      // Number of elements in heap
private AnyType [ ] array; // The heap array

/**
* Internal method to percolate down in the heap.
* @param hole the index at which the percolate begins.
*/
private void percolateDown( int hole )
{
   int child;
   AnyType tmp = array[ hole ];

   for( ; hole * 2 <= currentSize; hole = child )
   {
       child = hole * 2;
       if( child != currentSize &&
               array[ child + 1 ].compareTo( array[ child ] ) < 0 )
           child++;
       if( array[ child ].compareTo( tmp ) < 0 )
           array[ hole ] = array[ child ];
       else
           break;
   }
   array[ hole ] = tmp;
}

   // Test program
public static void main( String [ ] args )
{
   int numItems = 10000;
   Random rnd = new Random();
   rnd.setSeed(100);
   
  
   
   BinaryHeap<Integer> h = new BinaryHeap<>( );
   long startTime;
   
   for (int i=0; i<10; i++)
   {
	   h.makeEmpty();
	   startTime = System.nanoTime();
	   for (int j=0; j < numItems; j++)
	   {
		   //h.insert(rnd.nextInt());
		   h.insert(j);
	   }
	   //System.out.println( "The time for order number insert is " + (System.nanoTime() - startTime)); 
   }
   long totaltime = 0;
   long totaltime2 = 0;
   h.makeEmpty();
   for (int i=0; i<numItems; i++)
   {
	   startTime = System.nanoTime();
	   h.insert(i);
	   totaltime += System.nanoTime() - startTime;
   }
   System.out.println( "The time for order number insert is " + totaltime / 30);
   System.out.println( "The time for order number delete is " + totaltime2 / 30); 
   totaltime = 0;
   totaltime2 = 0;
   for (int i=0; i<30; i++)
   {
	   h.makeEmpty();
	   startTime = System.nanoTime();
	   for (int j=0; j < numItems; j++)
	   {
		   h.insert(rnd.nextInt());
	   }
	   totaltime += System.nanoTime() - startTime;
	   startTime = System.nanoTime();
	   while(!h.isEmpty())
	   {
		   h.deleteMin();
	   }
	   
	   totaltime2 += System.nanoTime() - startTime;
   }
   System.out.println( "The time for random number insert is " + totaltime / 30); 
   System.out.println( "The time for random number delete is " + totaltime2 / 30); 

}
}
