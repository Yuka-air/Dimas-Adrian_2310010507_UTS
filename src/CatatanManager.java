/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author acer3
 */

import java.io.*;
import java.util.*;
import javax.swing.DefaultListModel;

public class CatatanManager {
    private DefaultListModel<Catatan> daftarCatatan = new DefaultListModel<>();

    public DefaultListModel<Catatan> getModel() {
        return daftarCatatan;
    }

    public void tambahCatatan(String judul, String isi) {
        daftarCatatan.addElement(new Catatan(judul, isi));
    }

    public void editCatatan(int index, String judul, String isi) {
        if (index >= 0 && index < daftarCatatan.size()) {
            Catatan c = daftarCatatan.get(index);
            c.setJudul(judul);
            c.setIsi(isi);
        }
    }

    public void hapusCatatan(int index) {
        if (index >= 0 && index < daftarCatatan.size()) {
            daftarCatatan.remove(index);
        }
    }

    public void clear() {
        daftarCatatan.clear();
    }

    // =============================================================
    // =============== FORMAT TXT DEFAULT ==========================
    // =============================================================
    public void eksporData(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(fileName)) {
            for (int i = 0; i < daftarCatatan.size(); i++) {
                Catatan c = daftarCatatan.getElementAt(i);
                writer.println(c.getJudul() + ";" + c.getIsi());
            }
        }
    }

    public void imporData(String fileName) throws IOException {
        daftarCatatan.clear();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(";", 2);
                if (data.length == 2) {
                    daftarCatatan.addElement(new Catatan(data[0], data[1]));
                }
            }
        }
    }

    // =============================================================
    // =============== FORMAT TXT WRAPPER ==========================
    // =============================================================
    public void eksporTXT(String fileName) throws IOException {
    try (PrintWriter writer = new PrintWriter(fileName)) {
        for (int i = 0; i < daftarCatatan.size(); i++) {
            Catatan c = daftarCatatan.get(i);

            writer.println(c.getJudul() + " :");
            writer.println();
            writer.println(c.getIsi());
            writer.println();
            writer.println("---------------"); 
            writer.println();
        }
    }
}


    public void imporTXT(String fileName) throws IOException {
        try (Scanner sc = new Scanner(new File(fileName))) {

            String judul = "";
            StringBuilder isi = new StringBuilder();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                if (line.equals("-----")) {
                    daftarCatatan.addElement(new Catatan(judul, isi.toString()));
                    judul = "";
                    isi = new StringBuilder();
                    continue;
                }

                if (judul.isEmpty()) {
                    judul = line;
                } else {
                    isi.append(line).append("\n");
                }
            }

            // Simpan catatan terakhir jika tidak diakhiri '-----'
            if (!judul.isEmpty()) {
                daftarCatatan.addElement(new Catatan(judul, isi.toString()));
            }
        }
    }
}




