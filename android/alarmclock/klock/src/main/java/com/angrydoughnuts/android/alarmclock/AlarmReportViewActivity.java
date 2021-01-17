package com.angrydoughnuts.android.alarmclock;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class AlarmReportViewActivity extends Activity {
	private AlarmReportViewAdapter alarmReportViewAdapter;
	private ListView alarmReportListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_report_view_layout);
		alarmReportListView = (ListView)findViewById(R.id.report_list);
	}

	@Override
	public void onResume() {
		super.onResume();
		final String buildQuery = buildQuery(getQueryDates(2));
		final Cursor c = getApplicationContext().getContentResolver().query(AlarmClockProvider.ALARM_ANALYZE_URI,null,buildQuery,null,null);
		if (alarmReportViewAdapter == null) {
			if (c.moveToNext()) {
				final AlarmAnalyzerObject first = new AlarmAnalyzerObject(c);
				final ArrayList<AlarmAnalyzerObject> aDetails = new ArrayList<>(Arrays.asList(first));
				alarmReportViewAdapter = new AlarmReportViewAdapter(this,R.layout.alarm_report_view_item,aDetails);
				alarmReportListView.setAdapter(alarmReportViewAdapter);
			}
		}
		while (c.moveToNext()) {
			alarmReportViewAdapter.add(new AlarmAnalyzerObject(c));
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (alarmReportViewAdapter != null)
			alarmReportViewAdapter.clear();
		alarmReportViewAdapter = null;
	}

	private String[] getQueryDates(int... args) {
		SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (args.length == 1) {
			String[] retVal = new String[args[0]];
			long time = new Date().getTime();
			for (int i = 0; i < args[0]; i++) {
				retVal[i] = "\"" + localDateFormat.format(new Date(time)) + "%\"";
				time = time - (24 * 60 * 60 * 1000);
			}
			return retVal;
		}
		return null;
	}

	private String buildQuery(String[] dates) {
		if (dates == null)
			return null;
		else {
			String query = AlarmClockProvider.AlarmAnalyzer.TIME_OF_EVENT + " like ";
			for (String d : dates)
				query += d + " or " + AlarmClockProvider.AlarmAnalyzer.TIME_OF_EVENT + " like ";
			int lastI = query.lastIndexOf(" or " + AlarmClockProvider.AlarmAnalyzer.TIME_OF_EVENT + " like ");
			if (lastI >= 0)
				query = query.substring(0,lastI);
			return query;
		}
	}
}

