package controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class App {
    public static void main(String[] args) throws IOException {
        String[] URLS = {
                "http://services.runescape.com/m=itemdb_rs/Clean+kwuarm/viewitem?obj=263",
                "http://services.runescape.com/m=itemdb_rs/Shark/viewitem?obj=385",
                "http://services.runescape.com/m=itemdb_rs/Dragon+bones/viewitem?obj=536",
                "http://services.runescape.com/m=itemdb_rs/Black+dragonhide/viewitem?obj=1747",
                "http://services.runescape.com/m=itemdb_rs/Elder+rune+bar/viewitem?obj=44844"

        };
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Double> prices = new ArrayList<Double>();

        for(String URL : URLS){
            try{
                final Document document = Jsoup.connect(URL).get();
                String name = document.select("div.item-description h2").text();
                Double price = Double.parseDouble(document
                        .select("div.stats h3 span").attr("title")
                        .replace(",",""));
                names.add(name);
                prices.add(price);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("prices");

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String today = dateFormat.format(date);

        for(int i = 0; i < names.size(); i++){
            Row row = sheet.createRow(i);
            Cell dateCell = row.createCell(0);
            dateCell.setCellValue(today);
            Cell itemNameCell = row.createCell(1);
            itemNameCell.setCellValue(names.get(i));
            Cell itemPriceCell = row.createCell(2);
            itemPriceCell.setCellValue(prices.get(i));
        }
        FileOutputStream outputStream = new FileOutputStream("prices.xlsx");
        workbook.write(outputStream);
    }
}