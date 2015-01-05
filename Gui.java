import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

class Gui implements ActionListener{

	File file=null;

	JFileChooser fileC;
	FileNameExtensionFilter filter = new FileNameExtensionFilter(
        ".txt", "txt");
	JFrame wind, wind2;
	JLabel op, op2, sel, sel2;
	JButton open, select, done;
	JTextField nameF, searchtermF;
	Run r;

	String name, searchterm, amount, date;

	Gui(Run run){
		r=run;
		r.addGui(this);
		choseFileRead();

	}

	void choseFileRead(){

		fileC= new JFileChooser();
		fileC.setFileFilter(filter);
		fileC.setCurrentDirectory(new File("."));

		op=new JLabel("                              Please select a file to read from.");
		op2 = new JLabel("You can download these textfiles from your online bank-account!");


		wind = new JFrame("EazyMoney");
		wind.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wind.setLayout(new BorderLayout());

		open = new JButton("Open file");
		open.addActionListener(this);

		wind.add(op, BorderLayout.NORTH);
		wind.add(op2, BorderLayout.CENTER);
		wind.add(open, BorderLayout.SOUTH);
		wind.pack();
		wind.setLocationRelativeTo(null);
		wind.setVisible(true);
	}

	void choseFileWrite(){

		fileC= new JFileChooser();
		fileC.setFileFilter(filter);
		fileC.setCurrentDirectory(new File("."));

		sel= new JLabel("                       Select where you want to store the results!");
		sel2 = new JLabel("If you want a new file, just write the name you want (remember \".txt\")");

		wind = new JFrame("EazyMoney");
		wind.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wind.setLayout(new BorderLayout());

		select = new JButton("Select file");
		select.addActionListener(this);

		wind.add(sel, BorderLayout.NORTH);
		wind.add(sel2, BorderLayout.CENTER);
		wind.add(select, BorderLayout.SOUTH);
		
		wind.pack();
		wind.setLocationRelativeTo(null);
		wind.repaint();
		wind.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){


		if(e.getSource() == open){
			//Lar deg velge en fil, så lenge filen er gyldig.
			int returnVal = fileC.showOpenDialog(wind);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
	           	file = fileC.getSelectedFile();
	           	if(file.canRead()){
	           		wind.setVisible(false);
	           		r.compl(file);
	           		//Get information from bank-statement
					r.readFile();
					//Print new searchterms and keys to document
					r.printSearch();
					//Chose outputfile
	           		choseFileWrite();
	           	}
	        }else{
	        	System.out.println("You did not select a valid file!\n");
	        }
	    } else{
	    	//Lar deg velge en fil, så lenge filen er gyldig.
			int returnVal = fileC.showOpenDialog(wind);

			if(returnVal == JFileChooser.APPROVE_OPTION) {
	           	file = fileC.getSelectedFile();
	           	
	           	if(!file.exists()){
	           		try{
		           		if(!file.createNewFile()){
		           			System.out.println("Error while creating file. Critical error, system closing!");
							System.out.println(" ");
							System.exit(-6);
		           		}
		           	}catch(Exception ex){
		           		ex.printStackTrace();
		           		System.out.println("Error while creating file. Critical error, system closing!");
						System.out.println(" ");
						System.exit(-6);
		           	}
	           	}

	           	if(file.canWrite()){
	           		wind.setVisible(false);
	           		r.printInfo(file);
	           		System.exit(0);
	           	}else{
	           		System.out.println("You did not select a valid file! Try again!");
	           		choseFileWrite();
	           	}
	        }
	    }
	}
}