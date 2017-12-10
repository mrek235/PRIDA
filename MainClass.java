import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.io.ssl.ALPNProcessor.Client;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

public class MainClass {
	private static TastingWindow main= new TastingWindow();
	private static MagicVars mv=new MagicVars();

	public static class MagicVars{
		public static final String resourcesName = "resources";
		public static final String bashScriptText=
				"mkdir -p \""+"%s"/*safeCandraPath eg. /mnt/c/Users/CanDrA.v+ */+"\""+"\n"+
				"cp -R \""+"%s"/*CandraFolder*/+
					 "\" \""+"%s"/*safeCandraPath, same with first*/+"\""+"\n"+
				//"cd \""+"%s"/*same with first*/+"\""+"\n"+
				//doThisAtJava/"mkdir -p \""+"%s"/*resultsFolderPathInsideTheResourcesPath*/+"\""+"\n"+
				"perl %s/open_candra.pl BRCA \""+"%s"/*inputFileAbsPath*/+
									  "\" > \""+"%s"/*outputFileAbsPath*/+"\""+"\n"+
				//"cd .."+"\n"+
				//"cd .."+"\n"+
				"rm -r \""+"%s"/*safePridaPath*/+"\""+"\n"+
				"rm %s"+"\n"+
				"rm \"$0\"";


		public static String	batScriptText;

				/*
				"@echo off"+"%n"+
				"<!-- : --- Self-Elevating Batch Script ---------------------------"+"%n"+
				"@whoami /groups | find \"S-1-16-12288\" > nul && goto :admin"+"%n"+
				"set \"ELEVATE_CMDLINE=cd /d \"%%~dp0\" & call \"%%~f0\" %%*\""+"%n"+
				"cscript //nologo \"%%~f0?.wsf\" //job:Elevate & exit /b"+"%n"+

				"-->"+"%n"+
				"<job id=\"Elevate\"><script language=\"VBScript\">"+"%n"+
				  "Set objShell = CreateObject(\"Shell.Application\")"+"%n"+
				  "Set objWshShell = WScript.CreateObject(\"WScript.Shell\")"+"%n"+
				  "Set objWshProcessEnv = objWshShell.Environment(\"PROCESS\")"+"%n"+
				  "strCommandLine = Trim(objWshProcessEnv(\"ELEVATE_CMDLINE\"))"+"%n"+
				  "objShell.ShellExecute \"cmd\", \"/c \" & strCommandLine, \"\", \"runas\""+"%n"+
				"</script></job>"+"%n"+
				":admin -----------------------------------------------------------"+"%n"+

				"@echo off"+"%n"+
				"echo Running as elevated user."+"%n"+
				"echo Script file : %%~f0"+"%n"+
				"echo Arguments   : %%*"+"%n"+
				"echo Working dir : %%cd%%"+"%n"+
				"echo."+"%n"+
				":: administrator commands here"+"%n"+
				":: e.g., run shell as admin"+"%n"+
				"cmd /c bash -c \"sh %s\"";*/

		public static String bashOnUbuntuOnWindowsInitialPath="/mnt/";
	}

	public static void queryCandraOnWindows(String candraFolderAbsPath,String outputAbsPath,String inputAbsPath) throws Exception{

		String  linuxCandraPath=mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(candraFolderAbsPath.charAt(0))+candraFolderAbsPath.substring(2).replace('\\','/'),
				linuxSafePridaPath=linuxCandraPath.substring(0, 7)+"prida",
				linuxSafeCandraPath=linuxSafePridaPath+"/CanDrA.v+",
				linuxInputFilePath=mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(inputAbsPath.charAt(0))+inputAbsPath.substring(2).replace('\\','/'),
				linuxOutputFileAbsPath=mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(outputAbsPath.charAt(0))+outputAbsPath.substring(2).replace('\\','/');


		File currentDir= new File(System.getProperty("user.dir"));
		File bashScript=File.createTempFile("candraTest", ".sh", currentDir);
		//bashScript.deleteOnExit();
		File batScript=File.createTempFile("candraTest", ".bat",currentDir);
		//batScript.deleteOnExit();


		try(BufferedWriter out = new BufferedWriter(new FileWriter(bashScript))){

			out.write(String.format(mv.bashScriptText,
					linuxSafeCandraPath,
					linuxCandraPath,
					linuxSafePridaPath,
					linuxSafeCandraPath,
					linuxInputFilePath,
					linuxOutputFileAbsPath,
					linuxSafePridaPath,
					//mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(bashScript.getCanonicalPath().charAt(0))+bashScript.getCanonicalPath().substring(2,bashScript.getCanonicalPath().length()-3).replace('\\','/')+"rm"
					batScript.getName()
					//TODO casename to current path, with absolute unix
					));
			out.flush();


		} catch (IOException e) {e.printStackTrace();}


		try(BufferedWriter out = new BufferedWriter(new FileWriter(batScript))){
			out.write(String.format(mv.batScriptText,
					bashScript.getName()
					));
			out.flush();

		} catch (IOException e) {e.printStackTrace();}

		Runtime.getRuntime().exec(String.format("\"%s\"",batScript.getCanonicalPath()));
	}

	private static String windowsPathToLinuxPath(String path){
		return mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(path.charAt(0))+path.substring(2).replace('\\','/');

	}

	//TODO unfinished
	public static void runCmdWithEcho(String args) throws Exception{
		ProcessBuilder   ps=new ProcessBuilder(String.format("cmd.exe && %s",args));

		ps.redirectErrorStream(true);

		Process pr = ps.start();

		BufferedReader i1 = new BufferedReader(new InputStreamReader(pr.getInputStream())),
					   i2 = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
		String line;
		while ((line = i1.readLine()) != null) {
		    System.out.println(line);
		}
		while ((line = i2.readLine()) != null) {
		    System.out.println(line);
		}
		pr.waitFor();

		i1.close();
		i2.close();
	}

	public static void main(String[] args) throws Exception {
		/*
		String candraFolderAbsPath="C:\\Users\\konuralp ilim\\workspace\\MutTasting\\resources\\CanDrA.v+",
				inputAbsPath="C:\\Users\\konuralp ilim\\workspace\\MutTasting\\resources\\input.txt",
				outputAbsPath="C:\\Users\\konuralp ilim\\workspace\\MutTasting\\resources";
		String  linuxCandraPath=mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(candraFolderAbsPath.charAt(0))+candraFolderAbsPath.substring(2).replace('\\','/'),
				linuxSafePridaPath=linuxCandraPath.substring(0, 7)+"prida",
				linuxSafeCandraPath=linuxSafePridaPath+"/CanDrA.v+",
				linuxInputFilePath=mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(inputAbsPath.charAt(0))+inputAbsPath.substring(2).replace('\\','/'),
				linuxOutputFileAbsPath=mv.bashOnUbuntuOnWindowsInitialPath+Character.toLowerCase(outputAbsPath.charAt(0))+outputAbsPath.substring(2).replace('\\','/');

		*/


	}

	private static boolean debug=false;
	private static void d(String str){
		 System.out.println(str);
	}
	private static String debugPath="c:\\users\\konuralp ilim\\desktop\\debug\\";
	private static void df(String str, String path) throws IOException{
		 BufferedWriter br = new BufferedWriter( new FileWriter(path));

		 br.write(str);

		 br.flush();
		 br.close();
	}
	private static void debugHtmlDisplay(String path){
		 try {
		     Desktop.getDesktop().browse(new File(path).toURI());
		 } catch (IOException e) {
		     // TODO Auto-generated catch block
		 }
	 }
	public static void sleepQuiet(Long mil){
		 try {
			Thread.sleep(mil);
		} catch (InterruptedException e) {
			if(debug)
				d(e.toString());
		}
	}

	private static class RLabel extends JLabel{

		 public RLabel(String name){
			 super(name);
		 }

		 public void paintComponent(Graphics g){
			 JLabel label=this;
			 Font labelFont = label.getFont();
			 String labelText = label.getText();

			 int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
			 int componentWidth = label.getWidth();


			 double widthRatio = (double)componentWidth / (double)stringWidth;

			 int newFontSize = (int)(labelFont.getSize() * widthRatio);
			 int componentHeight = label.getHeight();


			 int fontSizeToUse = (int) (Math.min(newFontSize, componentHeight)*0.7);


			 label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));

			 super.paintComponent(g);
		 }


	 }

	private static class TastingWindow{
		private TastingModel		m=new TastingModel();
		private TastingView 		v=new TastingView();
		private TastingController 	c=new TastingController();

		private class TastingModel{
			/*package*/ JButton
				inputFileBtn=new JButton("Input File Location:"),
				outputDestBtn=new JButton("OutputDestination:"),
				searchBtn=new JButton("Search");
			/*package*/ RLabel
				inputFileLbl=new RLabel("No File Selected:"),
				outputDestLbl=new RLabel(new File("").getAbsolutePath()),
				statusLbl=new RLabel("No input Selected");
			/*package*/ String
				inputFileDest,
				outputDest=new File("").getAbsolutePath(),
				outputFileName=".tsv";
			/*package*/ JFrame window;
			/*package*/ byte state=0;
				public TastingModel(){
					mv.batScriptText=retrieveAutoBat();
					System.setProperty("webdriver.gecko.driver",new File(mv.resourcesName+File.separator+"geckodriver.exe").getAbsolutePath());
				}
				private String retrieveAutoBat() {
					try{
						BufferedReader br= new BufferedReader(new FileReader(mv.resourcesName+File.separator+"prot2.bat"));
						String batText=null;
						StringBuilder sb= new StringBuilder();
						sb.append(br.readLine());

						while(br.ready()){
						sb.append(String.format("%n"));
						sb.append(br.readLine());
						}

						batText=sb.toString();
						batText=batText.replaceAll("%", "%%")
									   .replaceAll("%%s", "%s");
					return batText;


					}catch(Exception e){e.printStackTrace();}
					return null;
				}
			}

		private class TastingView {

			public TastingView(){
				m.window=new JFrame("Mutation Numnum");
				try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {e.printStackTrace();}
				m.window.setSize(800, 600);
				m.window.setMinimumSize(new Dimension(600,400));
				//m.window.setResizable(false);
				m.window.setLocation(100,100);
				m.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				m.window.setContentPane(new JPanel());
				m.window.setVisible(true);


				//statusLbl.setForeground(Color.white);
				m.statusLbl.setBackground(Color.WHITE);
				m.statusLbl.setOpaque(true);
				m.statusLbl.setHorizontalAlignment(SwingConstants.CENTER);

				m.window.addComponentListener(new ComponentAdapter() {
				    public void componentResized(ComponentEvent e) {
				        placeComponents();
				    }

					private void placeComponents() {
						Ratiolizer r = new Ratiolizer(m.window.getWidth(),m.window.getHeight(),80,60);
						m.statusLbl.setBounds(r.locate(20, 5, 40, 5));

						m.inputFileLbl.setBounds(r.locate(10, 15, 60, 0,0,-25,0,25));
						m.inputFileBtn.setBounds(r.locate(10, 15, 60, 5));

						m.outputDestLbl.setBounds(r.locate(10, 25, 60, 0,0,-25,0,25));
						m.outputDestBtn.setBounds(r.locate(10, 25, 60, 5));

						m.searchBtn.setBounds(r.locate(30, 30, 20, 5,0,25,0,0));

					}
				});



				m.inputFileBtn.addActionListener(e->c.inputFileBtn());
				m.outputDestBtn.addActionListener(e->c.outputDestBtn());
				m.searchBtn.addActionListener(e->c.searchBtn());

				JPanel pane= (JPanel) m.window.getContentPane();

				pane.setLayout(null);
				pane.add(m.statusLbl);
				pane.add(m.inputFileLbl);
				pane.add(m.inputFileBtn);
				pane.add(m.outputDestLbl);
				pane.add(m.outputDestBtn);
				pane.add(m.searchBtn);

				m.window.setVisible(true);

				m.statusLbl.grabFocus();

			}

		}

		private class TastingController{

			private void searchBtn() {
				if(m.inputFileLbl.getText().equals("No File Selected:")){
					m.statusLbl.setText("Select an input File First");
				}else if(m.inputFileDest!=null){
					Timer t= new Timer(500,(e)->fetching());
					t.start();
					m.statusLbl.setText("Fetching the data");

					m.searchBtn.setEnabled(false);

					final SearchState[] endState= new SearchState[3];//0:mutTaster,1:condel,2:candra
					endState[0]=SearchState.SEARCHING;
					endState[1]=SearchState.SEARCHING;
					endState[2]=SearchState.SEARCHING;

					try {
						File tempF=File.createTempFile("%s-", ".tsv", new File(m.outputDest));
						m.outputFileName=tempF.getCanonicalPath();
						tempF.delete();
					} catch (IOException e2) {e2.printStackTrace();}

					final ArrayList<Thread> ts= new ArrayList<Thread>(8);

					ts.add(new Thread(()->{
						try {
							endState[0]=SearchState.SEARCHING;
							downloadTsvMutTaster(m.inputFileDest,String.format(m.outputFileName,"mutTaster"));
							endState[0]=SearchState.FINISHED;

						} catch (Exception e) {

							e.printStackTrace();
							endState[0]=SearchState.ERROR;
					}}));

					ts.add(new Thread(()->{
						try {
							endState[1]=SearchState.SEARCHING;
							downloadTsvCondel(m.inputFileDest,String.format(m.outputFileName,"condel"));
							endState[1]=SearchState.FINISHED;

						} catch (Exception e) {

							e.printStackTrace();
							endState[1]=SearchState.ERROR;
						}}));

					ts.add(new Thread(()->{
						try {
							endState[2]=SearchState.SEARCHING;
							downloadTsvCandra(new File("resources"+File.separator+"CanDrA.v+").getCanonicalPath(),m.inputFileDest,String.format(m.outputFileName,"candra"));
							endState[2]=SearchState.FINISHED;

						} catch (Exception e) {

							e.printStackTrace();
							endState[2]=SearchState.ERROR;
						}}));

					for(Thread th:ts)
						th.start();


					new Thread(()->{
						for(Thread th:ts)
							try {
								th.join();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}

						processTheRawOutputs();




						StringBuffer resultDisplayText=new StringBuffer("");
						SearchState state;
						int i = 0;

						switch(i){
						case 0: resultDisplayText.append("Mut. Taster: ");
							break;
						case 1: resultDisplayText.append("Condel: ");
							break;
						case 2: resultDisplayText.append("Candra: ");
							break;
						}

						state=endState[i];
						switch(state){
							case FINISHED: resultDisplayText.append("DONE");
								break;
							case ERROR: resultDisplayText.append("FAILED");
								break;
							case SEARCHING: resultDisplayText.append("PUNCH JVM DEVELOPER");
								break;
						}

						for(i = 1;i<endState.length;i++ ){
							resultDisplayText.append(" | ");
							switch(i){
							case 0: resultDisplayText.append("Mut. Taster: ");
								break;
							case 1: resultDisplayText.append("Condel: ");
								break;
							case 2: resultDisplayText.append("Candra: ");
								break;
							}

							state=endState[i];
							switch(state){
								case FINISHED: resultDisplayText.append("DONE");
									break;
								case ERROR: resultDisplayText.append("FAILED");
									break;
								case SEARCHING: resultDisplayText.append("PUNCH JVM DEVELOPER");
									break;
							}
						}

						t.stop();
						m.statusLbl.setText(resultDisplayText.toString());
						m.searchBtn.setEnabled(true);

					}).start();




				}else{
					//TODO
					d("error1");
				}

			}

			private void processTheRawOutputs() {
				// TODO Auto-generated method stub

			}

			private void fetching() {
				switch(m.state){
				case 0:
					m.statusLbl.setText("Fetching the data.");
					m.window.getContentPane().repaint();
					m.state=1;
					break;
				case 1:
					m.statusLbl.setText("Fetching the data. .");
					m.window.getContentPane().repaint();
					m.state=2;
					break;
				case 2:
					m.statusLbl.setText("Fetching the data. . .");
					m.window.getContentPane().repaint();
					m.state=3;
					break;
				case 3:
					m.statusLbl.setText("Fetching the data");
					m.window.getContentPane().repaint();
					m.state=0;
					break;
				}
			}

			private void outputDestBtn(){
				JFileChooser jiChooser = new JFileChooser();
				jiChooser.setDialogTitle("Select the input file");
				jiChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jiChooser.setAcceptAllFileFilterUsed(false);
				int option = jiChooser.showOpenDialog(m.window.getContentPane());
				if(option!=JFileChooser.APPROVE_OPTION)
					{return;}

				m.outputDestLbl.setText("Output Destination:"+jiChooser.getSelectedFile().getAbsolutePath());

				try {
					m.outputDest=jiChooser.getSelectedFile().getCanonicalPath();
					d(m.outputDest);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				m.statusLbl.setText("Output is selected");
			}

			private void inputFileBtn() {
				JFileChooser jiChooser = new JFileChooser();
				jiChooser.setDialogTitle("Select the input file");
				jiChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jiChooser.setAcceptAllFileFilterUsed(false);
				int option = jiChooser.showOpenDialog(m.window.getContentPane());
				if(option!=JFileChooser.APPROVE_OPTION)
					{return;}

				m.inputFileLbl.setText("File Destination:"+jiChooser.getSelectedFile().getAbsolutePath());

				try {
					m.inputFileDest=jiChooser.getSelectedFile().getCanonicalPath();
					d(m.inputFileDest);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				m.statusLbl.setText("Input is selected");
			}

		}

		private enum SearchState{
			SEARCHING,
			FINISHED,
			ERROR
		}

	}

	public static String getOsType(){
		//TODO
		return null;
	}


	public static void downloadTsvCondel(String inputFilePath, String outputFilePath) throws Exception{
		String test_file_name=inputFilePath;
		String str="hmm",
				str2="mmh";
		boolean first_time=true;
		WebDriver driver;
		RemoteWebDriver browser;
		while(true){
			try{
				FirefoxProfile profile= new FirefoxProfile();
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.manager.showWhenStarting", false);
				profile.setPreference("browser.downloads.dir", outputFilePath);

				profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip");
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/text");

				DesiredCapabilities capabilities=DesiredCapabilities.firefox();
				capabilities.setCapability("marionette", true);
				capabilities.setCapability(FirefoxDriver.PROFILE, profile);

				driver= new FirefoxDriver(capabilities);

				//ideal/driver.manage().window().setSize(new org.openqa.selenium.Dimension( 0, 0));

				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				Double w = screenSize.getWidth();
				Double h = screenSize.getHeight();
				driver.manage().window().setPosition(new Point(w.intValue(), h.intValue()));

				//debug/driver.manage().window().setPosition(new Point(50, 50));
				//debug/
				driver.manage().window().setSize(new org.openqa.selenium.Dimension( 100, 100));


				browser = (RemoteWebDriver) driver;
		        first_time=true;
		        break;
			}catch(Exception e){
			        if(first_time){
			            e.printStackTrace();
			            first_time=false;
			        }
			        Thread.sleep(250);
			        continue;
			}
		}

		WebElement inter;
		browser.get("http://bg.upf.edu/fannsdb/signin?next=%2Ffannsdb%2Fquery%2Fcondel");
		inter = browser.findElementById("username");
		inter.click();

		while(true){
		    try{
		        inter = browser.findElementByXPath("//input[@type='text']");
		        inter.sendKeys("pridaproject@gmail.com");
		        first_time=true;
		        break;
		    }catch(Exception e){
		        if(first_time){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}

		while(true){
		    try{
		        inter = browser.findElementByXPath("//input[@name='password']");
		        inter.sendKeys("sirdancealot");
		        inter=browser.findElementByXPath("//button[@type='submit']");
		        inter.click();
		        first_time=true;
		        break;
		    }catch(Exception e){
		        if(first_time){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}

		while(true){
		    try{
		        //browser.switchTo().accept();
		        first_time=true;
		        break;
		    }catch(Exception e){
		        if(first_time){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}

		while(true){
		    try{
		        //browser.switchTo().accept();
		    	browser.switchTo().alert().accept();
		        first_time=true;
		        break;
		    }catch(Exception e){
		        if(first_time){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}

		while(true){
		    try{
		        inter=browser.findElementByXPath("//input[@type='file']");
		        inter.sendKeys(inputFilePath);
		        first_time=true;
		        break;
		    }catch(Exception e){
		        if(first_time){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}


		while(true){
		    try{
		    	inter = browser.findElementByXPath("//input[@id='project_name']");
				inter.clear();
				inter.sendKeys("PRIDA");

				inter = browser.findElementByXPath("//form[@role='form']");
				inter.submit();
		        first_time=true;
		        break;
		    }catch(Exception e){
		        if(first_time){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}

		String urlPath = null;
		while(true){
		    try{
				ArrayList<WebElement> downs =	(ArrayList<WebElement>) browser.findElementsByXPath("//a[text() = ' Download']");
				for(WebElement d : downs)
					d(d.toString());
				WebElement d = downs.get(downs.size()-1);
				//urlPath=browser.getCurrentUrl();
				//urlPath=d.getAttribute("href");
				//d(urlPath);
				d.click();
				break;
		    }catch(Exception e){
		        if(true){
		            e.printStackTrace();
		            first_time=false;
		        Thread.sleep(250);
		        continue;
		        }
		    }
		}

		//TODO remove the downloaded file from the list before closing

		d("im here 1");
		try{
		browser.quit();
		browser.close();
		}catch(Exception e){}

		d("im here 2");
		String downloadsPath= System.getProperty("user.home")+File.separator+"Downloads";

		ZipInputStream zip=new ZipInputStream( new FileInputStream(downloadsPath+File.separator+"PRIDA.zip"));

		zip.getNextEntry();


		File tempFile= File.createTempFile("condel", ".temp");

		BufferedInputStream in1 = new BufferedInputStream(zip,1024*16);
		BufferedOutputStream out1= new BufferedOutputStream(new FileOutputStream(tempFile),1024*16);


    	IOUtils.copy(in1,out1);
    	d("im here 4");
    	zip.close();
    	out1.flush();
    	out1.close();
    	in1.close();
    	zip.close();

    	BufferedReader in2= new BufferedReader(new FileReader(tempFile),1024*16);
		BufferedWriter out2=new BufferedWriter(new FileWriter(outputFilePath),1024*16);

		in2.readLine();
		in2.readLine();
		in2.readLine();

		IOUtils.copy(in2,out2);

		in2.close();
		out2.close();
		d("im here 5");

		/*

		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

	    HtmlPage currentPage = null;
		try {
		WebClient client;
		client = new WebClient(BrowserVersion.CHROME);

			while(true){try{
				currentPage= client.getPage("http://bg.upf.edu/fannsdb/signin?next=%2Ffannsdb%2Fquery%2Fcondel");
				df(currentPage.asXml(),debugPath+"test.html");

					((HtmlElement) currentPage.getFirstByXPath("//a[@class='anonymous']")).click();
					MainClass.sleepQuiet(1500L);
					((HtmlInput) currentPage.getFirstByXPath("//input[@type='text']")).setNodeValue("pridaproject@gmail.com");
					((HtmlInput) currentPage.getFirstByXPath("//input[@type='password']")).setNodeValue("sirdancealot");
					currentPage=((HtmlForm) currentPage.getFirstByXPath("//form[@class='auth0-lock-widget']")).click();
				    //client.getPage(urlPath);
					MainClass.sleepQuiet(1500L);
				    df(currentPage.asXml(),debugPath+ Math.round(Math.random()*1000)+".html");
				}catch(Exception e){
					Thread.sleep(1000L);
					e.printStackTrace();
					d("htmlunit connecting...");
					continue;}
					break;
			}

			ZipInputStream zip=new ZipInputStream( client.getPage(urlPath).getWebResponse().getContentAsStream()  );
			//currentPage.cleanUp();

			zip.getNextEntry();


			BufferedInputStream in = new BufferedInputStream(zip,1024*16);
			BufferedOutputStream out= new BufferedOutputStream(new FileOutputStream(outputFilePath+File.separator+fileName),1024*16);


	    	IOUtils.copy(in,out);

	    	zip.close();
	    	out.flush();
	    	out.close();


		} catch (Exception e) {
			e.printStackTrace();
		}

		*/
	}

	public static void downloadTsvMutTaster(String inputFilePath, String outputFilePath) throws Exception{

			//HtmlUnitDriver browser = new HtmlUnitDriver(BrowserVersion.FIREFOX_45);
			//browser.setJavascriptEnabled(true);

			DesiredCapabilities capabilities=DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);
			WebDriver driver = new FirefoxDriver(capabilities);
			//driver.manage().window().setSize(new org.openqa.selenium.Dimension( 0, 0));
			driver.manage().window().setSize(new org.openqa.selenium.Dimension( 100, 100));
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Double w = screenSize.getWidth();
			Double h = screenSize.getHeight();
			driver.manage().window().setPosition(new Point(w.intValue(), h.intValue()));


			RemoteWebDriver browser = (RemoteWebDriver) driver;

			browser.get("http://www.mutationtaster.org/StartQueryEngine.html");
			browser.findElementByXPath("//input[@type='file']").sendKeys(inputFilePath);
			browser.findElementByName("name").sendKeys("PRIDA");

			browser.findElementByName("email").sendKeys("ogulcan.cingiler@ozu.edu.tr");
			browser.findElementByName("tgp_filter_homo").click();


			browser.findElementByName("min_cov").sendKeys("0");

			browser.findElementByName("Submit").click();



			while(true){
				try{
				browser.findElementByName("tuples");
				}catch(Exception e){
				Thread.sleep(1000L);
				d("waiting for mut taster to prepare");
				continue;}
				break;
			}



			browser.findElementByXPath("//input[@value='export as TSV']").click();
			String urlPath=	browser.getCurrentUrl();
			d(urlPath);


			try{
			browser.quit();
			}catch(Exception e){}

			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

		    //browser açtım

		    //sayfayı tutacağım değişkeni tanımladım
		    HtmlPage currentPage = null;
			//try {

				//istediğim sayfanın linkine değiştirdim, pythondan bu sayfanın linki gelebilirse buna gerek yok, ama yük değil buraya
				urlPath=urlPath.substring(0, urlPath.lastIndexOf("/")+1)+"PRIDA_list.html";

				//sayfaya gidiyor
				while(true){
					WebClient client = new WebClient(BrowserVersion.FIREFOX_45);
					try{
						currentPage  = client.getPage(urlPath);
					}catch(Exception e){
						Thread.sleep(1000L);
						d("htmlunit connecting...");
						continue;}
						break;
				}

				//link olan elementi çekiyor
				HtmlElement a= currentPage.getFirstByXPath("/html/body/a");
				currentPage.cleanUp();

				//linke basınca indirme sayfasını veriyor, indirme sayfasının yolladığı veriyi/response'i web sitesine
				//çevirmeye çalışmak yerine byte verisi olarak tut diyorum, bu input'u zip input stream ile wrap ediyorum
				//ki gelen veri zip verisi, indirip dosyaya yazıp bi daha zip i açmaya çalışmak yerine daha indirirken zip i açsın

				ZipInputStream zip=new ZipInputStream( a.click().getWebResponse().getContentAsStream() );

				//zip'te entry'ler vardır içindeki her dosya için, burda zip tek dosya içeriyor, sıradakini al diyoruz, ilk ve tek entry'i okunabilir yapıyor
				zip.getNextEntry();

				//sonraki iki satır performansla alakalı, bunlar olmadan da çalışır da
				//küçük parçalar halinde atmak yerine büyük buffer halinde atması daha verimli
				//buffer size'i 16kb yaptım ama o kullandığım işlemcinin cache'ine bağlı
				//çok büyük yapınca cache'de değil ram'de tutuo veriyi performans yine düşüyor
				//çok az olursa tek tek yollamasından farkı kalmıyor, vs, gereksiz detay burası
				BufferedInputStream in = new BufferedInputStream(zip,1024*16);
				BufferedOutputStream out= new BufferedOutputStream(new FileOutputStream(outputFilePath),1024*16);

				//in'den al out'a ver
		    	IOUtils.copy(in,out);

		    	//bunlar işletim sisteminden izin alıyor, işletim sistemi de bunların her an gönderebileceği
		    	//istekler için resource harcıo (cpu time, ram) o yüzden kapamamak resource leak
		    	//flush da buffer la out yapıldığı zaman en son hali buffer dolmamış olsa da yazdır anlamında
		    	//flush demesen de çalışır ama çalışmaya da bilir, emin olmak için sifonu çekiyorlar genelde
		    	zip.close();
		    	out.flush();
		    	out.close();

		    //input/output işlerinde java zorunlu exception kullandırtıyor, hata çıkarsa hata neymiş konsol'a yazdır
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}



			/*
			WebClient client = new WebClient(BrowserVersion.FIREFOX_45);
			client.getOptions().setJavaScriptEnabled(true);
			client.getOptions().setCssEnabled(true);
			client.setJavaScriptTimeout(180000);
			HtmlPage browser = client.getPage("http://www.mutationtaster.org/StartQueryEngine.html");

			((HtmlElement)browser.getFirstByXPath("//input[@type='file']")).setAttribute("value", inputFilePath);;
			((HtmlElement)browser.getElementByName("name")).setAttribute("value", "PRIDA");
			((HtmlElement)browser.getElementByName("email")).setAttribute("value" ,"ogulcan.cingiler@ozu.edu.tr");
			((HtmlElement)browser.getElementByName("tgp_filter_homo")).click();


			((HtmlElement)browser.getElementByName("min_cov")).setAttribute("value" ,"0");

			browser=((HtmlInput)browser.getElementByName("Submit")).click();
			*/
	 }

	public static void downloadTsvCandra(String candraFolderPath,String inputFilePath, String outputFilePath) throws Exception{

		String os= OSCheck();
		switch(os){
			case "WINDOWS": queryCandraOnWindows(candraFolderPath,outputFilePath,inputFilePath);
				break;
			case "LINUX":
				break;
			case "MAC_OS":
				break;
		}

	}



//taken from StackOverflow

public String OSCheck(){
	String osName = System.getProperty("os.name");

	String osNameMatch = osName.toLowerCase();

	if(osNameMatch.contains("linux")) {
		return  "LINUX";
	}else if(osNameMatch.contains("windows")) {
		return  "WINDOWS";
	}else if(osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
		return  "SOLARIS";
	}else if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
		return  "MAC_OS";
	}else {
	}

}


}
