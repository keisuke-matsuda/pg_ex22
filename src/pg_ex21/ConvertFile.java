package pg_ex21;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConvertFile {
		static String dirpath = "C:\\KM_Person\\pleiades-4.6.3-ultimate-win-64bit-jre_20170422\\pg_ex21\\src\\pg_ex21\\";
		static String recordname = dirpath +"record.log";
		static String reqfilename =dirpath +"invoice.dat";
		static int basecharge = 1000;	//基本料金
		static int callcharge = 20;	//通話料金(1分毎)
		static int e1discount = 5;		//E1サービスにて適用される割引料金(分あたり)

		static String secline = "====================";

	public static void main(String[] args) throws IOException{ //メソッド名は後で変更する
		File record =  new File(recordname);
		BufferedReader br = new BufferedReader(new FileReader(record));
		File reqfile = new File(reqfilename);
		BufferedWriter bw = new BufferedWriter(new FileWriter(reqfile,true));

		String line = null;
		ArrayList<String> nums_c1 = new ArrayList<String>() ;
		ArrayList<Integer> times_call = new ArrayList<Integer>(); //とりあえずInteger型でいく。Date型にするとかは別途検討

		boolean yes_c1 = false;
		boolean yes_e1 = false;
		String phone_num = null;
		String base_charge = null;	//int型の方がよい??
		int sum_charge_call = 0;


		while ((line = br.readLine()) != null){
//			bw.write(line+"\n");
			char type = line.charAt(0);

			switch (type){
			case '1' :
				phone_num = line.substring(2,15);
				break;
			case '2' :
				//割引関連の情報を変数に代入する
				//各サービスに加入しているか?
				if (line.contains("C1")){
					yes_c1 = true;
					nums_c1.add(line.substring(5,18));
				}

				if (line.contains("E1")){
					yes_e1 = true;
				}
				break;
			case '5' :
					String time_unform = (line.substring(13,18));
					int time_start = Integer.parseInt(time_unform.substring(0, 2)+time_unform.substring(3,5));
					String num_call = line.substring(23,36);
					int time_call = Integer.parseInt(line.substring(19,22));
					//配列にする必要ある?times_call.add(Integer.parseInt(time_formed));

					int charge_call = calcCall(yes_c1,yes_e1,time_start,time_call,num_call,nums_c1);
					sum_charge_call += charge_call;
					break;
			case '9' :
				//計算メソッドを利用して、基本料金を計算する
				base_charge = calcBase(yes_c1,yes_e1);

				bw.write(phone_num+"\n"); 	//とりあえず代理の書き込み
				bw.write(base_charge+"\n");		//とりあえず代理の書き込み
				bw.write(sum_charge_call+"\n");	//とりあえず代理の書き込み
				bw.write(secline+"\n");		//とりあえず代理の書き込み
				//書き込みメソッドを作成してここに入れる

//				for (int s : times_call){
//				System.out.println(s);
//				}


				yes_c1 = false;				//とりえあず代理の初期化(メソッド作成後に削除)
				yes_e1 = false;			//とりあえず代理の初期化(メソッド作成後に削除)
				sum_charge_call = 0;	//とりあえず代理の初期化(メソッド作成後に削除)
				nums_c1.clear();		//とりあえず代理の初期化(メソッド作成後に削除)
				times_call.clear(); 	//とりあえず代理の初期化(メソッド作成後に削除)
				//変数を初期化するメソッドを作成してここにいれる
				break;
			}
	}

//		System.out.println(nums_c1.size());
		br.close();
		bw.close();

//		String test_teststring = "5 2004/06/04 03:34 003 090-1234-0002";
//		String test_time_unform = (test_teststring.substring(13,18));
//		System.out.println(test_time_unform);
//		String test_time_formed = test_time_unform.substring(0, 2) + test_time_unform.substring(3,5);
//		System.out.println(test_time_formed);

	}

	//戻り値は基本料金(int型)、引数はyes_c1,yes_e1,
	static String calcBase(boolean yes_c1,boolean yes_e1){
		int charge_base = basecharge;
		if (yes_c1 == true){
			charge_base += 100;
		}

		if (yes_e1 == true){
			charge_base += 200;
		}
		return String.valueOf(charge_base);

	}

	static int calcCall(boolean yes_c1,boolean yes_e1,int time_start,int time_call,String num_call,ArrayList<String> nums_c1){
		int charge_call = 0;

		if (yes_c1 == false && yes_e1 == false){
			charge_call += callcharge*time_call;		//重複してる1
		}else if (yes_c1 == true && yes_e1 == false){
			if (nums_c1.contains(num_call)){;
				charge_call += callcharge/2*time_call;
			}else {
				charge_call += callcharge*time_call;	//重複してる2
			}
		}else if (yes_c1 == false && yes_e1 == true){
			if (time_start > 759 && time_start < 1800){		//時間帯の条件としてクラス変数にしてもいいかも?
				charge_call += (int) (callcharge-e1discount)*time_call;		//とりあえずキャストで切り捨てをしている。要修正
			}else{
				charge_call += callcharge*time_call;	//重複してる3
			}
		}else if (yes_c1 == true && yes_e1 == true){
			if (time_start > 759 && time_start < 1800 && nums_c1.contains(num_call)){
				charge_call += (int) (callcharge-e1discount)/2*time_call;	//とりあえずキャストで切りすてしているが、修正したほうがよい
			}else if (time_start > 759 && time_start < 1800 && !(nums_c1.contains(num_call))){
				charge_call += (int) (callcharge-e1discount)*time_call;				//キャスト
			}else if (!(time_start > 759 && time_start < 1800) && nums_c1.contains(num_call)){
				charge_call += (int) callcharge/2*time_call;
			}else {
				charge_call += callcharge*time_call;	//重複してる4
			}
		}
		return charge_call;
	}

	static void calcE1(){
		int e1_Extra;
		int e1_Range;
	}

}
