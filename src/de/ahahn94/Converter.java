package de.ahahn94;

import sun.awt.OSInfo;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

/**
 * Class for the conversion of the input file
 * @author ahahn94
 * @version 1.0
 */
public class Converter {

    static String osSlash = "";
    static String shellName;
    static String shellOption;
    Audiobook audiobook;

    /**
     * Constructor
     * @param audiobook Audiobook metadata of the input file
     */
    Converter(Audiobook audiobook){
        Instant start = Instant.now();
        this.audiobook = audiobook;
        createDir();
        convert();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.printf("\nAudiobook successfully converted in %d:%02d min.\n\n",
                duration.toMinutes(), (((int)duration.toMillis()/1000)-(duration.toMinutes()*60)));
    }

    /**
     * Set OS specific settings
     */
    public static void setOsSpecifics(){
        if (OSInfo.getOSType() == OSInfo.OSType.WINDOWS){
            osSlash = "\\";
            shellName = "cmd";
            shellOption = "/c";
        }
        if (OSInfo.getOSType() == OSInfo.OSType.LINUX){
            osSlash = "/";
            shellName = "/bin/bash";
            shellOption = "-c";
        }
    }

    /**
     * Create the ouput directory for the audiobook
     */
    private void createDir(){
        String command = "mkdir -p ".concat("\"").concat(audiobook.getOutputpath()).concat("\"");
        ProcessBuilder pb = new ProcessBuilder(shellName, shellOption, command);
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Could not create output directory!");
        }
        System.out.printf("\nDestination directory was successfully created!\n");
    }

    /**
     * Run conversion and splitting
     */
    private void convert(){
        for (Audiobook.Chapter chapter: audiobook.getChapters()){
            String command = "ffmpeg -y -ss ".concat(chapter.getStart())   //fast skip to chapter beginning.
                    .concat(" -i ").concat("\"").concat(audiobook.getInputpath()).concat("\"") //overwrite existing and set input
                    //Codec:
                    .concat(" -codec:a libmp3lame -vn -b:a 320k")
                    .concat(" -to ").concat(chapter.getEnd()).concat(" -copyts") //Chapter End Mark, fix timestamps
                    //Set Metadata
                    //Title:
                    .concat(" -metadata title=").concat("\"").concat(chapter.getName()).concat("\"") //Set Title-Tag
                    //Tracknumber #/#:
                    .concat(" -metadata track=").concat("\"").concat(chapter.getTracknumber()).concat("\"")
                    //Output Filepath and -name:
                    .concat(" \"").concat(audiobook.getOutputpath()).concat(osSlash).concat(chapter.getName()).concat(".mp3\"");
            /*
            Start conversion
            */
            ProcessBuilder processBuilder = new ProcessBuilder(shellName, shellOption, command);
            processBuilder.redirectErrorStream(true);
            try {
                Instant processStart = Instant.now();
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                System.out.println("\nConverting \"" + chapter.getName() + "\" (" + chapter.getTracknumber() + "):");
                while ((line = reader.readLine()) != null) {
                    if (line.contains("size=")){
                        System.out.printf("\r%s", line);
                    }
                }
                Instant processEnd = Instant.now();
                Duration duration =Duration.between(processStart, processEnd);
                System.out.printf("\nFile successfully converted in %d:%02d min.\n\n",
                        duration.toMinutes(), (((int)duration.toMillis()/1000)-(duration.toMinutes()*60)));
            } catch (IOException e) {
                System.out.printf("Error during conversion!");
                e.printStackTrace();
            }
        }
    }
}
