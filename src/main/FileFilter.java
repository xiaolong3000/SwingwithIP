package main;

import java.io.File;

/**
 * Created by Administrator on 2017/5/3 0003.
 */
public class FileFilter extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        return f.getName().endsWith(".xls");
    }

    @Override
    public String getDescription() {
        return ".xls";
    }
}
