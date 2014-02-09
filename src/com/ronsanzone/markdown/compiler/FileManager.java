package com.ronsanzone.markdown.compiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager
{
    static void writeFile(String filename)
    {
        try
        {
            FileWriter filestream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(filestream);

            out.write(Compiler.htmlString);
            out.close();
        } catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static String readFile(String pathname) throws IOException
    {
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = "\n";

        try
        {
            while (scanner.hasNextLine())
            {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally
        {
            scanner.close();
        }
    }
}