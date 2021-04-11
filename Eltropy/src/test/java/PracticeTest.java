import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.xml.crypto.Data;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class PracticeTest {

        public static WebDriver driver = null;
        public static JavascriptExecutor js =null;
        @BeforeSuite
        public void initialize() throws IOException {

            System.setProperty("webdriver.chrome.driver", "/Users/oyo/Downloads/chromedriver");

            driver = new ChromeDriver();
            js = (JavascriptExecutor) driver;
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//            driver.get("https://www.twitter.com");


        }
        @Test
        public void MainTest() throws IOException, InterruptedException {
            List<MainData> twitterData= MainData.readData();
            StringBuilder htmlContent=new StringBuilder();
            GenerateReport.processReportHeader(htmlContent);

            //going to twitter handle of each user
            for(MainData itr : twitterData){

                driver.manage().window().maximize();
                driver.get("https://www.twitter.com/"+itr.name);

                htmlContent.append("<tr>");
                GenerateReport.addColumnFor(htmlContent, itr.name);
                GenerateReport.addColumnFor(htmlContent,"https://www.twitter.com/"+itr.name);

                //Take the screenshot
                WebDriverWait wait = new WebDriverWait(driver, 8);
                WebElement firstResult = wait.until(presenceOfElementLocated(By.xpath("//article[@role=\"article\"]")));
//              driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                try {
                    String filepath="src/main/resources/Screenshots/Screenshot-"+itr.name+".png";
                    FileUtils.copyFile(screenshot, new File(filepath));
                    GenerateReport.addImage(htmlContent,filepath);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                //top 10 tweets
                GenerateReport.addTweets(htmlContent,itr.tweets);

                //Screenshot of top 10 tweets
                htmlContent.append("<td style=\"border: 1px solid black; border-collapse: collapse; padding: 5px;\">");
                for(int i=0;i<10;i++){
                    driver.get("https://twitter.com/BarackObama/status/"+itr.tweets.get(i).getId_str());

                    WebElement visel = wait.until(presenceOfElementLocated(By.xpath("//article[@role=\"article\"]")));
                    screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String filepath="src/main/resources/Screenshots/Screenshot-"+itr.name+"-"+i+".png";
                    try {
                        FileUtils.copyFile(screenshot, new File(filepath));
                        htmlContent.append("<img src="+filepath+" style=width:100px;/>");
                        if(i%2!=0)
                            htmlContent.append("<br>");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                htmlContent.append("</td>");
                GenerateReport.addFriends(htmlContent,itr.friends);
                htmlContent.append("</tr>");

            }
            htmlContent.append("</table>");
//            FileOutputStream Reportfile = new FileOutputStream("src/test/java/Reportfile.html");
//            Reportfile.write(htmlContent);
            File file = new File("src/test/java/Reportfile.html");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(htmlContent.toString());
            }

        }


        @AfterSuite
//Test cleanup
        public void TestClean()
        {
            PracticeTest.driver.quit();
        }
}


