package com.example.italyword.view.forgetableWord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.italyword.R;
import com.example.italyword.databinding.FragmentForgotableBinding;
import com.example.italyword.view.home.HomeAdapter;
import com.example.italyword.viewmodel.ForgatableViewModel;
import com.example.italyword.viewmodel.WordViewModel;


public class ForgotableFragment extends Fragment {

    private FragmentForgotableBinding binding;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;

    private WordViewModel wordViewModel;
    private ForgatableViewModel forgatableViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        ForgotableViewModel galleryViewModel =
                new ViewModelProvider(this).get(ForgotableViewModel.class);

    binding = FragmentForgotableBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        recyclerView = (RecyclerView) root.findViewById(R.id.forgotableRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wordViewModel =new ViewModelProvider(getActivity()).get(WordViewModel.class);
        forgatableViewModel = new ViewModelProvider(getActivity()).get(ForgatableViewModel.class);

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        showData();
    }

    public void deleetForgotableWord(int wordid){


        forgatableViewModel.deleteForgotableWord(wordid).observe(this,blog ->{
            if(blog.equals("deletd")){
                Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "not deleted", Toast.LENGTH_SHORT).show();
            }
            showData();
        });
    }

    public void showData(){
        forgatableViewModel.getFortableAllWordList().observe(this,blogs ->{
            adapter = new HomeAdapter(blogs,getContext(),ForgotableFragment.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
    }
}