package firstQuestion;

import java.util.Arrays;
import java.util.Scanner;

public class ArraySort {

	public static void main(String[] args) {
		// Getting input from the user
		System.out.println("Please enter the size of the array:");
		Scanner input = new Scanner(System.in);
		int arraySize = input.nextInt();
		System.out.println("Please enter those " + arraySize + " numbers");
		
        // Create a new array with user entered size
        int[] array = new int[arraySize];
        
        // Get the value of each element in the array
        for(int i = 0; i < array.length; i++)
            array[i] = input.nextInt();
        
        System.out.println("Input Array : ");
        for(int i = 0; i < array.length; i++)
        	System.out.print(array[i] + " "); 
        System.out.println("");
        
        //Sorting the input array
        Arrays.sort(array);
                
        //Counting the number of 1's in the integer and storing it in a separate array
        //Using one Temp Array for counting the 1's in Bin form
        int[] tempArray = new int[arraySize];
        for(int i = 0; i < arraySize; i++)
        	tempArray[i] = array[i];
        int[] count = new int[arraySize];
        for(int i = 0; i < tempArray.length; i++){
//        	System.out.println("Binary of " + tempArray[i] + ":");
//        	System.out.println(Integer.toBinaryString(array[i]));
        	while(tempArray[i] != 0){
        		count[i] += tempArray[i] & 1;
        		tempArray[i] >>= 1;
        	}

        }
//        System.out.println("Count Array: " + " ");
//        for(int i =0;i<arraySize;i++){
//      	  System.out.print(count[i] + " ");
//        }
//        System.out.println("");
        
        //Sorting the count and input array
        for(int i =1;i<arraySize;i++){
        	int tempCount = count[i];
        	int element = array[i];
        	int j = i -1 ;
        	
        	while(j >= 0 && count[j] > tempCount){
        		count[j+1] = count[j];
        		array[j+1] = array[j];
        		j -= 1;
        	}
        	count[j+1] = tempCount;
        	array[j+1] = element;
        }
      
      System.out.println("Output Array: ");
      for(int i =0;i<arraySize;i++){
    	  System.out.print(array[i] + " ");
      }
        
	}

}
