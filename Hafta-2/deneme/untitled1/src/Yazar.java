public class Yazar {
    private String ad;
    private String soyad;

    public Yazar(String ad, String soyad) {
        this.ad = ad;
        this.soyad = soyad;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getBilgi() {
        return this.ad + " " + this.soyad;
    }
}

