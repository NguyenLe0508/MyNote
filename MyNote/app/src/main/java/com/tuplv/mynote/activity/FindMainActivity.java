package com.tuplv.mynote.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuplv.mynote.R;
import com.tuplv.mynote.adapter.NoteAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class FindMainActivity extends AppCompatActivity {
EditText edtFind;
ImageView imgMove;
RecyclerView rcvFìndNote;
List<Note> listNote;
NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_main);
        Anhxa();
        imgMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 listNote = MyDatabase.getInstance(FindMainActivity.this).FindTitle(s.toString());
                noteAdapter=new NoteAdapter(FindMainActivity.this,R.layout.item_note,listNote);
                rcvFìndNote.setAdapter(noteAdapter);
                StaggeredGridLayoutManager staggeredGridLayoutManager
                        = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                rcvFìndNote.setLayoutManager(staggeredGridLayoutManager);


            }
        });
    }


    private void Anhxa() {
        edtFind = findViewById(R.id.edtFind);
        imgMove = findViewById(R.id.imgMove);
        rcvFìndNote = findViewById(R.id.rcvFindNote);

    }

}