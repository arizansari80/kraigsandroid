package com.angrydoughnuts.android.alarmclock;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class AlarmReportViewAdapter extends ArrayAdapter<AlarmAnalyzerObject> implements Comparator<AlarmAnalyzerObject> {
	private final Activity context;
	private final ArrayList<AlarmAnalyzerObject> alarmAnalyzeArray;

	public AlarmReportViewAdapter(final Activity context, final int resource, final ArrayList<AlarmAnalyzerObject> objects) {
		super(context, resource, objects);
		this.context = context;
		alarmAnalyzeArray = objects;
	}

	@Override
	public View getView(final int position, final View view, final ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		final View rowView = inflater.inflate(R.layout.alarm_report_view_item, null,true);
		((TextView) rowView.findViewById(R.id.alarmTimeValue)).setText(alarmAnalyzeArray.get(position).getTimeOfAlarm());
		((TextView) rowView.findViewById(R.id.alarmLabelValue)).setText(alarmAnalyzeArray.get(position).getLabel());
		((TextView) rowView.findViewById(R.id.actionTimeValue)).setText(alarmAnalyzeArray.get(position).getTimeOfEvent());
		((TextView) rowView.findViewById(R.id.actionTakenValue)).setText(alarmAnalyzeArray.get(position).getEventVerbose());
		return rowView;
	}

	@Override
	public void add(final AlarmAnalyzerObject alarmDetails) {
		super.add(alarmDetails);
		sort(this);
	}

	@Override
	public int compare(AlarmAnalyzerObject alarmAnalyzerObject, AlarmAnalyzerObject t1) {
		return alarmAnalyzerObject.compare(t1);
	}
}

class AlarmAnalyzerObject {
	private String timeOfAlarm;
	private String label;
	private String timeOfEvent;
	private String eventVerbose;

	protected AlarmAnalyzerObject(final Cursor c) {
		timeOfAlarm = c.getString(c.getColumnIndex(AlarmClockProvider.AlarmAnalyzer.TIME));
		label = c.getString(c.getColumnIndex(AlarmClockProvider.AlarmAnalyzer.LABEL));
		timeOfEvent = c.getString(c.getColumnIndex(AlarmClockProvider.AlarmAnalyzer.TIME_OF_EVENT));
		try {
			eventVerbose = c.getString(c.getColumnIndex(AlarmClockProvider.AlarmAnalyzer.ACTION));
		} catch (ArrayIndexOutOfBoundsException e) {
			eventVerbose = "Some Error Occurred!";
		}
	}

	public String getTimeOfAlarm() {
		return timeOfAlarm;
	}

	public String getLabel() {
		return label;
	}

	public String getTimeOfEvent() {
		return timeOfEvent;
	}

	public String getEventVerbose() {
		return eventVerbose;
	}

	public int compare(final AlarmAnalyzerObject obj) {
		final String timeOfEvent1 = this.timeOfEvent.replaceAll(" :-","");
		final String timeOfEvent2 = obj.timeOfEvent.replaceAll(" :-","");
		return -1 * timeOfEvent1.compareToIgnoreCase(timeOfEvent2);
	}
}

