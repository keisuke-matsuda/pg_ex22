package pg_ex22;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OutputReqfile implements Configure {

	// ディレクトリパス
	public static final String DIRPATH = "C:/KM_Person/pleiades-4.6.3-ultimate-win-64bit-jre_20170422/pg_ex21/src/pg_ex21/";
	// レコードファイル名
	public static final String RECORDNAME = DIRPATH + "record.log";
	// 請求ファイル名
	public static final String REQFILENAME = DIRPATH + "invoice.dat";

	public static void output(String[] args) throws IOException { // メソッド名は後で変更する
		File record = new File(RECORDNAME);
		BufferedReader br = new BufferedReader(new FileReader(record));

		File reqfile = new File(REQFILENAME);
		BufferedWriter bw = new BufferedWriter(new FileWriter(reqfile, true));

		String line = null;

		ArrayList<String> numC1 = new ArrayList<String>();
		boolean yesC1 = false;
		boolean yesE1 = false;
		String phoneNum = null;
		int sumBaseCharge = 0;
		int sumCallCharge = 0;

		while ((line = br.readLine()) != null) {
			char head = line.charAt(0);

			switch (head) {
			case '1':
				// 契約者の電話番号を抽出
				phoneNum = line.substring(2, 15);
				break;
			case '2':
				// 割引関連の情報を変数に代入
				if (line.contains("C1")) {
					yesC1 = true;
					numC1.add(line.substring(5, 18));
				}

				if (line.contains("E1")) {
					yesE1 = true;
				}
				break;
			case '5':
				// 通話時間を抽出してint型に整形
				String timeUnform = (line.substring(13, 18));
				int startTime = Integer.parseInt(timeUnform.substring(0, 2) + timeUnform.substring(3, 5));

				// 通話先の電話番号を抽出
				String callNum = line.substring(23, 36);

				// 通話時間を抽出
				int callTime = Integer.parseInt(line.substring(19, 22));

				// calcCallメソッドから1回あたりの通話料金を受け取る
				int callCharge = CalcCharge.calcCall(yesC1, yesE1, startTime, callTime, callNum, numC1);

				// 契約者毎の通話料金の合計を求める
				sumCallCharge += callCharge;
				break;
			case '9':
				// 計算メソッドを利用して、基本料金を計算する
				sumBaseCharge = CalcCharge.calcBase(yesC1, yesE1);

				// 請求ファイルに書き込み
				if (phoneNum != null) {
					bw.write(LINENUM_SUBNUM + phoneNum + "\n");
					bw.write(LINENUM_BASECHARGE + sumBaseCharge + "\n");
					bw.write(LINENUM_CALLCHARGE + sumCallCharge + "\n");
					bw.write(LINENUM_SEPARATER + SEPARATER + "\n");
				}

				// 変数、配列の初期化
				yesC1 = false;
				yesE1 = false;
				sumCallCharge = 0;
				numC1.clear();
				break;
			}
		}
		br.close();
		bw.close();
	}

}
