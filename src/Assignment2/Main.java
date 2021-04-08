package Assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if (args.length == 2) {
            File inFile=new File(args[0]);
            File outFile = new File ( args[1] );
            PrintWriter out = null;
            try {
                 out= new PrintWriter ( outFile );
            }
            catch (FileNotFoundException e) {
                System.out.println ( "No such output file found" );
            }
            try {
                Scanner in=new Scanner (inFile);
                SimulationManager simulationManager = new SimulationManager ( in,out );
                Thread t=new Thread ( simulationManager );
                t.start ();
            }
            catch (FileNotFoundException e) {
                System.out.println ( "No such input file found" );
            }
        }
        else
            System.out.println ( " The number of arguments is wrong" );

    }
}
