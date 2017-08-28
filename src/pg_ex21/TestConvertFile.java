package pg_ex21;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TestConvertFile {

	@Test
	public void testCalcMain() {
		String charge_ff = ConvertFile.calcBase(false, false);
		assertEquals("1000",charge_ff);

		String charge_tf = ConvertFile.calcBase(true, false);
		assertEquals("1100",charge_tf);

		String charge_ft = ConvertFile.calcBase(false,true);
		assertEquals("1200",charge_ft);

		String charge_tt = ConvertFile.calcBase(true, true);
		assertEquals("1300",charge_tt);
	}

	@Test
	public void testCalcCall() {
		ArrayList<String> nums_c1 = new ArrayList<String>() ;
		nums_c1.add("090-8596-7103");

		int charge_noserv = ConvertFile.calcCall(false,false,800,10,"090-8596-7103",nums_c1);
		assertEquals(200,charge_noserv);

		int charge_c1_1 = ConvertFile.calcCall(true, false,800,10, "090-8596-7103", nums_c1);
		assertEquals(100,charge_c1_1);

		int charge_c1_2 = ConvertFile.calcCall(true, false,800,10, "090-8596-7102", nums_c1);
		assertEquals(200,charge_c1_2);

		int charge_e1_1 = ConvertFile.calcCall(false, true,1200,10, "090-8596-7102", nums_c1);
		assertEquals(150,charge_e1_1);

		int charge_e1_2 = ConvertFile.calcCall(false, true,730,10, "090-8596-7102", nums_c1);
		assertEquals(200,charge_e1_2);

		int charge_both_1 = ConvertFile.calcCall(true, true,850,10, "090-8596-7103", nums_c1);
		assertEquals(70,charge_both_1);

		int charge_both_2 = ConvertFile.calcCall(true, true,850,10, "090-8596-7102", nums_c1);
		assertEquals(100,charge_both_2);

		int charge_both_3 = ConvertFile.calcCall(true, true,2000,10, "090-8596-7103", nums_c1);
		assertEquals(150,charge_both_3);

	}

	@Test
	public void test() {
		int ans = (int) (20-5)/2*10;
		assertEquals(70,ans);

		String time_unform = ("5 2004/06/06 13:50 010 090-1234-9999".substring(13,18));
		String time_formed = time_unform.substring(0, 2)+time_unform.substring(3,5);
		int time = Integer.parseInt(time_formed);
		assertEquals(1350,time);

		String time_un = ("5 2004/06/06 13:50 010 090-1234-9999".substring(13,18));
		int time_2 = Integer.parseInt(time_unform.substring(0, 2)+time_unform.substring(3,5));
		String num_call = "5 2004/06/06 13:50 010 090-1234-9999".substring(23,36);
		assertEquals(1350,time_2);
		assertEquals("090-1234-9999",num_call);

	}

}
