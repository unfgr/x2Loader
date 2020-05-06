/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package x2loader;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import static java.lang.System.exit;
import java.nio.file.Files;
import java.nio.file.Paths;
import jdk.nashorn.internal.objects.NativeArray;
import nl.lxtreme.binutils.hex.*;
import sun.misc.IOUtils;


/**
 *
 * @author User
 */
public class X2Loader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
    MainGui mg = new MainGui();
    Comms.getInstance().setMgRef(mg);
    mg.setVisible(true);
    } 
}
