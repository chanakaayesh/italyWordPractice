package com.example.italyword.view.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.italyword.MainActivity;
import com.example.italyword.R;
import com.example.italyword.databinding.FragmentHomeBinding;
import com.example.italyword.model.ForgatableWordModel;
import com.example.italyword.model.WordModel;
import com.example.italyword.viewmodel.ForgatableViewModel;
import com.example.italyword.viewmodel.WordViewModel;

import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextToSpeech speakEnglish ,speakItaly;
    private FragmentHomeBinding binding;
    private  HomeAdapter adapter;
    private List<WordModel> wordList;
    private RecyclerView recyclerView;
    private WordViewModel wordViewModel;
    private String tag =this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private ForgatableViewModel forgatableViewModel;
    private SwipeRefreshLayout swipeRefresher;
    private int recyclerViewPosition ;
    private EditText mSearchWord ;
    private TextView mNO_WORD_TEXTVIEW;

    private Integer paginNumber ;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root  = FragmentHomeBinding.inflate(inflater, container, false).getRoot();
        wordViewModel =new ViewModelProvider(getActivity()).get(WordViewModel.class);
        forgatableViewModel = new ViewModelProvider(getActivity()).get(ForgatableViewModel.class);
        swipeRefresher =(SwipeRefreshLayout) root.findViewById(R.id.swipeRefresher);

        recyclerView=(RecyclerView) root.findViewById(R.id.recyclerview);
        mNO_WORD_TEXTVIEW=(TextView) root.findViewById(R.id.no_word_txview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onRefresh() {
                if(paginNumber !=null){
                    paginNumber =paginNumber+5;
                    showData(paginNumber);
                    progressDialog.dismiss();
                    swipeRefresher.setRefreshing(false);
                }
            }
        });

        swipeRefresher.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                Log.d(tag,"swipeRefresher  12");
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(adapter.getItemCount() <5){
                    paginNumber =-5;
                }
            }


        });

        mSearchWord = root.findViewById(R.id.searchWord);
        mSearchWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                wordViewModel.searchWords(mSearchWord.getText().toString());
            }
        });

        return root;
    }

    public void speakEnglishWord(String word){
        speakEnglish.speak(word,TextToSpeech.QUEUE_FLUSH,null,null);
    }
    public void speakItalyhWord(String word){
        speakItaly.speak(word,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    public void makeWordCheckStatus(String status,int wordId){
        wordViewModel.updateWordCheckStatus(status,wordId);
    }
    public void resetAllWordStatus(){
        wordViewModel.resetAllWordStatus();
    }

    @Override
    public void onPause() {
        if(speakEnglish !=null){
            speakEnglish.stop();
            speakEnglish.shutdown();
        }
        //speakItaly
        if(speakItaly !=null){
            speakItaly.stop();
            speakItaly.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        paginNumber =0;
        showData(paginNumber);

        speakEnglish = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status == TextToSpeech.SUCCESS){
                    Log.e(tag,"speact initialitation pass");
                    speakEnglish.setLanguage(Locale.ENGLISH);
                }else {
                    Log.e(tag,"speact initialitation fail");
                }
            }
        });

        speakItaly = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if(status == TextToSpeech.SUCCESS){
                    Log.e(tag,"speact initialitation pass");
                    speakItaly.setLanguage(Locale.ITALY);
                }else {
                    Log.e(tag,"speact initialitation fail");
                }
            }
        });

    }

    public void showData(Integer offset){

        wordViewModel.getValueListPaging(offset).observe(getParentFragment(),blogs->{
            if(blogs !=null && blogs.size() <0){
                mNO_WORD_TEXTVIEW.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            }
            else{
                if(blogs !=null){
                    mNO_WORD_TEXTVIEW.setVisibility(blogs.size()==0? View.VISIBLE:View.GONE);
                    recyclerView.setVisibility(blogs.size()!=0? View.VISIBLE:View.GONE);
                    adapter = new HomeAdapter(blogs,getActivity(),HomeFragment.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();

                }else{
                    Toast.makeText(getContext(), "nul zero", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void insertForgotable(ForgatableWordModel model){

        forgatableViewModel.setInsertForgotable(model);
        forgatableViewModel.getInsertForgotable().observe(this,blog ->{
            if(blog.equals("Success")){
                Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Fail To Add Forgotable", Toast.LENGTH_SHORT).show();
            }
        });

        forgatableViewModel.getInsertForgotable().removeObservers(this);




    }
    public void justCheck(){
        Toast.makeText(getContext(), "work", Toast.LENGTH_SHORT).show();
    }

    public void findByid(int id){
        wordViewModel.findById(id).observe(this, blog->{

            if(blog !=null){
                Toast.makeText(getActivity(), "model available", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "model not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkStatusCount(){
        ((MainActivity)getActivity()).setCheckCountStatud();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}