package in.datasciencetech.customerapplication;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
private List<Location.Result> mData;
private LayoutInflater mInflater;

        Context mContext;
        RowListner rowListner;
        HashMap<String, String> stringsData = new HashMap<String, String>();
        // data is passed into the constructor
        RecyclerAdapter(Context context, List<Location.Result> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        }

@Override
public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtSrNo.setText(String.valueOf(position));
        holder.txtAcName.setText(mData.get(position).getAccountName());
        holder.txtAddress.setText(mData.get(position).getAddress());
        }

// inflates the row layout from xml when needed
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
        }

@Override
public int getItemCount() {
        return mData.size();
        }

// inflates the row layout from xml when needed
// stores and recycles views as they are scrolled off screen
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txtSrNo, txtAcName, txtAddress;
    Button btnView;


    ViewHolder(View itemView) {
        super(itemView);
        txtSrNo = itemView.findViewById(R.id.textViewSrNo);
        txtAcName = itemView.findViewById(R.id.text_view_ac_name);

        txtAddress = itemView.findViewById(R.id.text_view_address);

        btnView=itemView.findViewById(R.id.button_view);

        btnView.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (rowListner != null)
            switch (view.getId()){
                case R.id.button_view:{
                    rowListner.onViewButtonClick(view,getAdapterPosition());
                    return;
                }
                default: {
                    rowListner.onItemClick(view, getAdapterPosition());
                    return;
                }
            }


    }
}

    // allows clicks events to be caught
    public void setClickListener(RowListner itemClickListener) {
        this.rowListner = itemClickListener;
    }

}
