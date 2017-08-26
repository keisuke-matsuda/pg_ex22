package pg_ex21;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertFile {
		static String dirpath = "C:\\KM_Person\\pleiades-4.6.3-ultimate-win-64bit-jre_20170422\\pg_ex21\\src\\pg_ex21\\";
		static String recordname = dirpath +"record.log";
		static String reqfilename =dirpath +"invoice.dat";
//		static File record =  new File("C:\\KM_Person\\pleiades-4.6.3-ultimate-win-64bit-jre_20170422\\pg_ex21\\src\\pg_ex21\\record.log");

	public static void main(String[] args) throws IOException{ //メソッド名は後で変更する
		File record =  new File(recordname);
		File reqfile = new File(reqfilename);
//		FileReader r = new FileReader(record);		//省略しない場合
//		BufferedReader br = new BufferedReader(r);	//省略しない場合
		BufferedReader br = new BufferedReader(new FileReader(record)); //省略した場合

//		FileWriter fw = new FileWriter(reqfilename,true);	//省略しない場合
//		BufferedWriter bw = new BufferedWriter(fw);			//省略しない場合
		BufferedWriter bw = new BufferedWriter(new FileWriter(reqfile,true));		//省略した場合


		String line = null;
		while ((line = br.readLine()) != null){
			bw.write(line+"\n");
			System.out.println(line);

		}
		br.close();
		bw.close();
	}

	static void calcMain(){
		int basicCharge;
		int callCharge;

	}

	static void calcC1(){
		int c1_Discount;
		int c1_Maxnum;

	}

	static void calcE1(){
		int e1_Extra;
		int e1_Range;
	}

}
