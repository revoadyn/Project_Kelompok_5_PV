package form;

public class UserID {

    private static String id;
    private static String nama;

    static void setIDAdmin(String idAdmin) {
        UserID.id = idAdmin;
    }

    public static String getIDAdmin() {
        return id;
    }

    public static void setNamaAdmin(String namaAdmin) {
        UserID.nama = namaAdmin;
    }

    public static String getNamaAdmin() {
        return nama;
    }
}
