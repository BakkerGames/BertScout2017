package com.bertrobotics.bertscout2017;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chime on 12/27/2016.
 */

public final class ExportData {

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getDocumentStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static void ExportData(String filename, JSONObject data) {
        try {
            FileWriter outFile = new FileWriter(filename);
            outFile.write(data.toString());
            outFile.close();
        } catch (IOException e) {
            return; // ### show error ###
        }
    }

    public static String ImportData(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
            StringBuilder result = new StringBuilder();
            int c;
            while ((c = bfr.read()) >= 0) {
                result.append((char) c);
            }
            bfr.close();
            fis.close();
            return result.toString();
        } catch (IOException e) {
            return null; // ### show error ###
        }
    }

}
