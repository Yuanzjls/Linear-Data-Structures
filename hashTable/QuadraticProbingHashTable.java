package hashTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JComponent;
import javax.swing.JFrame;


import stringgenerator.*;
import gui.*;

//QuadraticProbing Hash table class
//
//CONSTRUCTION: an approximate initial size or default of 101
//
//******************PUBLIC OPERATIONS*********************
//bool insert( x )       --> Insert x
//bool remove( x )       --> Remove x
//bool contains( x )     --> Return true if x is present
//void makeEmpty( )      --> Remove all items


/**
* Probing table implementation of hash tables.
* Note that all "matching" is based on the equals method.
* @author Mark Allen Weiss
*/
public class QuadraticProbingHashTable<AnyType>
{
 /**
  * Construct the hash table.
  */
 public QuadraticProbingHashTable( )
 {
     this( DEFAULT_TABLE_SIZE );
 }

 /**
  * Construct the hash table.
  * @param size the approximate initial size.
  */
 public QuadraticProbingHashTable( int size )
 {
     allocateArray( size );
     doClear( );
 }

 /**
  * Insert into the hash table. If the item is
  * already present, do nothing.
  * @param x the item to insert.
  */
 public boolean insert( AnyType x )
 {
         // Insert x as active
     int currentPos = findPos( x );
     if( isActive( currentPos ) )
         return false;

     array[ currentPos ] = new HashEntry<>( x, true );
     theSize++;
     
         // Rehash; see Section 5.5
     if( ++occupied > array.length / 2 )
         rehash( );
     
     return true;
 }

 /**
  * Expand the hash table.
  */
 private void rehash( )
 {
     HashEntry<AnyType> [ ] oldArray = array;

         // Create a new double-sized, empty table
     allocateArray( 2 * oldArray.length );
     occupied = 0;
     theSize = 0;

         // Copy table over
     for( HashEntry<AnyType> entry : oldArray )
         if( entry != null && entry.isActive )
             insert( entry.element );
 }

 /**
  * Method that performs quadratic probing resolution.
  * @param x the item to search for.
  * @return the position where the search terminates.
  */
 private int findPos( AnyType x )
 {
     int offset = 1;
     int currentPos = myhash( x );
     
     while( array[ currentPos ] != null &&
             !array[ currentPos ].element.equals( x ) )
     {
         currentPos += offset;  // Compute ith probe
         offset += 2;
         if( currentPos >= array.length )
             currentPos -= array.length;
     }
     
     return currentPos;
 }

 /**
  * Remove from the hash table.
  * @param x the item to remove.
  * @return true if item removed
  */
 public boolean remove( AnyType x )
 {
     int currentPos = findPos( x );
     if( isActive( currentPos ) )
     {
         array[ currentPos ].isActive = false;
         theSize--;
         return true;
     }
     else
         return false;
 }
 
 /**
  * Get current size.
  * @return the size.
  */
 public int size( )
 {
     return theSize;
 }
 
 /**
  * Get length of internal table.
  * @return the size.
  */
 public int capacity( )
 {
     return array.length;
 }

 /**
  * Find an item in the hash table.
  * @param x the item to search for.
  * @return the matching item.
  */
 public boolean contains( AnyType x )
 {
     int currentPos = findPos( x );
     return isActive( currentPos );
 }

 /**
  * Return true if currentPos exists and is active.
  * @param currentPos the result of a call to findPos.
  * @return true if currentPos is active.
  */
 private boolean isActive( int currentPos )
 {
     return array[ currentPos ] != null && array[ currentPos ].isActive;
 }

 /**
  * Make the hash table logically empty.
  */
 public void makeEmpty( )
 {
     doClear( );
 }

 private void doClear( )
 {
     occupied = 0;
     for( int i = 0; i < array.length; i++ )
         array[ i ] = null;
 }
 
 private int myhash( AnyType x )
 {
     int hashVal = x.hashCode( );

     hashVal %= array.length;
     if( hashVal < 0 )
         hashVal += array.length;

     return hashVal;
 }
 
 private static class HashEntry<AnyType>
 {
     public AnyType  element;   // the element
     public boolean isActive;  // false if marked deleted

     public HashEntry( AnyType e )
     {
         this( e, true );
     }

     public HashEntry( AnyType e, boolean i )
     {
         element  = e;
         isActive = i;
     }
 }

 private static final int DEFAULT_TABLE_SIZE = 101;

 private HashEntry<AnyType> [ ] array; // The array of elements
 private int occupied;                 // The number of occupied cells
 private int theSize;                  // Current size

 /**
  * Internal method to allocate array.
  * @param arraySize the size of the array.
  */
 private void allocateArray( int arraySize )
 {
     array = new HashEntry[ nextPrime( arraySize ) ];
 }

 /**
  * Internal method to find a prime number at least as large as n.
  * @param n the starting number (must be positive).
  * @return a prime number larger than or equal to n.
  */
 private static int nextPrime( int n )
 {
     if( n % 2 == 0 )
         n++;

     for( ; !isPrime( n ); n += 2 )
         ;

     return n;
 }

 /**
  * Internal method to test if a number is prime.
  * Not an efficient algorithm.
  * @param n the number to test.
  * @return the result of the test.
  */
 private static boolean isPrime( int n )
 {
     if( n == 2 || n == 3 )
         return true;

     if( n == 1 || n % 2 == 0 )
         return false;

     for( int i = 3; i * i <= n; i += 2 )
         if( n % i == 0 )
             return false;

     return true;
 }

 public static long timeforinsert(QuadraticProbingHashTable<? extends String> H, String[] stringstore)
 {
 	QuadraticProbingHashTable<String> h_temp = (QuadraticProbingHashTable<String>) H;
 	
 	long start_time;
 	start_time = System.nanoTime();
 	for (int i=0; i<stringstore.length; i++)
 	{
 		h_temp.insert(stringstore[i]);
 	}
 	
 	return System.nanoTime() - start_time;
 }

 public static long timeforremove(QuadraticProbingHashTable<? extends String> H, String[] stringstore)
 {
 	QuadraticProbingHashTable<String> h_temp = (QuadraticProbingHashTable<String>) H;
 	
 	long start_time;
 	start_time = System.nanoTime();
 	for (int i=0; i<stringstore.length; i++)
 	{
 		h_temp.remove(stringstore[i]);
 	}
 	
 	return System.nanoTime() - start_time;
 }
 
//Simple main
public static void main( String [ ] args )
{
	 JFrame testFrame = new JFrame();
	 testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 LinesComponent comp = new LinesComponent();
	 comp.setPreferredSize(new Dimension(1000, 800));
	 testFrame.getContentPane().add(comp, BorderLayout.CENTER);
	 
   testFrame.pack();
   testFrame.setVisible(true);
	 
	 
	 
	 final int TIMES = 10;
	 
	  System.out.println( "Fill in the table..." );

	  QuadraticProbingHashTable<String> H = new QuadraticProbingHashTable<>();
	  RandomStringGenerator rng = new RandomStringGenerator();
	  
	  String string_temp[] = new String[2];
	  string_temp[0] = rng.RandomString();
	  string_temp[1] = rng.RandomString();
	  timeforinsert(H, string_temp);
	  timeforremove(H, string_temp);
	  long datax1[] = new long[14], datax2[] = new long[14], max1=0, max2=0;
	  datax1[0] = 0;
	  datax2[0] = 0;
	 for (int i=0; i<13; i++)
	 {

		 int range = (int) Math.pow(2, i);      
		 String stringstore[] = new String[range];		 
		 long total1=0, total2=0;
		 for (int h=0; h<TIMES; h++)
		 {			 
			 for( int j=0; j < range; j++)
			 {
				 stringstore[j]=rng.RandomString(2);	    	 
			 }	     
			 total1 += timeforinsert(H, stringstore);  	     
			 total2 += timeforremove(H, stringstore);
		 }
	    System.out.println( "The time for " + range + " insert cost is :" + total1/TIMES);
	    datax1[i+1] = total1/TIMES;
	    if (datax1[i+1] > max1)
	    {
	    	max1 = datax1[i+1];
	    }
	    System.out.println( "The time for " + range + " remove cost is :" + total2/TIMES);
	    datax2[i+1] = total2/TIMES;
	    if (datax2[i+1] > max2)
	    {
	    	max2 = datax2[i+1];
	    }
	}
	for (int i=0; i<13; i++)
	{
		comp.addLine(i*50, (int)(400-datax1[i]*350/max1), (i+1)*50, (int)(400-datax1[i+1]*350/max1), Color.black);
		comp.addLine(i*50, (int)(800-datax2[i]*350/max2), (i+1)*50, (int)(800-datax2[i+1]*350/max2), Color.black);
	}
	System.out.println( "Finished. ");
}

}

