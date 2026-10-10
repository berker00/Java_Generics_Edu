public class Kasa<T> {
    // 1. Jenerik alan tanımı
    private T icerik;

    // 2. Kurucu Metot (Constructor)
    public Kasa(T icerik) {
        this.icerik = icerik;
    }

    // Kasadaki içeriği güncelleyen metot
    public void icerikDegistir(T yeniIcerik) {
        this.icerik = yeniIcerik;
    }

    // Kasadaki T tipindeki içeriği döndüren metot
    public T icerikGetir() {
        return icerik;
    }

    // Kasadaki içerik ve tür bilgisi
    public void bilgiVer() {
        if (icerik != null) {
            System.out.println("Kasadaki nesne: " + icerik + " | Türü: " + icerik.getClass().getSimpleName());
        } else {
            System.out.println("Kasa boş!");
        }
    }

    // 3. Test Metodu
    public static void main(String[] args) {
        // String tutan Kasa
        Kasa<String> evrakKasasi = new Kasa<>("Evrak");
        evrakKasasi.bilgiVer();

        // İçeriği güncelleme ve getirme
        evrakKasasi.icerikDegistir("Değiştirilmiş Evrak");
        System.out.println("Okunan Veri: " + evrakKasasi.icerikGetir());

        System.out.println("----------------------------------------");

        // Double tutan Kasa (Aynı sınıf, farklı tip)
        Kasa<Integer> paraKasasi = new Kasa<>(null);
        paraKasasi.bilgiVer();
    }
}