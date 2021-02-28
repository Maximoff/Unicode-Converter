package ru.maximoff.unicode;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.maximoff.unicode.R;

public class MainActivity extends Activity {
	private EditText str;
	private EditText uni;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		str = findViewById(R.id.mainEditText1);
		uni = findViewById(R.id.mainEditText2);
		str.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// todo
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// todo
				}

				@Override
				public void afterTextChanged(Editable p1) {
					if (str.isFocused()) {
						try {
							uni.setText(string2unicode(p1.toString()));
						} catch (Exception e) {
							Toast.makeText(MainActivity.this, R.string.bad, Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		uni.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// todo
				}

				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
					// todo
				}

				@Override
				public void afterTextChanged(Editable p1) {
					if (uni.isFocused()) {
						try {
							str.setText(unicode2string(p1.toString()));
						} catch (Exception e) {
							Toast.makeText(MainActivity.this, R.string.bad, Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
		ImageView copyStr = findViewById(R.id.mainImageView1);
		copyStr.setClickable(true);
		copyStr.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					setClipboard(str.getText().toString());
				}
			});
		ImageView copyUni = findViewById(R.id.mainImageView2);
		copyUni.setClickable(true);
		copyUni.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					setClipboard(uni.getText().toString());
				}
			});
		showKeyboard();
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

	private void setClipboard(String text) {
		if (text.equals("")) {
			return;
		}
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText("Copied Text", text);
		clipboard.setPrimaryClip(clip);
		Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
	}

	private void showKeyboard() {
		new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					str.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(str, InputMethodManager.SHOW_IMPLICIT);
				}
			}, 100);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				str.setText("");
				uni.setText("");
				showKeyboard();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
