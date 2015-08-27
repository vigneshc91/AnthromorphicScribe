package speech;


import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.awt.FlowLayout;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import javax.imageio.*;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;



import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;



	@SuppressWarnings("serial")
	public class AutomaticSpeechRecognition extends JFrame implements ActionListener{
		static JTextArea text = new JTextArea(20,40);
	//	JPanel panel = new JPanel();
	//	JTextArea console_msg = new JTextArea(5,62);
		JButton start = new JButton("Start");
	//	JTextField file_name = new JTextField(15);
	//	JButton browse = new JButton("Browse");
	//	JButton save = new JButton("Save");
	//	JButton start_rec = new JButton("Start Recording");
		JFileChooser get_file = new JFileChooser();
		JFileChooser save_file = new JFileChooser();
		JFileChooser saveAs_file = new JFileChooser();
		JFileChooser save_audio_to_disk = new JFileChooser();
		JFileChooser convert_source = new JFileChooser();
		JFileChooser convert_destination = new JFileChooser();
		FileFilter doc_files = new FileNameExtensionFilter("doc files","doc");
		FileFilter wav_files = new FileNameExtensionFilter("WAV files","wav");
		BufferedImage anthro_img;
		ImageIcon processing = new ImageIcon(this.getClass().getResource("loading.gif"));
		ImageIcon import_img = new ImageIcon(this.getClass().getResource("import.png"));
		ImageIcon save_img = new ImageIcon(this.getClass().getResource("save1.png"));
		ImageIcon saveAs_img = new ImageIcon(this.getClass().getResource("save_as.png"));
		ImageIcon exit_img = new ImageIcon(this.getClass().getResource("exit.png"));
		ImageIcon start_rec_img = new ImageIcon(this.getClass().getResource("start rec.png"));
		ImageIcon stop_rec_img = new ImageIcon(this.getClass().getResource("stoprecd.png"));
		ImageIcon play_back_img = new ImageIcon(this.getClass().getResource("play.png"));
		ImageIcon play_img = new ImageIcon(this.getClass().getResource("play icon.png"));
		ImageIcon stop_img = new ImageIcon(this.getClass().getResource("stop icon.png"));
		ImageIcon save_to_disk_img = new ImageIcon(this.getClass().getResource("save to disk.png"));
		ImageIcon select_src_img = new ImageIcon(this.getClass().getResource("src.png"));
		ImageIcon select_dest_img = new ImageIcon(this.getClass().getResource("dest.png"));
		ImageIcon how_to_use_img = new ImageIcon(this.getClass().getResource("help.png"));
		ImageIcon about_img = new ImageIcon(this.getClass().getResource("about.png"));
		static JLabel for_proc = new JLabel();
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu record = new JMenu("Record");
		JMenu play_back = new JMenu("Play Back"); 
		JMenu convert = new JMenu("Convert");
		JMenu help = new JMenu("Help");
		JMenuItem imp = new JMenuItem("Import", import_img);
		JMenuItem save = new JMenuItem("Save", save_img);
		JMenuItem saveAs = new JMenuItem("Save As...", saveAs_img);
		JMenuItem exit = new JMenuItem("Exit", exit_img);
		JMenuItem start_rec = new JMenuItem("Start Recording", start_rec_img);
		JMenuItem stop_rec = new JMenuItem("Stop Recording", stop_rec_img);
	//	JMenuItem play_back = new JMenuItem("Play Back", play_back_img);
	//	JMenuItem convert = new JMenuItem("Convert");
		JMenuItem play = new JMenuItem("Play", play_img); 
	//	JMenuItem pause = new JMenuItem("Pause");
		JMenuItem stop = new JMenuItem("Stop", stop_img); 
		JMenuItem save_to_disk = new JMenuItem("Save To Disk", save_to_disk_img);
		JMenuItem select_src = new JMenuItem("Select Source", select_src_img);
		JMenuItem select_dest = new JMenuItem("Select Destination", select_dest_img);
		JMenuItem how_to_use = new JMenuItem("How To Use", how_to_use_img);
		JMenuItem about = new JMenuItem("About", about_img);
		JFrame hel;
		File src_convert_file;
		File dest_convert_file;
		String wav_file_name = "";
		boolean isHelpViewed = false;
		boolean isPlayed;
		Thread thread;
		Thread playBack;
		//ProgressMonitor process = new ProgressMonitor(Frame.this,"Processing Automatic Speech Regonition",null,0,0);
		AutomaticSpeechRecognition() throws IOException{
			super("Anthromorphic Scribe");
			
			setSize(700, 700);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//	addWindowListener(new WindowEventHandler());
		//	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			FlowLayout flow = new FlowLayout();
			setLayout(flow);
		//	BorderLayout border = new BorderLayout();
		//	panel.setLayout(border);
			Menu();
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			add(new JScrollPane(text,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
		//	console_msg.setLineWrap(true);
		//	console_msg.setWrapStyleWord(true);
		//	add(new JScrollPane(console_msg,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
			//console_msg.setBounds(110, 110, 10, 10);
		//	File temp_file = File.createTempFile("anthro_Scribe_record", ".wav");
			add(start);
			start.addActionListener(this);
			//add(panel);
		//	add(start_rec);
		//	start_rec.addActionListener(this);
			for_proc.setIcon(processing);
			add(for_proc);
			
			
			//add(console_msg);
		//	add(console_msg);
		//	add(file_name);
		//	add(browse);
		//	browse.addActionListener(this);
			get_file.addChoosableFileFilter(wav_files);
		//	add(save);
		//	save.addActionListener(this);
			save_file.addChoosableFileFilter(doc_files);
			save_audio_to_disk.addChoosableFileFilter(wav_files);
			convert_destination.addChoosableFileFilter(wav_files);
			get_file.setAcceptAllFileFilterUsed(false);
			get_file.setMultiSelectionEnabled(false);
			save_file.setAcceptAllFileFilterUsed(false);
			save_file.setMultiSelectionEnabled(false);
			save_audio_to_disk.setAcceptAllFileFilterUsed(false);
			save_audio_to_disk.setMultiSelectionEnabled(false);
			convert_destination.setAcceptAllFileFilterUsed(false);
			convert_destination.setMultiSelectionEnabled(false);
			anthro_img  = ImageIO.read(this.getClass().getResource("speech img.png"));
			
			setIconImage(anthro_img);
			
			setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
			
			onLoad();
		}
		
	public	void Menu()
		{
			//file.add(new JSeparator());
			//help.add(new JSeparator());
			file.setMnemonic(KeyEvent.VK_F);
			record.setMnemonic(KeyEvent.VK_R);
			play_back.setMnemonic(KeyEvent.VK_P);
			convert.setMnemonic(KeyEvent.VK_C);
			help.setMnemonic(KeyEvent.VK_H);
		/*	JMenuItem imp = new JMenuItem("Import", KeyEvent.VK_I);
		//	JMenuItem rec = new JMenuItem("Record", KeyEvent.VK_R);
			JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
			JMenuItem saveAs = new JMenuItem("Save As...", KeyEvent.VK_A);
			JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
			JMenuItem start_rec = new JMenuItem("Start Recording", KeyEvent.VK_S);
			JMenuItem stop_rec = new JMenuItem("Stop Recording", KeyEvent.VK_O);
			JMenuItem play_back = new JMenuItem("Play Back",KeyEvent.VK_P);
			JMenuItem save_to_disk = new JMenuItem("Save To Disk", KeyEvent.VK_S);
			JMenuItem about = new JMenuItem("About"); */
		//	rec.add(new JSeparator());
		//	save.add(new JSeparator());
		//	saveAs.add(new JSeparator());
		//	exit.add(new JSeparator());
			imp.setMnemonic(KeyEvent.VK_I);
			save.setMnemonic(KeyEvent.VK_S);
			saveAs.setMnemonic(KeyEvent.VK_A);
			exit.setMnemonic(KeyEvent.VK_X);
			start_rec.setMnemonic(KeyEvent.VK_S);
			stop_rec.setMnemonic(KeyEvent.VK_O);
// 			play_back.setMnemonic(KeyEvent.VK_P);
			save_to_disk.setMnemonic(KeyEvent.VK_S);
			select_src.setMnemonic(KeyEvent.VK_R);
			select_dest.setMnemonic(KeyEvent.VK_T);
			play_back.setIcon(play_back_img);
			file.add(imp);
			imp.addActionListener(this);
			//imp.setMnemonic('I');
			imp.setAccelerator(KeyStroke.getKeyStroke('I', CTRL_DOWN_MASK));
			file.add(save);
			save.addActionListener(this);
			save.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_DOWN_MASK));
			file.add(saveAs);
			saveAs.addActionListener(this);
			
			file.add(convert);
			convert.addActionListener(this);
		//	file.add(rec);
		//	rec.addActionListener(this);
		//	rec.setAccelerator(KeyStroke.getKeyStroke('R', CTRL_DOWN_MASK));
			file.add(exit);
			exit.addActionListener(this);
			exit.setAccelerator(KeyStroke.getKeyStroke('X', ActionEvent.ALT_MASK));
			record.add(start_rec);
			start_rec.addActionListener(this);
			start_rec.setAccelerator(KeyStroke.getKeyStroke('R', CTRL_DOWN_MASK));
			record.add(stop_rec);
			stop_rec.addActionListener(this);
			stop_rec.setAccelerator(KeyStroke.getKeyStroke('O', CTRL_DOWN_MASK));
			record.add(play_back);
//			play_back.addActionListener(this);
//			play_back.setAccelerator(KeyStroke.getKeyStroke('P', ActionEvent.ALT_MASK));
			play_back.add(play); 
			play.addActionListener(this); 
		//	play_back.add(pause);
		//	pause.addActionListener(this);
			play_back.add(stop); 
			stop.addActionListener(this); 
			record.add(save_to_disk);
			save_to_disk.addActionListener(this);
			save_to_disk.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
			convert.add(select_src);
			select_src.addActionListener(this);
			select_src.setAccelerator(KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
			convert.add(select_dest);
			select_dest.addActionListener(this);
			select_dest.setAccelerator(KeyStroke.getKeyStroke('T', ActionEvent.ALT_MASK));
			help.add(how_to_use);
			how_to_use.addActionListener(this);
			how_to_use.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
			help.add(about);
			about.addActionListener(this);
			menu.add(file);
			menu.add(record);
			//menu.add(play_back);
			menu.add(convert);
			menu.add(help);
			setJMenuBar(menu);
			
			
		}
		

		void onLoad(){
			for_proc.setVisible(false);
			text.setEditable(false);
			stop_rec.setEnabled(false);
			play_back.setEnabled(false);
			stop.setEnabled(false);
			save_to_disk.setEnabled(false);
			select_dest.setEnabled(false);
			thread = new JavaSoundRecorder();
			
			
		//	((JavaSoundRecorder) thread).init();
		//	PrintStream out = new PrintStream(new consoleRedirect(text));
		//	System.setOut(out);
		//	System.setErr(out);
			addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent evt){
					int confirm = JOptionPane.showConfirmDialog(null, "Are You Want To Exit...", "Exit", JOptionPane.YES_NO_OPTION);
					if(confirm == JOptionPane.OK_OPTION){
					
					if((!((JavaSoundRecorder) thread).isTempFileExist()) && (isPlayed == true)){
					
						((playBackAudio) playBack).stopPlay();
						((JavaSoundRecorder) thread).deleteTempFile();
					}
					//System.out.print("closing");
					if (isHelpViewed == true){
						hel.dispose();
					}
					System.exit(1);
					//dispose();
					//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				}
			});
			
		//	start_rec.setVisible(false);
			
			//for_proc.hide();
		//	file_name.disable();
		//	save.disable();
		}
		
	/*	void ReadFromFile(){
			text.setText("");
			try{
				FileReader file = new FileReader("output.txt");
				BufferedReader buff = new BufferedReader(file);
				boolean eof = false;
				while (!eof) {
					String line = buff.readLine();
					if (line == null)
						eof = true;
					else
						text.append(line + " ");
				}
				buff.close();
				
			} catch (IOException e) {
				System.out.println("Error — " + e.toString());
			}
			
			
		} */
		
		
		
		public  void actionPerformed(final ActionEvent event){
			//Object source = event.getSource();
			
	//Import option from the file menu
			
			if(event.getActionCommand().equals("Import")){
				int ret = get_file.showOpenDialog(this);
				if(ret == JFileChooser.APPROVE_OPTION){
					File s = get_file.getSelectedFile();
					wav_file_name = s.toString();
					JOptionPane.showMessageDialog(null, "File: "+wav_file_name+" Imported Successfully...", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
	//Save option from the file menu
			
			else if(event.getActionCommand().equals("Save")){
				
				String src =  text.getText();
				
				if(src.equals("")){
					JOptionPane.showMessageDialog(null, "The Document Is Empty! ", "Warning", JOptionPane.WARNING_MESSAGE);
					
				}
				else{
					
				int ret = save_file.showSaveDialog(this);
				if(ret == JFileChooser.APPROVE_OPTION){
					
					File saved_file = save_file.getSelectedFile();
					String file_name = saved_file.toString();
					if(!file_name.toLowerCase().endsWith(".doc")){
						saved_file = new File(file_name+".doc");
					}
					try{
						FileWriter fw = new FileWriter(file_name);
						fw.write(src);  
						fw.close();
						
					}
					catch(IOException e){
						System.err.println(e);
					}
				}
			 }
			}
			
	//Save As option from the file menu
			
			else if(event.getActionCommand().equals("Save As...")){
				String src =  text.getText();
				
				if(src.equals("")){
					JOptionPane.showMessageDialog(null, "The Document Is Empty! ", "Warning", JOptionPane.WARNING_MESSAGE);
					
				}
				else{
				saveAs_file.setDialogTitle("Save As");
				int ret = saveAs_file.showSaveDialog(this);
				
				if(ret == JFileChooser.APPROVE_OPTION){
					
					File saved_file = saveAs_file.getSelectedFile();
					String file_name = saved_file.toString();
					try{
						FileWriter fw = new FileWriter(file_name);
						fw.write(src);  
						fw.close();
						
					}
					catch(IOException e){
						System.err.println(e);
					}
				}
			 }
			}
			
	//Convert option from the file menu
	/*		else if(event.getActionCommand().equals("Convert")){
				for_proc.setVisible(true);
				convertToWavFormat();
				JOptionPane.showMessageDialog(null, "Converted Successfully...", "success", JOptionPane.INFORMATION_MESSAGE);
				for_proc.setVisible(false);
			} */
			
	//Record option from the file menu
	/*		else if(event.getActionCommand().equals("Record")){
				start_rec.setVisible(true);
			} */
			
	//Exit option from the file menu
			
			else if(event.getActionCommand().equals("Exit")){
				//System.exit(1);
				int confirm = JOptionPane.showConfirmDialog(null, "Are You Want To Exit...", "Exit", JOptionPane.YES_NO_OPTION);
				if(confirm == JOptionPane.OK_OPTION){
				
				if((!((JavaSoundRecorder) thread).isTempFileExist()) && (isPlayed == true)){
				
					((playBackAudio) playBack).stopPlay();
					((JavaSoundRecorder) thread).deleteTempFile();
				}
				//System.out.print("closing");
				if (isHelpViewed == true){
					hel.dispose();
				}
				System.exit(1);
				//dispose();
				//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
			
	
			
	//Start Recording button to start the audio recording from the record option of file menu
	//	 Object obj = event.getSource();
			// final JavaSoundRecorder recorder = new JavaSoundRecorder();
	/*		if(obj.equals(start_rec)){
				Thread th = new Thread(new Runnable(){
					public void run(){
				if(start_rec.getText().startsWith("Start Recording")){
						thread = new JavaSoundRecorder();
						start_rec.setLabel("Stop Recording");
						for_proc.setVisible(true);
						
						thread.start();
						*/
					/*	 try {
			                    Thread.sleep(10000);
			                } catch (InterruptedException ex) {
			                    ex.printStackTrace();
			                } 
			                recorder.finish(); */
						
			        		/* Here, the recording is actually started.
			        		 */
			        	//	recorder.start();
			        		
					
				
				//recorder.start();
		/*		} else if(start_rec.getText().startsWith("Stop Recording")){
					start_rec.setLabel("Start Recording");
					start_rec.setVisible(false);
					for_proc.setVisible(false);
					//thread = null;
					((JavaSoundRecorder) thread).finish();
					
					
				}
					}
				});
				th.start();
			} */
			
	//Start Recording option from record menu
			
			 if(event.getActionCommand().equals("Start Recording")){
				 
				Thread th = new Thread(){
					public void run(){
						
						JOptionPane.showMessageDialog(null, "Ready For Recording...", "Record", JOptionPane.INFORMATION_MESSAGE);
						for_proc.setVisible(true);
					//	start_rec.setLabel("Stop Recording");
						start_rec.setEnabled(false);
						stop_rec.setEnabled(true);
					//	thread = new JavaSoundRecorder();
						thread.start();
					}
				};
				th.start();
			}
			
	//Stop Recording option from record menu
			
			else if(event.getActionCommand().equals("Stop Recording")){
			//	start_rec.setLabel("Start Recording");
			//	start_rec.setVisible(false);
			//	JavaSoundRecorder recorder = new JavaSoundRecorder();
			//	final JavaSoundRecorder recorder = new JavaSoundRecorder();
			//	recorder.finish();
				
				((JavaSoundRecorder) thread).finish();
			//	JOptionPane.showMessageDialog(null, "Recorded Successfully...", "Success", JOptionPane.INFORMATION_MESSAGE);
				for_proc.setVisible(false);
				start_rec.setEnabled(true);
				stop_rec.setEnabled(false);
				play_back.setEnabled(true);
			//	pause.setEnabled(false);
			//	stop.setEnabled(false);
				play.setEnabled(true);
				stop.setEnabled(false);
				save_to_disk.setEnabled(true);
			//	thread.stop();
			} 
			
	//Play Back option from record menu
			
	/*		else if(event.getActionCommand().equals("Play Back")){
				Thread p = new Thread(){
					public void run(){
						
						
						for_proc.setVisible(true);
						//playBack();
					//	playBackAudio();
						 playBack = new playBackAudio();
						playBack.start();
						for_proc.setVisible(false);
					}
				};
				p.start();
			} */
			
 	// Play option from the play back sub menu of record menu
//	 Thread play_for = new Thread(){
		// playBackAudio play_audio = new playBackAudio();
//		 public void run(){
	 
			 if(event.getActionCommand().equals("Play")){
				 isPlayed = true;
				 for_proc.setVisible(true);
				 playBack = new playBackAudio();
				play.setEnabled(false);
			//	pause.setEnabled(true);
				stop.setEnabled(true);
			//	playBack();
				playBack.start();
				for_proc.setVisible(false);
			}
			
	// Pause option from the play back sub menu of record menu
			
	/*		else if(event.getActionCommand().equals("Pause")){
			//	pause.setLabel("Resume");
			//	playBack();
				((playBackAudio) playBack).pauseAudio();
				
			}
			
	//Resume option created by clicking pause of the Record-->Play Back-->Pause
			
			else if(event.getActionCommand().equals("Resume")){
			//	pause.setLabel("Pause");
			//	playBack();
				((playBackAudio) playBack).resumeAudio();
			} */
			
	// Stop option from the play back sub menu of record menu
			
			 if(event.getActionCommand().equals("Stop")){
				stop.setEnabled(false);
			//	pause.setEnabled(false);
				play.setEnabled(true);
			//	playBack();
			//	play_audio.finish();
				((playBackAudio) playBack).stopPlay();
			}
			
		// }
	// };
//	 play_for.start(); 
			
	//Save To Disk option from record menu
			
			 if(event.getActionCommand().equals("Save To Disk")){
				saveToDisk();
			}
			
	//Select Source option from the convert menu
			 
			 if(event.getActionCommand().equals("Select Source")){
				 int ret = convert_source.showOpenDialog(this);
				//	File src = null;
					if(ret == JFileChooser.APPROVE_OPTION){
						 src_convert_file = convert_source.getSelectedFile();
						String source_name = src_convert_file.toString();
						JOptionPane.showMessageDialog(null, "Source File: "+source_name+" Selected", "Source", JOptionPane.INFORMATION_MESSAGE);
					}
					select_dest.setEnabled(true);
					select_src.setEnabled(false);
			 }
			 
	//Select Destination option from the convert menu
			 
			 else if(event.getActionCommand().equals("Select Destination")){
				 final int ret = convert_destination.showSaveDialog(this);
				 Thread con = new Thread(){
					 public void run(){
						 
					 
				 
				//	File dest = null;
					String dest_name = null;
					if(ret == JFileChooser.APPROVE_OPTION){
						 dest_convert_file = convert_destination.getSelectedFile();
						 dest_name = dest_convert_file.toString();
						 if(!dest_name.toLowerCase().endsWith(".wav")){
							 dest_convert_file = new File(dest_name+".wav");
						 }
						JOptionPane.showMessageDialog(null, "Destination File: "+dest_name+".wav"+" Selected", "Destination", JOptionPane.INFORMATION_MESSAGE);
					}
					for_proc.setVisible(true);
					convertToWavFormat(src_convert_file,dest_convert_file);
					select_dest.setEnabled(false);
					select_src.setEnabled(true);
					for_proc.setVisible(false);
					 }
				 };
				 con.start();
			 }
			 
	// How To Use option from the Help menu
			 
			 if(event.getActionCommand().equals("How To Use"))
				try {
					{
					//	 Desktop desk = Desktop.getDesktop();
						 
					//	 String help_file_name = AutomaticSpeechRecognition.class.getResource("help.txt").getFile();
						 InputStream help_file_Stream = this.getClass().getResourceAsStream("help.txt");

						// String os = System.getProperty("os.name");
						// System.out.println(os);
						// String s = "C:\\Users\\Jagadesh\\Documents\\android\\anthro_scribe\\src\\speech\\help.txt";
						// File how_to = new File(f.toString());
					/*	 try {
							Runtime.getRuntime().exec("notepad "+s);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} */
						 
						/*	try {
								desk.open(how_to);
								
								//desk.print(how_to);
								//desk.edit(how_to);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} */
							
							 hel = new JFrame();
							 isHelpViewed = true;
							JTextPane tp = new JTextPane();
							hel.add(tp);
							hel.add(new JScrollPane(tp,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
							tp.setEditable(false);
							
							FileReader fr = null;
							
								
							//	fr = new FileReader(help_file_name);
								 
								 try {
									tp.read(help_file_Stream, null);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							 
							hel.setTitle("Help");
							//setLocationRelativeTo(null);
							hel.setSize(500,500);
							hel.setIconImage(anthro_img);
							hel.setVisible(true);
							addWindowListener(new WindowAdapter(){
								public void windowClosing(WindowEvent evt){ 
									isHelpViewed = false;
								}
							});
					 }
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
	// About option from the Help menu
				
			 if(event.getActionCommand().equals("About")){
				String msg = "Developed using sphinx 4";
				JOptionPane.showMessageDialog(null, msg);
			}
			 
	//Start button to start the recognition
			
			 if (event.getActionCommand().equals("Start") ){
				//AutomaticSpeechRecognition asr = new AutomaticSpeechRecognition();
				// playBack.stop();
				 if(isPlayed == true){
				 ((playBackAudio) playBack).stopPlay();
				 }
				 final JavaSoundRecorder rec = new JavaSoundRecorder();
				 final String recordedFileToLoad = ((JavaSoundRecorder) thread).isTheTempFileExist();
			//	 rec.isTheTempFileExist();
			//	 final String recordedFileToLoad = rec.recorded_wav_file;
				if ((wav_file_name.equals("")) && (recordedFileToLoad.equals(""))){
					JOptionPane.showMessageDialog(null, "No File Is Selected", "Select File", JOptionPane.WARNING_MESSAGE);
				}
				else if(wav_file_name.equals("")){
					wav_file_name = recordedFileToLoad;
				} 
				
				else {
				 Thread t = new Thread(){
					public void run(){
			
						JOptionPane.showMessageDialog(null, "Selected File: "+wav_file_name, "Input File", JOptionPane.INFORMATION_MESSAGE);
						for_proc.setVisible(true);
						start.setEnabled(false);
						//for_proc.show();	
						//browse.disable();
						//JOptionPane.showConfirmDialog(null, "Start Recognizing", "start", JOptionPane.OK_CANCEL_OPTION); 
						imp.setEnabled(false);
						record.setEnabled(false);
						convert.setEnabled(false);
						text.setText("");
						//String s = file_name.getText();
						try{
						reco(wav_file_name);
						} catch(OutOfMemoryError e){
							JOptionPane.showMessageDialog(null, "Increase You JVM Heap Space...", "Error", JOptionPane.ERROR_MESSAGE);
						}
						//	file_name.setText(null);
						//for_proc.hide();
						wav_file_name = "";
						if(!recordedFileToLoad.equals("")){
							((JavaSoundRecorder) thread).deleteTempFile();
						}
						for_proc.setVisible(false);
						start.setEnabled(true);
						
						imp.setEnabled(true);
						record.setEnabled(true);
						convert.setEnabled(true);
						
						
					}
				};
				t.start();	
				}
			}
		/*	else if (event.getActionCommand().equals("Browse")){
				int ret = get_file.showOpenDialog(null);
				if(ret == JFileChooser.APPROVE_OPTION){
					File s = get_file.getSelectedFile();
					file_name.setText(s.toString());
				}
				
			} */
		/*	else if (event.getActionCommand().equals("Save")){
				
					String src =  text.getText();
					File f = new File("output.doc");
					try {
						FileWriter fw = new FileWriter(f);
						fw.write(src);  
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			} */
			
		}
		
		public void convertToWavFormat(File src, File dest){
			
			
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec("pcm_s16le");
			audio.setBitRate(new Integer(16));
			audio.setChannels(new Integer(1));
			audio.setSamplingRate(new Integer(16000));
			EncodingAttributes attrs = new EncodingAttributes();
			attrs.setFormat("wav");
			attrs.setAudioAttributes(audio);
			Encoder encoder = new Encoder();
			try {
				encoder.encode(src, dest, attrs);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Illegal Arguments...", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (InputFormatException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Input Format Not Supported...", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Error In Encoding...", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "File Converted Successfully...", "Converted", JOptionPane.INFORMATION_MESSAGE);
			
			
			
		} 
		
		
		
	/*	public void playBack(){
			JavaSoundRecorder reco = new JavaSoundRecorder();
			File play_back_file = reco.wavFile;
			boolean toPause = false;
			boolean toResume = false;
			boolean toStop = false;
			try{
				AudioInputStream audio = AudioSystem.getAudioInputStream(play_back_file);
				Clip clip = AudioSystem.getClip();
				clip.open(audio);
				clip.start();
				if(toPause == true){
					try{
					clip.wait();
					}catch(Exception e){
						System.out.println("pause: "+ e);
					}
				}
				else if(toResume == true){
					try{
					clip.notify();
					}catch(Exception e){
						System.out.println("Resume:"+e);
					}
				}
				 if(toStop == true){
					try{
						clip.stop();
					}catch(Exception e){
						System.out.println("Stop:"+e);
					}
				} 
				
			} catch(Exception e){
				System.err.println(e);
			}
		//	play.setEnabled(true);
		}
			*/
	
		
		public void saveToDisk(){
			
			int ret = save_audio_to_disk.showSaveDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION){
				JavaSoundRecorder recod = new JavaSoundRecorder();
				AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
					AudioInputStream audio = null;
					//File f = recod.wavFile;
					File f = ((JavaSoundRecorder) thread).getRecordedFile();
					try {
						audio = AudioSystem.getAudioInputStream(f);
						 
					} catch (UnsupportedAudioFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//Clip clip = AudioSystem.getClip();
					//clip.open(audio);
					
				File saved_file = save_audio_to_disk.getSelectedFile();
				
				String file_name = saved_file.toString();
				if(!file_name.toLowerCase().endsWith(".wav")){
					saved_file = new File(file_name+".wav");
				}

				try{
					AudioSystem.write(audio, fileType, saved_file);
					JOptionPane.showMessageDialog(null, "Saved Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				catch(IOException e){
					System.err.println(e);
				}
			}
		}
	public  class playBackAudio extends Thread{
		Player audioPlayer = null;
	//	JavaSoundRecorder re = new JavaSoundRecorder();
		File play_back_file = ((JavaSoundRecorder) thread).getRecordedFile();
		URL url = null;
		
		public void start(){
			try {
				url = play_back_file.toURI().toURL();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				audioPlayer = Manager.createPlayer(url);
			} catch (NoPlayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			audioPlayer.start();
		}
		
	/*	void pauseAudio(){
			try {
				audioPlayer.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		void resumeAudio(){
			audioPlayer.notify();
		}
		
		void stopPl(){
			audioPlayer.stop();
		} */
		
		void stopPlay(){
			
			audioPlayer.stop();
			audioPlayer.close();
			
		//	play.setEnabled(true);
		}
		
		
		
			
			
	}
	
	
	
	
 /* 	public class consoleRedirect extends OutputStream{
		JTextArea ttt;
		consoleRedirect(JTextArea control){
			
			ttt = control;
		}
		@Override
		public void write(int a) throws IOException {
			// TODO Auto-generated method stub
			text.append(String.valueOf((char)a));
		}
		
	} */
	
/*	public class WindowEventHandler extends WindowAdapter{
		public void windowClosing(WindowEvent evt){
			System.out.print("closing");
		}
	} */
	
		public class JavaSoundRecorder extends Thread{
		    // record duration, in milliseconds
		 //   static final long RECORD_TIME = 60000;  // 1 minute
		 
			
			// path of the wav file
		    String temp = System.getProperty("java.io.tmpdir");
		    File temp_file;
		 //   File wavFile = new File(temp+"anthro_scribe_temp.wav");
		    String recorded_wav_file =""; // = wavFile.toString();
		    // format of audio file
		    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
		 
		    // the line from which audio data is captured
		    TargetDataLine line;
		    
		   /* JavaSoundRecorder(){
		    	try {
					temp_file = File.createTempFile("anthro_temp", ".wav");
					recorded_wav_file = temp_file.toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			   recorded_wav_file = "";
		    } */
		    
		   File getRecordedFile(){
		    	return temp_file;
		    } 
		   
		   boolean isTempFileExist(){
			   if(recorded_wav_file.equals("")){
				  recorded_wav_file = "";
			   }
			   return false;
		   }
		    
		    String isTheTempFileExist(){
		    //	if(!wavFile.exists()){
		    //		recorded_wav_file = "";
		    //	}
		    	if(recorded_wav_file.equals("")){
		    		recorded_wav_file = "";
		    	} 
		    	return recorded_wav_file;
		    }
		    
		    void deleteTempFile(){
		    	try{
		    		
		    	temp_file.delete();
		    	}catch(Exception e){
		    		System.err.println(e);
		    	}
		    	recorded_wav_file = "";
		    //	System.out.print("deleted");
		    	stop_rec.setEnabled(false);
		    	play_back.setEnabled(false);
		    	save_to_disk.setEnabled(false);
		    	
		    }
		    
		    /**
		     * Defines an audio format
		     */
		    AudioFormat getAudioFormat() {
		        float sampleRate = 16000;
		        int sampleSizeInBits = 16;
		        int channels = 1;
		        boolean signed = true;
		        boolean bigEndian = true;
		        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
		                                             channels, signed, bigEndian);
		        return format;
		    }
		 
		    /**
		     * Captures the sound and record into a WAV file
		     */
		     public void start() {
		    	//JavaSoundRecorder recorder = new JavaSoundRecorder();
		    	 try {
						temp_file = File.createTempFile("anthro_temp", ".wav");
						recorded_wav_file = temp_file.toString();
						temp_file.deleteOnExit();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		        try {
		            AudioFormat format = getAudioFormat();
		            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		          //  System.out.println(temp);
		            // checks if system supports the data line
		            if (!AudioSystem.isLineSupported(info)) {
		              //  System.out.println("Line not supported");
		              //  System.exit(0);
		            	JOptionPane.showInternalMessageDialog(null, "Line Not Supported", "Warning", JOptionPane.WARNING_MESSAGE);
		            }
		            line = (TargetDataLine) AudioSystem.getLine(info);
		            line.open(format);
		            
		            line.start();   // start capturing
		 
		          //  System.out.println("Start capturing...");
		            
		            AudioInputStream ais = new AudioInputStream(line);
		            
		           // System.out.println("Start recording...");
		            
		            // start recording
		            
		            AudioSystem.write(ais, fileType, temp_file);
		            
		            
		           
		        } catch (LineUnavailableException ex) {
		            ex.printStackTrace();
		        } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
        		/* Here, the recording is actually started.
        		 */
        		
		    }
		 
		    /**
		     * Closes the target data line to finish capturing and recording
		     */
		    void finish() {
		    	try{
		        line.stop();
		        line.close();
		        line = null;
		    	}catch(Exception e){
		    		System.err.println(e);
		    	}
		    	
		    	JOptionPane.showMessageDialog(null, "Recording Completed Successfully...", "Completed", JOptionPane.INFORMATION_MESSAGE);
		       // System.out.println("Finished");
		    	
		    }
		/*    JavaSoundRecorder recorder = new JavaSoundRecorder();
		 public void actionPerformed(ActionEvent e){
			 if(e.getActionCommand().equals("Start Recording")){
				 for_proc.setVisible(true);
				 start_rec.setLabel("Stop Recording");
				 recorder.start();
			 }
			 else if(e.getActionCommand().equals("Stop Recording")){
				 recorder.finish();
				 for_proc.setVisible(false);
			 }
		 } */
		    /**
		     * Entry to run the program
		     */
		/*    public void main(String[] args) {
		        final JavaSoundRecorder recorder = new JavaSoundRecorder();
		 
		        // creates a new thread that waits for a specified
		        // of time before stopping
		        Thread stopper = new Thread(new Runnable() {
		            public void run() {
		                try {
		                    Thread.sleep(RECORD_TIME);
		                } catch (InterruptedException ex) {
		                    ex.printStackTrace();
		                }
		                recorder.finish();
		            }
		        });
		 
		        stopper.start();
		 
		        // start recording
		        recorder.start(); 
		    }  */
		    
		  /*  protected void finalize() throws Throwable{
		    	((playBackAudio) playBack).stopPlay();
		    	System.out.print("finally");
		    } */
		}
		
	
	

	public static void reco(String args)   {
       
		
		URL audioURL = null;
       // String outfilename = "output.txt";
        
            
				try {
					audioURL = new File(args).toURI().toURL();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			     
          
        
        URL configURL = AutomaticSpeechRecognition.class.getResource("config.xml");
     // Output file
 	/*	PrintStream corpusout = null;
 		
        try {
			corpusout = new PrintStream(new FileOutputStream(outfilename), true, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();			
			System.exit(1);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(1);
		} */

        ConfigurationManager cm = new ConfigurationManager(configURL);
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        
        long now = System.currentTimeMillis();

     		
        /* allocate the resource necessary for the recognizer */
        recognizer.allocate();
        try{
        AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(audioURL, null);
        }catch(Exception e){
        	System.err.println(e);
        	JOptionPane.showMessageDialog(null, "File Format Not Supported...", "Incompatible", JOptionPane.ERROR_MESSAGE);
        	
        }


        // Loop until last utterance in the audio file has been decoded, in which case the recognizer will return null.
        Result result = recognizer.recognize();

		int segments = 0;
		while (result != null) {

				String resultText = result.getBestFinalResultNoFiller();
				//System.out.println(resultText);
				
                 text.append(resultText+" ");
                 //corpusout.println(resultText);
                 segments++;
     			
     			result = recognizer.recognize();

                
                
        }
		long elapsed = System.currentTimeMillis() - now;

        //corpusout.close();
        JOptionPane.showMessageDialog(null, "Completed recognition in " + elapsed / 1000 + " seconds in " + segments + " segment(s).", "Completed", JOptionPane.INFORMATION_MESSAGE);
      //  recognizer.deallocate();
    	   
       
    }
	
}
