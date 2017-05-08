package main;

import javax.swing.*;

/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class main {
    public static void main(String[] args){
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible",false);
        } catch (Exception e) {
            e.printStackTrace();
        }
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ui.ManageView();
                }
            });


    }
}
