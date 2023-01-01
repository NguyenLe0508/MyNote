package com.tuplv.mynote.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tuplv.mynote.R;
import com.tuplv.mynote.activity.FindMainActivity;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class SearchMainActivity extends AppCompatActivity {
EditText edtStart,edtEnd;
ImageView imgMoveDate;
Button btnSearch;
RecyclerView rvFindNote;
List<Note> listNote;
NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        Anhxa();
        imgMoveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        String start = edtStart.getText().toString();
        String end =edtEnd.getText().toString();
        listNote = MyDatabase.getInstance(SearchMainActivity.this).SearchDate(start,end);
        noteAdapter=new NoteAdapter(SearchMainActivity.this,R.layout.item_note,listNote);
                rvFindNote.setAdapter(noteAdapter);
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rvFindNote.setLayoutManager(staggeredGridLayoutManager);


            }
        });
    }

    private void Anhxa() {
        edtStart = findViewById(R.id.edtStart);
        edtEnd = findViewById(R.id.edtEnd);
        imgMoveDate = findViewById(R.id.imgMoveDate);
        btnSearch = findViewById(R.id.btnSearch);
        rvFindNote = findViewById(R.id.rvFindNote);
    }

}