package de.ahahn94;

import java.time.Duration;
import java.time.Instant;

/**
 * Main class.
 */
public class Main {

    /**
     * Main program
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        /*
        Display help and quit program.
         */
        for (String arg: args){
            if (arg.equals("-h") || arg.equals("--help")){
                displayHelp();
                return;
            }
        }

        Converter.setOsSpecifics();
        Instant programStart = Instant.now();
        for (String arg: args){
            Converter converter = new Converter(new Audiobook(arg));
        }
        Instant programEnd = Instant.now();
        Duration duration = Duration.between(programStart, programEnd);
        System.out.printf("\nAll Done! Duration: %d:%02d min.\n\n",
                duration.toMinutes(), (((int)duration.toMillis()/1000)-(duration.toMinutes()*60)));
    }

    /**
     * Display the help screen
     */
    public static void displayHelp(){
        System.out.println("AudioBookConverter - Split m4a files by chapter marks");
        System.out.println("Copyright (C) 2017  ahahn94");
        System.out.println("This program is published under the terms of the GNU GPL V2\n");

        System.out.println("\t\tWelcome to the AudioBookConverter!\n");

        System.out.println("This program can be used to split m4a files by the chapter marks they contain.");
        System.out.println("The program will produce a folder named after the input file and one mp3 file per chapter.");
        System.out.println("The file name and title tag will be the same as the chapters name.\n");

        System.out.println("\tm4b files can be used after renaming them to m4a!");
        System.out.println("\tm4b audiobooks from iTunes will not work because of the embedded DRM.\n");

        System.out.println("Make sure FFmpeg and libmp3lame are installed on your system.");
    }
}