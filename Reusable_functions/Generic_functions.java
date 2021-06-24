package Reusable_functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class Generic_functions {
	public static AndroidDriver<AndroidElement> driver;
	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFCell cell,f;
	public static XSSFRow row;
	public static String CellData,path;
	public static int iter; 
	public static int col;
	public static WebElement mob;
	static File file = new File("Configuration/config.properties");
	static Properties prop = new Properties();
	public static MobileElement element;

	/* Launch the application and provide desired capabilities of connected device.Also give the URL of the Appium server*/
	public static void app_launch() throws IOException  {
		FileInputStream fileInput;
		fileInput = new FileInputStream(file);
		prop.load(fileInput);
		DesiredCapabilities cap= new DesiredCapabilities();
		cap.setCapability("deviceName",getDeviceName());
		cap.setCapability("udid", getUDID());
		cap.setCapability("platformName", getPlatformName());
		cap.setCapability("platformVersion", getPlatformVersion());
		cap.setCapability("appPackage", getAppPackage());
		cap.setCapability("appActivity", getAppActivity());
		URL url = new URL(getURL());
		driver= new AndroidDriver<AndroidElement>(url,cap);
	}

	/* Function will fetch the device name from the config.properties file*/
	public static String getDeviceName() {
		String deviceName= prop.getProperty("deviceName");
		if(deviceName!=null) return deviceName ;
		else throw new RuntimeException ("deviceName is not specified in the Config.properties");
	}

	/* Function will fetch the device UDID from the config.properties file*/
	public static String getUDID() {
		String udid= prop.getProperty("udid");
		if(udid!=null) return udid ;
		else throw new RuntimeException ("udid is not specified in the Config.properties");
	}

	/* Function will fetch the platform from the config.properties file*/
	public static String getPlatformName() {
		String platformName= prop.getProperty("platformName");
		if(platformName!=null) return platformName ;
		else throw new RuntimeException ("platformName is not specified in the Config.properties");
	}

	/* Function will fetch the platform version from the config.properties file*/
	public static String getPlatformVersion() {
		String platformVersion= prop.getProperty("platformVersion");
		if(platformVersion!=null) return platformVersion ;
		else throw new RuntimeException ("platformVersion is not specified in the Config.properties");
	}

	/* Function will fetch the app package from the config.properties file*/
	public static String getAppPackage() {
		String appPackage= prop.getProperty("appPackage");
		if(appPackage!=null) return appPackage ;
		else throw new RuntimeException ("appPackage is not specified in the Config.properties");
	}

	/* Function will fetch the app activity from the config.properties file*/
	public static String getAppActivity() {
		String appActivity= prop.getProperty("appActivity");
		if(appActivity!=null) return appActivity ;
		else throw new RuntimeException ("appActivity is not specified in the Config.properties");
	}

	/* Function will fetch the URLof the Appium server from the config.properties file*/
	public static String getURL() {
		String URL= prop.getProperty("URL");
		if(URL!=null) return URL ;
		else throw new RuntimeException ("URL is not specified in the Config.properties");
	}

	/* Refresh function to refresh the current page opened  */
	public static void page_refresh() {
		driver.navigate().refresh();
	}

	/* To wait the page till the time passed to this function */
	public static void page_wait(int time) {
		driver.manage().timeouts().implicitlyWait(time,TimeUnit.SECONDS);
	}

	/* Reading Excel file path  from config.properties   */
	public static String getFilepath() {
		String filepath= prop.getProperty("Filepath");
		if(filepath!=null) return filepath ;
		else throw new RuntimeException ("Filepath is not specified in the Config.properties");
	}

	/*To get directory path of screenshots folder*/
	public static String getDir() {
		String dirpath= prop.getProperty("Dirpath");
		if(dirpath!=null) return dirpath ;
		else throw new RuntimeException ("user Dir is not specified in the Config.properties");
	}

	/*  Taking Screenshot of failed test cases  */
	public static  void takeScreenShot(String fileName) throws IOException {
		File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, new File(getDir()+fileName+".png"));
	}

	/* To find object locator value of a particular fieldname passing to this function by loading Excel workbook*/
	public static  String OR_reader(String sheetname,String Fieldname) throws IOException  {
		File src=new File(getFilepath());
		FileInputStream finput;
		finput = new FileInputStream(src);
		workbook = new XSSFWorkbook(finput);
		sheet = workbook.getSheet(sheetname);
		int rowCount = sheet.getPhysicalNumberOfRows();
		row = sheet.getRow(0);
		for(int i=1;i<rowCount;i++) {
			cell = sheet.getRow(i).getCell(0);
			CellData = cell.getStringCellValue();
			if(CellData.equals(Fieldname))
			{
				f= sheet.getRow(i).getCell(2);
				path = f.getStringCellValue();
				break;
			}
			else
			{
				continue;
			}
		}
		return path;
	}

	/* To read test data value of a particular fieldname passing to this function using findRow function to get row number from excel sheet  */
	public static String td_reader(String fieldname) {
		sheet = workbook.getSheetAt(0);
		col= Dataiter();
		String td_value=sheet.getRow(findRow(fieldname)).getCell(col).getStringCellValue();
		return td_value;
	}

	/* To read test data value of a particular fieldname using index  where its values are seperated with a comma within cell in excel sheet  */
	public static String td_reader(String fieldname,int index){
		sheet = workbook.getSheetAt(0);
		col= Dataiter();
		String td_value = sheet.getRow(findRow(fieldname)).getCell(col).getStringCellValue();
		String[] str = td_value.split(",");
		return str[index];
	}

	/* To get row number of a particular fieldname passing to this function from excel sheet  */
	public static int findRow(String fieldname) {
		sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cell.getRichStringCellValue().getString().trim().equals(fieldname)) {
					return row.getRowNum();  
				}
			}
		}       
		return 0;
	}

	/*To get test data iteration value from config.properties file*/
	public static int Dataiter() {            
		iter=Integer.parseInt(prop.getProperty("Data_iteration"));
		return iter;		
	}

	/* Click operation for a particular fieldname that is passing to this function through finding locator value of fieldname using OR_reader function*/
	public static void click(String fieldname) throws IOException {
		driver.findElement(By.xpath(OR_reader("Object Locator", fieldname))).click();
	}
	
	/* To go back to the previous page */
	public static void browser_back(){
		driver.navigate().back();
	}
	
	/*close the application*/
	public static void  close() {
		driver.closeApp();
	}
	/*Function to clear the value in a particular field*/
	 public static void field_clear(String fieldname) throws IOException {
		mob = driver.findElement(By.xpath(OR_reader("Object Locator", fieldname)));
		mob.clear();
		 }
	 
	 public static void scrolldown() throws IOException {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.PAGE_DOWN).build().perform();
		}
	
	 /*Function to scroll down until the given the element */
	 public static void scrolldown(String elementchoose) {
		  element = (MobileElement) driver.findElement(MobileBy.AndroidUIAutomator(
			        "new UiScrollable(new UiSelector().scrollable(true))" +
			         ".scrollIntoView(new UiSelector().text(\""+elementchoose+"\"))"));
	     element.click(); 	
	 }
	 /*Function to scroll down-up and click the given the element */
	 public static void scrollAndClick(String visibleText) {
	 
		 String uiSelector = "new UiSelector().textMatches(\"" + visibleText
                 + "\")";

         String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                 + uiSelector + ");";

        driver.findElementByAndroidUIAutomator(command).click();
	        }
	    }
