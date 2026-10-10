## 1. Neden Generics? (Temel Motivasyon)

Generics öncesinde veri yapılarında her şey `Object` olarak saklanırdı. Bu durum iki büyük soruna yol açıyordu:

1. **Tür Dönüşümü (Explicit Casting):** Elemanları okurken sürekli manuel casting gerekiyordu.
2. **Çalışma Zamanı Hataları (Runtime Errors):** Farklı türlerin aynı listeye eklenmesi derleme anında fark edilmez, çalışma zamanında `ClassCastException` fırlatılırdı.

Java

```
// Generics Olmadan (Java 1.4 ve öncesi)
List liste = new ArrayList();
liste.add("Ders Notu");
liste.add(42); // Derleyici izin verir!

String metin = (String) liste.get(1); // RUNTIME ERROR: ClassCastException
```

Generics bu süreci **derleme zamanında tür güvenliği (Type Safety)** sağlayarak çözer:

Java

```
// Generics İle (Modern Java)
List<String> liste = new ArrayList<>();
liste.add("Ders Notu");
// liste.add(42); // DERLEME HATASI: Derleyici kodu reddeder.

String metin = liste.get(0); // Casting gerektirmez.
```

## 2. Tip Parametresi Adlandırma Standartları

Java konvansiyonlarına göre tip parametreleri tek karakter ve büyük harf olarak seçilir:

| Parametre | Temsil Ettiği Anlam | Kullanım Alanı |
| --- | --- | --- |
| `T` | Type | Genel veri tipleri |
| `E` | Element | Koleksiyon elemanları (`List<E>`, `Set<E>`) |
| `K` | Key | Harita anahtarları (`Map<K, V>`) |
| `V` | Value | Harita değerleri |
| `N` | Number | Sayısal tipler |

## 3. Jenerik Sınıflar ve Arayüzler (Generic Classes & Interfaces)

Bir sınıfın veya arayüzün hangi tür ile çalışacağını nesne oluşturulma anına bırakır.

### Jenerik Sınıf Tanımı

Java

```
public class Kutu<T>{
    private T icerik;

    public void koy(T icerik){
        this.icerik = icerik;
    }

    public T al(){
        return icerik;
    }
}

// Kullanım:
Kutu<String> stringKutu = new Kutu<>();
stringKutu.koy("Merhaba");

Kutu<Integer> sayiKutu = new Kutu<>();
sayiKutu.koy(100);
```

### Jenerik Arayüz Tanımı

Java

```
public interface Depo<T>{
    void kaydet(T eleman);
    T bul(int id);
}

// Belirli bir türle uygulama:
public class KullaniciDeposu implements Depo<Kullanici>{
    @Override
    public void kaydet(Kullanici eleman){ /* ... */ }
    @Override
    public Kullanici bul(int id){ return null; }
}

// Jenerik yapıyı koruyarak uygulama:
public class BellekDeposu<T> implements Depo<T>{
    @Override
    public void kaydet(T eleman){ /* ... */ }
    @Override
    public T bul(int id){ return null; }
}
```

## 4. Jenerik Metotlar (Generic Methods)

Sınıf jenerik olmasa bile sadece belirli metotlar jenerik tanımlanabilir. Tip parametresi dönüş türünden önce bildirilir.

Java

```
public class DiziYardimcisi{

    // Jenerik metot tanımı: <T> bildirimi dönüş tipinden öncedir
    public static <T> void diziYazdir(T[] dizi){
        for (T eleman : dizi) {
            System.out.print(eleman + " ");
        }
        System.out.println();
    }
}

// Kullanım:
String[] isimler = {"Ali", "Veli", "Ayşe"};
Integer[] sayilar = {1, 2, 3, 4};

DiziYardimcisi.diziYazdir(isimler);
DiziYardimcisi.diziYazdir(sayilar);
```

## 5. Sınırlı Tip Parametreleri (Bounded Type Parameters)

Kullanılabilecek tipleri belirli bir üst sınıfa veya arayüze kısıtlamak istediğimizde `extends` anahtar kelimesi kullanılır.

Java

```
// T sadece Number veya Number'dan türeyen bir sınıf olabilir
public class Hesaplayici<T extends Number>{
    private T sayi;

    public Hesaplayici(T sayi){
        this.sayi = sayi;
    }

    public double kareAl(){
        return sayi.doubleValue() * sayi.doubleValue();
    }
}

// Geçerli:
Hesaplayici<Integer> h1 = new Hesaplayici<>(5);
Hesaplayici<Double> h2 = new Hesaplayici<>(4.5);

// Geçersiz (Derleme Hatası):
// Hesaplayici<String> h3 = new Hesaplayici<>("Metin");
```

> **Çoklu Sınır (Multiple Bounds):** Bir tip aynı anda hem bir sınıfı hem de arayüzleri sınırlandırabilir:
> 
> 
> `<T & Arayuz1 Arayuz2 Sınıf extends>`. Java'nın tekli kalıtım kuralı gereği sınıf adı her zaman en başta yer almalıdır.
> 

## 6. Joker Karakterler (Wildcards: `?`)

Java'da diziler kovaryanttır (`Integer[]`, `Number[]` yerine geçebilir), ancak jenerikler **değişmezdir (invariant)**. Yani `Integer`, `Number`'ın alt sınıfı olsa bile, `List<Integer>` bir `List<Number>` değildir. Bu esnekliği sağlamak için joker karakterler (`?`) kullanılır.

Java

```
List<Integer> intList = new ArrayList<>();
// List<Number> numList = intList; // DERLEME HATASI! Tür uyuşmazlığı.
```

### Wildcard Türleri ve PECS Prensibi

| Joker Yapısı | Adı | Anlamı / Kullanım Amacı |
| --- | --- | --- |
| `<?>` | Unbounded Wildcard | Herhangi bir türü kabul eder (sadece `Object` metodlarına erişilir). |
| `<? extends T>` | Upper Bounded Wildcard | `T` veya onun alt sınıflarını kabul eder. |
| `<? super T>` | Lower Bounded Wildcard | `T` veya onun üst sınıflarını kabul eder. |

#### Altın Kural: PECS (Producer Extends, Consumer Super)

- **Producer Extends:** Eğer koleksiyondan sadece veri **okuyacaksanız** (`get`), `<? extends T>` kullanın.
- **Consumer Super:** Eğer koleksiyona veri **yazacaksanız** (`add`), `<? super T>` kullanın.

Java

```
// Sadece okuma yapılıyor (Producer)
public static double toplamAl(List<? extends Number> liste){
    double toplam = 0.0;
    for (Number n : liste) {
        toplam += n.doubleValue();
    }
    // liste.add(10); // HATA: Derleyici listenin tam türünü bilmediği için eklemeye izin vermez!
    return toplam;
}

// Yazma yapılıyor (Consumer)
public static void sayiEkle(List<? super Integer> liste){
    liste.add(1);
    liste.add(2); // Güvenle Integer ve alt türleri eklenebilir.
}
```

## 7. Tip Silinmesi (Type Erasure) ve Kısıtlamalar

Java geriye dönük uyumluluğu korumak için Generics'i **derleme aşamasında uygular**. Derleme bittiğinde tip bilgileri silinir ve bytecode'a dönüştürülür:

- Sınırsız tipler (`<T>`) bytecode düzeyinde `Object` türüne dönüştürülür.
- Sınırlı tipler (`<T Number extends>`) sınır türüne (`Number`) dönüştürülür.
- Gerekli yerlere derleyici otomatik casting ekler.

### Tip Silinmesinin Getirdiği Kısıtlamalar

Java

```
// 1. İlkel (primitive) tipler kullanılamaz (int, double yerine Integer, Double gerekir):
// List<int> list = new ArrayList<>(); // HATA

// 2. Tip parametresi ile doğrudan nesne üretilemez:
// T nesne = new T(); // HATA

// 3. Tip parametresi ile dizi oluşturulamaz:
// T[] dizi = new T[10]; // HATA

// 4. Statik alanlarda sınıf seviyesindeki T kullanılamaz:
// private static T veri; // HATA

// 5. instanceof ile jenerik tip kontrolü yapılamaz:
// if (nesne instanceof List<String>) // HATA (Ancak 'instanceof List<?>' serbesttir)

// 6. Generic Throwable/Exception sınıfları oluşturulamaz:
// public class OzelHata<T> extends Exception {} // HATA
```
