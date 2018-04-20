package pe.com.app.nose;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;


public class HomeFragment extends Fragment {
  @Nullable

  private TextView uidTextView;
    private TextView nameTextView;
    private TextView emailTextView;
    private ImageView photoImageView;
    private TextView idTextView;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener firebaseAuthListener;

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
      final View view=inflater.inflate(R.layout.fragment_home,container,false);

      uidTextView = (TextView) view.findViewById(R.id.uidTextView);
      photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
      nameTextView = (TextView) view.findViewById(R.id.nameTextView);
      emailTextView = (TextView) view.findViewById(R.id.emailTextView);
      idTextView = (TextView) view.findViewById(R.id.idTextView);
      return view;


  }
}
