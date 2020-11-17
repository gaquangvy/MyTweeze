package pages;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Owner
 */
public class PositiveWordsCollection {
    public static List<String> outList() throws FileNotFoundException {
        List<String> output = new ArrayList<>();
        Scanner in = new Scanner(new File("src/pages/PostiveWords.txt"));
        while (in.hasNextLine()) {
            String line = in.nextLine();
            line = line.split(". ")[1].toLowerCase();
            output.add(line);
        }
        in.close();
        return output;
    }
}