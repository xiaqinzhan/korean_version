package any4media_27.challenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import com.kaf.GeneralException;
import com.kaf.KafManager;
public class KafUtil {
	
	static Activity act = null;
	
	public static int KafInit(Context ct, int copyright) {
		KafManager kafManager = KafManager.getInstance();
		
		act = (Activity)ct;
		int result = 0;
		try {
			result = kafManager.Initialize(ct, copyright);
			AlertDialog.Builder alert = new AlertDialog.Builder(ct);
			alert.setTitle("알림");
			alert.setCancelable(false);
			if(result == 0) {
				Log.d("================>", "Initialize Success");
			} else if(result == -1) {
				Log.d("================>", "Initialize Fail");
				alert.setMessage("오류�?발생하였습니�? 잠시 �? 다시 실행하세�?");
				alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						act.finish();
					}
				});
				alert.show();
			} else if(result == -2) {
				Log.d("================>", "No Usim");
				alert.setMessage("USIM�?장착�?프로그램�?실행�?주세�?");
				alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						act.finish();
					}
				});
				alert.show();
			} else if(result == -3) {
				Log.d("================>", "Not Copy");
				alert.setMessage("불법복제�?Application입니�?");
				alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						act.finish();
					}
				});
				alert.show();
			} else if(result == -4) {
				Log.d("================>", "No Kaf");
			} else if(result == -5) {
				Log.d("================>", "Initialize ing...");
			}
		} catch(IllegalAccessException e) {
			Log.e("================>", "IllegalAccessException");
		} catch(IllegalArgumentException e) {
			Log.e("================>", "IllegalArgumentException");
		} catch(GeneralException e) {
			Log.e("================>", "GeneralException");
		}
		
		return result;
	}	
}