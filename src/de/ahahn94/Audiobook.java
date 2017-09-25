package de.ahahn94;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent the metadata of an m4a file
 * @author ahahn94
 * @version 1.0
 */
public class Audiobook {

    private ArrayList<Chapter> chapters; //Chapter list
    private String inputpath;   //Path and Filename of the input file, e.g. "Lord of the Rings.m4a"
    private String outputpath;  //Path to the output files, e.g. "Lord of the Rings"

    /**
     * Constructor
     * @param inputpath Path to the input file
     */
    Audiobook(String inputpath){
        chapters = new ArrayList<>();
        this.inputpath = inputpath;
        outputpath = inputpath.substring(0, inputpath.lastIndexOf("."));
        setChapters();
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public String getInputpath() {
        return inputpath;
    }

    public String getOutputpath() {
        return outputpath;
    }

    /**
     * Get the chapter properties from the inputfile
     */
    void setChapters(){
        String ffprobe = "ffprobe \"".concat(inputpath).concat("\" 2>&1 >/dev/null");
        ProcessBuilder pb = new ProcessBuilder(Converter.shellName, Converter.shellOption, ffprobe);
        pb.redirectErrorStream(true);
        List<String> listStart = new ArrayList();
        List<String> listEnd = new ArrayList();
        List<String> listNames = new ArrayList();
        /*
        Read metadata into lists.
         */
        try {
            Process pr = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while((line=reader.readLine())!=null) {
                if (line.contains("Chapter #")){
                    String[] vars = line.split("\\s+");
                    listStart.add(vars[4].replace(",",""));
                    listEnd.add(vars[6]);
                }
                if (line.contains("      title")){
                    listNames.add(line.replace("      title           : ", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        Generate chapter entries from lists.
         */
        if (listNames.size() == listStart.size() && listStart.size() == listEnd.size())
        {
            if (listNames.size()<100){
                for (int i = 0; i < listNames.size(); i++){
                    chapters.add(new Chapter(String.format("%02d", (i+1)).concat(" - ").concat(listNames.get(i)), listStart.get(i), listEnd.get(i), String.format("%02d/%02d", (i+1), listNames.size())));
                }
            }
            else{
                for (int i = 0; i < listNames.size(); i++){
                    chapters.add(new Chapter(String.format("%03d", (i+1)).concat(" - ").concat(listNames.get(i)), listStart.get(i), listEnd.get(i), String.format("%03d/%03d", (i+1), listNames.size())));
                }
            }
        }
        else {
            System.out.println("Can not import chapters!");
        }
    }

    /**
     * Class to represent the chapters
     */
    public class Chapter{
        private String name, start, end;
        String tracknumber;

        /**
         * Constructor
         * @param name Chapter name
         * @param start Timestamp of the chapters beginning
         * @param end Timestamp of the chapters end
         * @param tracknumber Tracknumber of the chapter
         */
        public Chapter(String name, String start, String end, String tracknumber) {
            this.name = name;
            this.start = start;
            this.end = end;
            this.tracknumber = tracknumber;
        }

        public String getName() {
            return name;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public String getTracknumber(){
            return tracknumber;
        }
    }
}