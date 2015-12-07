package id.flwi.example.gcmappdemo;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsRowAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;

	public NewsRowAdapter(Activity act, int resource, List<Item> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;
	}

	@SuppressLint("CutPasteId")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);
			holder = new ViewHolder();
			view.setTag(holder);
		} 
		else {
			holder = (ViewHolder) view.getTag();
		}

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.tvName = (TextView) view.findViewById(R.id.titleTxt);
		holder.tvCity = (TextView) view.findViewById(R.id.tvstatus);
		holder.tvAge = (TextView) view.findViewById(R.id.tvStatusOrder);
		
		holder.tvStatusOrder = (TextView) view.findViewById(R.id.tvStatusOrderTerima);
		holder.tvStatusTempat = (TextView) view.findViewById(R.id.tvOrderStatusTempat);
		holder.tvStatusPenumpang = (TextView) view.findViewById(R.id.statusPenumpang);

		if (holder.tvName != null && null != objBean.getName() && objBean.getName().trim().length() > 0) {
			holder.tvName.setText(Html.fromHtml(objBean.getName()));
			
		}
		if (holder.tvCity != null && null != objBean.getCity() && objBean.getCity().trim().length() > 0) {
			holder.tvCity.setText(Html.fromHtml(objBean.getCity()));
		}
		
		if (holder.tvAge != null && null != objBean.getAge() && objBean.getAge().trim().length() > 0) {
			holder.tvAge.setText(Html.fromHtml("" + objBean.getAge()));
		}
		
		if (holder.tvStatusOrder != null && null != objBean.getStatus() && objBean.getStatus().trim().length() > 0){
			holder.tvStatusOrder.setText(Html.fromHtml("" + objBean.getStatus()));
		}
		
		if (holder.tvStatusTempat != null && null != objBean.getTempat() && objBean.getTempat().trim().length() > 0){
			holder.tvStatusTempat.setText(Html.fromHtml("" + objBean.getTempat()));
		}
		
		if (holder.tvStatusPenumpang != null && null != objBean.getNama() && objBean.getNama().trim().length() >0){
			holder.tvStatusPenumpang.setText(Html.fromHtml("" + objBean.getNama()));
		}
				
		return view;
	}

	public class ViewHolder {
		public TextView tvName, tvCity,tvAge, tvStatusOrder, tvStatusTempat, tvStatusPenumpang;
	}
}