import java.time.Year;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // kullanıcı adı ve şifre için gerekli string değişken tanımlıyoruz.

        String userName, password, password2, request;
        char a;

        // scanner nesnesi ile dışarıdan kullanıcı adı ve şifre aldıracağız.
        Scanner input = new Scanner(System.in);

        System.out.print("Kullanıcı adı giriniz : ");
        userName = input.nextLine();

        System.out.print("Sifre giriniz : ");
        password = input.nextLine();

        //kullanıcı adı ve şifreyi aldık şimdi doğruluk sorgulayacağız.
        if (userName.equals("Patika") && password.equals("Dev123")) {
            System.out.println("Sifre Doğru, giriş yaptınız.");

        } else {
            System.out.println("Yanlış şifre girdiniz.");

        }
        System.out.println("Şifrenizi sıfırlamak ister misniz?");
        System.out.println("Y/N");
        a = input.next().charAt(0);

        String password3="";
        switch (a) {
            case 'Y':
                System.out.println("Yeni Sifre giriniz : ");
                input.nextLine();
                password2 = input.nextLine();

                //kullanıcı adı ve şifreyi aldık şimdi önceki şifre ile doğruluk sorgulayacağız.
                if (password2.equals("Dev123")) {
                    System.out.println("Eski şifre ile aynı olmamalıdır.");
                    System.out.println("Tekrar deneyin");
                    password3 = input.nextLine();

                } else if (password3.equals("Dev123")) {
                    System.out.println("Eski şifre ile aynı olmamalıdır. tekrar deneyin.");
                }else {

                    System.out.println("Yeni şifre ile giriş yaptınız");


                    break;
                }
                    case 'N':
                        System.out.println("Çıkış Yaptınız. ");
                        break;
                    default:
                        System.out.println("Y/N değeri giriniz");

                }

        }


    }
