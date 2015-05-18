package com.example.dayjob;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class AddTaskMap extends FragmentActivity implements OnMapClickListener {

	GoogleMap map;
	Geocoder mCoder;
	private LocationManager mLocMan;
	private String mProvider;
	private List<Address> addr;
	private SearchView sv;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task_map);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		mLocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mProvider = mLocMan.getBestProvider(new Criteria(), true);

		Location location = mLocMan.getLastKnownLocation(mProvider);

		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(
				new LatLng(location.getLatitude(), location.getLongitude()), 15));

		map.setOnMapClickListener(this);

		mCoder = new Geocoder(this);

		try {
			addr = mCoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tv = (TextView) findViewById(R.id.textView2);

		tv.setText("������ġ : " + addr.get(0).getAddressLine(0));

		sv = (SearchView) findViewById(R.id.searchView1);
		sv.setQueryHint("�ּҷ� �˻�");
		sv.setIconifiedByDefault(true);
		sv.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				try {
					addr = mCoder.getFromLocationName(query, 5);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						addr.get(0).getLatitude(), addr.get(0).getLongitude()),
						15));

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});

	}

	@Override
	public void onMapClick(final LatLng latLng) {

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle("��� ����")
				.setPositiveButton("����", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent addTask = new Intent(AddTaskMap.this,
								AddTask.class);
						addTask.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						addTask.putExtra("latitude",
								String.valueOf(latLng.latitude));
						addTask.putExtra("longitude",
								String.valueOf(latLng.longitude));
						startActivity(addTask);

					}
				})
				.setNegativeButton("���", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				}).show();

	}

}