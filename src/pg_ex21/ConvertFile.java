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
		static int c1start = 759;		//C1サービス適用開始時間
		static int c1end = 1800;		//C1サービス適用終了時間

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
		int sum_base_charge = 0;
		int sum_charge_call = 0;


		while ((line = br.readLine()) != null){
			char head = line.charAt(0);

			switch (head){
			case '1' :
				//契約者の電話番号を抽出
				phone_num = line.substring(2,15);
				break;
			case '2' :
				//割引関連の情報を変数に代入
				if (line.contains("C1")){
					yes_c1 = true;
					nums_c1.add(line.substring(5,18));
				}

				if (line.contains("E1")){
					yes_e1 = true;
				}
				break;
			case '5' :
				 	//通話時間を抽出してint型に整形
					String time_unform = (line.substring(13,18));
					int time_start = Integer.parseInt(time_unform.substring(0, 2)+time_unform.substring(3,5));

					//通話先の電話番号を抽出
					String num_call = line.substring(23,36);

					//通話時間を抽出
					int time_call = Integer.parseInt(line.substring(19,22));

					//calcCallメソッドから1回あたりの通話料金を受け取る
					int charge_call = calcCall(yes_c1,yes_e1,time_start,time_call,num_call,nums_c1);

					//契約者毎の通話料金の合計を求める
					sum_charge_call += charge_call;
					break;
			case '9' :
				//計算メソッドを利用して、基本料金を計算する
				sum_base_charge = calcBase(yes_c1,yes_e1);

				//請求ファイルに書き込み
				if ( phone_num != null){
				bw.write("1 "+phone_num+"\n");
				bw.write("5 "+sum_base_charge+"\n");
				bw.write("7 "+sum_charge_call+"\n");
				bw.write("9 "+"===================="+"\n");
				}

				//変数、配列の初期化
				yes_c1 = false;
				yes_e1 = false;
				sum_charge_call = 0;
				nums_c1.clear();
				times_call.clear();
				break;
			}
		}
		br.close();
		bw.close();
	}


	static int calcBase(boolean yes_c1,boolean yes_e1){
		//契約者毎の基本料金を求めるメソッド
		int charge_base = basecharge;
		if (yes_c1 == true){
			charge_base += 100;
		}else if (yes_e1 == true){
			charge_base += 200;
		}
		return charge_base;

	}

	static int calcCall(boolean yes_c1,boolean yes_e1,int time_start,int time_call,String num_call,ArrayList<String> nums_c1){
		//通話1回あたりの通話料金を求める
		int charge_call = 0;
		int no_serv = callcharge*time_call;	//どのサービスにも該当しない場合の計算式

		//加入サービス毎に場合分けして、通話料金を求める
		if (yes_c1 == false && yes_e1 == false){
			//加入者がどのサービスにも加入していないケース
			charge_call += no_serv;
		}else if (yes_c1 == true && yes_e1 == false){
			//C1にのみ加入しているケース
			if (nums_c1.contains(num_call)){;
				//通話先が割引対象であるケース
				charge_call += callcharge/2*time_call;
			}else {
				//通話先が割引対象でないケース
				charge_call += no_serv;
			}
		}else if (yes_c1 == false && yes_e1 == true){
			//加入者がE1にのみ加入しているケース
			if (time_start > c1start && time_start < c1end){		//時間帯の条件としてクラス変数にしてもいいかも?
				//通話開始時間が割引対象であるケース
				charge_call += (int) (callcharge-e1discount)*time_call;		//とりあえずキャストで切り捨てをしている。要修正
			}else{
				//通話開始時間が割引対象でないケース
				charge_call += no_serv;
			}
		}else if (yes_c1 == true && yes_e1 == true){
			//加入者がC1・E1に加入しているケース
			if (time_start > c1start && time_start < c1end && nums_c1.contains(num_call)){
				//通話先が割引対象である、かつ通話開始時間が割引対象であるケース
				charge_call += (int) (callcharge-e1discount)/2*time_call;	//とりあえずキャストで切りすてしているが、修正したほうがよい
			}else if (time_start > c1start && time_start < c1end && !(nums_c1.contains(num_call))){
				///通話先が割引対象でない,かつ通話開始時間が割引対象であるケース
				charge_call += (int) (callcharge-e1discount)*time_call;				//キャスト
			}else if (!(time_start > c1start && time_start < c1end) && nums_c1.contains(num_call)){
				//通話先が割引対象である、かつ通話開始時間が割引対象でないケース
				charge_call += (int) callcharge/2*time_call;
			}else {
				//通話先が割引対象出ない、かつ通話開始時間が割引対象でないケース
				charge_call += no_serv;
			}
		}
		return charge_call;
	}
}
