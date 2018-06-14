package pe.com.app.nose.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import pe.com.app.nose.Eventodb;
import pe.com.app.nose.R;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Eventodb> eData;
    private OnItemClickListener mListener;

    public MyEventsAdapter(Context context, ArrayList<Eventodb> eData) {
        this.context = context;
        this.eData = eData;
    }
    public interface OnItemClickListener{
        void onEditEvent(int position);
        void onDeleteEvent(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public MyEventsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.myevents_cv,parent,false);
        return new MyEventsAdapter.MyViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titulo_e.setText(eData.get(position).getTitulo());
        holder.categoria_e.setText(eData.get(position).getCategoria());
        holder.descripcion_e.setText(eData.get(position).getDescripcion());
        holder.hora_e.setText(eData.get(position).getHora());
        holder.categoria_img.setBackgroundResource(R.drawable.otros2);
        holder.fecha_e.setText(eData.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return eData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView titulo_e;
        TextView categoria_e;
        TextView descripcion_e;
        TextView hora_e;
        TextView fecha_e;
        ImageView categoria_img;
        Button delete_e;
        Button edit_e;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            titulo_e = (TextView) itemView.findViewById(R.id.titulo_e);
            categoria_e = (TextView) itemView.findViewById(R.id.categoria_e);
            descripcion_e = (TextView) itemView.findViewById(R.id.descripcion_e);
            hora_e = (TextView) itemView.findViewById(R.id.hora_e);
            fecha_e = (TextView) itemView.findViewById(R.id.fecha_e);
            categoria_img = (ImageView) itemView.findViewById(R.id.categoria_img);
            delete_e = (Button)itemView.findViewById(R.id.delete_e);
            edit_e = (Button)itemView.findViewById(R.id.edit_e);

            edit_e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onEditEvent(position);
                        };
                    }
                }
            });

            delete_e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onDeleteEvent(position);
                        };
                    }
                }
            });

        }
    }
    public void filterList(ArrayList<Eventodb> filteredList){
        eData = filteredList;
        notifyDataSetChanged();
    }
}
