package pe.com.app.nose.Adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pe.com.app.nose.Entidad.Eventodb;
import pe.com.app.nose.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Eventodb> pData;

    public NotificationAdapter(Context context, ArrayList<Eventodb> pData) {
        this.context = context;
        this.pData = pData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.notifications_cv,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.userN.setText(pData.get(position).getEmail());
        holder.fechaN.setText(pData.get(position).getFecha());
        holder.horaN.setText(pData.get(position).getHora());

    }

    @Override
    public int getItemCount() {
        return pData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView userN;
        TextView fechaN;
        TextView horaN;

        public MyViewHolder(View itemView) {
            super(itemView);

            userN = (TextView) itemView.findViewById(R.id.userN);
            fechaN = (TextView) itemView.findViewById(R.id.fechaN);
            horaN = (TextView) itemView.findViewById(R.id.horaN);

        }
    }
}
