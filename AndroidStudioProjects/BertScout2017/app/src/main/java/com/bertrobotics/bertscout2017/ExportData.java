package com.bertrobotics.bertscout2017;

import android.os.Environment;

import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by chime on 12/27/2016.
 */

public final class ExportData {

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getDocumentStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (!file.exists())
        {
            file.mkdir();
        }
        return file;
    }

    public void ExportData(String filename, JSONArray data)
    {
//        if (!isExternalStorageWritable())
//        {
//            return; // ### show error ###
//        }
        try
        {
//            outFileBase.createNewFile();
            FileWriter outFile = new FileWriter(filename);
            outFile.write(data.toString());
            outFile.close();
        }
        catch (IOException e)
        {
            return; // ### show error ###
        }
    }

}
