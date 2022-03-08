package org.techtown.sns_project.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.sns_project.Board.BoardAdapter;
import org.techtown.sns_project.Model.PostInfo;
import org.techtown.sns_project.R;
import org.techtown.sns_project.UploadActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardFragment extends Fragment {

 FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
 FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
 FirebaseFirestore db = FirebaseFirestore.getInstance();
//d
 FirebaseFirestore db2 = FirebaseFirestore.getInstance();
 private View view;
 private String TAG = "프래그먼트";
 RecyclerView recyclerView_BoardItem;
 static BoardAdapter adapter;

 HashMap<String,Object> List = new HashMap<String,Object>();

 static ArrayList<String> listImgUrl = new ArrayList<>();
 static ArrayList<String> listDescription = new ArrayList<>();
 static ArrayList<QuerySnapshot> future = new ArrayList<>();
 @Nullable
 @Override
 public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
  view = inflater.inflate(R.layout.board_fragment, container, false);
  Button upload_btn = (Button)view.findViewById(R.id.upload);
  recyclerView_BoardItem = view.findViewById(R.id.recyclerView_BoardItem);

  LinearLayoutManager BoardItem = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
  recyclerView_BoardItem.setLayoutManager(BoardItem); //Linear로 recyclerview layout 만들기
  adapter = new BoardAdapter();
  recyclerView_BoardItem.setAdapter(adapter);

  db.collectionGroup("board_Data").get().
         addOnCompleteListener(task -> {
          if(task.isSuccessful()) {
           listImgUrl.clear();
           listDescription.clear();
           adapter.listData.clear();
           for(QueryDocumentSnapshot document : task.getResult()) {

            List = (HashMap<String, Object>) document.getData();

            PostInfo data = new PostInfo((String)List.get("publisher"),(String)List.get("ImageUrl"),(String)List.get("description"));
            adapter.addItem(data);
           }
           adapter.notifyDataSetChanged();
          }


         });



  upload_btn.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
   Intent intent = new Intent(getContext(), UploadActivity.class);
   startActivity(intent);
   }
  });

  return view;
 }

}
