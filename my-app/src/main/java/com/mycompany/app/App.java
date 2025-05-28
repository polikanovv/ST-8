package com.mycompany.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        WebDriver driver = new ChromeDriver();

        driver.get("http://www.papercdcase.com/index.php");

        BufferedReader reader = new BufferedReader(new FileReader("../data/data.txt"));

        String artistLine = reader.readLine();
        String artist = artistLine.substring(artistLine.indexOf(":") + 2);

        String titleLine = reader.readLine();
        String title = titleLine.substring(titleLine.indexOf(":") + 2);

        reader.readLine();

        String[] tracks = new String[18];
        for (int i = 0; i < 18; i++) {
            String line = reader.readLine();
            if (line == null || line.isEmpty()) break;
            tracks[i] = line.substring(line.indexOf(".") + 2);
        }
        reader.close();

        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[1]/td[2]/input")).sendKeys(artist);
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[2]/td[2]/input")).sendKeys(title);

        for (int i = 0; i < 8 && tracks[i] != null; i++) {
            driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[3]/td[2]/table/tbody/tr/td[1]/table/tbody/tr[" + (i + 1) + "]/td[2]/input")).sendKeys(tracks[i]);
        }
        for (int i = 8; i < 10 && tracks[i] != null; i++) {
            driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[3]/td[2]/table/tbody/tr/td[2]/table/tbody/tr[" + (i - 8 + 1) + "]/td[2]/input")).sendKeys(tracks[i]);
        }

        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[4]/td[2]/input[2]")).click();
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/table/tbody/tr[5]/td[2]/input[2]")).click();
        driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[1]/div/form/p/input")).click();

        Thread.sleep(3000);

        List<String> tabs = List.copyOf(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));

        String pdfUrl = driver.getCurrentUrl();

        InputStream in = new java.net.URL(pdfUrl).openStream();
        FileOutputStream out = new FileOutputStream("../result/cd.pdf");

        byte[] buffer = new byte[4096];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }

        in.close();
        out.close();
        driver.quit();
    }
}
