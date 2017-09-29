package hashTable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import gui.LinesComponent;
import stringgenerator.RandomStringGenerator;

// SeparateChaining Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// void makeEmpty( )      --> Remove all items

/**
 * Separate chaining table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class SeparateChainingHashTable<AnyType>
{
    /**
     * Construct the hash table.
     */
    public SeparateChainingHashTable( )
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size approximate table size.
     */
    public SeparateChainingHashTable( int size )
    {
        theLists = new LinkedList[ nextPrime( size ) ];
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ] = new LinkedList<>( );
    }

    /**
     * Insert into the hash table. If the item is
     * already present, then do nothing.
     * @param x the item to insert.
     */
    public void insert( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        if( !whichList.contains( x ) )
        {
            whichList.add( x );

                // Rehash; see Section 5.5
            if( ++currentSize > theLists.length )
                rehash( );
        }
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     */
    public void remove( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        if( whichList.contains( x ) )
    {
        whichList.remove( x );
            currentSize--;
    }
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return true if x isnot found.
     */
    public boolean contains( AnyType x )
    {
        List<AnyType> whichList = theLists[ myhash( x ) ];
        return whichList.contains( x );
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty( )
    {
        for( int i = 0; i < theLists.length; i++ )
            theLists[ i ].clear( );
        currentSize = 0;    
    }

    /**
     * A hash routine for String objects.
     * @param key the String to hash.
     * @param tableSize the size of the hash table.
     * @return the hash value.
     */
    public static int hash( String key, int tableSize )
    {
        int hashVal = 0;

        for( int i = 0; i < key.length( ); i++ )
            hashVal = 37 * hashVal + key.charAt( i );

        hashVal %= tableSize;
        if( hashVal < 0 )
            hashVal += tableSize;

        return hashVal;
    }

    private void rehash( )
    {
        List<AnyType> [ ]  oldLists = theLists;

            // Create new double-sized, empty table
        theLists = new List[ nextPrime( 2 * theLists.length ) ];
        for( int j = 0; j < theLists.length; j++ )
            theLists[ j ] = new LinkedList<>( );

            // Copy table over
        currentSize = 0;
        for( List<AnyType> list : oldLists )
            for( AnyType item : list )
                insert( item );
    }

    private int myhash( AnyType x )
    {
        int hashVal = x.hashCode( );

        hashVal %= theLists.length;
        if( hashVal < 0 )
            hashVal += theLists.length;

        return hashVal;
    }
    
    private static final int DEFAULT_TABLE_SIZE = 101;

        /** The array of Lists. */
    private List<AnyType> [ ] theLists; 
    private int currentSize;

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
    
    public static long timeforinsert(SeparateChainingHashTable<? extends String> H, String[] stringstore)
    {
    	SeparateChainingHashTable<String> h_temp = (SeparateChainingHashTable<String>) H;
    	
    	long start_time;
    	start_time = System.nanoTime();
    	for (int i=0; i<stringstore.length; i++)
    	{
    		h_temp.insert(stringstore[i]);
    	}
    	
    	return System.nanoTime() - start_time;
    }

    public static long timeforremove(SeparateChainingHashTable<? extends String> H, String[] stringstore)
    {
    	SeparateChainingHashTable<String> h_temp = (SeparateChainingHashTable<String>) H;
    	
    	long start_time;
    	start_time = System.nanoTime();
    	for (int i=0; i<stringstore.length; i++)
    	{
    		h_temp.remove(stringstore[i]);
    	}
    	
    	return System.nanoTime() - start_time;
    }

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

   	  SeparateChainingHashTable<String> H = new SeparateChainingHashTable<>();
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
