import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


class EazyMoney_med{

	public static void main(String[] args) {
		final Run r = new Run();

		SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new Gui(r);
            }
        });
	}
}

class Run{
	

	File file;
	//Name of post, total activity 
	HashMap <String, ArrayList<String>> info = new HashMap <String, ArrayList<String>> (); 
	//Searchstring, name of post
	HashMap <String, String> searchToKey = new HashMap <String, String> ();
	//Searchstring
	HashSet <String> searchTerm = new HashSet<String>();
	//ArrayList
	ArrayList<String> arList;

	Scanner read = new Scanner(System.in);

	Gui g;

	public void addGui(Gui g){
		this.g=g;
	}

	public void compl(File f){
		file=f;
		//Read in previous searchinformation
		readSearch();
	}

	private String findName(String test){
		for(String s: searchTerm){
			s=s.toLowerCase();
			test=test.toLowerCase();
			if(test.contains(s)){
				return s;
			}
		}

		return null;
	}

	private boolean isMatch(String test){
		if(searchTerm.isEmpty()){
			return false;
		}
		for(String s: searchTerm){
			s=s.toLowerCase();
			test=test.toLowerCase();
			if(test.contains(s)){
				return true;
			}
		}

		return false;
	}
	//Outputs the search-information to file
	void printSearch(){
		//Create new file with correct name
		File f = new File ("searchterms.txt");
				
		if(!f.exists()){
			System.out.println("THIS SHOULD NOT HAPPEN! CRITICAL LOGICAL ERROR!");
			try{
				if(f.createNewFile()){
						System.out.println("New searchterm-file created!");
						System.out.println(" ");
				} else{
						System.out.println("Error while creating file. Non-critical error, system continuing!");
						System.out.println(" ");
				}
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Cant create file! :(");
				System.out.println(" ");
			}
		} else{
			try{
				BufferedWriter w = new BufferedWriter(new FileWriter(f));

				for(String s: searchTerm){
					String name = searchToKey.get(s);
					String output = s + ":" + name;
					w.write(output);
					w.newLine();
				}

				w.close();
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Error during writing to searchterm-file!");
				System.out.println("Non-critical error, system continuing!");
				System.out.println(" ");
			}
		}
	}
	//Reads the previously saved searchterms from file and stores them in memory
	void readSearch(){
		try{

			//Create new file with correct name
			File f = new File ("searchterms.txt");
				
			if(!f.exists()){

				if(f.createNewFile()){
					System.out.println("New searchterm-file created!");
					System.out.println(" ");
				} else{
					System.out.println("Error while creating file. Non-critical error, system continuing!");
					System.out.println(" ");
				}
			} else{

				Scanner sc = new Scanner(new FileReader(f));

				while(sc.hasNext()){
					//[0] = searchstring, [1] = name
					String[] in = new String [2];
					in = sc.nextLine().split(":");
					ArrayList <String> a = new ArrayList <String> ();
					info.put(in[1], a);
					searchTerm.add(in[0]);
					searchToKey.put(in[0], in[1]);
				}
				sc.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error during reading from searchterm-file!");
			System.out.println("Non-critical error, system continuing!");
			System.out.println(" ");
		}
	}

	public void createNew(String name, String searchphrase, String sum, String date){
		//Decide name of key and searchterm! Add searchterm to HashSet.
		//Create a new arraylist and add the sum to it and add this to the "info"-hashmap
		arList= new ArrayList<String>();
		String add = date+":"+sum;
		arList.add(add);
		searchTerm.add(searchphrase);
		searchToKey.put(searchphrase, name);
		info.put(name, arList);
	}

	public void readFile(){
		String searchphrase;
		String name;
		boolean exists;

		//[0] == date, [1] == Name/Info, [2] == useless, [3] = Expense, [4] = Deposit
		try{

			Scanner sc = new Scanner(new FileReader(file));
			sc.nextLine();

			while(sc.hasNext()){
				String [] in = new String [5]; 
				in=sc.nextLine().replaceAll("\"", "").split(";");

				if(!in[3].equals("")){
					if(in[3].contains(".")){
						int pl;
						while((pl = in[3].indexOf('.')) != -1)	in[3]=in[3].substring(0, pl)+in[3].substring(pl+1);
					}
					
					in[3]=in[3].replace(',', '.');
				} else{
					if(in[4].contains(".")){
						int pl;
						while((pl = in[4].indexOf('.')) != -1)	in[4]=in[4].substring(0, pl)+in[4].substring(pl+1);
					}

					in[4]=in[4].replace(',', '.');
				}

				//Read from file OK
				if(in.length>1){

					exists=isMatch(in[1]);

					//This post already exists
					if(exists){

						//Add the correct sum to the correct arraylist

						searchphrase=findName(in[1]);
						if(searchphrase==null){
							System.out.println("Phrase not found, CRITICAL ERROR!");
							System.exit(-1);
						}

						name=searchToKey.get(searchphrase);
						ArrayList<String> use = new ArrayList <String>();
						use = info.get(name);
						if(use==null){
							System.out.println("Critical error! WTF?");
							System.out.println("Search: " + searchphrase + " Name: " + name);
							System.exit(-5);
						}

						String add;
						
						//Deposited to account
						if(!in[3].equals("")){
							add=in[0]+":-"+in[3];
							use.add(add);
						//Withdrawn from account
						}else{
							add=in[0]+":"+in[4];
							use.add(add);
						}

						info.put(name, use);

					//This post is new
					}else{

						//Hvordan lage "lock", så den bare kjører en om gangen?

						//Deposited to account
						if(!in[3].equals("")){
							String snd = "-"+in[3];

							g.giveName(in[1], snd, in[0]);

						//Withdrawn from account
						}else{
							g.giveName(in[1], in[4], in[0]);
						}
					}
				}
			}
			sc.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error during reading from file! System closing!");
			System.exit(-2);
		}
	}
	//Prints information
	public void printInfo(File f){

		try{
			BufferedWriter w = new BufferedWriter(new FileWriter(f));

			double totAll=0.0;

			for(Map.Entry<String, ArrayList<String>> r : info.entrySet()){
				Double tot=0.0;
				String totPrint="";
				
				ArrayList <String> a = r.getValue();

				for(String s : a){
					totPrint=(a+"\n").replaceAll("[\\[\\]]", "").replaceAll(", ", "\n");
					String[] cnt = new String [2];
					cnt=s.split(":");
					tot+=Double.parseDouble(cnt[1]);
				}
				totAll += tot;
				w.write("Name of post: " +r.getKey() + ".\nTotal funds transfered by post: " + tot);
				w.newLine();
				w.newLine();
				w.write(totPrint);
				w.newLine();
				w.write("------------------------------");
				w.newLine();
				w.newLine();
			}
			w.write("Total of all transactions: " + (int)totAll);
			w.newLine();
			w.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error during writing to file! System closing!");
			System.exit(-3);
		}
	
		//Closes reading from user
		read.close();
	}
}