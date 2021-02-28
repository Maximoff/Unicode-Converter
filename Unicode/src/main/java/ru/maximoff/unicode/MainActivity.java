package ru.maximoff.unicode;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		final EditText str = findViewById(R.id.mainEditText1);
		final EditText uni = findViewById(R.id.mainEditText2);
		str.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {

				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {

				}

				@Override
				public void afterTextChanged(Editable p1) {
					if (str.isFocused()) {
						try {
							uni.setText(string2unicode(p1.toString()));
						} catch (Exception e) {
							Toast.makeText(MainActivity.this, "Bad string!", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		uni.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {

				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {

				}

				@Override
				public void afterTextChanged(Editable p1) {
					if (uni.isFocused()) {
						try {
							str.setText(unicode2string(p1.toString()));
						} catch (Exception e) {
							Toast.makeText(MainActivity.this, "Bad unicode string!", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
    }

	private String unicode2string(String str) throws Exception {
		Pattern p = Pattern.compile("\\\\u([0-9a-f]{4})", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, String.valueOf((char) Integer.parseInt(m.group(1), 16)));
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private String string2unicode(String str) throws Exception {
		if (str.equals("")) {
			return str;
		}
		StringBuilder sb = new StringBuilder();
		for (char c : str.toCharArray()) {
			if (c >= 128) {
				sb.append("\\u").append(String.format("%04X", (int) c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
