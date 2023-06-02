
//Ödev sahipleri : Gülçin Sağbaş 22120205017, Nesibe Ebrar Çimen 22120205024

import java.awt.*;
import java.awt.image.BufferedImage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import javax.imageio.ImageIO;

public class Main extends JFrame {
    private JButton cekButton;
    private JComboBox<String> effectsComboBox;
    private JButton efektUygulaButton;
    private JButton kaydetButton;
    private JButton paylasButton;
    private JLabel previewLabel;
    private Webcam webcam;
    private BufferedImage resmiTut;
    private File saveDirectory;

    public Main() {
        setTitle("Kamera Uygulaması");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cekButton = new JButton("Resim Çek");
        cekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resmiCek();
            }
        });

        effectsComboBox = new JComboBox<>(new String[]{"Aydınlat", "Parlat", "Siyah-Beyaz"});
        efektUygulaButton = new JButton("Efekti Uygula");
        efektUygulaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resmeEfektUygula();
            }
        });

        kaydetButton = new JButton("Kaydet");
        kaydetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cekilenResmiKaydet();
            }
        });

        paylasButton = new JButton("Paylaş");
        paylasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cekilenResmiPaylas();
            }
        });
        //Resim önizlemesini görüntülemek için bir Label oluşturulur
        previewLabel = new JLabel();
        previewLabel.setPreferredSize(new Dimension(800, 600));
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);



        //Kontrol bileşenlerini içeren bir panel oluşturulur
        JPanel controlPanel = new JPanel();
        controlPanel.add(cekButton);
        controlPanel.add(effectsComboBox);
        controlPanel.add(efektUygulaButton);
        controlPanel.add(kaydetButton);
        controlPanel.add(paylasButton);


        //Bileşenlerin yerleştirileceği bir Container nesnesi oluşturulur.
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(previewLabel, BorderLayout.CENTER);
        container.add(controlPanel, BorderLayout.SOUTH);

        // Masaüstünde EbrarGulcinResim dosyasını kontrol et ve oluştur
        String desktopPath = System.getProperty("user.home") + "/Desktop";
        saveDirectory = new File(desktopPath + "/EbrarGulcinResim");
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
    }

    private void resmiCek() {
        try {
            webcam = Webcam.getDefault(); //web kamerası webcam değişkenine atanır.
            webcam.open();
            resmiTut = webcam.getImage();//webcam nesnesinin getImage() yöntemi çağrılarak anlık bir resim resmiTut değişkenine atanır
            previewLabel.setIcon(new ImageIcon(resmiTut));
        } catch (WebcamException ex) {
            ex.printStackTrace();
        }


    }

    private void resmeEfektUygula() {
        if (resmiTut != null) {
            String selectedEffect = (String) effectsComboBox.getSelectedItem();
            if (selectedEffect.equals("Aydınlat")) {
                resmiTut = Efekt.applyBrightnessEffect(resmiTut);
            } else if (selectedEffect.equals("Parlat")) {
                resmiTut = Efekt.applySharpenEffect(resmiTut);
            } else if (selectedEffect.equals("Siyah-Beyaz")) {
                resmiTut = Efekt.applyBlackAndWhiteEffect(resmiTut);
            }
            previewLabel.setIcon(new ImageIcon(resmiTut));
        }
    }

    private void cekilenResmiKaydet() {
        if (resmiTut != null) {
            JFileChooser fileChooser = new JFileChooser(saveDirectory);//kaydetme işlemi için kaydedilecek dosya konumunu belirlemek için `saveDirectory` kullanılır.
            fileChooser.setDialogTitle("Resmi Kaydet");
            fileChooser.setSelectedFile(new File(saveDirectory.getPath() + "/image.jpg"));
            //Önerilen dosya adını ayarlar, burada varsayılan olarak "image.jpg" olarak belirlenir.
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                //()`: Seçilen dosya, `fileChooser` üzerinden alınır ve `selectedFile` değişkenine atanır.
                try {
                    ImageIO.write(resmiTut, "jpg", selectedFile);
                    JOptionPane.showMessageDialog(this, "Resim başarıyla kaydedildi.");
                    //Kaydetme işlemi başarılı olduğunda kullanıcıya bir iletişim kutusu ile bilgi verilir.
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void cekilenResmiPaylas() {
        if (resmiTut != null) {
            if (resmiTut != null) {
                // Facebook giriş bilgileri
                String facebookEmail = "jarawoj595@duscore.com";
                String facebookPassword = "bilmiyorum";

                // Resmi geçici bir dosyaya kaydet
                File tempImageFile = new File(saveDirectory, "temp_image.jpg");
                try {
                    ImageIO.write(resmiTut, "jpg", tempImageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // WebDriver'ı yapılandır
                WebDriverManager.chromedriver().setup();
                WebDriver driver = new ChromeDriver();

                try {
                    // Facebook'a giriş yap
                    driver.get("https://www.facebook.com");
                    driver.findElement(By.id("email")).sendKeys(facebookEmail);
                    driver.findElement(By.id("pass")).sendKeys(facebookPassword);
                    driver.findElement(By.name("login")).click();


                    // Gönderi oluştur
                    driver.findElement(By.cssSelector("[aria-label='Ne düşünüyorsun?']")).click();

                    // Resmi yükle
                    WebElement fileInput = driver.findElement(By.cssSelector("input[type='file']"));

                    fileInput.sendKeys(tempImageFile.getAbsolutePath());

                    // Paylaş düğmesini bekleyin
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(70) ); // 60 saniye bekle
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("[aria-label='Paylaş']")));

                    // Paylaş düğmesine tıkla
                    driver.findElement(By.className("[aria-label='Paylaş']")).click();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Tarayıcıyı kapat
                    driver.quit();

                    // Geçici dosyayı sil
                    tempImageFile.delete();
                }
            }


        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}





class Efekt {

    public static BufferedImage applyBrightnessEffect(BufferedImage image) {//aydınlatma efekti uygular
        //görüntüyü yeniden ölçeklendirir ve aydınlatma efektini uygular
        RescaleOp rescaleOp = new RescaleOp(1.2f, 15, null);
        return rescaleOp.filter(image, null);
    }

    public static BufferedImage applySharpenEffect(BufferedImage image) {//parlatma efekti uygular
        //Özel bir matris kullanarak ConvolveOp sınıfıyla görüntüyü keskinleştirir.
        float[] SHARPEN_MATRIX = {
                -1, -1, -1,
                -1,  9, -1,
                -1, -1, -1
        };
        Kernel kernel = new Kernel(3, 3, SHARPEN_MATRIX);
        ConvolveOp convolveOp = new ConvolveOp(kernel);
        return convolveOp.filter(image, null);
    }

    public static BufferedImage applyBlackAndWhiteEffect(BufferedImage image) { //siyah beyaz hale cevirir
        //BufferedImage.TYPE_BYTE_GRAY türünde yeni bir gri tonlamalı görüntü oluşturulur
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = grayImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return grayImage;
    }
}







