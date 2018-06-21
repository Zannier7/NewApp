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

import pe.com.app.nose.Entidad.Eventodb;

import pe.com.app.nose.R;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Eventodb> eData;

    public FavoritosAdapter(Context context, ArrayList<Eventodb> eData) {
        this.context = context;
        this.eData = eData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);
        view = mInflater.inflate(R.layout.favorito_cv,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titulo_fav.setText(eData.get(position).getTitulo());
        holder.categoria_fav.setText(eData.get(position).getCategoria());
        holder.descripcion_fav.setText(eData.get(position).getDescripcion());
        holder.hora_fav.setText(eData.get(position).getHora());
        holder.categoria_img_fav.setBackgroundResource(R.drawable.concert);
        holder.fecha_fav.setText(eData.get(position).getFecha());
    }

    @Override
    public int getItemCount() {
        return eData.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView titulo_fav;
        TextView categoria_fav;
        TextView descripcion_fav;
        TextView hora_fav;
        TextView fecha_fav;
        ImageView categoria_img_fav;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo_fav = (TextView) itemView.findViewById(R.id.titulo_fav);
            categoria_fav = (TextView) itemView.findViewById(R.id.categoria_fav);
            descripcion_fav = (TextView) itemView.findViewById(R.id.descripcion_fav);
            hora_fav = (TextView) itemView.findViewById(R.id.hora_fav);
            fecha_fav = (TextView) itemView.findViewById(R.id.fecha_fav);
            categoria_img_fav = (ImageView) itemView.findViewById(R.id.categoria_img_fav);

        }
    }

}
