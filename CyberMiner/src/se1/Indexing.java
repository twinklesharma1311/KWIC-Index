/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author Sruthi
 * 
 * KWIK Web Search - This program will accept # of strings as input
 * then will perform circular rotation on each string and then sort all 
 * the different formats of input strings in alphabetical order.
 */

public class Indexing {
    public static ArrayList<String> rotated_Lines = new ArrayList();
    
    public static void main (String[]args)
    {
        Scanner keyboard = new Scanner(System.in);
   
        System.out.println("Number of input lines");
        int numLines = keyboard.nextInt();
        keyboard.nextLine();// becuase  nextIntnot read the last newline character of your input, and thus that newline is consumed in the next call to Scanner#nextLine
        String [] lines = new String[numLines];
        String temp;
        for(int i=0; i<numLines; i++)
        {
            System.out.println("Enter String" + (i+1));
             temp = keyboard.nextLine();
              lines[i]= temp;
        }
        
        //method call to recursively acquire different variations of input line
        for(int i = 0; i<lines.length; i++)
        {
            rotation(lines[i]);
        }
        Collections.sort(rotated_Lines);
        System.out.println("After Sort");
        System.out.println(rotated_Lines.toString());
        
    }
    
    public static void rotation(String s)
    {
        String[] input = s.split(" "); // to determine the # of words
        String temp = s;
        rotated_Lines.add(temp);
        for(int j=0; j<input.length-1;j++)
        {
            input = temp.split(" ");
            temp = "";
            for(int i=0; i<input.length -1;i++)
            {
                
                temp += input[i+1] + " ";
            }
            temp +=input[0];
            System.out.println("Temp "+ temp);
            rotated_Lines.add(temp);
        }
        System.out.println("Before Sort");
        System.out.println(rotated_Lines.toString());
    }
}
