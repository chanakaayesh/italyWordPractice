package com.example.italyword.view.home;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.italyword.R;
import com.example.italyword.model.ForgatableWordModel;
import com.example.italyword.model.WordModel;
import com.example.italyword.view.createword.CreateWordActivity;
import com.example.italyword.view.forgetableWord.ForgotableFragment;
import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.homeViewHplder> {
    private List<WordModel> dataList;
    private Context context;

    private Fragment application;

    public HomeAdapter(List<WordModel> dataList, Context context,Fragment app) {
        this.dataList = dataList;
        this.context = context;
        this.application =app;
    }

    @NonNull
    @Override
    public HomeAdapter.homeViewHplder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerresours,parent,false);
        return new homeViewHplder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.homeViewHplder holder, @SuppressLint("RecyclerView") int position) {

        boolean buttonClick =true;
        holder.englishWord.setText(dataList.get(position).englishWord);



        holder.italyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.italyWord.setText(dataList.get(position).italyWord);
                if(!holder.italyWord.getCompoundDrawables().equals(context.getDrawable(R.drawable.word_selected_btn_background)) && application.getClass().getName().equals("com.example.italyword.view.home.HomeFragment")){

                    makewordDone(holder,position);
                    ((HomeFragment) application).speakItalyhWord(dataList.get(position).italyWord);

                }



            }
        });

        int position1=position;
        Log.d("class name is ",application.getClass().getName());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context.getApplicationContext(), CreateWordActivity.class);
                Gson json = new Gson();
                String passingModel =json.toJson(dataList.get(position));
                intent.putExtra("mo",passingModel);
                context.startActivity(intent);

            }
        });

        if(application.getClass().getName().equals("com.example.italyword.view.home.HomeFragment")){

            holder.forgotable.setVisibility(View.VISIBLE);
            holder.deletebtn.setVisibility(View.GONE);
            holder.forgotable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ForgatableWordModel model= new ForgatableWordModel();
                    model.wordId =dataList.get(position1).id;
                    //   ((HomeFragment) application).findByid(dataList.get(position1).id);
                    ((HomeFragment) application).insertForgotable(model);
                }
            });


        holder.englishWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HomeFragment) application).speakEnglishWord(dataList.get(position).englishWord);
            }
        });



        }else {


            holder.forgotable.setVisibility(View.GONE);
            holder.deletebtn.setVisibility(View.VISIBLE);


            holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((ForgotableFragment) application).deleetForgotableWord(dataList.get(position1).id);
                }
            });
        }


        Glide.with(holder.itemView.getContext()).load(dataList.get(position).imageUrl).into(holder.imageView) ;


    }


    private void makewordDone(HomeAdapter.homeViewHplder holder,int position){
        holder.italyWord.setBackgroundDrawable(context.getDrawable(R.drawable.word_selected_btn_background));
        ((HomeFragment)application).makeWordCheckStatus("done",dataList.get(position).id);

        ((HomeFragment)application).checkStatusCount();
    }

    @Override
    public int getItemCount() {

        if(dataList.size() >0){
            return dataList.size();
        }else{
            return 0;
        }

    }

    public class homeViewHplder extends RecyclerView.ViewHolder {

        private Button englishWord;
        private Button italyWord;

        private Button forgotable;
        private ImageView deletebtn;
        private ImageView imageView ;
        private RelativeLayout relativeLayout;

        public homeViewHplder(@NonNull View itemView) {
            super(itemView);
            englishWord =(Button) itemView.findViewById(R.id.englsihWordBT);
            italyWord =(Button) itemView.findViewById(R.id.italyWordBT1);
            forgotable =(Button) itemView.findViewById(R.id.forgatableWordBT);
            deletebtn =(ImageView) itemView.findViewById(R.id.deletebtn);
            imageView =(ImageView) itemView.findViewById(R.id.wordImage);

            relativeLayout =(RelativeLayout) itemView.findViewById(R.id.relativvelaoutresouse);


        }
    }
}
