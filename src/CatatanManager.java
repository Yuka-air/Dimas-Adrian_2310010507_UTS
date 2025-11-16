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
import org.json.*;  // pastikan library JSON sudah ditambahkan

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

                if (line.equals("---------------")) {
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

            if (!judul.isEmpty()) {
                daftarCatatan.addElement(new Catatan(judul, isi.toString()));
            }
        }
    }

    // =============================================================
    // ====================== EXPORT JSON ==========================
    // =============================================================
    public void exportToJson(File file) throws Exception {
        JSONArray arr = new JSONArray();

        for (int i = 0; i < daftarCatatan.size(); i++) {
            Catatan c = daftarCatatan.get(i);

            JSONObject obj = new JSONObject();
            obj.put("judul", c.getJudul());
            obj.put("isi", c.getIsi());

            arr.put(obj);
        }

        try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
            pw.println(arr.toString(4)); 
        }
    }

    // =============================================================
    // ==================== IMPORT JSON/TXT AUTO ===================
    // =============================================================
    public void importFromFile(File file) throws Exception {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".json")) {
            importJson(file);
        } else if (name.endsWith(".txt")) {
            imporTXT(file.getAbsolutePath());
        } else {
            throw new Exception("Format file tidak didukung (hanya .txt dan .json)");
        }
    }

    private void importJson(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        JSONArray arr = new JSONArray(sb.toString());

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            tambahCatatan(obj.getString("judul"), obj.getString("isi"));
        }
    }
}
