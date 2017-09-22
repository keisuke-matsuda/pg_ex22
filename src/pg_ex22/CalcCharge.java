package pg_ex22;

import java.util.ArrayList;

public class CalcCharge implements Configure{
	static int calcBase(boolean yes_c1, boolean yes_e1) {
		// 契約者毎の基本料金を求めるメソッド
		int baseCharge = BASECHARGE;
		if (yes_c1 == true) {
			baseCharge += 100;
		}
		if (yes_e1 == true) {
			baseCharge += 200;
		}
		return baseCharge;

	}

	static int calcCall(boolean yes_c1, boolean yes_e1, int time_start	, int time_call, String num_call,
			ArrayList<String> nums_c1) {
		// 通話1回あたりの通話料金を求める
		int callCharge = 0;
		int serviceNo = CALLCHARGE * time_call;

		// 加入サービス毎に場合分けして、通話料金を求める
		if (yes_c1 == false && yes_e1 == false) {
			// 加入者がどのサービスにも加入していないケース
			callCharge += serviceNo;
		} else if (yes_c1 == true && yes_e1 == false) {
			// C1にのみ加入しているケース
			if (nums_c1.contains(num_call)) {
				;
				// 通話先が割引対象であるケース
				callCharge += CALLCHARGE / 2 * time_call;
			} else {
				// 通話先が割引対象でないケース
				callCharge += serviceNo;
			}
		} else if (yes_c1 == false && yes_e1 == true) {
			// 加入者がE1にのみ加入しているケース
			if (time_start > C1STARTTIME && time_start < C1ENDTIME) { // 時間帯の条件としてクラス変数にしてもいいかも?
				// 通話開始時間が割引対象であるケース
				callCharge +=  (CALLCHARGE - E1DISCOUNT) * time_call; // とりあえずキャストで切り捨てをしている。要修正
			} else {
				// 通話開始時間が割引対象でないケース
				callCharge += serviceNo;
			}
		} else if (yes_c1 == true && yes_e1 == true) {
			// 加入者がC1・E1に加入しているケース
			if (time_start > C1STARTTIME && time_start < C1ENDTIME && nums_c1.contains(num_call)) {
				// 通話先が割引対象である、かつ通話開始時間が割引対象であるケース
				callCharge +=  (CALLCHARGE - E1DISCOUNT) / 2 * time_call; // とりあえずキャストで切りすてしているが、修正したほうがよい
			} else if (time_start > C1STARTTIME && time_start < C1ENDTIME && !(nums_c1.contains(num_call))) {
				/// 通話先が割引対象でない,かつ通話開始時間が割引対象であるケース
				callCharge +=  (CALLCHARGE - E1DISCOUNT) * time_call; // キャスト
			} else if (!(time_start > C1STARTTIME && time_start < C1ENDTIME) && nums_c1.contains(num_call)) {
				// 通話先が割引対象である、かつ通話開始時間が割引対象でないケース
				callCharge +=  CALLCHARGE / 2 * time_call;
			} else {
				// 通話先が割引対象出ない、かつ通話開始時間が割引対象でないケース
				callCharge += serviceNo;
			}
		}
		return callCharge;
	}

}
