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
//Cuckoo Hash table class
//
//CONSTRUCTION: a hashing function family and
//            an approximate initial size or default of 101
//
//******************PUBLIC OPERATIONS*********************
//bool insert( x )       --> Insert x
//bool remove( x )       --> Remove x
//bool contains( x )     --> Return true if x is present
//void makeEmpty( )      --> Remove all items
//int  size( )           --> Return number of items


/**
* Cuckoo hash table implementation of hash tables.
* @author Mark Allen Weiss
*/
public class CuckooHashTable<AnyType>
{
 /**
  * Construct the hash table.
  * @param hf the hash family
  */
 public CuckooHashTable( HashFamily<? super AnyType> hf )
 {
     this( hf, DEFAULT_TABLE_SIZE );
 }

 /**
  * Construct the hash table.
  * @param hf the hash family
  * @param size the approximate initial size.
  */
 public CuckooHashTable( HashFamily<? super AnyType> hf, int size )
 {
     allocateArray( nextPrime( size ) );
     doClear( );
     hashFunctions = hf;
     numHashFunctions = hf.getNumberOfFunctions( );
 }

 private Random r = new Random( );
 
 private static final double MAX_LOAD = 0.40;
 private static final int ALLOWED_REHASHES = 1;
 
 private int rehashes = 0;
   
 private boolean insertHelper1( AnyType x )
 {
     final int COUNT_LIMIT = 100;
     
     while( true )
     {
         int lastPos = -1;
         int pos;

         for( int count = 0; count < COUNT_LIMIT; count++ )
         {
             for( int i = 0; i < numHashFunctions; i++ )
             {
                 pos = myhash( x, i );

                 if( array[ pos ] == null )
                 {
                     array[ pos ] = x;
                     currentSize++;
                     return true;
                 }
             }

             // none of the spots are available. Kick out a random one
             int i = 0;
             do
             {
                 pos = myhash( x, r.nextInt( numHashFunctions ) );
             } while( pos == lastPos && i++ < 5 );

             AnyType tmp = array[ lastPos = pos ];
             array[ pos ] = x;
             x = tmp;
         }

         if( ++rehashes > ALLOWED_REHASHES )
         {
             expand( );      // Make the table bigger
             rehashes = 0;
         }
         else
             rehash( );
     }
 }
 
 private boolean insertHelper2( AnyType x )
 {
     final int COUNT_LIMIT = 100;
     
     while( true )
     {
         for( int count = 0; count < COUNT_LIMIT; count++ )
         {
             int pos = myhash( x, count % numHashFunctions );

             AnyType tmp = array[ pos ];
             array[ pos ] = x;

             if( tmp == null )
                 return true;
             else
                 x = tmp;
         }
     
         if( ++rehashes > ALLOWED_REHASHES )
         {
             expand( );      // Make the table bigger
             rehashes = 0;
         }
         else
             rehash( );
     }
 }
 
 /**
  * Insert into the hash table. If the item is
  * already present, return false.
  * @param x the item to insert.
  */
 public boolean insert( AnyType x )
 {         
     if( contains( x ) )
         return false;
     
     if( currentSize >= array.length * MAX_LOAD )
         expand( );
     
     return insertHelper1( x );
 }

 private int myhash( AnyType x, int which )
 {
     int hashVal = hashFunctions.hash( x, which );
     
     hashVal %= array.length;
     if( hashVal < 0 )
         hashVal += array.length;
     
     return hashVal;
 }
     
 private void expand( )
 {
     rehash( (int) ( array.length / MAX_LOAD ) );
 }
 
 private void rehash( )
 {
     //System.out.println( "NEW HASH FUNCTIONS " + array.length );
     hashFunctions.generateNewFunctions( );
     rehash( array.length );
 }
 
 private void rehash( int newLength )
 {
     //System.out.println( "REHASH: " + array.length + " " + newLength + " " + currentSize );
     AnyType [ ] oldArray = array;    // Create a new double-sized, empty table
         
     allocateArray( nextPrime( newLength ) );
     
     currentSize = 0;
     
         // Copy table over
     for( AnyType str : oldArray )
         if( str != null )
             insert( str );
 }

 
 /**
  * Gets the size of the table.
  * @return number of items in the hash table.
  */
 public int size( )
 {
     return currentSize;
 }
 
 /**
  * Gets the length (potential capacity) of the table.
  * @return length of the internal array in the hash table.
  */
 public int capacity( )
 {
     return array.length;
 }
 
 /**
  * Method that searches all hash function places.
  * @param x the item to search for.
  * @return the position where the search terminates, or -1 if not found.
  */
 private int findPos( AnyType x )
 {
     for( int i = 0; i < numHashFunctions; i++ )
     {
         int pos = myhash( x, i );
         if( array[ pos ] != null && array[ pos ].equals( x ) )
             return pos;
     }
     
     return -1;
 }

 /**
  * Remove from the hash table.
  * @param x the item to remove.
  * @return true if item was found and removed
  */
 public boolean remove( AnyType x )
 {
     int pos = findPos( x );
     
     if( pos != -1 )
     {
         array[ pos ] = null;
         currentSize--;
     }
     
     return pos != -1;
 }

 /**
  * Find an item in the hash table.
  * @param x the item to search for.
  * @return the matching item.
  */
 public boolean contains( AnyType x )
 {
     return findPos( x ) != -1;
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
     currentSize = 0;
     for( int i = 0; i < array.length; i++ )
         array[ i ] = null;
 }
 

 
 private static final int DEFAULT_TABLE_SIZE = 101;

 private final HashFamily<? super AnyType> hashFunctions;
 private final int numHashFunctions;
 private AnyType [ ] array; // The array of elements
 private int currentSize;              // The number of occupied cells

 /**
  * Internal method to allocate array.
  * @param arraySize the size of the array.
  */
 private void allocateArray( int arraySize )
 {
     array = (AnyType[]) new Object[ arraySize ];
 }

 /**
  * Internal method to find a prime number at least as large as n.
  * @param n the starting number (must be positive).
  * @return a prime number larger than or equal to n.
  */
 protected static int nextPrime( int n )
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

 
public static long timeforinsert(CuckooHashTable<? extends String> H, String[] stringstore)
{
	CuckooHashTable<String> h_temp = (CuckooHashTable<String>) H;
	
	long start_time;
	start_time = System.nanoTime();
	for (int i=0; i<stringstore.length; i++)
	{
		h_temp.insert(stringstore[i]);
	}
	
	return System.nanoTime() - start_time;
}

public static long timeforremove(CuckooHashTable<? extends String> H, String[] stringstore)
{
	CuckooHashTable<String> h_temp = (CuckooHashTable<String>) H;
	
	long start_time;
	start_time = System.nanoTime();
	for (int i=0; i<stringstore.length; i++)
	{
		h_temp.remove(stringstore[i]);
	}
	
	return System.nanoTime() - start_time;
}
//GUi function
//public static class LinesComponent extends JComponent
//{
//	 /**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	public class Line
//	 {
//		 final int x1;
//		 final int y1;
//		 final int x2;
//		 final int y2;
//		 final Color color;
//	 
//		 public Line(int x1, int y1, int x2, int y2, Color color) 
//		 {
//	        this.x1 = x1;
//	        this.y1 = y1;
//	        this.x2 = x2;
//	        this.y2 = y2;
//	        this.color = color;
//		 }     
//	 } 
//	 public LinkedList<Line> lines = new LinkedList<Line>();
//
//
//
//public  void addLine(int x1, int x2, int x3, int x4, Color color) {
//	    lines.add(new LinesComponent.Line(x1,x2,x3,x4, color));        
//	    repaint();
//	}
//
//@Override
//protected void paintComponent(Graphics g) {
//    super.paintComponent(g);
//    for (Line line : lines) {
//        g.setColor(line.color);
//        g.drawLine(line.x1, line.y1, line.x2, line.y2);
//    }
//}
//
//}



 // Simple main
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

	  CuckooHashTable<String> H = new CuckooHashTable<>( new StringHashFamily( 3 ), (int) Math.pow(2, 20));
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
