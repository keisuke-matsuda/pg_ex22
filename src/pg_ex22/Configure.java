package pg_ex22;

public interface Configure {

	/////////////////////////////////////////////
	// ファイルフォーマット関連
	/////////////////////////////////////////////
	//// 請求ファイルのフォーマット
	// 契約情報行のヘッダ番号
	public static final String LINENUM_SUBNUM = "1";
	// 基本料金行のヘッダ番号
	public static final String LINENUM_BASECHARGE = "5";
	// 通話料金行のヘッダ番号
	public static final String LINENUM_CALLCHARGE = "7";
	// 区切り行のヘッダ番号
	public static final String LINENUM_SEPARATER = "9";
	// セパレータ
	public static final String SEPARATER = "====================";

	/////////////////////////////////////////////
	// 料金関連
	/////////////////////////////////////////////
	//// サービスで共通する値
	// 基本料金
	public static final int BASECHARGE = 1000;
	// 通話料金(1分毎)
	public static final int CALLCHARGE = 20;

	//// C1サービスに関する値
	// 割り増し料金
	public static final int C1EXTRACHARGE = 100;
	// サービス適用開始時間
	public static final int C1STARTTIME = 800;
	// サービス適用終了時間
	public static final int C1ENDTIME = 1759;

	//// E1サービスに関する値
	// 割り増し料金
	public static final int E1EXTRACHARGE = 200;
	// 割引料金(分あたり)
	public static final int E1DISCOUNT = 5;

}
